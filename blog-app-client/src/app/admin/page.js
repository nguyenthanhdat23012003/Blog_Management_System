"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { jwtDecode } from "jwt-decode";
import { fetcher } from "@/services/api";
import { useAuth } from "@/context/AuthContext";

const AdminLoginPage = () => {
    const [formData, setFormData] = useState({ email: "", password: "" });
    const [error, setError] = useState(null);
    const router = useRouter();
    const { loginAdmin } = useAuth();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        try {
            const response = await fetcher("/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData),
            });

            const adminToken = response.token;
            console.log(adminToken);
            const decoded = jwtDecode(adminToken);

            // Check if user ID is 1 (admin ID)
            if (decoded.id === 1) {
                loginAdmin(adminToken);
                router.push("/admin/dashboard"); // Redirect to dashboard
            } else {
                setError("You do not have access to the admin panel.");
            }
        } catch (err) {
            console.error("Login failed:", err);
            setError("Invalid email or password.");
        }
    };

    return (
        <div className="rounded-lg flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
            <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
                <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">Admin Log in</h2>
                {error && (
                    <div className="mb-4 text-red-600 bg-red-50 border border-red-400 rounded-lg p-4">
                        {error}
                    </div>
                )}
                <form onSubmit={handleSubmit} className="space-y-6">
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
                            placeholder="Enter your email"
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
                            placeholder="Enter your password"
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    >
                        Log in
                    </button>
                </form>
            </div>
        </div>
    );
};

export default AdminLoginPage;
