"use client";

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { fetcher } from '@/services/api';

export default function CategoryPage() {
    const { id } = useParams();
    const [blogs, setBlogs] = useState([]);
    const [filteredBlogs, setFilteredBlogs] = useState([]);
    const [paginatedBlogs, setPaginatedBlogs] = useState([]);
    const [category, setCategory] = useState(null);
    const [search, setSearch] = useState('');
    const [blogsPerPage, setBlogsPerPage] = useState(6); // Default value
    const [customBlogsPerPage, setCustomBlogsPerPage] = useState(''); // Custom input
    const [currentPage, setCurrentPage] = useState(1);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchCategoryData = async () => {
            try {
                setLoading(true);

                const blogsData = await fetcher(`/blogs/categories/${id}`);
                setBlogs(blogsData);
                setFilteredBlogs(blogsData);

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

    useEffect(() => {
        if (search.trim()) {
            setFilteredBlogs(
                blogs.filter((blog) =>
                    blog.title.toLowerCase().includes(search.toLowerCase())
                )
            );
        } else {
            setFilteredBlogs(blogs);
        }
    }, [search, blogs]);

    useEffect(() => {
        const startIndex = (currentPage - 1) * blogsPerPage;
        const endIndex = startIndex + blogsPerPage;
        setPaginatedBlogs(filteredBlogs.slice(startIndex, endIndex));
    }, [currentPage, blogsPerPage, filteredBlogs]);

    const handleCustomBlogsPerPage = (e) => {
        if (e.key === 'Enter') {
            const value = parseInt(customBlogsPerPage, 10);
            if (value > 0) {
                setBlogsPerPage(value);
                setCustomBlogsPerPage('');
            } else {
                alert('Please enter a valid number greater than 0');
            }
        }
    };

    const generatePagination = () => {
        const visiblePages = 5; // Số trang tối đa được hiển thị
        let pages = [];
    
        if (totalPages <= visiblePages) {
            // Hiển thị tất cả các trang nếu ít hơn hoặc bằng 5
            pages = Array.from({ length: totalPages }, (_, i) => i + 1);
        } else {
            // Hiển thị trang đầu, trang cuối và dấu "..."
            if (currentPage <= Math.ceil(visiblePages / 2)) {
                pages = [...Array.from({ length: visiblePages - 1 }, (_, i) => i + 1), '...', totalPages];
            } else if (currentPage > totalPages - Math.floor(visiblePages / 2)) {
                pages = [1, '...', ...Array.from({ length: visiblePages - 1 }, (_, i) => totalPages - visiblePages + i + 2)];
            } else {
                pages = [
                    1,
                    '...',
                    currentPage - 1,
                    currentPage,
                    currentPage + 1,
                    '...',
                    totalPages,
                ];
            }
        }
    
        return pages;
    };

    const totalPages = Math.max(
        Math.ceil(filteredBlogs.length / (blogsPerPage || 1)), // Avoid division by zero
        1 // At least 1 page
    );

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

            {/* Search Bar */}
            <div className="bg-white p-4 shadow-md rounded-lg mb-4 flex items-center gap-4">
                <input
                    type="text"
                    placeholder="Search blogs by title..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    className="flex-1 border border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-600"
                />
                <div className="flex items-center gap-2">
                    <span>Blogs per page:</span>
                    <select
                        value={blogsPerPage || ''} // Hiển thị giá trị hiện tại hoặc custom
                        onChange={(e) => {
                            const value = e.target.value;
                            if (value === 'custom') {
                                setBlogsPerPage('');
                            } else {
                                const numericValue = parseInt(value, 10);
                                if (numericValue > 0) {
                                    setBlogsPerPage(numericValue);
                                }
                            }
                        }}
                        className="border border-gray-300 rounded-md p-2"
                    >
                        <option value="3">3</option>
                        <option value="6">6</option>
                        <option value="9">9</option>
                        <option value="custom">Custom</option>
                    </select>
                    {blogsPerPage === '' && (
                        <input
                            type="number"
                            min="1"
                            placeholder="Enter number"
                            value={customBlogsPerPage}
                            onChange={(e) => setCustomBlogsPerPage(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === 'Enter') {
                                    const value = parseInt(customBlogsPerPage, 10);
                                    if (value > 0) {
                                        setBlogsPerPage(value); // Cập nhật số vừa nhập
                                        setCustomBlogsPerPage(''); // Reset input sau khi áp dụng
                                    } else {
                                        alert('Please enter a valid number greater than 0');
                                    }
                                }
                            }}
                            className="border border-gray-300 rounded-md p-2 w-16"
                        />
                    )}
                </div>
                <button
                    onClick={() => setSearch('')}
                    className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
                >
                    Clear
                </button>
            </div>
            {/* Blog List */}
            {paginatedBlogs.length > 0 ? (
                <ul className="space-y-4">
                    {paginatedBlogs.map((blog) => (
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
                <div className="text-gray-600">No blogs match your search.</div>
            )}

            {/* Pagination */}
            <div className="flex justify-center mt-6">
                    <button
                        onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                        disabled={currentPage === 1}
                        className="px-4 py-2 border rounded-md mr-2 hover:bg-gray-200 disabled:opacity-50"
                    >
                        Previous
                    </button>
                    {generatePagination().map((page, index) => (
                        <button
                            key={index}
                            disabled={page === "..."}
                            onClick={() => page !== "..." && setCurrentPage(page)}
                            className={`px-4 py-2 border rounded-md mx-1 ${
                                currentPage === page
                                    ? "bg-blue-600 text-white"
                                    : "hover:bg-gray-200"
                            }`}
                        >
                            {page}
                        </button>
                    ))}
                    <button
                        onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
                        disabled={currentPage === totalPages}
                        className="px-4 py-2 border rounded-md ml-2 hover:bg-gray-200 disabled:opacity-50"
                    >
                        Next
                    </button>
                </div> 
        </div>
    );
}
