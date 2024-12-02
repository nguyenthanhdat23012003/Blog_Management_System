"use client";

import React, { useState, useEffect, ChangeEvent } from "react";
import { fetcher } from "@/services/api";
import { useRouter } from "next/navigation";
import { jwtDecode } from "jwt-decode";

// Define types for user, blog, series, and form data
type User = {
  id: number;
  name: string;
  email: string;
  about?: string;
};

type Blog = {
  id: number;
  title: string;
};

type Series = {
  id: number;
  title: string;
};

type FormData = {
  name: string;
  password: string;
  about: string;
};

type SelectedItemToDelete = {
  id: number;
  type: "blog" | "series";
};

const MyAccountPage: React.FC = () => {
  const [user, setUser] = useState<User | null>(null);
  const [blogs, setBlogs] = useState<Blog[]>([]);
  const [series, setSeries] = useState<Series[]>([]);
  const [formData, setFormData] = useState<FormData>({
    name: "",
    password: "",
    about: "",
  });
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [editMode, setEditMode] = useState<boolean>(false);
  const [selectedItemToDelete, setSelectedItemToDelete] =
    useState<SelectedItemToDelete | null>(null);
  const [deleteModalOpen, setDeleteModalOpen] = useState<boolean>(false);
  const router = useRouter();

  const token = localStorage.getItem("token");
  let userId: number | undefined;

  if (token) {
    try {
      const decoded: { userId?: number; id?: number } = jwtDecode(token);
      userId = decoded.userId || decoded.id;
    } catch (err) {
      console.error("Failed to decode token:", err);
      setError("Invalid token.");
    }
  }

  // Fetch user information and related content
  useEffect(() => {
    if (!userId) return;

    const fetchUser = async () => {
      try {
        const data = await fetcher<User>(`/users/${userId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setUser(data);
        setFormData({
          name: data.name || "",
          about: data.about || "",
          password: "",
        });
        setLoading(false);
      } catch (err) {
        console.error("Failed to fetch user:", err);
        setError("Failed to load user information.");
        setLoading(false);
      }
    };

    const fetchUserContent = async () => {
      try {
        const blogsData = await fetcher<Blog[]>(`/blogs/users/${userId}`);
        const seriesData = await fetcher<Series[]>(`/series/users/${userId}`);

        setBlogs(blogsData);
        setSeries(seriesData);
      } catch (err) {
        console.error("Failed to fetch user content:", err);
        setError("Failed to load user blogs or series.");
      }
    };

    fetchUser();
    fetchUserContent();
  }, [userId, token]);

  // Handle form changes
  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // Handle update user information
  const handleUpdate = async () => {
    try {
      setLoading(true);
      const updatedUser = await fetcher<User>(`/users/${userId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      setUser(updatedUser);
      setEditMode(false);
      setLoading(false);
    } catch (err) {
      console.error("Failed to update user:", err);
      setError("Failed to update user information.");
      setLoading(false);
    }
  };

  // Handle delete confirmation modal
  const handleDelete = async () => {
    try {
      if (!selectedItemToDelete) return;

      const endpoint =
        selectedItemToDelete.type === "blog"
          ? `/blogs/${selectedItemToDelete.id}`
          : `/series/${selectedItemToDelete.id}`;

      await fetcher(endpoint, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (selectedItemToDelete.type === "blog") {
        setBlogs((prev) =>
          prev.filter((blog) => blog.id !== selectedItemToDelete.id)
        );
      } else {
        setSeries((prev) =>
          prev.filter((serie) => serie.id !== selectedItemToDelete.id)
        );
      }

      setDeleteModalOpen(false);
      setSelectedItemToDelete(null);
    } catch (err) {
      console.error("Failed to delete item:", err);
    }
  };

  if (loading) {
    return <div className="container mx-auto py-6">Loading...</div>;
  }

  if (error) {
    return <div className="container mx-auto py-6 text-red-600">{error}</div>;
  }

  return (
    <div className="rounded-xl flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
        <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">
          My Account
        </h2>
        {editMode ? (
          <>
            <div className="mb-6">
              <label
                htmlFor="name"
                className="block text-lg font-semibold text-gray-800"
              >
                Name
              </label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                placeholder="Enter your name"
                className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
              />
            </div>
            <div className="mb-6">
              <label
                htmlFor="password"
                className="block text-lg font-semibold text-gray-800"
              >
                Password
              </label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Enter new password"
                className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
              />
            </div>
            <div className="mb-6">
              <label
                htmlFor="about"
                className="block text-lg font-semibold text-gray-800"
              >
                About
              </label>
              <textarea
                id="about"
                name="about"
                value={formData.about}
                onChange={handleChange}
                placeholder="Tell something about yourself"
                className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
              />
            </div>
            <button
              onClick={handleUpdate}
              className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
            >
              Save Changes
            </button>
          </>
        ) : (
          <>
            <div className="mb-6">
              <p className="text-lg font-semibold text-gray-800">
                Name: <span className="text-gray-600">{user?.name}</span>
              </p>
            </div>
            <div className="mb-6">
              <p className="text-lg font-semibold text-gray-800">
                Email: <span className="text-gray-600">{user?.email}</span>
              </p>
            </div>
            <div className="mb-6">
              <p className="text-lg font-semibold text-gray-800">
                About:{" "}
                <span className="text-gray-600">{user?.about || "N/A"}</span>
              </p>
            </div>
            <button
              onClick={() => setEditMode(true)}
              className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
            >
              Edit Profile
            </button>
          </>
        )}
      </div>

      {/* Series and Blogs */}
      <div className="bg-white shadow-xl rounded-lg p-8 max-w-4xl w-full mt-8">
        <h3 className="text-2xl font-bold text-gray-800 mb-4">My Series</h3>
        {series.length > 0 ? (
          <ul className="space-y-4">
            {series.map((serie) => (
              <li
                key={serie.id}
                className="flex justify-between items-center border-b pb-2"
              >
                <span>{serie.title}</span>
                <div className="space-x-2">
                  <button
                    onClick={() => router.push(`/series/edit/${serie.id}`)}
                    className="text-blue-600 hover:underline"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => {
                      setSelectedItemToDelete({ id: serie.id, type: "series" });
                      setDeleteModalOpen(true);
                    }}
                    className="text-red-600 hover:underline"
                  >
                    Delete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-600">No series found.</p>
        )}

        <h3 className="text-2xl font-bold text-gray-800 mt-8 mb-4">My Blogs</h3>
        {blogs.length > 0 ? (
          <ul className="space-y-4">
            {blogs.map((blog) => (
              <li
                key={blog.id}
                className="flex justify-between items-center border-b pb-2"
              >
                <span>{blog.title}</span>
                <div className="space-x-2">
                  <button
                    onClick={() => router.push(`/blogs/edit/${blog.id}`)}
                    className="text-blue-600 hover:underline"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => {
                      setSelectedItemToDelete({ id: blog.id, type: "blog" });
                      setDeleteModalOpen(true);
                    }}
                    className="text-red-600 hover:underline"
                  >
                    Delete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-600">No blogs found.</p>
        )}
      </div>

      {/* Delete Confirmation Modal */}
      {deleteModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg max-w-sm w-full">
            <p className="text-center text-gray-800">
              Are you sure you want to delete this item?
            </p>
            <div className="mt-4 flex justify-center space-x-4">
              <button
                onClick={handleDelete}
                className="bg-red-600 text-white py-2 px-4 rounded-lg hover:bg-red-700"
              >
                Delete
              </button>
              <button
                onClick={() => setDeleteModalOpen(false)}
                className="bg-gray-600 text-white py-2 px-4 rounded-lg hover:bg-gray-700"
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default MyAccountPage;
