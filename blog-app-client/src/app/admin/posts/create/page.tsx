"use client";

import React, { useEffect, useRef, useState } from "react";
import EditorJS, { OutputData } from "@editorjs/editorjs";
import Header from "@editorjs/header";
import List from "@editorjs/list";
import Paragraph from "@editorjs/paragraph";
import ImageTool from "@editorjs/image";
import { fetcher } from "@/services/api";
import { useRouter } from "next/navigation";

// Interfaces for types
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

const CreateBlogPage: React.FC = () => {
  const editorRef = useRef<EditorJS | null>(null);
  const router = useRouter();

  const [title, setTitle] = useState<string>("");
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<number[]>([]);
  const [series, setSeries] = useState<Series[]>([]);
  const [selectedSeries, setSelectedSeries] = useState<number | null>(null);
  const [users, setUsers] = useState<User[]>([]);
  const [selectedUser, setSelectedUser] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);

  // Check admin token and redirect if not present
  useEffect(() => {
    const adminToken = localStorage.getItem("adminToken");
    if (!adminToken) {
      router.push("/admin"); // Redirect to login page if token is missing
    }
  }, [router]);

  // Fetch users
  useEffect(() => {
    const adminToken = localStorage.getItem("adminToken");
    const headers = { Authorization: `Bearer ${adminToken}` };

    const fetchUsers = async () => {
      try {
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

  // Fetch series for selected user
  useEffect(() => {
    if (!selectedUser) {
      setSeries([]); // Clear series when no user is selected
      return;
    }

    const fetchSeries = async () => {
      const adminToken = localStorage.getItem("adminToken");
      const headers = { Authorization: `Bearer ${adminToken}` };

      try {
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

  // Initialize EditorJS
  useEffect(() => {
    if (typeof window !== "undefined" && !editorRef.current) {
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
        placeholder: "Start building your content here...",
      });
    }

    return () => {
      if (editorRef.current?.destroy) {
        editorRef.current.destroy();
        editorRef.current = null;
      }
    };
  }, []);

  // Handle add and remove categories
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
      const outputData: OutputData = await editorRef.current.save();

      const blogData = {
        title,
        content: outputData,
        categoryIds: selectedCategories,
        seriesId: selectedSeries,
        authorId: selectedUser,
      };

      await fetcher("/blogs", {
        method: "POST",
        body: JSON.stringify(blogData),
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("adminToken")}`,
        },
      });

      router.push("/admin/posts"); // Redirect to posts grid
    } catch (err) {
      console.error("Failed to create blog:", err);
      setError("Failed to create blog.");
    }
  };

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
          Create Blog (Admin)
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

        {/* Select User */}
        <div className="mb-6">
          <label
            htmlFor="user"
            className="block text-lg font-semibold text-gray-800"
          >
            Select User (Author)
          </label>
          <select
            id="user"
            value={selectedUser || ""}
            onChange={(e) =>
              setSelectedUser(e.target.value ? parseInt(e.target.value) : null)
            }
            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
          >
            <option value="">Select a user</option>
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
              onChange={(e) =>
                setSelectedSeries(
                  e.target.value ? parseInt(e.target.value) : null
                )
              }
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
              Please select a user first to see their series.
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
          Publish Blog
        </button>
      </div>
    </div>
  );
};

export default CreateBlogPage;
