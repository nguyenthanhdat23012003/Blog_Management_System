"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";
import { CheckCircleIcon } from "@heroicons/react/24/outline";

// Define type interfaces
interface Post {
  id: number;
  title: string;
  authorId: number;
  categoryIds: number[];
  seriesId?: number;
  create_at: string;
  update_at: string;
}

interface Category {
  id: number;
  title: string;
}

interface Author {
  id: number;
  name: string;
}

interface Series {
  id: number;
  title: string;
}

interface Filters {
  idFrom: string;
  idTo: string;
  createFrom: string;
  createTo: string;
  updateFrom: string;
  updateTo: string;
}

interface SortConfig {
  key: keyof Post;
  direction: "asc" | "desc";
}

const ManagePostPage: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [filteredPosts, setFilteredPosts] = useState<Post[]>([]);
  const [categoriesMap, setCategoriesMap] = useState<Record<number, string>>(
    {}
  );
  const [authorsMap, setAuthorsMap] = useState<Record<number, string>>({});
  const [seriesMap, setSeriesMap] = useState<Record<number, string>>({});
  const [search, setSearch] = useState<string>("");
  const [searchBy, setSearchBy] = useState<
    "category" | "author" | "blog" | "series"
  >("category");
  const [filters, setFilters] = useState<Filters>({
    idFrom: "",
    idTo: "",
    createFrom: "",
    createTo: "",
    updateFrom: "",
    updateTo: "",
  });
  const [sortConfig, setSortConfig] = useState<SortConfig>({
    key: "id",
    direction: "asc",
  });
  const [postsPerPage, setPostsPerPage] = useState<number | "all">(3);
  const [currentPage, setCurrentPage] = useState<number>(1);

  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState<boolean>(false);
  const [postToDelete, setPostToDelete] = useState<number | null>(null);
  const [isFilterOpen, setIsFilterOpen] = useState<boolean>(false);
  const [successModalOpen, setSuccessModalOpen] = useState<boolean>(false);

  const router = useRouter();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const adminToken = localStorage.getItem("adminToken");
        if (!adminToken) {
          console.error("Admin token is missing.");
          return;
        }

        const headers = { Authorization: `Bearer ${adminToken}` };

        const [blogs, allCategories, allUsers, allSeries] = await Promise.all([
          fetcher<Post[]>("/blogs", { headers }),
          fetcher<Category[]>("/categories", { headers }),
          fetcher<Author[]>("/users", { headers }),
          fetcher<Series[]>("/series", { headers }),
        ]);

        const categoryMap: Record<number, string> = {};
        allCategories.forEach((category) => {
          categoryMap[category.id] = category.title;
        });

        const authorMap: Record<number, string> = {};
        allUsers.forEach((user) => {
          authorMap[user.id] = user.name;
        });

        const seriesMap: Record<number, string> = {};
        allSeries.forEach((serie) => {
          seriesMap[serie.id] = serie.title;
        });

        setCategoriesMap(categoryMap);
        setAuthorsMap(authorMap);
        setSeriesMap(seriesMap);
        setPosts(blogs);
        setFilteredPosts(blogs);
      } catch (err) {
        console.error("Failed to fetch data", err);
      }
    };

    fetchData();
  }, []);

  const applySearchFilter = (data: Post[]): Post[] => {
    const lowerCaseSearch = search.toLowerCase();
    return data.filter((post) => {
      switch (searchBy) {
        case "author":
          return authorsMap[post.authorId]
            ?.toLowerCase()
            .includes(lowerCaseSearch);
        case "blog":
          return post.title.toLowerCase().includes(lowerCaseSearch);
        case "series":
          return seriesMap[post.seriesId || 0]
            ?.toLowerCase()
            .includes(lowerCaseSearch);
        case "category":
          return post.categoryIds.some((categoryId) =>
            categoriesMap[categoryId]?.toLowerCase().includes(lowerCaseSearch)
          );
        default:
          return true;
      }
    });
  };

  const handleSearch = () => {
    const filtered = applySearchFilter(posts);
    setFilteredPosts(filtered);
    setCurrentPage(1);
  };

  const handleFilter = () => {
    const { idFrom, idTo, createFrom, createTo, updateFrom, updateTo } =
      filters;

    let filtered = applySearchFilter(posts);

    filtered = filtered.filter((post) => {
      return (
        (!idFrom || post.id >= parseInt(idFrom)) &&
        (!idTo || post.id <= parseInt(idTo)) &&
        (!createFrom || new Date(post.create_at) >= new Date(createFrom)) &&
        (!createTo || new Date(post.create_at) <= new Date(createTo)) &&
        (!updateFrom || new Date(post.update_at) >= new Date(updateFrom)) &&
        (!updateTo || new Date(post.update_at) <= new Date(updateTo))
      );
    });

    setFilteredPosts(filtered);
    setCurrentPage(1);
  };

  const handleResetFilters = () => {
    setFilters({
      idFrom: "",
      idTo: "",
      createFrom: "",
      createTo: "",
      updateFrom: "",
      updateTo: "",
    });
    setFilteredPosts(posts);
  };

  const openDeleteModal = (postId: number) => {
    setPostToDelete(postId);
    setIsDeleteModalOpen(true);
  };

  const handleDelete = async () => {
    try {
      const adminToken = localStorage.getItem("adminToken");
      if (!adminToken) {
        console.error("Admin token is missing.");
        return;
      }

      const headers = { Authorization: `Bearer ${adminToken}` };

      await fetcher(`/blogs/${postToDelete}`, {
        method: "DELETE",
        headers,
      });

      setPosts((prev) => prev.filter((post) => post.id !== postToDelete));
      setFilteredPosts((prev) =>
        prev.filter((post) => post.id !== postToDelete)
      );
      setIsDeleteModalOpen(false);
      setSuccessModalOpen(true);

      setTimeout(() => setSuccessModalOpen(false), 3000);
    } catch (err) {
      console.error("Failed to delete post", err);
    }
  };

  const handleSort = (key: keyof Post) => {
    if (sortConfig.key === key) {
      setSortConfig((prev) => ({
        key,
        direction: prev.direction === "asc" ? "desc" : "asc",
      }));
    } else {
      setSortConfig({ key, direction: "asc" });
    }
  };

  const generatePagination = (): (number | "...")[] => {
    const visiblePages = 5;
    let pages: (number | "...")[] = [];

    if (totalPages <= visiblePages) {
      pages = Array.from({ length: totalPages }, (_, i) => i + 1);
    } else {
      if (currentPage <= Math.ceil(visiblePages / 2)) {
        pages = [
          ...Array.from({ length: visiblePages - 1 }, (_, i) => i + 1),
          "...",
          totalPages,
        ];
      } else if (currentPage > totalPages - Math.floor(visiblePages / 2)) {
        pages = [
          1,
          "...",
          ...Array.from(
            { length: visiblePages - 1 },
            (_, i) => totalPages - visiblePages + i + 2
          ),
        ];
      } else {
        pages = [
          1,
          "...",
          currentPage - 1,
          currentPage,
          currentPage + 1,
          "...",
          totalPages,
        ];
      }
    }

    return pages;
  };

  const sortedPosts = [...filteredPosts].sort((a, b) => {
    const { key, direction } = sortConfig;

    const valueA = a[key as keyof Post];
    const valueB = b[key as keyof Post];

    if (valueA === undefined || valueB === undefined) return 0;

    if (valueA < valueB) return direction === "asc" ? -1 : 1;
    if (valueA > valueB) return direction === "asc" ? 1 : -1;
    return 0;
  });

  const sortableKeys: (keyof Post)[] = [
    "id",
    "authorId",
    "title",
    "categoryIds",
    "seriesId",
    "create_at",
    "update_at",
  ];

  const totalPages =
    postsPerPage === "all" ? 1 : Math.ceil(sortedPosts.length / postsPerPage);
  const paginatedPosts =
    postsPerPage === "all"
      ? sortedPosts
      : sortedPosts.slice(
          (currentPage - 1) * postsPerPage,
          currentPage * postsPerPage
        );

  return (
    <div className="rounded-3xl min-h-screen bg-gradient-to-r from-blue-50 to-indigo-100 p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-700">Manage Posts</h1>
        <div className="flex space-x-4">
          <button
            onClick={() => router.push("/admin/dashboard")}
            className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
          >
            Back to Dashboard
          </button>
          <button
            onClick={() => router.push("/admin/posts/create")}
            className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
          >
            Create New Post
          </button>
        </div>
      </div>

      {/* Search and Filter Section */}
      <div className="flex justify-between items-center bg-white p-4 shadow-md rounded-lg mb-4">
        <div className="flex items-center gap-4">
          <select
            value={searchBy}
            onChange={(e) =>
              setSearchBy(
                e.target.value as "category" | "author" | "blog" | "series"
              )
            }
            className="border border-gray-300 rounded-md p-2"
          >
            <option value="category">Search by Category</option>
            <option value="author">Search by Author</option>
            <option value="blog">Search by Blog Name</option>
            <option value="series">Search by Series</option>
          </select>
          <input
            type="text"
            placeholder="Search..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="flex-1 border border-gray-300 rounded-md p-2"
          />
          <button
            onClick={handleSearch}
            className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
          >
            Search
          </button>
          <button
            onClick={() => setIsFilterOpen(!isFilterOpen)}
            className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
          >
            {isFilterOpen ? "Close Filters" : "Filters"}
          </button>
        </div>

        <div className="flex items-center gap-2">
          <span>Posts per page:</span>
          <select
            value={postsPerPage}
            onChange={(e) => {
              const value =
                e.target.value === "all" ? "all" : parseInt(e.target.value);
              setPostsPerPage(value);
              setCurrentPage(1);
            }}
            className="border border-gray-300 rounded-md p-2"
          >
            <option value="3">3</option>
            <option value="6">6</option>
            <option value="9">9</option>
            <option value="all">All</option>
          </select>
        </div>
      </div>

      {/* Filter Section */}
      {isFilterOpen && (
        <div className="bg-white p-6 shadow-md rounded-lg mb-6">
          <div className="grid grid-cols-2 gap-4">
            {/* Filter Inputs */}
            <div>
              <label className="block text-sm font-medium text-gray-700">
                ID From
              </label>
              <input
                type="number"
                placeholder="From"
                value={filters.idFrom}
                onChange={(e) =>
                  setFilters({ ...filters, idFrom: e.target.value })
                }
                className="border border-gray-300 rounded-md p-2"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                ID To
              </label>
              <input
                type="number"
                placeholder="To"
                value={filters.idTo}
                onChange={(e) =>
                  setFilters({ ...filters, idTo: e.target.value })
                }
                className="border border-gray-300 rounded-md p-2"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Created From
              </label>
              <input
                type="date"
                value={filters.createFrom}
                onChange={(e) =>
                  setFilters({ ...filters, createFrom: e.target.value })
                }
                className="border border-gray-300 rounded-md p-2"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Created To
              </label>
              <input
                type="date"
                value={filters.createTo}
                onChange={(e) =>
                  setFilters({ ...filters, createTo: e.target.value })
                }
                className="border border-gray-300 rounded-md p-2"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Updated From
              </label>
              <input
                type="date"
                value={filters.updateFrom}
                onChange={(e) =>
                  setFilters({ ...filters, updateFrom: e.target.value })
                }
                className="border border-gray-300 rounded-md p-2"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Updated To
              </label>
              <input
                type="date"
                value={filters.updateTo}
                onChange={(e) =>
                  setFilters({ ...filters, updateTo: e.target.value })
                }
                className="border border-gray-300 rounded-md p-2"
              />
            </div>
          </div>
          <div className="mt-4 flex justify-end gap-4">
            <button
              onClick={handleFilter}
              className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
            >
              Apply Filters
            </button>
            <button
              onClick={handleResetFilters}
              className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
            >
              Reset Filters
            </button>
          </div>
        </div>
      )}

      {/* Table Section */}
      <div className="bg-white shadow-md rounded-lg overflow-hidden">
        <table className="table-auto w-full text-left">
          <thead className="bg-gray-100">
            <tr>
              {sortableKeys.map((key) => (
                <th
                  key={key}
                  onClick={() => handleSort(key)} // Now `key` is inferred as `keyof Post`
                  className="px-6 py-3 border-b text-sm font-medium text-gray-600 cursor-pointer"
                >
                  {key.replace(/_/g, " ").toUpperCase()}
                  {sortConfig.key === key && (
                    <span className="ml-1">
                      {sortConfig.direction === "asc" ? "▲" : "▼"}
                    </span>
                  )}
                </th>
              ))}
              <th className="px-6 py-3 border-b text-sm font-medium text-gray-600">
                Actions
              </th>
            </tr>
          </thead>
          <tbody>
            {paginatedPosts.map((post) => (
              <tr key={post.id} className="hover:bg-gray-50">
                <td className="px-6 py-4 border-b text-gray-700">{post.id}</td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {authorsMap[post.authorId] || "Unknown"}
                </td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {post.title}
                </td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {post.categoryIds && post.categoryIds.length > 0 ? (
                    <div className="flex flex-wrap gap-2">
                      {post.categoryIds.map((categoryId) => (
                        <span
                          key={categoryId}
                          className="bg-blue-100 text-blue-800 text-sm font-semibold mr-2 px-3 py-1 rounded"
                        >
                          {categoriesMap[categoryId] || "Unknown"}
                        </span>
                      ))}
                    </div>
                  ) : (
                    <span className="text-gray-500 italic">No categories</span>
                  )}
                </td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {post.seriesId
                    ? seriesMap[post.seriesId] || "Unknown"
                    : "No Series"}
                </td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {post.create_at}
                </td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {post.update_at}
                </td>
                <td className="px-6 py-4 border-b">
                  <button
                    onClick={() => router.push(`/admin/posts/edit/${post.id}`)}
                    className="text-indigo-600 hover:underline mr-4"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => openDeleteModal(post.id)}
                    className="text-red-600 hover:underline"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination Section */}
      {postsPerPage !== "all" && totalPages > 1 && (
        <div className="flex justify-center mt-6">
          <button
            onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
            disabled={currentPage === 1}
            className="px-4 py-2 border rounded-md mr-2 hover:bg-gray-200 disabled:opacity-50"
          >
            Previous
          </button>
          {generatePagination().map((page, index) => (
            <button
              key={index}
              disabled={page === "..."}
              onClick={() => page !== "..." && setCurrentPage(page)}
              className={`px-4 py-2 border rounded-md mx-1 ${
                currentPage === page
                  ? "bg-blue-600 text-white"
                  : "hover:bg-gray-200"
              }`}
            >
              {page}
            </button>
          ))}
          <button
            onClick={() =>
              setCurrentPage((prev) => Math.min(prev + 1, totalPages))
            }
            disabled={currentPage === totalPages}
            className="px-4 py-2 border rounded-md ml-2 hover:bg-gray-200 disabled:opacity-50"
          >
            Next
          </button>
        </div>
      )}

      {/* Delete Modal Section */}
      {isDeleteModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div className="bg-white p-6 rounded-lg shadow-md w-96">
            <h2 className="text-lg font-bold mb-4 text-gray-800">
              Confirm Delete
            </h2>
            <p className="text-sm text-gray-600 mb-6">
              Are you sure you want to delete this post? This action cannot be
              undone.
            </p>
            <div className="flex justify-end space-x-4">
              <button
                onClick={() => setIsDeleteModalOpen(false)}
                className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
              >
                Cancel
              </button>
              <button
                onClick={handleDelete}
                className="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700"
              >
                Delete
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Success Modal Section */}
      {successModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div className="bg-white p-6 rounded-lg shadow-md w-96 flex flex-col items-center justify-center">
            <CheckCircleIcon className="h-12 w-12 text-green-500 mb-4" />
            <h2 className="text-lg font-bold text-gray-800 text-center">
              Successfully Deleted!
            </h2>
          </div>
        </div>
      )}
    </div>
  );
};

export default ManagePostPage;
