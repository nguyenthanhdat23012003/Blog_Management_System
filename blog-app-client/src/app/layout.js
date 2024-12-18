import "./globals.css"; // Global styles (Tailwind CSS)

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
                    <main>{children}</main>
                </body>
            </html>
        </AuthProvider>

    );
}
