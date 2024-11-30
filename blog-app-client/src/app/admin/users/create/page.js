"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";

const CreateUserPage = () => {
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        roleIds: [], // Mảng chứa các ID role được chọn
        about: "",
    });
    const [roles, setRoles] = useState([]); // Lưu danh sách role từ API
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const router = useRouter();

    // Lấy danh sách các role khi component được mount
    useEffect(() => {
        const fetchRoles = async () => {
            const adminToken = localStorage.getItem("adminToken");
            if (!adminToken) {
                router.push("/admin"); // Chuyển hướng đến trang đăng nhập admin
                return;
            }

            try {
                const response = await fetcher(`/roles`, {
                    headers: { Authorization: `Bearer ${adminToken}` },
                });
                setRoles(response); // Cập nhật danh sách role
            } catch (err) {
                setError("Unable to fetch roles. Please try again.");
            }
        };

        fetchRoles();
    }, [router]);

    // Xử lý thay đổi form input
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Xử lý chọn role
    const handleRoleChange = (e) => {
        const selectedRoles = Array.from(e.target.selectedOptions, (option) => Number(option.value));
        setFormData({ ...formData, roleIds: selectedRoles });
    };

    // Gửi form để tạo người dùng
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            const adminToken = localStorage.getItem("adminToken");
            if (!adminToken) {
                setError("Admin not authenticated. Please log in.");
                setLoading(false);
                return;
            }

            await fetcher(`/users`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${adminToken}`,
                },
                body: JSON.stringify(formData),
            });

            router.push("/admin/users"); // Chuyển về danh sách người dùng
        } catch (err) {
            setError(err.message || "Failed to create user.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="rounded-xl flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
            <button
                onClick={() => router.push("/admin/users")}
                className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400 my-10"
            >
                Back to User List
            </button>
            <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
                <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">Create New User (Admin)</h2>
                {error && (
                    <div className="mb-4 text-red-600 bg-red-50 border border-red-400 rounded-lg p-4">
                        {error}
                    </div>
                )}
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label
                            htmlFor="name"
                            className="block text-lg font-semibold text-gray-800"
                        >
                            Name
                        </label>
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            required
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                            placeholder="Enter user name"
                        />
                    </div>
                    <div>
                        <label
                            htmlFor="email"
                            className="block text-lg font-semibold text-gray-800"
                        >
                            Email
                        </label>
                        <input
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                            placeholder="Enter user email"
                        />
                    </div>
                    <div>
                        <label
                            htmlFor="password"
                            className="block text-lg font-semibold text-gray-800"
                        >
                            Password
                        </label>
                        <input
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                            placeholder="Enter user password"
                        />
                    </div>
                    <div>
                        <label
                            htmlFor="about"
                            className="block text-lg font-semibold text-gray-800"
                        >
                            About
                        </label>
                        <textarea
                            name="about"
                            value={formData.about}
                            onChange={handleChange}
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                            placeholder="Tell something about the user"
                        ></textarea>
                    </div>
                    <div>
                        <label
                            htmlFor="roleIds"
                            className="block text-lg font-semibold text-gray-800"
                        >
                            Roles
                        </label>
                        <select
                            name="roleIds"
                            multiple
                            value={formData.roleIds}
                            onChange={handleRoleChange}
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                        >
                            {roles.map((role) => (
                                <option key={role.id} value={role.id}>
                                    {role.name}
                                </option>
                            ))}
                        </select>
                        <p className="text-sm text-gray-500 mt-2">
                            Hold <strong>Ctrl</strong> or <strong>Cmd</strong> to select multiple roles.
                        </p>
                    </div>
                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    >
                        {loading ? "Creating..." : "Create User"}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default CreateUserPage;
