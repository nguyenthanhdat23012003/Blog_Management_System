"use client";

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation'; // Sử dụng useParams để lấy dynamic id từ URL
import { fetcher } from '@/services/api';

export default function CategoryPage() {
    const { id } = useParams(); // Lấy id từ dynamic route
    const [blogs, setBlogs] = useState([]);
    const [category, setCategory] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchCategoryData = async () => {
            try {
                setLoading(true);

                // Gọi API để lấy danh sách blog thuộc category
                const blogsData = await fetcher(`/blogs/categories/${id}`);
                setBlogs(blogsData);

                // Gọi API để lấy thông tin chi tiết về category
                const categoryData = await fetcher(`/categories/${id}`);
                setCategory(categoryData);

                setLoading(false);
            } catch (err) {
                console.error('Failed to fetch category or blogs:', err);
                setError('Failed to load category or blogs.');
                setLoading(false);
            }
        };

        if (id) fetchCategoryData();
    }, [id]);

    if (loading) {
        return <div className="container mx-auto py-6">Loading...</div>;
    }

    if (error) {
        return <div className="container mx-auto py-6 text-red-600">{error}</div>;
    }

    if (!category) {
        return <div className="container mx-auto py-6 text-gray-600">Category not found</div>;
    }

    return (
        <div className="container mx-auto py-6">
            <h1 className="text-3xl font-bold mb-4">{category.title || 'Category'}</h1>
            <p className="mb-6 text-gray-600">{category.description || 'No description available.'}</p>

            {blogs.length > 0 ? (
                <ul className="space-y-4">
                    {blogs.map((blog) => (
                        <li key={blog.id} className="border-b pb-4">
                            <h2 className="text-xl font-semibold">{blog.title}</h2>
                            <p className="text-gray-600">{blog.summary}</p>
                            <a
                                href={`/blogs/${blog.id}`}
                                className="text-blue-600 hover:underline"
                            >
                                Read more
                            </a>
                        </li>
                    ))}
                </ul>
            ) : (
                <div className="text-gray-600">No blogs found for this category.</div>
            )}
        </div>
    );
}
