"use client";

import { createContext, useContext, useState, useEffect } from "react";
import { fetcher } from "@/services/api";
import { useRouter } from "next/navigation";

const AuthContext = createContext();

export const useAuth = () => {
    return useContext(AuthContext);
};

export const AuthProvider = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isAdminLoggedIn, setIsAdminLoggedIn] = useState(false);
    const [user, setUser] = useState(null);
    const [admin, setAdmin] = useState(null);
    const router = useRouter();

    useEffect(() => {
        const token = localStorage.getItem("token");
        const adminToken = localStorage.getItem("adminToken");

        if (token) checkUserLogin(token);
        if (adminToken) checkAdminLogin(adminToken);
    }, []);

    const checkUserLogin = async (token) => {
        try {
            const headers = { Authorization: `Bearer ${token}` };
            await fetch("/auth/me",{
                method: "POST",
                headers
                });
            setIsLoggedIn(true);
        } catch (error) {
            console.error("User login verification failed:", error);
            logout();
        }
    };

    const checkAdminLogin = async (adminToken) => {
        try {
            const headers = { Authorization: `Bearer ${adminToken}` };

            await fetch("/auth/admin/me", {
                method: "POST",
                headers,
            });
            setIsAdminLoggedIn(true);
        } catch (error) {
            console.error("Admin login verification failed:", error);
            logoutAdmin();
        }
    };

    const login = (token) => {
        localStorage.setItem("token", token);
        setIsLoggedIn(true);
    };

    const loginAdmin = (adminToken) => {
        localStorage.setItem("adminToken", adminToken);
        setIsAdminLoggedIn(true);
    };

    const logout = () => {
        localStorage.removeItem("token");
        setIsLoggedIn(false);
        router.push("/auth/login");
    };

    const logoutAdmin = () => {
        localStorage.removeItem("adminToken");
        setIsAdminLoggedIn(false);
    };

    return (
        <AuthContext.Provider
            value={{
                isLoggedIn,
                isAdminLoggedIn,
                user,
                admin,
                login,
                loginAdmin,
                logout,
                logoutAdmin,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};
