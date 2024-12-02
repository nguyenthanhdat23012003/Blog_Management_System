"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";

interface FormData {
  title: string;
  description: string;
}

const CreateCategoryPage = () => {
  const [formData, setFormData] = useState<FormData>({
    title: "",
    description: "",
  });
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  // Handle form data change
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // Handle form submission
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);

    try {
      const adminToken = localStorage.getItem("adminToken");
      if (!adminToken) {
        setError("Admin not authenticated. Please log in.");
        return;
      }

      await fetcher("/categories", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${adminToken}`,
        },
        body: JSON.stringify(formData),
      });

      // Redirect to category list
      router.push(`/admin/categories`);
    } catch (err: any) {
      setError(err.message || "Something went wrong!");
    }
  };

  return (
    <div className="rounded-xl flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <button
        onClick={() => router.push("/admin/categories")}
        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400 mb-6"
      >
        Back to Category List
      </button>
      <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
        <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">
          Create Category (Admin)
        </h2>
        {error && (
          <div className="mb-4 text-red-600 bg-red-50 border border-red-400 rounded-lg p-4">
            {error}
          </div>
        )}
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label
              htmlFor="title"
              className="block text-lg font-semibold text-gray-800"
            >
              Title
            </label>
            <input
              type="text"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
              placeholder="Enter category title"
            />
          </div>
          <div>
            <label
              htmlFor="description"
              className="block text-lg font-semibold text-gray-800"
            >
              Description
            </label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
              placeholder="Enter category description (optional)"
            ></textarea>
          </div>
          <button
            type="submit"
            className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            Create Category
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreateCategoryPage;
