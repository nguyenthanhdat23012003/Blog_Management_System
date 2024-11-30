"use client";

import { useEffect, useState } from "react";
import { useRouter, useParams } from "next/navigation";
import { fetcher } from "@/services/api";

const EditSeriesPage = () => {
    const [formData, setFormData] = useState({
        title: "",
        description: "",
        authorId: null, // Selected user
    });
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const router = useRouter();
    const { id: seriesId } = useParams();

    useEffect(() => {
        const fetchSeriesData = async () => {
            const adminToken = localStorage.getItem("adminToken");
            if (!adminToken) {
                router.push("/admin");
                return;
            }

            try {
                // Fetch users for author selection
                const usersData = await fetcher("/users", {
                    headers: { Authorization: `Bearer ${adminToken}` },
                });
                setUsers(usersData);

                // Fetch series details
                const seriesData = await fetcher(`/series/${seriesId}`, {
                    headers: { Authorization: `Bearer ${adminToken}` },
                });
                setFormData({
                    title: seriesData.title || "",
                    description: seriesData.description || "",
                    authorId: seriesData.authorId || null,
                });
                setLoading(false);
            } catch (err) {
                setError(err.message || "Something went wrong!");
                setLoading(false);
            }
        };

        if (seriesId) fetchSeriesData();
    }, [seriesId, router]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        try {
            const adminToken = localStorage.getItem("adminToken");
            if (!adminToken) {
                setError("Admin not authenticated. Please log in.");
                return;
            }

            if (!formData.authorId) {
                setError("Please select an author.");
                return;
            }

            await fetcher(`/series/${seriesId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${adminToken}`,
                },
                body: JSON.stringify(formData),
            });

            router.push(`/admin/series`);
        } catch (err) {
            setError(err.message || "Something went wrong!");
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
                onClick={() => router.push("/admin/series")}
                className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400 my-10"
            >
                Back to Series List
            </button>
            <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
                <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">Edit Series (Admin)</h2>
                {error && (
                    <div className="mb-4 text-red-600 bg-red-50 border border-red-400 rounded-lg p-4">
                        {error}
                    </div>
                )}
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label
                            htmlFor="title"
                            className="block text-lg font-semibold text-gray-800"
                        >
                            Title
                        </label>
                        <input
                            type="text"
                            name="title"
                            value={formData.title}
                            onChange={handleChange}
                            required
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                            placeholder="Enter series title"
                        />
                    </div>
                    <div>
                        <label
                            htmlFor="description"
                            className="block text-lg font-semibold text-gray-800"
                        >
                            Description
                        </label>
                        <textarea
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                            placeholder="Enter series description (optional)"
                        ></textarea>
                    </div>
                    <div>
                        <label
                            htmlFor="authorId"
                            className="block text-lg font-semibold text-gray-800"
                        >
                            Select Author
                        </label>
                        <select
                            name="authorId"
                            value={formData.authorId || ""}
                            onChange={handleChange}
                            required
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                        >
                            <option value="">Select an author</option>
                            {users.map((user) => (
                                <option key={user.id} value={user.id}>
                                    {user.name} - {user.email}
                                </option>
                            ))}
                        </select>
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    >
                        Update Series
                    </button>
                </form>
            </div>
        </div>
    );
};

export default EditSeriesPage;
