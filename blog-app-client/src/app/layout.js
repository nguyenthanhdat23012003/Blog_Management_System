import "./globals.css"; // Global styles (Tailwind CSS)

import Header from "@/components/Header";
import Footer from "@/components/Footer";
import {AuthProvider} from "@/context/AuthContext";

export const metadata = {
    title: "Blog Application",
    description: "A modern blog platform built with Next.js",
};

export default function RootLayout({ children }) {
    return (
        <AuthProvider>
            <html lang="en">
                <body className="bg-gray-50 text-gray-900">
                    <Header/>
                        <main className="container mx-auto p-4">{children}</main>
                    <Footer/>
                </body>
            </html>
        </AuthProvider>

    );
}
