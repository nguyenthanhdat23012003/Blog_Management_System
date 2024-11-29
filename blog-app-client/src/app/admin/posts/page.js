"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";

const ManagePostPage = () => {
    const [posts, setPosts] = useState([]);
    const [filteredPosts, setFilteredPosts] = useState([]);
    const [categoriesMap, setCategoriesMap] = useState({});
    const [authorsMap, setAuthorsMap] = useState({});
    const [seriesMap, setSeriesMap] = useState({});
    const [search, setSearch] = useState("");
    const [searchBy, setSearchBy] = useState("category");
    const [filters, setFilters] = useState({
        idFrom: "",
        idTo: "",
        createFrom: "",
        createTo: "",
        updateFrom: "",
        updateTo: "",
    });

    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [postToDelete, setPostToDelete] = useState(null);
    const [isFilterOpen, setIsFilterOpen] = useState(false);

    const router = useRouter();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const adminToken = localStorage.getItem("adminToken");

                if (!adminToken) {
                    console.error("Admin token is missing.");
                    return;
                }

                const headers = { Authorization: `Bearer ${adminToken}` };

                // Fetch blogs, categories, users, and series
                const [blogs, allCategories, allUsers, allSeries] = await Promise.all([
                    fetcher("/blogs", { headers }),
                    fetcher("/categories", { headers }),
                    fetcher("/users", { headers }),
                    fetcher("/series", { headers }),
                ]);

                // Map categoryId -> category.title
                const categoryMap = {};
                allCategories.forEach((category) => {
                    categoryMap[category.id] = category.title;
                });

                // Map authorId -> author.name
                const authorMap = {};
                allUsers.forEach((user) => {
                    authorMap[user.id] = user.name;
                });

                // Map seriesId -> series.title
                const seriesMap = {};
                allSeries.forEach((serie) => {
                    seriesMap[serie.id] = serie.title;
                });

                setCategoriesMap(categoryMap);
                setAuthorsMap(authorMap);
                setSeriesMap(seriesMap);
                setPosts(blogs);
                setFilteredPosts(blogs); // Initialize filtered posts
            } catch (err) {
                console.error("Failed to fetch data", err);
            }
        };

        fetchData();
    }, []);

    const applySearchFilter = (data) => {
        const lowerCaseSearch = search.toLowerCase();
        return data.filter((post) => {
            switch (searchBy) {
                case "author":
                    return authorsMap[post.authorId]?.toLowerCase().includes(lowerCaseSearch);
                case "blog":
                    return post.title.toLowerCase().includes(lowerCaseSearch);
                case "series":
                    return seriesMap[post.seriesId]?.toLowerCase().includes(lowerCaseSearch);
                case "category":
                    return post.categoryIds.some((categoryId) =>
                        categoriesMap[categoryId]?.toLowerCase().includes(lowerCaseSearch)
                    );
                default:
                    return true;
            }
        });
    };

    const handleSearch = () => {
        const filtered = applySearchFilter(posts);
        setFilteredPosts(filtered);
    };

    const handleFilter = () => {
        const { idFrom, idTo, createFrom, createTo, updateFrom, updateTo } = filters;

        // Apply search conditions first
        let filtered = applySearchFilter(posts);

        // Then apply filter conditions
        filtered = filtered.filter((post) => {
            return (
                (!idFrom || post.id >= parseInt(idFrom)) &&
                (!idTo || post.id <= parseInt(idTo)) &&
                (!createFrom || new Date(post.create_at) >= new Date(createFrom)) &&
                (!createTo || new Date(post.create_at) <= new Date(createTo)) &&
                (!updateFrom || new Date(post.update_at) >= new Date(updateFrom)) &&
                (!updateTo || new Date(post.update_at) <= new Date(updateTo))
            );
        });

        setFilteredPosts(filtered);
    };

    const handleResetFilters = () => {
        setFilters({
            idFrom: "",
            idTo: "",
            createFrom: "",
            createTo: "",
            updateFrom: "",
            updateTo: "",
        });
        setFilteredPosts(posts);
    };

    const openDeleteModal = (postId) => {
        setPostToDelete(postId);
        setIsDeleteModalOpen(true);
    };

    const handleDelete = async () => {
        try {
            const adminToken = localStorage.getItem("adminToken");

            if (!adminToken) {
                console.error("Admin token is missing.");
                return;
            }

            const headers = { Authorization: `Bearer ${adminToken}` };

            await fetcher(`/blogs/${postToDelete}`, {
                method: "DELETE",
                headers,
            });

            setPosts((prev) => prev.filter((post) => post.id !== postToDelete));
            setFilteredPosts((prev) => prev.filter((post) => post.id !== postToDelete));
            setIsDeleteModalOpen(false);
        } catch (err) {
            console.error("Failed to delete post", err);
        }
    };

    return (
        <div className="rounded-3xl min-h-screen bg-gradient-to-r from-blue-50 to-indigo-100 p-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-gray-700">Manage Posts</h1>
                <div className="flex space-x-4">
                    <button
                        onClick={() => router.push("/admin/dashboard")}
                        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
                    >
                        Back to Dashboard
                    </button>
                    <button
                        onClick={() => router.push("/admin/create-post")}
                        className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                    >
                        Create New Post
                    </button>
                </div>
            </div>

            {/* Search */}
            <div className="bg-white p-4 shadow-md rounded-lg mb-4 flex items-center gap-4">
                <select
                    value={searchBy}
                    onChange={(e) => setSearchBy(e.target.value)}
                    className="border border-gray-300 rounded-md p-2"
                >
                    <option value="category">Search by Category</option>
                    <option value="author">Search by Author</option>
                    <option value="blog">Search by Blog Name</option>
                    <option value="series">Search by Series</option>
                </select>
                <input
                    type="text"
                    placeholder="Search..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    className="flex-1 border border-gray-300 rounded-md p-2"
                />
                <button
                    onClick={handleSearch}
                    className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                >
                    Search
                </button>
                <button
                    onClick={() => setIsFilterOpen(!isFilterOpen)}
                    className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
                >
                    {isFilterOpen ? "Close Filters" : "Filters"}
                </button>
            </div>

            {/* Filters */}
            {isFilterOpen && (
                <div className="bg-white p-6 shadow-md rounded-lg mb-6">
                    {/* Filter Inputs */}
                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700">
                                ID From
                            </label>
                            <input
                                type="number"
                                placeholder="From"
                                value={filters.idFrom}
                                onChange={(e) => setFilters({ ...filters, idFrom: e.target.value })}
                                className="border border-gray-300 rounded-md p-2"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">
                                ID To
                            </label>
                            <input
                                type="number"
                                placeholder="To"
                                value={filters.idTo}
                                onChange={(e) => setFilters({ ...filters, idTo: e.target.value })}
                                className="border border-gray-300 rounded-md p-2"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">
                                Created From
                            </label>
                            <input
                                type="date"
                                placeholder="mm/dd/yyyy"
                                value={filters.createFrom}
                                onChange={(e) =>
                                    setFilters({ ...filters, createFrom: e.target.value })
                                }
                                className="border border-gray-300 rounded-md p-2"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">
                                Created To
                            </label>
                            <input
                                type="date"
                                placeholder="mm/dd/yyyy"
                                value={filters.createTo}
                                onChange={(e) =>
                                    setFilters({ ...filters, createTo: e.target.value })
                                }
                                className="border border-gray-300 rounded-md p-2"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">
                                Updated From
                            </label>
                            <input
                                type="date"
                                placeholder="mm/dd/yyyy"
                                value={filters.updateFrom}
                                onChange={(e) =>
                                    setFilters({ ...filters, updateFrom: e.target.value })
                                }
                                className="border border-gray-300 rounded-md p-2"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">
                                Updated To
                            </label>
                            <input
                                type="date"
                                placeholder="mm/dd/yyyy"
                                value={filters.updateTo}
                                onChange={(e) =>
                                    setFilters({ ...filters, updateTo: e.target.value })
                                }
                                className="border border-gray-300 rounded-md p-2"
                            />
                        </div>
                    </div>
                    {/* Filter Buttons */}
                    <div className="mt-4 flex justify-end gap-4">
                        <button
                            onClick={handleFilter}
                            className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                        >
                            Apply Filters
                        </button>
                        <button
                            onClick={handleResetFilters}
                            className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
                        >
                            Reset Filters
                        </button>
                    </div>
                </div>
            )}

            {/* Table */}
            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <table className="table-auto w-full text-left">
                    <thead className="bg-gray-100">
                    <tr>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            ID
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Author
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Blog Name
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Categories
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Series
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Created At
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Updated At
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Actions
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredPosts.map((post) => (
                        <tr key={post.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 border-b text-gray-700">{post.id}</td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {authorsMap[post.authorId] || "Unknown"}
                            </td>
                            <td className="px-6 py-4 border-b text-gray-700">{post.title}</td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {post.categoryIds && post.categoryIds.length > 0 ? (
                                    <div className="flex flex-wrap gap-2">
                                        {post.categoryIds.map((categoryId) => (
                                            <span
                                                key={categoryId}
                                                className="bg-blue-100 text-blue-800 text-sm font-semibold mr-2 px-3 py-1 rounded"
                                            >
                    {categoriesMap[categoryId] || "Unknown"}
                </span>
                                        ))}
                                    </div>
                                ) : (
                                    <span className="text-gray-500 italic">No categories</span>
                                )}
                            </td>

                            <td className="px-6 py-4 border-b text-gray-700">
                                {post.seriesId
                                    ? seriesMap[post.seriesId] || "Unknown"
                                    : "No Series"}
                            </td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {post.create_at}
                            </td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {post.update_at}
                            </td>
                            <td className="px-6 py-4 border-b">
                                <button
                                    onClick={() =>
                                        router.push(
                                            `/admin/edit-post/${post.id}`
                                        )
                                    }
                                    className="text-indigo-600 hover:underline mr-4"
                                >
                                    Edit
                                </button>
                                <button
                                    onClick={() => openDeleteModal(post.id)}
                                    className="text-red-600 hover:underline"
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            {isDeleteModalOpen && (

                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">

                    <div className="bg-white p-6 rounded-lg shadow-md w-96">

                        <h2 className="text-lg font-bold mb-4 text-gray-800">Confirm Delete</h2>

                        <p className="text-sm text-gray-600 mb-6">

                            Are you sure you want to delete this post? This action cannot be undone.

                        </p>

                        <div className="flex justify-end space-x-4">

                            <button

                                onClick={() => setIsDeleteModalOpen(false)}

                                className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"

                            >

                                Cancel

                            </button>

                            <button

                                onClick={handleDelete}

                                className="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700"

                            >

                                Delete

                            </button>

                        </div>

                    </div>

                </div>

            )}
        </div>
    );
};

export default ManagePostPage;
