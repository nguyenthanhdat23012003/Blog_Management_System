"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { fetcher } from "@/services/api";
import { useAuth } from "@/context/AuthContext";

// Define types
type Blog = {
  id: number;
  title: string;
};

type Series = {
  id: number;
  title: string;
  description: string;
};

export default function SeriesDetailPage() {
  const { id } = useParams() as { id: string }; // Explicitly type `id` as a string
  const [blogs, setBlogs] = useState<Blog[]>([]);
  const [filteredBlogs, setFilteredBlogs] = useState<Blog[]>([]);
  const [series, setSeries] = useState<Series | null>(null);
  const [search, setSearch] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const { isLoggedIn } = useAuth();
  const [showLoginModal, setShowLoginModal] = useState<boolean>(false);

  useEffect(() => {
    const fetchSeriesBlogs = async () => {
      try {
        setLoading(true);

        const blogsData = await fetcher<Blog[]>(`/blogs/series/${id}`);
        setBlogs(blogsData);
        setFilteredBlogs(blogsData);

        const seriesData = await fetcher<Series>(`/series/${id}`);
        setSeries(seriesData);

        setLoading(false);
      } catch (err) {
        console.error("Failed to fetch series or blogs:", err);
        setError("Failed to load series or blogs.");
        setLoading(false);
      }
    };

    if (id) fetchSeriesBlogs();
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

  const handleCreateSeries = () => {
    if (isLoggedIn) {
      window.location.href = "/series/create";
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

  if (!series) {
    return (
      <div className="container mx-auto py-6 text-gray-600">
        Series not found
      </div>
    );
  }

  return (
    <div className="container mx-auto py-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">{series.title || "Series"}</h1>
        <button
          onClick={handleCreateSeries}
          className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700"
        >
          Create Your Own Series
        </button>
      </div>

      <p className="mb-6 text-gray-600">
        {series.description || "No description available."}
      </p>

      {/* Search Bar */}
      <div className="bg-white p-4 shadow-md rounded-lg mb-4 flex items-center gap-4">
        <input
          type="text"
          placeholder="Search blogs in this series..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="flex-1 border border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-600"
        />
        <button
          onClick={() => setSearch("")}
          className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
        >
          Clear
        </button>
      </div>

      {/* Blog List */}
      {filteredBlogs.length > 0 ? (
        <ul className="space-y-4">
          {filteredBlogs.map((blog) => (
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
        <div className="text-gray-600">No blogs match your search.</div>
      )}

      {/* Login Modal */}
      {showLoginModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-md">
            <p className="text-lg font-semibold text-gray-800 mb-4">
              You need to log in to create a series.
            </p>
            <div className="flex justify-end space-x-4">
              <button
                onClick={() => setShowLoginModal(false)}
                className="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
              >
                Back
              </button>
              <button
                onClick={() => (window.location.href = "/auth/login")}
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
