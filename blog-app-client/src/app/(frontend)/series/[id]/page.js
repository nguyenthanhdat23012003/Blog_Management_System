"use client";

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { fetcher } from '@/services/api';

export default function SeriesDetailPage() {
    const { id } = useParams();
    const [blogs, setBlogs] = useState([]);
    const [series, setSeries] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSeriesBlogs = async () => {
            try {
                setLoading(true);
                const blogsData = await fetcher(`/blogs/series/${id}`);
                setBlogs(blogsData);

                const seriesData = await fetcher(`/series/${id}`);
                setSeries(seriesData);

                setLoading(false);
            } catch (err) {
                console.error('Failed to fetch series or blogs:', err);
                setError('Failed to load series or blogs.');
                setLoading(false);
            }
        };

        if (id) fetchSeriesBlogs();
    }, [id]);

    if (loading) {
        return <div className="container mx-auto py-6">Loading...</div>;
    }

    if (error) {
        return <div className="container mx-auto py-6 text-red-600">{error}</div>;
    }

    if (!series) {
        return <div className="container mx-auto py-6 text-gray-600">Series not found</div>;
    }

    return (
        <div className="container mx-auto py-6">
            <h1 className="text-3xl font-bold mb-4">{series.title || 'Series'}</h1>
            <p className="mb-6 text-gray-600">{series.description || 'No description available.'}</p>

            {blogs.length > 0 ? (
                <ul className="space-y-4">
                    {blogs.map((blog) => (
                        <li key={blog.id} className="border-b pb-4">
                            <h2 className="text-xl font-semibold">{blog.title}</h2>
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
                <div className="text-gray-600">No blogs found for this series.</div>
            )}
        </div>
    );
}
