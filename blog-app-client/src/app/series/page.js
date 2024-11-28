"use client";

import { useEffect, useState } from 'react';
import { fetcher } from '@/services/api';

export default function SeriesPage() {
    const [seriesList, setSeriesList] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSeries = async () => {
            try {
                const data = await fetcher('/series');
                setSeriesList(data);
                setLoading(false);
            } catch (err) {
                console.error('Failed to fetch series:', err);
                setError('Failed to load series.');
                setLoading(false);
            }
        };

        fetchSeries();
    }, []);

    if (loading) {
        return <div className="container mx-auto py-6">Loading...</div>;
    }

    if (error) {
        return <div className="container mx-auto py-6 text-red-600">{error}</div>;
    }

    return (
        <div className="container mx-auto py-6">
            <h1 className="text-3xl font-bold mb-6">All Series</h1>
            {seriesList.length > 0 ? (
                <ul className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {seriesList.map((series) => (
                        <li
                            key={series.id}
                            className="border rounded-lg p-4 hover:shadow-md transition-shadow"
                        >
                            <h2 className="text-xl font-semibold">{series.title}</h2>
                            <p className="text-gray-600">{series.description || 'No description available.'}</p>
                            <a
                                href={`/series/${series.id}`}
                                className="text-blue-600 hover:underline mt-2 inline-block"
                            >
                                View Blogs
                            </a>
                        </li>
                    ))}
                </ul>
            ) : (
                <div className="text-gray-600">No series found.</div>
            )}
        </div>
    );
}
