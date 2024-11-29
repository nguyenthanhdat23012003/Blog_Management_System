"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";

const AdminCategoriesPage = () => {
    const [categories, setCategories] = useState([]);
    const [filteredCategories, setFilteredCategories] = useState([]);
    const [search, setSearch] = useState("");
    const [filters, setFilters] = useState({
        idFrom: "",
        idTo: "",
    });

    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [categoryToDelete, setCategoryToDelete] = useState(null);
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

                const allCategories = await fetcher("/categories", { headers });

                setCategories(allCategories);
                setFilteredCategories(allCategories); // Initialize filtered categories
            } catch (err) {
                console.error("Failed to fetch categories", err);
            }
        };

        fetchData();
    }, []);

    const handleSearch = () => {
        const lowerCaseSearch = search.toLowerCase();
        const filtered = categories.filter((category) =>
            category.title.toLowerCase().includes(lowerCaseSearch)
        );
        setFilteredCategories(filtered);
    };

    const handleFilter = () => {
        const { idFrom, idTo } = filters;

        let filtered = categories.filter((category) => {
            return (
                (!idFrom || category.id >= parseInt(idFrom)) &&
                (!idTo || category.id <= parseInt(idTo))
            );
        });

        // Apply the search filter as well
        if (search) {
            filtered = filtered.filter((category) =>
                category.title.toLowerCase().includes(search.toLowerCase())
            );
        }

        setFilteredCategories(filtered);
    };

    const handleResetFilters = () => {
        setFilters({
            idFrom: "",
            idTo: "",
        });
        setFilteredCategories(categories);
    };

    const openDeleteModal = (categoryId) => {
        setCategoryToDelete(categoryId);
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

            await fetcher(`/categories/${categoryToDelete}`, {
                method: "DELETE",
                headers,
            });

            setCategories((prev) => prev.filter((cat) => cat.id !== categoryToDelete));
            setFilteredCategories((prev) =>
                prev.filter((cat) => cat.id !== categoryToDelete)
            );
            setIsDeleteModalOpen(false);
        } catch (err) {
            console.error("Failed to delete category", err);
        }
    };

    return (
        <div className="rounded-3xl min-h-screen bg-gradient-to-r from-blue-50 to-indigo-100 p-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-gray-700">Manage Categories</h1>
                <div className="flex space-x-4">
                    <button
                        onClick={() => router.push("/admin/dashboard")}
                        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
                    >
                        Back to Dashboard
                    </button>
                    <button
                        onClick={() => router.push("/admin/create-category")}
                        className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                    >
                        Create New Category
                    </button>
                </div>
            </div>

            {/* Search */}
            <div className="bg-white p-4 shadow-md rounded-lg mb-4 flex items-center gap-4">
                <input
                    type="text"
                    placeholder="Search by title..."
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
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            ID
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Title
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Description
                        </th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Actions
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredCategories.map((category) => (
                        <tr key={category.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 border-b text-gray-700">{category.id}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{category.title}</td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {category.description.length > 50
                                    ? `${category.description.slice(0, 50)}...`
                                    : category.description}
                            </td>
                            <td className="px-6 py-4 border-b">
                                <button
                                    onClick={() =>
                                        router.push(`/admin/edit-category/${category.id}`)
                                    }
                                    className="text-indigo-600 hover:underline mr-4"
                                >
                                    Edit
                                </button>
                                <button
                                    onClick={() => openDeleteModal(category.id)}
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
                            Are you sure you want to delete this category? This action cannot be undone.
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

export default AdminCategoriesPage;
