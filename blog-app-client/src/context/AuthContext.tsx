"use client";

import { createContext, useContext, useState, useEffect, ReactNode } from "react";
import { fetcher } from "@/services/api";
import { useRouter } from "next/navigation";

// Define the type for AuthContext
type AuthContextType = {
  isLoggedIn: boolean;
  isAdminLoggedIn: boolean;
  user: any | null; // Replace `any` with a more specific type if user data structure is known
  admin: any | null; // Replace `any` with a more specific type if admin data structure is known
  login: (token: string) => void;
  loginAdmin: (adminToken: string) => void;
  logout: () => void;
  logoutAdmin: () => void;
};

// Create the context with a default value of undefined
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Custom hook to use AuthContext
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

// Define the props type for the AuthProvider
type AuthProviderProps = {
  children: ReactNode;
};

// AuthProvider component to manage authentication state
export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  const [isAdminLoggedIn, setIsAdminLoggedIn] = useState<boolean>(false);
  const [user, setUser] = useState<any | null>(null); // Replace `any` with specific user type
  const [admin, setAdmin] = useState<any | null>(null); // Replace `any` with specific admin type
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem("token");
    const adminToken = localStorage.getItem("adminToken");

    if (token) checkUserLogin(token);
    if (adminToken) checkAdminLogin(adminToken);
  }, []);

  const checkUserLogin = async (token: string) => {
    try {
      const headers = { Authorization: `Bearer ${token}` };
      await fetcher("/auth/me", {
        method: "POST",
        headers,
      });
      setIsLoggedIn(true);
    } catch (error) {
      console.error("User login verification failed:", error);
      logout();
    }
  };

  const checkAdminLogin = async (adminToken: string) => {
    try {
      const headers = { Authorization: `Bearer ${adminToken}` };
      await fetcher("/auth/admin/me", {
        method: "POST",
        headers,
      });
      setIsAdminLoggedIn(true);
    } catch (error) {
      console.error("Admin login verification failed:", error);
      logoutAdmin();
    }
  };

  const login = (token: string) => {
    localStorage.setItem("token", token);
    setIsLoggedIn(true);
  };

  const loginAdmin = (adminToken: string) => {
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
