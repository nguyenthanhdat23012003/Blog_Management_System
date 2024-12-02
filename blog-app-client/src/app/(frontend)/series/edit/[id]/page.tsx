"use client";

import { useEffect, useState } from "react";
import { useRouter, useParams } from "next/navigation";
import { fetcher } from "@/services/api";

type SeriesData = {
  title: string;
  description: string;
};

export default function EditSeriesPage() {
  const [formData, setFormData] = useState<SeriesData>({
    title: "",
    description: "",
  });
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);
  const router = useRouter();
  const { id: seriesId } = useParams<{ id: string }>(); // Typed params

  useEffect(() => {
    const fetchSeriesData = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem("token");
        if (!token) {
          setError("User not authenticated. Please log in.");
          return;
        }

        // Fetch series details
        const response = await fetcher<SeriesData>(`/series/${seriesId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        setFormData({
          title: response.title || "",
          description: response.description || "",
        });
        setLoading(false);
      } catch (err: any) {
        setError(err.message || "Something went wrong!");
        setLoading(false);
      }
    };

    if (seriesId) fetchSeriesData();
  }, [seriesId]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);
    setSuccess(false);

    try {
      const token = localStorage.getItem("token");
      if (!token) {
        setError("User not authenticated. Please log in.");
        return;
      }

      // Update series
      await fetcher(`/series/${seriesId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      setSuccess(true);
      router.push(`/series/${seriesId}`);
    } catch (err: any) {
      setError(err.message || "Something went wrong!");
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
      <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
        <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">
          Edit Series
        </h2>
        {error && (
          <div className="mb-4 text-red-600 bg-red-50 border border-red-400 rounded-lg p-4">
            {error}
          </div>
        )}
        {success && (
          <div className="mb-4 text-green-600 bg-green-50 border border-green-400 rounded-lg p-4">
            Series updated successfully! Redirecting...
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
              placeholder="Enter series title"
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
              placeholder="Enter series description (optional)"
            ></textarea>
          </div>
          <button
            type="submit"
            className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            Update Series
          </button>
        </form>
      </div>
    </div>
  );
}
