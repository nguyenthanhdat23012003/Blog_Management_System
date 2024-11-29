"use client";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

export async function fetcher(endpoint, options = {}) {
    const response = await fetch(`${API_URL}${endpoint}`, options);

    if (!response.ok) {
        let errorMessage = 'API error';
        try {
            const errorData = await response.json();

            // Kiểm tra nếu lỗi là một mảng các JSON
            if (Array.isArray(errorData)) {
                errorMessage = errorData.map(err => err.message).join(', '); // Ghép các message lại
            } else if (errorData.message) {
                errorMessage = errorData.message; // Lấy message từ JSON
            } else {
                errorMessage = 'An unknown error occurred!';
            }
        } catch (e) {
            errorMessage = 'An unknown error occurred!';
        }
        throw new Error(errorMessage); // Ném lỗi với thông báo chi tiết
    }

    return response.json(); // Trả về JSON nếu thành công
}
