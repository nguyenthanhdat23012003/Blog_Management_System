"use client";

import React from "react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";

// Define props for FrontendLayout
type FrontendLayoutProps = {
  children: React.ReactNode;
};

const FrontendLayout: React.FC<FrontendLayoutProps> = ({ children }) => {
  return (
    <>
      <Header />
      <main className="container mx-auto p-4">{children}</main>
      <Footer />
    </>
  );
};

export default FrontendLayout;
