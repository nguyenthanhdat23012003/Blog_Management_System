"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";

const AdminSeriesPage = () => {
    const [series, setSeries] = useState([]);
    const [authorsMap, setAuthorsMap] = useState({});
    const [filteredSeries, setFilteredSeries] = useState([]);
    const [search, setSearch] = useState("");
    const [searchBy, setSearchBy] = useState("title");
    const [filters, setFilters] = useState({
        idFrom: "",
        idTo: "",
        createFrom: "",
        createTo: "",
        updateFrom: "",
        updateTo: "",
    });

    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [seriesToDelete, setSeriesToDelete] = useState(null);
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

                // Fetch series and authors
                const [allSeries, allUsers] = await Promise.all([
                    fetcher("/series", { headers }),
                    fetcher("/users", { headers }),
                ]);

                // Map authorId -> author.name
                const authorMap = {};
                allUsers.forEach((user) => {
                    authorMap[user.id] = user.name;
                });

                setAuthorsMap(authorMap);
                setSeries(allSeries);
                setFilteredSeries(allSeries); // Initialize filtered series
            } catch (err) {
                console.error("Failed to fetch data", err);
            }
        };

        fetchData();
    }, []);

    const handleSearch = () => {
        const lowerCaseSearch = search.toLowerCase();
        const filtered = series.filter((item) => {
            if (searchBy === "title") {
                return item.title.toLowerCase().includes(lowerCaseSearch);
            } else if (searchBy === "author") {
                return authorsMap[item.authorId]
                    ?.toLowerCase()
                    .includes(lowerCaseSearch);
            }
            return true;
        });
        setFilteredSeries(filtered);
    };

    const handleFilter = () => {
        const { idFrom, idTo, createFrom, createTo, updateFrom, updateTo } = filters;

        let filtered = series.filter((item) => {
            return (
                (!idFrom || item.id >= parseInt(idFrom)) &&
                (!idTo || item.id <= parseInt(idTo)) &&
                (!createFrom || new Date(item.create_at) >= new Date(createFrom)) &&
                (!createTo || new Date(item.create_at) <= new Date(createTo)) &&
                (!updateFrom || new Date(item.update_at) >= new Date(updateFrom)) &&
                (!updateTo || new Date(item.update_at) <= new Date(updateTo))
            );
        });

        // Apply the search filter as well
        if (search) {
            filtered = filtered.filter((item) => {
                if (searchBy === "title") {
                    return item.title.toLowerCase().includes(search.toLowerCase());
                } else if (searchBy === "author") {
                    return authorsMap[item.authorId]
                        ?.toLowerCase()
                        .includes(search.toLowerCase());
                }
                return true;
            });
        }

        setFilteredSeries(filtered);
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
        setFilteredSeries(series);
    };

    const openDeleteModal = (seriesId) => {
        setSeriesToDelete(seriesId);
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

            await fetcher(`/series/${seriesToDelete}`, {
                method: "DELETE",
                headers,
            });

            setSeries((prev) => prev.filter((item) => item.id !== seriesToDelete));
            setFilteredSeries((prev) =>
                prev.filter((item) => item.id !== seriesToDelete)
            );
            setIsDeleteModalOpen(false);
        } catch (err) {
            console.error("Failed to delete series", err);
        }
    };

    return (
        <div className="rounded-3xl min-h-screen bg-gradient-to-r from-blue-50 to-indigo-100 p-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-gray-700">Manage Series</h1>
                <div className="flex space-x-4">
                    <button
                        onClick={() => router.push("/admin/dashboard")}
                        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
                    >
                        Back to Dashboard
                    </button>
                    <button
                        onClick={() => router.push("/admin/create-series")}
                        className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                    >
                        Create New Series
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
                    <option value="title">Search by Title</option>
                    <option value="author">Search by Author</option>
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
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">ID</th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Title
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Description
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Author
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
                    {filteredSeries.map((serie) => (
                        <tr key={serie.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 border-b text-gray-700">{serie.id}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{serie.title}</td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {serie.description.length > 100
                                    ? `${serie.description.slice(0, 100)}...`
                                    : serie.description}
                            </td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {authorsMap[serie.authorId] || "Unknown"}
                            </td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {serie.create_at}
                            </td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {serie.update_at}
                            </td>
                            <td className="px-6 py-4 border-b">
                                <button
                                    onClick={() =>
                                        router.push(`/admin/edit-series/${serie.id}`)
                                    }
                                    className="text-indigo-600 hover:underline mr-4"
                                >
                                    Edit
                                </button>
                                <button
                                    onClick={() => openDeleteModal(serie.id)}
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

            {/* Delete Modal */}
            {isDeleteModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
                    <div className="bg-white p-6 rounded-lg shadow-md w-96">
                        <h2 className="text-lg font-bold mb-4 text-gray-800">Confirm Delete</h2>
                        <p className="text-sm text-gray-600 mb-6">
                            Are you sure you want to delete this series? This action cannot be
                            undone.
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

export default AdminSeriesPage;
