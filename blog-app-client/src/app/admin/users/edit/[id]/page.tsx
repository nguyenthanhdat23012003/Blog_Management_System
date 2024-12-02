"use client";

import { useState, useEffect, ChangeEvent, FormEvent } from "react";
import { useRouter, useParams } from "next/navigation";
import { fetcher } from "@/services/api";

interface Role {
  id: number;
  name: string;
}

interface User {
  name: string;
  email: string;
  password: string;
  roleIds: number[];
  about: string;
}

const EditUserPage: React.FC = () => {
  const [formData, setFormData] = useState<User>({
    name: "",
    email: "",
    password: "********", // Masked password
    roleIds: [],
    about: "",
  });

  const [roles, setRoles] = useState<Role[]>([]); // List of roles
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [updating, setUpdating] = useState<boolean>(false);
  const router = useRouter();
  const { id: userId } = useParams();

  // Fetch user and roles data
  useEffect(() => {
    const fetchData = async () => {
      const adminToken = localStorage.getItem("adminToken");
      if (!adminToken) {
        router.push("/admin");
        return;
      }

      try {
        const [userResponse, rolesResponse] = await Promise.all([
          fetcher<User>(`/users/${userId}`, {
            headers: { Authorization: `Bearer ${adminToken}` },
          }),
          fetcher<Role[]>(`/roles`, {
            headers: { Authorization: `Bearer ${adminToken}` },
          }),
        ]);

        setFormData({
          name: userResponse.name || "",
          email: userResponse.email || "",
          password: "********", // Keep password masked
          roleIds: userResponse.roleIds || [],
          about: userResponse.about || "",
        });

        setRoles(rolesResponse);
      } catch (err) {
        setError("Unable to fetch user or roles. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    if (userId) {
      fetchData();
    }
  }, [userId, router]);

  // Handle form changes
  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Handle role selection
  const handleRoleChange = (e: ChangeEvent<HTMLSelectElement>) => {
    const selectedRoles = Array.from(e.target.selectedOptions, (option) =>
      Number(option.value)
    );
    setFormData({ ...formData, roleIds: selectedRoles });
  };

  // Handle form submission
  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);
    setUpdating(true);

    try {
      const adminToken = localStorage.getItem("adminToken");
      if (!adminToken) {
        setError("Admin not authenticated. Please log in.");
        setUpdating(false);
        return;
      }

      // Update user
      await fetcher(`/users/${userId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${adminToken}`,
        },
        body: JSON.stringify({
          name: formData.name,
          roleIds: formData.roleIds,
          about: formData.about,
        }),
      });

      router.push("/admin/users"); // Navigate to the user list
    } catch (err) {
      setError("Failed to update user. Please try again.");
    } finally {
      setUpdating(false);
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
        onClick={() => router.push("/admin/users")}
        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400 my-10"
      >
        Back to User List
      </button>
      <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
        <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">
          Edit User (Admin)
        </h2>
        {error && (
          <div className="mb-4 text-red-600 bg-red-50 border border-red-400 rounded-lg p-4">
            {error}
          </div>
        )}
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label
              htmlFor="name"
              className="block text-lg font-semibold text-gray-800"
            >
              Name
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
            />
          </div>
          <div>
            <label
              htmlFor="email"
              className="block text-lg font-semibold text-gray-800"
            >
              Email
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              disabled
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 bg-gray-200"
            />
          </div>
          <div>
            <label
              htmlFor="password"
              className="block text-lg font-semibold text-gray-800"
            >
              Password
            </label>
            <input
              type="password"
              name="password"
              value={formData.password}
              disabled
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 bg-gray-200"
            />
          </div>
          <div>
            <label
              htmlFor="roleIds"
              className="block text-lg font-semibold text-gray-800"
            >
              Roles
            </label>
            <select
              name="roleIds"
              multiple
              value={formData.roleIds.map(String)} // Convert number to string for controlled select
              onChange={handleRoleChange}
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
            >
              {roles.map((role) => (
                <option key={role.id} value={role.id}>
                  {role.name}
                </option>
              ))}
            </select>
            <p className="text-sm text-gray-500 mt-2">
              Hold <strong>Ctrl</strong> or <strong>Cmd</strong> to select
              multiple roles.
            </p>
          </div>
          <div>
            <label
              htmlFor="about"
              className="block text-lg font-semibold text-gray-800"
            >
              About
            </label>
            <textarea
              name="about"
              value={formData.about}
              onChange={handleChange}
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
              placeholder="Tell something about the user"
            ></textarea>
          </div>
          <button
            type="submit"
            disabled={updating}
            className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            {updating ? "Updating..." : "Update User"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default EditUserPage;
