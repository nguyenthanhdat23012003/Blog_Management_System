"use client";

import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import { UserCircleIcon, ArrowRightOnRectangleIcon } from "@heroicons/react/24/outline";

const AdminHeader: React.FC = () => {
  const router = useRouter();
  const { isAdminLoggedIn, logoutAdmin } = useAuth();

  const handleSignOut = (): void => {
    logoutAdmin();
    router.push("/admin");
  };

  return (
    <header className="bg-gradient-to-r from-blue-500 via-blue-400 to-blue-300 text-white py-6 shadow-lg">
      <div className="container mx-auto flex justify-between items-center">
        {/* Admin Logo */}
        <div className="flex items-center space-x-2">
          <UserCircleIcon className="w-8 h-8 text-white" />
          <span className="text-xl font-bold">Admin Panel</span>
        </div>

        {/* Sign Out Button */}
        {isAdminLoggedIn && (
          <button
            onClick={handleSignOut}
            className="flex items-center space-x-2 hover:text-gray-200"
          >
            <ArrowRightOnRectangleIcon className="w-6 h-6" />
            <span>Sign Out</span>
          </button>
        )}
      </div>
    </header>
  );
};

export default AdminHeader;
