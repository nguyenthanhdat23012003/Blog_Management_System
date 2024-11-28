// app/(frontend)/layout.js
import Header from "@/components/Header";
import Footer from "@/components/Footer";

export default function FrontendLayout({ children }) {
    return (
        <>
            <Header />
            <main className="container mx-auto p-4">{children}</main>
            <Footer />
        </>
    );
}
