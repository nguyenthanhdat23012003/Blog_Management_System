export const metadata = {
    title: "Admin Dashboard",
    description: "Admin panel for managing the platform",
};

export default function AdminLayout({ children }) {
    return (
        <div className="bg-gray-100 min-h-screen flex flex-col">
            <header className="bg-purple-600 text-white py-4">
                <div className="container mx-auto">
                    <h1 className="text-2xl font-bold">Admin Panel</h1>
                </div>
            </header>
            <main className="container mx-auto py-8 flex-grow">{children}</main>
            <footer className="bg-purple-800 text-white py-4">
                <div className="container mx-auto text-center">
                    Admin Dashboard © {new Date().getFullYear()}
                </div>
            </footer>
        </div>
    );
}
