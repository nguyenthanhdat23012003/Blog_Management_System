"use client";

export default function Footer() {
    return (
        <div className="flex flex-col min-h-screen">
            {/* Main content placeholder */}
            <div className="flex-grow"></div>

            {/* Footer content */}
            <footer>
                <div className="bg-gray-900 text-white py-10">
                    <div className="container mx-auto">
                        {/* Newsletter Section */}
                        <div className="text-center mb-8">
                            <h2 className="text-lg font-semibold mb-4">Newsletter</h2>
                            <form className="flex justify-center items-center space-x-2">
                                <input
                                    type="email"
                                    placeholder="Enter your email"
                                    className="w-1/2 px-4 py-2 rounded-lg border border-gray-400 bg-gray-800 text-white focus:outline-none focus:border-indigo-500"
                                />
                                <button
                                    type="submit"
                                    className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700"
                                >
                                    Subscribe
                                </button>
                            </form>
                        </div>

                        {/* Footer Links Section */}
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-8 text-sm text-gray-300">
                            {/* Company Column */}
                            <div>
                                <h3 className="text-white font-semibold mb-4">Company</h3>
                                <ul className="space-y-2">
                                    <li>
                                        <a href="#" className="hover:text-indigo-500">
                                            About
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#" className="hover:text-indigo-500">
                                            Careers
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#" className="hover:text-indigo-500">
                                            Events
                                        </a>
                                    </li>
                                </ul>
                            </div>

                            {/* Contact Column */}
                            <div>
                                <h3 className="text-white font-semibold mb-4">Contact</h3>
                                <ul className="space-y-2">
                                    <li>Address: Hanoi, Vietnam</li>
                                    <li>Phone: +84 123 456 789</li>
                                    <li>Email: info@blog.com</li>
                                </ul>
                            </div>

                            {/* Useful Links Column */}
                            <div>
                                <h3 className="text-white font-semibold mb-4">Useful Links</h3>
                                <ul className="space-y-2">
                                    <li>
                                        <a href="#" className="hover:text-indigo-500">
                                            Privacy Policy
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#" className="hover:text-indigo-500">
                                            Terms & Conditions
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#" className="hover:text-indigo-500">
                                            FAQ
                                        </a>
                                    </li>
                                </ul>
                            </div>

                            {/* Follow Us Column */}
                            <div>
                                <h3 className="text-white font-semibold mb-4">Follow Us</h3>
                                <ul className="space-y-2">
                                    <li>
                                        <a href="#" className="hover:text-indigo-500">
                                            Facebook
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#" className="hover:text-indigo-500">
                                            Instagram
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#" className="hover:text-indigo-500">
                                            LinkedIn
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="bg-gray-800 text-white py-2">
                    <div className="container mx-auto text-center">
                        <p>&copy; {new Date().getFullYear()} Blog Application. All rights reserved.</p>
                    </div>
                </div>
            </footer>
        </div>
    );
}
