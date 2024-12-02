import AdminHeader from "@/components/AdminHeader";
import AdminFooter from "@/components/AdminFooter";
import { ReactNode } from "react";

export const metadata = {
    title: "Admin Dashboard",
    description: "Admin panel for managing the platform",
};

type AdminLayoutProps = {
    children: ReactNode; // Typed prop to accept React children
};

export default function AdminLayout({ children }: AdminLayoutProps) {
    return (
        <div className="bg-gray-100 min-h-screen flex flex-col">
            <AdminHeader />
            <main className="container mx-auto py-8 flex-grow bg-white shadow-md rounded-lg px-6 my-20">
                {children}
            </main>
            <AdminFooter />
        </div>
    );
}
