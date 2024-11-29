"use client";

export default function ContactPage() {
    return (
        <div className="rounded-xl bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen py-12">
            <div className="container mx-auto px-16">
                <h1 className="text-3xl font-bold mb-6 text-blue-600">Contact Us</h1>
                <div className="prose">
                    <p>
                        Have questions, feedback, or collaboration opportunities? We'd love to hear from you! Use the
                        form
                        below to get in touch with the <span className="font-bold">Vie Insights</span> team.
                    </p>
                    <p>You can also reach out to us through the following channels:</p>
                    <ul>
                        <li>
                            <span className="font-bold">Email:</span> support@vieinsights.com
                        </li>
                        <li>
                            <span className="font-bold">Phone:</span> +1 234-567-890
                        </li>
                        <li>
                            <span className="font-bold">Address:</span> 123 Tech Street, Silicon Valley, CA
                        </li>
                    </ul>
                    <p>
                        For general inquiries, please use the form below. We'll get back to you as soon as possible!
                    </p>
                </div>
                <form className="bg-white shadow rounded-lg px-8 pt-6 pb-8 mb-4 max-w-lg mx-auto mt-20">
                    <div>
                        <label
                            htmlFor="name"
                            className="block text-lg font-semibold text-gray-800 "
                        >
                            Name
                        </label>
                        <input
                            type="text"
                            id="name"
                            name="name"
                            required
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                            placeholder="Enter your name"
                        />
                    </div>
                    <div>
                        <label
                            htmlFor="email"
                            className="block text-lg font-semibold text-gray-800 mt-4"
                        >
                            Email
                        </label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            required
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                            placeholder="Enter your email"
                        />
                    </div>
                    <div>
                        <label
                            htmlFor="message"
                            className="block text-lg font-semibold text-gray-800 mt-4"
                        >
                            Message
                        </label>
                        <textarea
                            id="message"
                            name="message"
                            rows="4"
                            required
                            className="mt-2 block w-full rounded-lg border-2 border-gray-300 shadow-sm p-2 focus:border-indigo-500 outline-none"
                            placeholder="Enter your message"
                        ></textarea>
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 mt-8"
                    >
                        Send Message
                    </button>
                </form>

            </div>
        </div>
    );
}
