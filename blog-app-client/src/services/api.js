"use client";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

export async function fetcher(endpoint, options = {}) {
    const response = await fetch(`${API_URL}${endpoint}`, options);
    if (!response.ok) {
        throw new Error('API error');
    }
    return response.json();
}
