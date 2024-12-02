"use client";

import { useEffect, useState, ChangeEvent, FormEvent } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";

// Define type interfaces
interface User {
  id: number;
  name: string;
  email: string;
}

interface FormData {
  title: string;
  description: string;
  authorId: number | null; // Selected user
}

const CreateSeriesPage: React.FC = () => {
  const [formData, setFormData] = useState<FormData>({
    title: "",
    description: "",
    authorId: null,
  });
  const [users, setUsers] = useState<User[]>([]);
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  // Fetch users for author selection
  useEffect(() => {
    const fetchUsers = async () => {
      const adminToken = localStorage.getItem("adminToken");
      if (!adminToken) {
        router.push("/admin"); // Redirect if not authenticated
        return;
      }

      try {
        const data = await fetcher<User[]>("/users", {
          headers: { Authorization: `Bearer ${adminToken}` },
        });
        setUsers(data);
      } catch (err) {
        console.error("Failed to fetch users:", err);
      }
    };

    fetchUsers();
  }, [router]);

  // Handle form data change
  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === "authorId" ? Number(value) : value,
    });
  };

  // Handle form submission
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    try {
      const adminToken = localStorage.getItem("adminToken");
      if (!adminToken) {
        setError("Admin not authenticated. Please log in.");
        return;
      }

      if (!formData.authorId) {
        setError("Please select an author.");
        return;
      }

      await fetcher("/series", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${adminToken}`,
        },
        body: JSON.stringify(formData),
      });

      router.push(`/admin/series`);
    } catch (err: any) {
      setError(err.message || "Something went wrong!");
    }
  };

  return (
    <div className="rounded-xl flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <button
        onClick={() => router.push("/admin/series")}
        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400 my-10"
      >
        Back to Series List
      </button>
      <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
        <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">
          Create Series (Admin)
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
          <div>
            <label
              htmlFor="authorId"
              className="block text-lg font-semibold text-gray-800"
            >
              Select Author
            </label>
            <select
              name="authorId"
              value={formData.authorId || ""}
              onChange={handleChange}
              required
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
          <button
            type="submit"
            className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            Create Series
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreateSeriesPage;
