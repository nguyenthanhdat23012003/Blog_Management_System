"use client";

import Link from "next/link";
import { useAuth } from "@/context/AuthContext";
import { useEffect, useState, useRef } from "react";
import {
    BellIcon,
    PencilSquareIcon,
    IdentificationIcon,
    UserCircleIcon,
    Cog8ToothIcon,
    ArrowRightOnRectangleIcon,
    DocumentTextIcon,
    DocumentDuplicateIcon,
} from "@heroicons/react/24/outline";


import { fetcher } from "@/services/api";

export default function Header() {
    const { isLoggedIn, logout } = useAuth();
    const [categories, setCategories] = useState([]);
    const [activeDropdown, setActiveDropdown] = useState(null);
    const categoryDropdownRef = useRef(null);
    const userDropdownRef = useRef(null);
    const notificationDropdownRef = useRef(null);
    const createDropdownRef = useRef(null);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const data = await fetcher("/categories");
                setCategories(data);
            } catch (err) {
                console.error("Failed to fetch categories:", err);
            }
        };

        fetchCategories();
    }, []);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (
                categoryDropdownRef.current &&
                !categoryDropdownRef.current.contains(event.target)
            ) {
                setActiveDropdown((prev) => (prev === "category" ? null : prev));
            }
            if (
                userDropdownRef.current &&
                !userDropdownRef.current.contains(event.target)
            ) {
                setActiveDropdown((prev) => (prev === "user" ? null : prev));
            }
            if (
                notificationDropdownRef.current &&
                !notificationDropdownRef.current.contains(event.target)
            ) {
                setActiveDropdown((prev) => (prev === "notification" ? null : prev));
            }
            if (
                createDropdownRef.current &&
                !createDropdownRef.current.contains(event.target)
            ) {
                setActiveDropdown((prev) => (prev === "create" ? null : prev));
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    const toggleDropdown = (type) => {
        setActiveDropdown((prev) => (prev === type ? null : type));
    };

    return (
        <header className="bg-white shadow-md py-4 sticky top-0 z-50">
            <div className="container mx-auto flex justify-between items-center">
                {/* Logo */}
                <Link href="/" className="text-xl font-bold text-blue-600">
                    Blog
                </Link>

                {/* Navigation */}
                <nav className="flex space-x-6 relative">
                    <Link href="/" className="text-gray-700 hover:text-blue-600">
                        Home
                    </Link>
                    <div className="relative" ref={categoryDropdownRef}>
                        <button
                            className="text-gray-700 hover:text-blue-600 font-medium relative"
                            onClick={() => toggleDropdown("category")}
                        >
                            Category
                        </button>
                        {activeDropdown === "category" && (
                            <div className="absolute left-0 bg-white shadow-lg mt-2 rounded-lg w-56 z-50 border border-gray-200">
                                <ul className="py-2">
                                    {categories.length > 0 ? (
                                        categories.map((category) => (
                                            <li
                                                key={category.id}
                                                className="px-4 py-3 hover:bg-indigo-100 text-sm font-medium text-gray-600 transition duration-200 cursor-pointer rounded-md"
                                            >
                                                <Link
                                                    href={`/category/${category.id}`}
                                                    className="block hover:text-indigo-600"
                                                >
                                                    {category.title}
                                                </Link>
                                            </li>
                                        ))
                                    ) : (
                                        <li className="px-4 py-3 text-sm text-gray-400">
                                            No categories found
                                        </li>
                                    )}
                                </ul>
                            </div>
                        )}
                    </div>
                    <Link href="/series" className="text-gray-700 hover:text-blue-600">
                        Series
                    </Link>
                    <Link href="/about" className="text-gray-700 hover:text-blue-600">
                        About
                    </Link>
                    <Link href="/contact" className="text-gray-700 hover:text-blue-600">
                        Contact
                    </Link>
                </nav>

                {/* User-Specific Icons or Authentication Buttons */}
                <div className="flex items-center justify-end space-x-4 h-[40px] w-[200px]">
                    {isLoggedIn ? (
                        <>
                            {/* Notification Icon */}
                            <div className="relative" ref={notificationDropdownRef}>
                                <BellIcon
                                    className="w-6 h-6 text-gray-700 hover:text-blue-600 cursor-pointer"
                                    onClick={() => toggleDropdown("notification")}
                                />
                                {activeDropdown === "notification" && (
                                    <div
                                        className="absolute right-0 bg-white shadow-lg mt-2 rounded-lg w-56 z-50 border border-gray-200"
                                    >
                                        <ul className="py-2">
                                            <li className="px-4 py-3 text-gray-500">
                                                No notifications yet
                                            </li>
                                        </ul>
                                    </div>
                                )}
                            </div>

                            {/* Create Icon */}
                            <div className="relative" ref={createDropdownRef}>
                                <PencilSquareIcon
                                    className="w-6 h-6 text-gray-700 hover:text-blue-600 cursor-pointer"
                                    onClick={() => toggleDropdown("create")}
                                />
                                {activeDropdown === "create" && (
                                    <div className="absolute right-0 bg-white shadow-lg mt-2 rounded-lg w-56 z-50 border border-gray-200">
                                        <ul className="py-2">
                                            <li className="px-4 py-3 hover:bg-indigo-100 text-sm font-medium text-gray-600 rounded-md cursor-pointer flex items-center space-x-2">
                                                <DocumentTextIcon className="w-5 h-5 text-indigo-600" />
                                                <Link href="/blogs/create" className="block hover:text-indigo-600">
                                                    Create Blog
                                                </Link>
                                            </li>
                                            <li className="px-4 py-3 hover:bg-indigo-100 text-sm font-medium text-gray-600 rounded-md cursor-pointer flex items-center space-x-2">
                                                <DocumentDuplicateIcon className="w-5 h-5 text-indigo-600" />
                                                <Link href="/series/create" className="block hover:text-indigo-600">
                                                    Create Series
                                                </Link>
                                            </li>
                                        </ul>
                                    </div>
                                )}
                            </div>

                            {/* User Icon */}
                            <div className="relative" ref={userDropdownRef}>
                                <IdentificationIcon
                                    className="w-6 h-6 text-gray-700 hover:text-blue-600 cursor-pointer"
                                    onClick={() => toggleDropdown("user")}
                                />
                                {activeDropdown === "user" && (
                                    <div className="absolute right-0 bg-white shadow-lg mt-2 rounded-lg w-56 z-50 border border-gray-200">
                                        <ul className="py-2">
                                            <li className="px-4 py-3 hover:bg-indigo-100 text-sm font-medium text-gray-600 rounded-md cursor-pointer flex items-center space-x-2">
                                                <UserCircleIcon className="w-5 h-5 text-indigo-600" />
                                                <Link href="/account" className="block hover:text-indigo-600">
                                                    My Account
                                                </Link>
                                            </li>
                                            <li className="px-4 py-3 hover:bg-indigo-100 text-sm font-medium text-gray-600 rounded-md cursor-pointer flex items-center space-x-2">
                                                <Cog8ToothIcon className="w-5 h-5 text-indigo-600" />
                                                <Link href="/settings" className="block hover:text-indigo-600">
                                                    Settings
                                                </Link>
                                            </li>
                                            <li className="px-4 py-3 hover:bg-indigo-100 text-sm font-medium text-gray-600 rounded-md cursor-pointer flex items-center space-x-2">
                                                <ArrowRightOnRectangleIcon className="w-5 h-5 text-indigo-600" />
                                                <button
                                                    onClick={() =>
                                                        logout()
                                                    }
                                                    className="block hover:text-indigo-600 w-full text-left"
                                                >
                                                    Log Out
                                                </button>
                                            </li>
                                        </ul>
                                    </div>
                                )}
                            </div>
                        </>
                    ) : (
                        <>
                            <Link
                                href="/auth/login"
                                className="px-4 py-2 border border-blue-600 text-blue-600 rounded hover:bg-blue-600 hover:text-white flex items-center justify-center"
                            >
                                Log in
                            </Link>
                            <Link
                                href="/auth/register"
                                className="px-4 py-2 bg-blue-600 border border-blue-600 text-white rounded hover:bg-blue-700 flex items-center justify-center"
                            >
                                Register
                            </Link>
                        </>
                    )}
                </div>
            </div>
        </header>
    );
}
