"use client"; // Đảm bảo đây là một client component

import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function AdminDashboard() {
    const router = useRouter();

    useEffect(() => {
        const token = localStorage.getItem("adminToken");

        // Redirect to login if no token found
        if (!token) {
            router.push("/admin");
        }
    }, [router]);

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
            <div className="bg-white shadow-xl rounded-lg p-8 max-w-4xl w-full">
                <h1 className="text-4xl font-bold text-gray-800 text-center mb-6">
                    Admin Dashboard
                </h1>
                <p className="text-center text-lg text-gray-600">
                    Welcome to the admin panel. Manage your application here.
                </p>
            </div>
        </div>
    );
}
