"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";
import { CheckCircleIcon } from "@heroicons/react/24/outline";

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
    const [successModalOpen, setSuccessModalOpen] = useState(false);

    const [sortConfig, setSortConfig] = useState({ key: "id", direction: "asc" });
    const [categoriesPerPage, setCategoriesPerPage] = useState(3);
    const [currentPage, setCurrentPage] = useState(1);

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
                setFilteredCategories(allCategories);
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
        setCurrentPage(1);
    };

    const handleFilter = () => {
        const { idFrom, idTo } = filters;

        let filtered = categories.filter((category) => {
            return (
                (!idFrom || category.id >= parseInt(idFrom)) &&
                (!idTo || category.id <= parseInt(idTo))
            );
        });

        if (search) {
            filtered = filtered.filter((category) =>
                category.title.toLowerCase().includes(search.toLowerCase())
            );
        }

        setFilteredCategories(filtered);
        setCurrentPage(1);
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
            const headers = { Authorization: `Bearer ${adminToken}` };

            await fetcher(`/categories/${categoryToDelete}`, {
                method: "DELETE",
                headers,
            });

            const updatedCategories = categories.filter((cat) => cat.id !== categoryToDelete);
            setCategories(updatedCategories);
            setFilteredCategories(updatedCategories);

            setIsDeleteModalOpen(false);
            setSuccessModalOpen(true);

            setTimeout(() => setSuccessModalOpen(false), 3000);
        } catch (err) {
            console.error("Failed to delete category", err);
        }
    };

    const handleSort = (key) => {
        if (sortConfig.key === key) {
            setSortConfig((prev) => ({
                key,
                direction: prev.direction === "asc" ? "desc" : "asc",
            }));
        } else {
            setSortConfig({ key, direction: "asc" });
        }
    };

    const generatePagination = () => {
        const visiblePages = 5; // Số trang tối đa được hiển thị
        let pages = [];
    
        if (totalPages <= visiblePages) {
            // Hiển thị tất cả các trang nếu ít hơn hoặc bằng 5
            pages = Array.from({ length: totalPages }, (_, i) => i + 1);
        } else {
            // Hiển thị trang đầu, trang cuối và dấu "..."
            if (currentPage <= Math.ceil(visiblePages / 2)) {
                pages = [...Array.from({ length: visiblePages - 1 }, (_, i) => i + 1), '...', totalPages];
            } else if (currentPage > totalPages - Math.floor(visiblePages / 2)) {
                pages = [1, '...', ...Array.from({ length: visiblePages - 1 }, (_, i) => totalPages - visiblePages + i + 2)];
            } else {
                pages = [
                    1,
                    '...',
                    currentPage - 1,
                    currentPage,
                    currentPage + 1,
                    '...',
                    totalPages,
                ];
            }
        }
    
        return pages;
    };
    

    const sortedCategories = [...filteredCategories].sort((a, b) => {
        const { key, direction } = sortConfig;
        if (a[key] < b[key]) return direction === "asc" ? -1 : 1;
        if (a[key] > b[key]) return direction === "asc" ? 1 : -1;
        return 0;
    });

    const totalPages =
        categoriesPerPage === "all"
            ? 1
            : Math.ceil(sortedCategories.length / categoriesPerPage);
    const paginatedCategories =
        categoriesPerPage === "all"
            ? sortedCategories
            : sortedCategories.slice(
                (currentPage - 1) * categoriesPerPage,
                currentPage * categoriesPerPage
            );

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
                        onClick={() => router.push("/admin/categories/create")}
                        className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                    >
                        Create New Category
                    </button>
                </div>
            </div>

            {/* Search, Filter, View Per Page */}
            <div className="flex justify-between items-center bg-white p-4 shadow-md rounded-lg mb-4 gap-4">
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
                <div className="flex items-center gap-2">
                    <span>Categories per page:</span>
                    <select
                        value={categoriesPerPage}
                        onChange={(e) => {
                            const value =
                                e.target.value === "all" ? "all" : parseInt(e.target.value);
                            setCategoriesPerPage(value);
                            setCurrentPage(1);
                        }}
                        className="border border-gray-300 rounded-md p-2"
                    >
                        <option value="3">3</option>
                        <option value="6">6</option>
                        <option value="9">9</option>
                        <option value="all">All</option>
                    </select>
                </div>
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
                                onChange={(e) =>
                                    setFilters({ ...filters, idFrom: e.target.value })
                                }
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
                                onChange={(e) =>
                                    setFilters({ ...filters, idTo: e.target.value })
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
                        {["id", "title", "description"].map((key) => (
                            <th
                                key={key}
                                className={`px-6 py-3 border-b text-sm font-medium text-gray-600 cursor-pointer ${
                                    key === "id" ? "inline-flex items-center" : ""
                                }`}
                                onClick={() => handleSort(key)}
                            >
                                <span>{key.toUpperCase()}</span>
                                {sortConfig.key === key && (
                                    <span className="ml-1">
                                            {sortConfig.direction === "asc" ? "▲" : "▼"}
                                        </span>
                                )}
                            </th>
                        ))}
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Actions
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {paginatedCategories.map((category) => (
                        <tr key={category.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 border-b text-gray-700">
                                {category.id}
                            </td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {category.title}
                            </td>
                            <td className="px-6 py-4 border-b text-gray-700">
                                {category.description.length > 50
                                    ? `${category.description.slice(0, 50)}...`
                                    : category.description}
                            </td>
                            <td className="px-6 py-4 border-b">
                                <button
                                    onClick={() =>
                                        router.push(`/admin/categories/edit/${category.id}`)
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

            {/* Pagination */}
            {categoriesPerPage !== "all" && totalPages > 1 && (
                <div className="flex justify-center mt-6">
                <button
                    onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                    disabled={currentPage === 1}
                    className="px-4 py-2 border rounded-md mr-2 hover:bg-gray-200 disabled:opacity-50"
                >
                    Previous
                </button>
                {generatePagination().map((page, index) => (
                    <button
                        key={index}
                        disabled={page === "..."}
                        onClick={() => page !== "..." && setCurrentPage(page)}
                        className={`px-4 py-2 border rounded-md mx-1 ${
                            currentPage === page
                                ? "bg-blue-600 text-white"
                                : "hover:bg-gray-200"
                        }`}
                    >
                        {page}
                    </button>
                ))}
                <button
                    onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
                    disabled={currentPage === totalPages}
                    className="px-4 py-2 border rounded-md ml-2 hover:bg-gray-200 disabled:opacity-50"
                >
                    Next
                </button>
            </div>
            
            )}

            {/* Delete Modal */}
            {isDeleteModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
                    <div className="bg-white p-6 rounded-lg shadow-md w-96">
                        <h2 className="text-lg font-bold mb-4 text-gray-800">Confirm Delete</h2>
                        <p className="text-sm text-gray-600 mb-6">
                            Are you sure you want to delete this category? This action cannot be
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

            {/* Success Modal */}
            {successModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
                    <div className="bg-white p-6 rounded-lg shadow-md w-96 flex flex-col items-center justify-center">
                        <CheckCircleIcon className="h-12 w-12 text-green-500 mb-4" />
                        <h2 className="text-lg font-bold text-gray-800 text-center">
                            Successfully Deleted!
                        </h2>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminCategoriesPage;
