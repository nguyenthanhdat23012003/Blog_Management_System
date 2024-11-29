"use client";

import React, { useEffect, useState } from "react";
import { fetcher } from "@/services/api";
import { DocumentTextIcon, Squares2X2Icon, UserGroupIcon, DocumentDuplicateIcon } from "@heroicons/react/24/solid";

const AdminDashboard = () => {
    const [data, setData] = useState({
        posts: 0,
        categories: 0,
        users: 0,
        series: 0,
        latestPosts: [],
        latestSeries: [],
    });

    useEffect(() => {
        const fetchDashboardData = async () => {
            try {
                const adminToken = localStorage.getItem("adminToken");

                if (!adminToken) {
                    console.error("Admin token is missing.");
                    return;
                }

                const headers = {
                    Authorization: `Bearer ${adminToken}`,
                };

                const posts = await fetcher("/blogs", { headers });
                const categories = await fetcher("/categories", { headers });
                const users = await fetcher("/users", { headers });
                const series = await fetcher("/series", { headers });

                const latestPosts = posts.slice(0, 3);
                const latestSeries = series.slice(0, 3);

                setData({
                    posts: posts.length,
                    categories: categories.length,
                    users: users.length,
                    series: series.length,
                    latestPosts,
                    latestSeries,
                });
            } catch (err) {
                console.error("Failed to fetch dashboard data", err);
            }
        };

        fetchDashboardData();
    }, []);

    return (
        <div className="rounded-3xl min-h-screen bg-gradient-to-r from-blue-50 to-indigo-100 flex">
            {/* Sidebar */}
            <aside className="w-1/4 bg-gradient-to-br from-blue-400 to-indigo-600 text-white p-6 rounded-3xl shadow-md">
                <h1 className="text-3xl font-bold mb-8">VieInsights.</h1>
                <nav className="space-y-6">
                    <a href="/admin/dashboard" className="block font-medium hover:text-indigo-200">
                        Dashboard
                    </a>
                    <a href="/admin/posts" className="block font-medium hover:text-indigo-200">
                        Posts
                    </a>
                    <a href="/admin/categories" className="block font-medium hover:text-indigo-200">
                        Categories
                    </a>
                    <a href="/admin/series" className="block font-medium hover:text-indigo-200">
                        Series
                    </a>
                    <a href="/admin/users" className="block font-medium hover:text-indigo-200">
                        Users
                    </a>
                    <a href="/admin/settings" className="block font-medium hover:text-indigo-200">
                        Settings
                    </a>
                </nav>
                <div className="mt-16 bg-blue-500 p-4 rounded-lg text-sm">
                    <p>User Guide Documentation</p>
                </div>
            </aside>

            {/* Main Content */}
            <main className="flex-1 p-8">
                <div className="flex items-center justify-between mb-8">
                    <h2 className="text-2xl font-bold text-gray-700">Dashboard</h2>
                    <div className="flex items-center space-x-4">
                        <img
                            src="https://via.placeholder.com/40"
                            alt="Admin Profile"
                            className="w-10 h-10 rounded-full"
                        />
                        <div>
                            <p className="font-medium text-gray-800">Admin</p>
                            <p className="text-sm text-gray-500">Administrator</p>
                        </div>
                    </div>
                </div>

                {/* Stats */}
                <div className="grid grid-cols-4 gap-8 mb-8">
                    <div className="bg-white p-6 shadow rounded-lg text-center flex flex-col items-center">
                        <DocumentTextIcon className="w-10 h-10 text-indigo-600 mb-4" />
                        <p className="text-gray-600 mb-2">Posts</p>
                        <h3 className="text-3xl font-bold text-gray-800">{data.posts}</h3>
                    </div>
                    <div className="bg-white p-6 shadow rounded-lg text-center flex flex-col items-center">
                        <Squares2X2Icon className="w-10 h-10 text-indigo-600 mb-4" />
                        <p className="text-gray-600 mb-2">Categories</p>
                        <h3 className="text-3xl font-bold text-gray-800">{data.categories}</h3>
                    </div>
                    <div className="bg-white p-6 shadow rounded-lg text-center flex flex-col items-center">
                        <DocumentDuplicateIcon className="w-10 h-10 text-indigo-600 mb-4" />
                        <p className="text-gray-600 mb-2">Series</p>
                        <h3 className="text-3xl font-bold text-gray-800">{data.series}</h3>
                    </div>
                    <div className="bg-white p-6 shadow rounded-lg text-center flex flex-col items-center">
                        <UserGroupIcon className="w-10 h-10 text-indigo-600 mb-4" />
                        <p className="text-gray-600 mb-2">Users</p>
                        <h3 className="text-3xl font-bold text-gray-800">{data.users}</h3>
                    </div>
                </div>

                {/* Visitor Growth */}
                <div className="bg-white p-6 shadow rounded-lg mb-8">
                    <h3 className="text-lg font-bold mb-4 text-gray-700">Visitor Growth</h3>
                    <p className="text-sm text-gray-500 mb-2">Overall Information</p>
                    <div className="flex justify-end space-x-4">
                        <button className="px-4 py-2 text-sm rounded bg-blue-100 text-blue-600 font-medium">
                            Monthly
                        </button>
                        <button className="px-4 py-2 text-sm rounded bg-gray-100 text-gray-500 font-medium">
                            Yearly
                        </button>
                    </div>
                    <div className="mt-4">
                        <div className="h-24 bg-gray-100"></div> {/* Placeholder for chart */}
                    </div>
                </div>

                {/* Latest Posts & Series */}
                <div className="grid grid-cols-2 gap-8">
                    <div>
                        <h3 className="text-lg font-bold mb-4 text-gray-700">Latest Posts</h3>
                        <ul className="space-y-4">
                            {data.latestPosts.map((post) => (
                                <li key={post.id} className="bg-white p-4 shadow rounded-lg">
                                    <p className="font-medium text-gray-800">{post.title}</p>
                                    <p className="text-sm text-gray-500">{post.create_at}</p>
                                </li>
                            ))}
                        </ul>
                    </div>
                    <div>
                        <h3 className="text-lg font-bold mb-4 text-gray-700">Latest Series</h3>
                        <ul className="space-y-4">
                            {data.latestSeries.map((serie) => (
                                <li key={serie.id} className="bg-white p-4 shadow rounded-lg">
                                    <p className="font-medium text-gray-800">{serie.title}</p>
                                    <p className="text-sm text-gray-500">{serie.create_at}</p>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default AdminDashboard;
