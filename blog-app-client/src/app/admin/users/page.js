"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";

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
                setFilteredUsers(allUsers); // Initialize filtered users
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
                default:
                    return true;
            }
        });
    };

    const handleSearch = () => {
        const filtered = applySearchFilter(users);
        setFilteredUsers(filtered);
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

            if (!adminToken) {
                console.error("Admin token is missing.");
                return;
            }

            const headers = { Authorization: `Bearer ${adminToken}` };

            await fetcher(`/users/${userToDelete}`, {
                method: "DELETE",
                headers,
            });

            setUsers((prev) => prev.filter((user) => user.id !== userToDelete));
            setFilteredUsers((prev) => prev.filter((user) => user.id !== userToDelete));
            setIsDeleteModalOpen(false);
        } catch (err) {
            console.error("Failed to delete user", err);
        }
    };

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
                        onClick={() => router.push("/admin/create-user")}
                        className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
                    >
                        Create New User
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
                    <option value="name">Search by Name</option>
                    <option value="email">Search by Email</option>
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
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">ID</th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">Name</th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">Email</th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">About</th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">Created At</th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">Updated At</th>
                        <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredUsers.map((user) => (
                        <tr key={user.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 border-b text-gray-700">{user.id}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{user.name}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{user.email}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{user.about}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{user.create_at}</td>
                            <td className="px-6 py-4 border-b text-gray-700">{user.update_at}</td>
                            <td className="px-6 py-4 border-b">
                                <button
                                    onClick={() => router.push(`/admin/edit-user/${user.id}`)}
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
        </div>
    );
};

export default AdminUsersPage;
