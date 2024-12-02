"use client";

const API_URL = process.env.NEXT_PUBLIC_API_URL as string; // Ensure API_URL is treated as a string

// Generic fetcher function to handle API requests
export async function fetcher<T>(
  endpoint: string, // The API endpoint (must be a string)
  options: RequestInit = {} // Options for the fetch request (RequestInit is a TypeScript built-in type)
): Promise<T> {
  const response = await fetch(`${API_URL}${endpoint}`, options);

  // Handle response status errors
  if (!response.ok) {
    let errorMessage = "API error";

    try {
      const errorData: { message?: string } | { message: string }[] = await response.json();

      // Check if errorData is an array of error messages
      if (Array.isArray(errorData)) {
        errorMessage = errorData.map((err) => err.message).join(", ");
      } else if (errorData.message) {
        errorMessage = errorData.message; // Use the `message` field
      } else {
        errorMessage = "An unknown error occurred!";
      }
    } catch (e) {
      errorMessage = "An unknown error occurred!";
    }

    throw new Error(errorMessage); // Throw a detailed error message
  }

  // Process response data
  try {
    const contentType = response.headers.get("Content-Type");

    if (contentType?.includes("application/json")) {
      return await response.json(); // Return JSON if Content-Type is application/json
    }

    // Handle non-JSON responses
    const textData = await response.text();
    if (textData) {
      return textData as unknown as T; // Return text casted to generic type T
    }

    throw new Error("Empty response received"); // Handle empty response
  } catch (e) {
    throw new Error("Failed to parse response"); // Handle JSON parsing errors
  }
}
