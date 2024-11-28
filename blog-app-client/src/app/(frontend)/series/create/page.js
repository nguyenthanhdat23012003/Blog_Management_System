"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";

const CreateSeriesPage = () => {
    const [formData, setFormData] = useState({
        title: "",
        description: "",
    });
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);
    const router = useRouter();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setSuccess(false);

        try {
            const token = localStorage.getItem("token"); // Get token from local storage
            if (!token) {
                setError("User not authenticated. Please log in.");
                return;
            }

            const response = await fetcher("/series", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`, // Attach Bearer token
                },
                body: JSON.stringify(formData),
            });

            setSuccess(true);

            // Redirect to the created series page
            router.push(`/series/${response.id}`);
        } catch (err) {
            setError(err.message || "Something went wrong!");
        }
    };

    return (
        <div className="rounded-xl flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
            <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
                <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">Create Series</h2>
                {error && (
                    <div className="mb-4 text-red-600 bg-red-50 border border-red-400 rounded-lg p-4">
                        {error}
                    </div>
                )}
                {success && (
                    <div className="mb-4 text-green-600 bg-green-50 border border-green-400 rounded-lg p-4">
                        Series created successfully! Redirecting...
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
                    <button
                        type="submit"
                        className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                    >
                        Create Series
                    </button>
                </form>
            </div>
        </div>
    );
};

export default CreateSeriesPage;
