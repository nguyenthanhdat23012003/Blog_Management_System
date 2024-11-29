"use client";

import { useEffect, useState } from 'react';
import { fetcher } from '@/services/api';
import { useAuth } from '@/context/AuthContext';

export default function SeriesPage() {
    const [seriesList, setSeriesList] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { isLoggedIn } = useAuth();
    const [showLoginModal, setShowLoginModal] = useState(false);

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

    const handleCreateSeries = () => {
        if (isLoggedIn) {
            window.location.href = '/series/create';
        } else {
            setShowLoginModal(true);
        }
    };

    if (loading) {
        return <div className="container mx-auto py-6">Loading...</div>;
    }

    if (error) {
        return <div className="container mx-auto py-6 text-red-600">{error}</div>;
    }

    return (
        <div className="container mx-auto py-6">
            {/* Title and Create Button */}
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-3xl font-bold">All Series</h1>
                <button
                    onClick={handleCreateSeries}
                    className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700"
                >
                    Create Series
                </button>
            </div>

            {/* Series List */}
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

            {/* Login Modal */}
            {showLoginModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white p-6 rounded-lg shadow-md">
                        <p className="text-lg font-semibold text-gray-800 mb-4">You need to log in to create a series.</p>
                        <div className="flex justify-end space-x-4">
                            <button
                                onClick={() => setShowLoginModal(false)}
                                className="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
                            >
                                Back
                            </button>
                            <button
                                onClick={() => (window.location.href = '/auth/login')}
                                className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700"
                            >
                                Log In
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
