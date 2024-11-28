"use client";

export default function AboutPage() {
    return (
        <div className="rounded-xl bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen py-12">
            <div className="container mx-auto px-16">
                {/* Header Section */}
                <div className="text-center mb-12">
                    <h1 className="text-4xl font-extrabold text-gray-800 mb-4">About Us</h1>
                    <p className="text-lg text-gray-600">
                        Welcome to our technology blog! We are passionate about sharing insights, tutorials, and the latest updates in the tech world.
                    </p>
                </div>

                {/* Mission Section */}
                <div className="grid md:grid-cols-2 gap-12 items-center">
                    <div>
                        <h2 className="text-3xl font-bold text-indigo-600 mb-4">Our Mission</h2>
                        <p className="text-gray-700 mb-6">
                            Our mission is to empower developers and technology enthusiasts by providing high-quality, easy-to-understand resources.
                            Whether you're a seasoned coder or just starting out, we aim to make technology accessible to everyone.
                        </p>
                        <ul className="space-y-4">
                            <li className="flex items-center">
                                <span className="w-8 h-8 bg-indigo-600 text-white rounded-full flex items-center justify-center mr-4">
                                    📘
                                </span>
                                <span className="text-gray-700">In-depth tutorials and guides</span>
                            </li>
                            <li className="flex items-center">
                                <span className="w-8 h-8 bg-indigo-600 text-white rounded-full flex items-center justify-center mr-4">
                                    🌐
                                </span>
                                <span className="text-gray-700">Latest trends in technology</span>
                            </li>
                            <li className="flex items-center">
                                <span className="w-8 h-8 bg-indigo-600 text-white rounded-full flex items-center justify-center mr-4">
                                    🚀
                                </span>
                                <span className="text-gray-700">Real-world project insights</span>
                            </li>
                        </ul>
                    </div>
                    <div>
                        <img
                            src="https://via.placeholder.com/500x300"
                            alt="Mission illustration"
                            className="rounded-lg shadow-lg"
                        />
                    </div>
                </div>

                {/* Team Section */}
                <div className="mt-16">
                    <h2 className="text-3xl font-bold text-center text-indigo-600 mb-8">Meet Our Team</h2>
                    <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-12">
                        {[
                            { name: "John Doe", role: "Lead Developer", img: "https://via.placeholder.com/150" },
                            { name: "Jane Smith", role: "Tech Writer", img: "https://via.placeholder.com/150" },
                            { name: "Sam Wilson", role: "UI/UX Designer", img: "https://via.placeholder.com/150" },
                        ].map((member, index) => (
                            <div key={index} className="text-center">
                                <img
                                    src={member.img}
                                    alt={member.name}
                                    className="w-24 h-24 mx-auto rounded-full shadow-lg mb-4"
                                />
                                <h3 className="text-lg font-bold text-gray-800">{member.name}</h3>
                                <p className="text-indigo-600">{member.role}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}
