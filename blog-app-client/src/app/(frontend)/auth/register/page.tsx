"use client";

import React, { useState, ChangeEvent, FormEvent } from "react";
import { useRouter } from "next/navigation"; // Router for navigation
import { fetcher } from "@/services/api";

// Define the type for form data
type FormData = {
  name: string;
  email: string;
  password: string;
};

const RegisterPage: React.FC = () => {
  const [formData, setFormData] = useState<FormData>({
    name: "",
    email: "",
    password: "",
  });

  const [error, setError] = useState<string | null>(null); // Error handling
  const [success, setSuccess] = useState<boolean>(false); // Success notification
  const router = useRouter();

  // Handle input change
  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  // Handle form submission
  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);

    try {
      const response = await fetcher("/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });
      setSuccess(true);
      setTimeout(() => {
        router.push("/auth/login");
      }, 2000);
    } catch (err: any) {
      setError(err.message || "Something went wrong!");
    }
  };

  return (
    <div className="rounded-xl flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="bg-white shadow-xl rounded-lg p-8 max-w-lg w-full">
        <h2 className="text-3xl font-bold text-gray-800 mb-6 text-center">
          Register
        </h2>
        {error && (
          <div className="mb-4 text-red-600 bg-red-50 border border-red-400 rounded-lg p-4">
            {error}
          </div>
        )}
        {success && (
          <div className="mb-4 text-green-600 bg-green-50 border border-green-400 rounded-lg p-4">
            Registration successful! Redirecting to login page...
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
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
              placeholder="Enter your name"
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
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
              placeholder="Enter your email"
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
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
              placeholder="Create a password"
            />
          </div>
          <button
            type="submit"
            className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            Register
          </button>
        </form>
        <p className="mt-6 text-center text-sm text-gray-600">
          Already have an account?{" "}
          <a
            href="/auth/login"
            className="text-indigo-600 font-medium hover:underline"
          >
            Login here
          </a>
        </p>
      </div>
    </div>
  );
};

export default RegisterPage;
