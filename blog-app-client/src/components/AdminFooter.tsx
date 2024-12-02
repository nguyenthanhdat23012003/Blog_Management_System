"use client";

import React from "react";

const AdminFooter: React.FC = () => {
    return (
        <footer className="bg-gradient-to-r from-blue-300 via-blue-400 to-blue-500 text-white py-6 shadow-inner">
            <div className="container mx-auto text-center">
                <p className="text-sm">
                    Admin Dashboard © {new Date().getFullYear()} | All Rights Reserved
                </p>
                <p className="text-xs mt-2">
                    Crafted with care for efficient management
                </p>
            </div>
        </footer>
    );
};

export default AdminFooter;
