import "./globals.css"; // Global styles (Tailwind CSS)
import { AuthProvider } from "@/context/AuthContext";
import { Metadata } from "next"; // Import Metadata type

// Define metadata with proper type
export const metadata: Metadata = {
  title: "Blog Application",
  description: "A modern blog platform built with Next.js",
};

// Define RootLayout props
type RootLayoutProps = {
  children: React.ReactNode;
};

export default function RootLayout({ children }: RootLayoutProps) {
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
