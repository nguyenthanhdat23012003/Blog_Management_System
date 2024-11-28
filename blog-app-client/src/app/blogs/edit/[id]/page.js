'use client';

import React, { useEffect, useRef, useState } from 'react';
import EditorJS from '@editorjs/editorjs';
import Header from '@editorjs/header';
import List from '@editorjs/list';
import Paragraph from '@editorjs/paragraph';
import ImageTool from '@editorjs/image';
import { fetcher } from '@/services/api';
import { useRouter, useParams } from 'next/navigation';
import { jwtDecode } from 'jwt-decode';

const EditBlogPage = () => {
    const editorRef = useRef(null);
    const router = useRouter();
    const { id: blogId } = useParams(); // Get blogId from dynamic route

    const [title, setTitle] = useState('');
    const [categories, setCategories] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [series, setSeries] = useState([]);
    const [selectedSeries, setSelectedSeries] = useState(null);
    const [error, setError] = useState(null);
    const [userId, setUserId] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Decode token to get userId
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decoded = jwtDecode(token);
                setUserId(decoded.userId || decoded.id); // Adjust based on backend implementation
            } catch (err) {
                console.error('Failed to decode token:', err);
            }
        }
    }, []);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const data = await fetcher('/categories');
                setCategories(data);
            } catch (err) {
                console.error('Failed to fetch categories:', err);
            }
        };

        const fetchSeries = async () => {
            if (!userId) return;
            try {
                const data = await fetcher(`/series/users/${userId}`);
                setSeries(data);
            } catch (err) {
                console.error('Failed to fetch series:', err);
            }
        };

        const fetchBlogDetails = async () => {
            try {
                const data = await fetcher(`/blogs/${blogId}`);
                setTitle(data.title);
                setSelectedCategories(data.categoryIds || []);
                setSelectedSeries(data.seriesId || null);

                // Delay initialization of EditorJS until DOM has rendered
                setTimeout(() => {
                    if (document.getElementById('editorjs') && !editorRef.current) {
                        editorRef.current = new EditorJS({
                            holder: 'editorjs',
                            tools: {
                                header: Header,
                                paragraph: Paragraph,
                                list: List,
                                image: {
                                    class: ImageTool,
                                    config: {
                                        endpoints: {
                                            byFile: '/upload/image',
                                            byUrl: '/upload/image-url',
                                        },
                                    },
                                },
                            },
                            data: data.content,
                            placeholder: 'Start building your content here...',
                        });
                    }
                }, 0);

                setLoading(false);
            } catch (err) {
                console.error('Failed to fetch blog details:', err);
                setError('Failed to load blog details.');
                setLoading(false);
            }
        };

        fetchCategories();
        fetchSeries();
        if (blogId) fetchBlogDetails();

        return () => {
            if (editorRef.current?.destroy) {
                editorRef.current.destroy();
            }
        };
    }, [userId, blogId]);

    const handleAddCategory = (categoryId) => {
        if (!selectedCategories.includes(categoryId)) {
            setSelectedCategories((prev) => [...prev, categoryId]);
        }
    };

    const handleRemoveCategory = (categoryId) => {
        setSelectedCategories((prev) => prev.filter((id) => id !== categoryId));
    };

    const handleSubmit = async () => {
        if (!editorRef.current || !title) {
            setError('Title and content are required!');
            return;
        }

        try {
            const outputData = await editorRef.current.save();

            const blogData = {
                title,
                content: outputData,
                categoryIds: selectedCategories,
                seriesId: selectedSeries,
            };

            await fetcher(`/blogs/${blogId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
                body: JSON.stringify(blogData),
            });

            router.push(`/blogs/${blogId}`);
        } catch (err) {
            console.error('Failed to update blog:', err);
            setError('Failed to update blog.');
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
            <div className="bg-white shadow-xl rounded-lg p-8 max-w-4xl w-full my-20">
                <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">
                    Edit Blog
                </h2>
                {error && (
                    <div className="mb-4 text-red-600 bg-red-50 border border-red-400 rounded-lg p-4">
                        {error}
                    </div>
                )}

                {/* Blog Title */}
                <div className="mb-6">
                    <label
                        htmlFor="title"
                        className="block text-lg font-semibold text-gray-800"
                    >
                        Blog Title
                    </label>
                    <input
                        type="text"
                        id="title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="Enter your blog title"
                        className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                    />
                </div>

                {/* Add Category */}
                <div className="mb-6">
                    <label
                        htmlFor="category"
                        className="block text-lg font-semibold text-gray-800"
                    >
                        Categories
                    </label>
                    <div className="mt-2 flex flex-wrap gap-2">
                        {categories.map((category) => (
                            <button
                                key={category.id}
                                onClick={() =>
                                    selectedCategories.includes(category.id)
                                        ? handleRemoveCategory(category.id)
                                        : handleAddCategory(category.id)
                                }
                                className={`px-4 py-2 rounded-lg border ${
                                    selectedCategories.includes(category.id)
                                        ? 'bg-indigo-600 text-white'
                                        : 'bg-gray-100 text-gray-800'
                                }`}
                            >
                                {category.title}
                            </button>
                        ))}
                    </div>
                </div>

                {/* Add Series */}
                <div className="mb-6">
                    <label
                        htmlFor="series"
                        className="block text-lg font-semibold text-gray-800"
                    >
                        Series
                    </label>
                    <select
                        id="series"
                        value={selectedSeries || ''} // Handle null value
                        onChange={(e) => setSelectedSeries(e.target.value || null)}
                        className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                    >
                        <option value="">Select a series (optional)</option>
                        {series.map((serie) => (
                            <option key={serie.id} value={serie.id}>
                                {serie.title}
                            </option>
                        ))}
                    </select>
                </div>

                {/* EditorJS Container */}
                <div
                    id="editorjs"
                    className="border rounded-lg p-4 bg-gray-50 mb-6"
                ></div>

                {/* Submit Button */}
                <button
                    onClick={handleSubmit}
                    className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                >
                    Update Blog
                </button>
            </div>
        </div>
    );
};

export default EditBlogPage;
