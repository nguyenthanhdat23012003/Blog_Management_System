"use client";

import React, { useEffect, useRef, useState } from "react";
import EditorJS, { OutputData } from "@editorjs/editorjs";
import Header from "@editorjs/header";
import List from "@editorjs/list";
import Paragraph from "@editorjs/paragraph";
import ImageTool from "@editorjs/image";
import { fetcher } from "@/services/api";
import { useRouter, useParams } from "next/navigation";

// Interfaces for type safety
interface User {
  id: number;
  name: string;
  email: string;
}

interface Category {
  id: number;
  title: string;
}

interface Series {
  id: number;
  title: string;
}

interface BlogDetails {
  title: string;
  content: OutputData;
  categoryIds: number[];
  seriesId: number | null;
  authorId: number | null;
}

const EditBlogPage: React.FC = () => {
  const editorRef = useRef<EditorJS | null>(null);
  const router = useRouter();
  const { id: blogId } = useParams() as { id: string };

  const [title, setTitle] = useState<string>("");
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<number[]>([]);
  const [series, setSeries] = useState<Series[]>([]);
  const [selectedSeries, setSelectedSeries] = useState<number | null>(null);
  const [users, setUsers] = useState<User[]>([]);
  const [selectedUser, setSelectedUser] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  // Check admin token and redirect if missing
  useEffect(() => {
    const adminToken = localStorage.getItem("adminToken");
    if (!adminToken) {
      router.push("/admin");
    }
  }, [router]);

  // Fetch users
  useEffect(() => {
    const fetchUsers = async () => {
      const adminToken = localStorage.getItem("adminToken");
      if (!adminToken) return;

      try {
        const headers = { Authorization: `Bearer ${adminToken}` };
        const data = await fetcher<User[]>("/users", { headers });
        setUsers(data);
      } catch (err) {
        console.error("Failed to fetch users:", err);
      }
    };

    fetchUsers();
  }, []);

  // Fetch categories
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const data = await fetcher<Category[]>("/categories");
        setCategories(data);
      } catch (err) {
        console.error("Failed to fetch categories:", err);
      }
    };

    fetchCategories();
  }, []);

  // Fetch series when author changes
  useEffect(() => {
    if (!selectedUser) {
      setSeries([]);
      return;
    }

    const fetchSeries = async () => {
      const adminToken = localStorage.getItem("adminToken");
      if (!adminToken) return;

      try {
        const headers = { Authorization: `Bearer ${adminToken}` };
        const data = await fetcher<Series[]>(`/series/users/${selectedUser}`, {
          headers,
        });
        setSeries(data);
      } catch (err) {
        console.error("Failed to fetch series:", err);
      }
    };

    fetchSeries();
  }, [selectedUser]);

  // Fetch blog details and initialize EditorJS
  useEffect(() => {
    const fetchBlogDetails = async () => {
      try {
        const data = await fetcher<BlogDetails>(`/blogs/${blogId}`);
        setTitle(data.title);
        setSelectedCategories(data.categoryIds || []);
        setSelectedSeries(data.seriesId || null);
        setSelectedUser(data.authorId || null);

        // Ensure the element exists before initializing EditorJS
        const initializeEditor = () => {
          const editorElement = document.getElementById("editorjs");
          if (editorElement && !editorRef.current) {
            editorRef.current = new EditorJS({
              holder: "editorjs",
              tools: {
                header: Header,
                paragraph: Paragraph,
                list: List,
                image: {
                  class: ImageTool,
                  config: {
                    endpoints: {
                      byFile: "/upload/image",
                      byUrl: "/upload/image-url",
                    },
                  },
                },
              },
              data: data.content,
              placeholder: "Start building your content here...",
            });
          } else if (!editorElement) {
            console.error("EditorJS holder element not found!");
          }
        };

        setLoading(false);
        setTimeout(initializeEditor, 0); // Delay initialization to ensure DOM rendering
      } catch (err) {
        console.error("Failed to fetch blog details:", err);
        setError("Failed to load blog details.");
        setLoading(false);
      }
    };

    if (blogId) fetchBlogDetails();

    return () => {
      if (editorRef.current?.destroy) {
        editorRef.current.destroy();
        editorRef.current = null;
      }
    };
  }, [blogId]);

  // Handle category selection
  const handleAddCategory = (categoryId: number) => {
    if (!selectedCategories.includes(categoryId)) {
      setSelectedCategories((prev) => [...prev, categoryId]);
    }
  };

  const handleRemoveCategory = (categoryId: number) => {
    setSelectedCategories((prev) => prev.filter((id) => id !== categoryId));
  };

  // Handle blog submission
  const handleSubmit = async () => {
    if (!editorRef.current || !title || !selectedUser) {
      setError("Title, content, and user are required!");
      return;
    }

    try {
      const outputData = await editorRef.current.save();

      const blogData = {
        title,
        content: outputData,
        categoryIds: selectedCategories,
        seriesId: selectedSeries,
        authorId: selectedUser,
      };

      const adminToken = localStorage.getItem("adminToken");
      if (!adminToken) return;

      await fetcher(`/blogs/${blogId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${adminToken}`,
        },
        body: JSON.stringify(blogData),
      });

      router.push("/admin/posts");
    } catch (err) {
      console.error("Failed to update blog:", err);
      setError("Failed to update blog.");
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
      <button
        onClick={() => router.push("/admin/posts")}
        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400 my-10"
      >
        Back to Posts Grid
      </button>
      <div className="bg-white shadow-xl rounded-lg p-8 max-w-4xl w-full mb-20">
        <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">
          Edit Blog (Admin)
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

        {/* Select Author */}
        <div className="mb-6">
          <label
            htmlFor="author"
            className="block text-lg font-semibold text-gray-800"
          >
            Select Author
          </label>
          <select
            id="author"
            value={selectedUser || ""}
            onChange={(e) => {
              const userId = e.target.value ? parseInt(e.target.value) : null;
              setSelectedUser(userId);
              setSelectedSeries(null);
            }}
            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
          >
            <option value="">Select an author</option>
            {users.map((user) => (
              <option key={user.id} value={user.id}>
                {user.name} - {user.email}
              </option>
            ))}
          </select>
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
                    ? "bg-indigo-600 text-white"
                    : "bg-gray-100 text-gray-800"
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
          {selectedUser ? (
            <select
              id="series"
              value={selectedSeries || ""}
              onChange={(e) => {
                const seriesId = e.target.value
                  ? parseInt(e.target.value)
                  : null;
                setSelectedSeries(seriesId);
              }}
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
            >
              <option value="">Select a series (optional)</option>
              {series.map((serie) => (
                <option key={serie.id} value={serie.id}>
                  {serie.title}
                </option>
              ))}
            </select>
          ) : (
            <p className="text-gray-600">
              Please select an author to see their series.
            </p>
          )}
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
