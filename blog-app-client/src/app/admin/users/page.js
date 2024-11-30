"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";
import { CheckCircleIcon } from "@heroicons/react/24/outline"

const AdminUsersPage = () => {
    const [users, setUsers] = useState([]);
    const [filteredUsers, setFilteredUsers] = useState([]);
    const [search, setSearch] = useState("");
    const [searchBy, setSearchBy] = useState("name");
    const [filters, setFilters] = useState({
        idFrom: "",
        idTo: "",
        createFrom: "",
        createTo: "",
        updateFrom: "",
        updateTo: "",
    });

    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [userToDelete, setUserToDelete] = useState(null);
    const [isFilterOpen, setIsFilterOpen] = useState(false);
    const [successModalOpen, setSuccessModalOpen] = useState(false);

    const [sortConfig, setSortConfig] = useState({ key: "id", direction: "asc" });
    const [usersPerPage, setUsersPerPage] = useState(3);
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
                const allUsers = await fetcher("/users", { headers });

                setUsers(allUsers);
                setFilteredUsers(allUsers);
            } catch (err) {
                console.error("Failed to fetch users", err);
            }
        };

        fetchData();
    }, []);

    const applySearchFilter = (data) => {
        const lowerCaseSearch = search.toLowerCase();
        return data.filter((user) => {
            switch (searchBy) {
                case "name":
                    return user.name.toLowerCase().includes(lowerCaseSearch);
                case "email":
                    return user.email.toLowerCase().includes(lowerCaseSearch);
                case "about":
                    return user.about?.toLowerCase().includes(lowerCaseSearch);
                default:
                    return true;
            }
        });
    };

    const handleSearch = () => {
        const filtered = applySearchFilter(users);
        setFilteredUsers(filtered);
        setCurrentPage(1); // Reset to the first page
    };

    const handleFilter = () => {
        const { idFrom, idTo, createFrom, createTo, updateFrom, updateTo } = filters;

        let filtered = applySearchFilter(users);

        filtered = filtered.filter((user) => {
            return (
                (!idFrom || user.id >= parseInt(idFrom)) &&
                (!idTo || user.id <= parseInt(idTo)) &&
                (!createFrom || new Date(user.create_at) >= new Date(createFrom)) &&
                (!createTo || new Date(user.create_at) <= new Date(createTo)) &&
                (!updateFrom || new Date(user.update_at) >= new Date(updateFrom)) &&
                (!updateTo || new Date(user.update_at) <= new Date(updateTo))
            );
        });

        setFilteredUsers(filtered);
        setCurrentPage(1); // Reset to the first page
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
        setFilteredUsers(users);
    };

    const openDeleteModal = (userId) => {
        setUserToDelete(userId);
        setIsDeleteModalOpen(true);
    };

    const handleDelete = async () => {
        try {
            const adminToken = localStorage.getItem("adminToken");
            const headers = { Authorization: `Bearer ${adminToken}` };

            await fetcher(`/users/${userToDelete}`, { method: "DELETE", headers });

            const updatedUsers = users.filter((user) => user.id !== userToDelete);
            setUsers(updatedUsers);
            setFilteredUsers(updatedUsers);

            setIsDeleteModalOpen(false);
            setSuccessModalOpen(true);

            setTimeout(() => setSuccessModalOpen(false), 3000); // Hide modal after 3 seconds
        } catch (err) {
            console.error("Failed to delete user", err);
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

    const sortedUsers = [...filteredUsers].sort((a, b) => {
        const { key, direction } = sortConfig;
        if (a[key] < b[key]) return direction === "asc" ? -1 : 1;
        if (a[key] > b[key]) return direction === "asc" ? 1 : -1;
        return 0;
    });

    const totalPages = usersPerPage === "all" ? 1 : Math.ceil(sortedUsers.length / usersPerPage);
    const paginatedUsers =
        usersPerPage === "all"
            ? sortedUsers
            : sortedUsers.slice((currentPage - 1) * usersPerPage, currentPage * usersPerPage);

    return (
        <div className="rounded-3xl min-h-screen bg-gradient-to-r from-blue-50 to-indigo-100 p-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-gray-700">Manage Users</h1>
                <div className="flex space-x-4">
                    <button
                        onClick={() => router.push("/admin/dashboard")}
                        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
                    >
                        Back to Dashboard
                    </button>
                    <button
                        onClick={() => router.push("/admin/users/create")}
                        className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                    >
                        Create New User
                    </button>
                </div>
            </div>

            {/* Search, Filter, View Per Page */}
            <div className="flex justify-between items-center bg-white p-4 shadow-md rounded-lg mb-4">
                <div className="flex items-center gap-4">
                    <select
                        value={searchBy}
                        onChange={(e) => setSearchBy(e.target.value)}
                        className="border border-gray-300 rounded-md p-2"
                    >
                        <option value="name">Search by Name</option>
                        <option value="email">Search by Email</option>
                        <option value="about">Search by About</option>
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

                <div className="flex items-center gap-2">
                    <span>Users per page:</span>
                    <select
                        value={usersPerPage}
                        onChange={(e) => {
                            const value = e.target.value === "all" ? "all" : parseInt(e.target.value);
                            setUsersPerPage(value);
                            setCurrentPage(1); // Reset to the first page
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
                        {["id", "name", "email", "about", "create_at", "update_at"].map((key) => (
                            <th
                                key={key}
                                className={`px-6 py-3 border-b text-sm font-medium text-gray-600 cursor-pointer ${
                                    key === "id" ? "inline-flex items-center" : ""
                                }`}
                                onClick={() => handleSort(key)}
                            >
                                <span>{key.replace(/_/g, " ").toUpperCase()}</span>
                                {sortConfig.key === key && (
                                    <span className="ml-1">{sortConfig.direction === "asc" ? "▲" : "▼"}</span>
                                )}
                            </th>
                        ))}
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                            Actions
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {paginatedUsers.map((user) => (
                        <tr key={user.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 border-b text-gray-700">{user.id}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{user.name}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{user.email}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{user.about}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{user.create_at}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{user.update_at}</td>
                            <td className="px-6 py-4 border-b">
                                <button
                                    onClick={() => router.push(`/admin/users/edit/${user.id}`)}
                                    className="text-indigo-600 hover:underline mr-4"
                                >
                                    Edit
                                </button>
                                <button
                                    onClick={() => openDeleteModal(user.id)}
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
            {usersPerPage !== "all" && totalPages > 1 && (
                <div className="flex justify-center mt-6">
                    <button
                        onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                        disabled={currentPage === 1}
                        className="px-4 py-2 border rounded-md mr-2 hover:bg-gray-200 disabled:opacity-50"
                    >
                        Previous
                    </button>
                    {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
                        <button
                            key={page}
                            onClick={() => setCurrentPage(page)}
                            className={`px-4 py-2 border rounded-md mx-1 ${
                                currentPage === page ? "bg-blue-600 text-white" : "hover:bg-gray-200"
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
                            Are you sure you want to delete this user? This action cannot be undone.
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
                        <CheckCircleIcon className="h-12 w-12 text-green-500 mb-4" /> {/* Icon Heroicons */}
                        <h2 className="text-lg font-bold text-gray-800 text-center">
                            Successfully Deleted!
                        </h2>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminUsersPage;
