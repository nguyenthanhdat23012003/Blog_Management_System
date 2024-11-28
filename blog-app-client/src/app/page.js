"use client";

import { useEffect, useState } from "react";
import { fetcher } from "@/services/api";
import Link from "next/link";

export default function HomePage() {
    const [blogs, setBlogs] = useState([]);
    const [categories, setCategories] = useState([]);
    const [series, setSeries] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);

                const [allBlogs, allCategories, allSeries] = await Promise.all([
                    fetcher("/blogs"),
                    fetcher("/categories"),
                    fetcher("/series"),
                ]);

                // Lấy 5 blog mới nhất
                setBlogs(allBlogs.slice(0, 5));

                // Lấy toàn bộ categories
                setCategories(allCategories);

                // Lấy 5 series mới nhất
                setSeries(allSeries.slice(0, 5));

                setLoading(false);
            } catch (error) {
                console.error("Error fetching data:", error);
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return <div className="text-center py-12">Loading...</div>;
    }

    return (
        <div className="rounded-xl bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen py-12">
            <div className="container mx-auto px-6">
                {/* Hero Section */}
                <section className="mb-12 text-center">
                    <h1 className="text-4xl font-bold text-gray-800 mb-4">Welcome to Tech Blog</h1>
                    <p className="text-gray-600">
                        Explore the latest blogs, categories, and series on technology, programming, and more.
                    </p>
                </section>

                {/* Latest Blogs */}
                <section className="mb-12">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">Latest Blogs</h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {blogs.map((blog) => (
                            <div
                                key={blog.id}
                                className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition"
                            >
                                <h3 className="text-lg font-semibold text-gray-800">{blog.title}</h3>
                                <p className="text-gray-600 mt-2">
                                    {blog.summary || "No summary available."}
                                </p>
                                <Link
                                    href={`/blogs/${blog.id}`}
                                    className="text-blue-600 hover:underline mt-4 block"
                                >
                                    Read more
                                </Link>
                            </div>
                        ))}
                    </div>
                </section>

                {/* Categories */}
                <section className="mb-12">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">Categories</h2>
                    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                        {categories.map((category) => (
                            <div
                                key={category.id}
                                className="bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition"
                            >
                                <Link href={`/category/${category.id}`}>
                                    <h3 className="text-lg font-semibold text-gray-800 hover:text-blue-600">
                                        {category.title}
                                    </h3>
                                </Link>
                            </div>
                        ))}
                    </div>
                </section>

                {/* Latest Series */}
                <section>
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">Latest Series</h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {series.map((seriesItem) => (
                            <div
                                key={seriesItem.id}
                                className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition"
                            >
                                <h3 className="text-lg font-semibold text-gray-800">{seriesItem.title}</h3>
                                <p className="text-gray-600 mt-2">
                                    {seriesItem.description || "No description available."}
                                </p>
                                <Link
                                    href={`/series/${seriesItem.id}`}
                                    className="text-blue-600 hover:underline mt-4 block"
                                >
                                    Explore Series
                                </Link>
                            </div>
                        ))}
                    </div>
                </section>
            </div>
        </div>
    );
}
