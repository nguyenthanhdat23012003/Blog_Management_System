"use client";

import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";
import { fetcher } from "@/services/api";

const EditUserPage = () => {
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        roleIds: [], // Vai trò được chọn
        about: "",
    });
    const [roles, setRoles] = useState([]); // Danh sách các vai trò
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [updating, setUpdating] = useState(false);
    const router = useRouter();
    const { id: userId } = useParams();

    // Lấy danh sách role và thông tin người dùng
    useEffect(() => {
        const fetchData = async () => {
            const adminToken = localStorage.getItem("adminToken");
            if (!adminToken) {
                router.push("/admin"); // Chuyển hướng đến trang đăng nhập admin
                return;
            }

            try {
                // Lấy thông tin user
                const userResponse = await fetcher(`/users/${userId}`, {
                    headers: { Authorization: `Bearer ${adminToken}` },
                });

                // Lấy danh sách roles
                const rolesResponse = await fetcher(`/roles`, {
                    headers: { Authorization: `Bearer ${adminToken}` },
                });

                setFormData({
                    name: userResponse.name || "",
                    email: userResponse.email || "",
                    password: "********", // Hiển thị password dạng ẩn
                    roleIds: userResponse.roleIds || [],
                    about: userResponse.about || "",
                });

                setRoles(rolesResponse);
            } catch (err) {
                setError("Unable to fetch user or roles. Please try again.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [userId, router]);

    // Xử lý thay đổi dữ liệu form
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Xử lý thay đổi role
    const handleRoleChange = (e) => {
        const selectedRoles = Array.from(e.target.selectedOptions, (option) => Number(option.value));
        setFormData({ ...formData, roleIds: selectedRoles });
    };

    // Gửi dữ liệu cập nhật
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setUpdating(true);

        try {
            const adminToken = localStorage.getItem("adminToken");
            if (!adminToken) {
                setError("Admin not authenticated. Please log in.");
                setUpdating(false);
                return;
            }

            // Gửi yêu cầu cập nhật
            await fetcher(`/users/${userId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${adminToken}`,
                },
                body: JSON.stringify({
                    name: formData.name,
                    roleIds: formData.roleIds,
                    about: formData.about,
                }),
            });

            router.push("/admin/users"); // Chuyển về danh sách người dùng sau khi cập nhật
        } catch (err) {
            setError("Failed to update user. Please try again.");
        } finally {
            setUpdating(false);
        }
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <p>Loading...</p>
            </div>
        );
    }

    return (
        <div className="rounded-xl flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
            <button
                onClick={() => router.push("/admin/users")}
                className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400 my-10"
            >
                Back to User List
            </button>
            <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
                <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">Edit User (Admin)</h2>
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
                            disabled
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 bg-gray-200"
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
                            disabled
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 bg-gray-200"
                        />
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
                    <button
                        type="submit"
                        disabled={updating}
                        className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    >
                        {updating ? "Updating..." : "Update User"}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default EditUserPage;
