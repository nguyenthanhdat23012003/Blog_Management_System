import Link from "next/link";

export default function Header() {
    return (
        <header className="bg-white shadow-md py-4">
            <div className="container mx-auto flex justify-between items-center">
                {/* Logo */}
                <Link href="/" className="text-xl font-bold text-blue-600">
                    Blog
                </Link>

                {/* Navigation */}
                <nav className="flex space-x-4">
                    <Link href="/" className="text-gray-700 hover:text-blue-600">
                        Home
                    </Link>
                    <Link href="/category" className="text-gray-700 hover:text-blue-600">
                        Category
                    </Link>
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

                {/* Authentication Buttons */}
                <div className="flex space-x-2">
                    <Link
                        href="/login"
                        className="px-4 py-2 border border-blue-600 text-blue-600 rounded hover:bg-blue-600 hover:text-white"
                    >
                        Log in
                    </Link>
                    <Link
                        href="/register"
                        className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                    >
                        Register
                    </Link>
                </div>
            </div>
        </header>
    );
}
