"use client";

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation'; // Sử dụng useParams để lấy dynamic id từ URL
import { fetcher } from '@/services/api';

export default function BlogPage() {
    const { id } = useParams();
    const [blog, setBlog] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBlog = async () => {
            try {
                setLoading(true);
                const blogData = await fetcher(`/blogs/${id}`);
                setBlog(blogData);
                setLoading(false);
            } catch (err) {
                console.error('Failed to fetch blog:', err);
                setError('Failed to load blog.');
                setLoading(false);
            }
        };

        if (id) fetchBlog();
    }, [id]);

    if (loading) {
        return <div className="container mx-auto py-6">Loading...</div>;
    }

    if (error) {
        return <div className="container mx-auto py-6 text-red-600">{error}</div>;
    }

    if (!blog) {
        return <div className="container mx-auto py-6 text-gray-600">Blog not found</div>;
    }

    return (
        <div className="container mx-auto py-6">
            <h1 className="text-4xl font-bold mb-4">{blog.title}</h1>
            <p className="text-gray-500 mb-4">
                Published on {blog.create_at} | Last updated on {blog.update_at}
            </p>
            <div className="prose max-w-none">
                {blog.content?.blocks.map((block) => {
                    switch (block.type) {
                        case 'header':
                            return (
                                <h2
                                    key={block.id}
                                    className={`text-${block.data.level}xl font-bold mb-4`}
                                >
                                    {block.data.text}
                                </h2>
                            );
                        case 'paragraph':
                            return (
                                <p key={block.id} className="mb-4">
                                    {block.data.text}
                                </p>
                            );
                        case 'list':
                            if (!block.data.items || !Array.isArray(block.data.items)) {
                                return (
                                    <p key={block.id} className="text-gray-500 italic">
                                        Invalid list format
                                    </p>
                                );
                            }
                            return block.data.style === 'unordered' ? (
                                <ul key={block.id} className="list-disc list-inside mb-4">
                                    {block.data.items.map((item, i) => (
                                        <li key={i}>{item.content || 'Empty item'}</li>
                                    ))}
                                </ul>
                            ) : (
                                <ol key={block.id} className="list-decimal list-inside mb-4">
                                    {block.data.items.map((item, i) => (
                                        <li key={i}>{item.content || 'Empty item'}</li>
                                    ))}
                                </ol>
                            );
                        default:
                            return (
                                <p key={block.id} className="text-gray-500 italic">
                                    Unsupported block type: {block.type}
                                </p>
                            );
                    }
                })}
            </div>
        </div>
    );
}
