"use client";

import React, { useEffect, useState, ChangeEvent } from "react";
import { useRouter } from "next/navigation";
import { fetcher } from "@/services/api";
import { CheckCircleIcon } from "@heroicons/react/24/outline";

// Define interfaces
interface Series {
  id: number;
  title: string;
  description: string;
  authorId: number;
  create_at: string;
  update_at: string;
}

interface User {
  id: number;
  name: string;
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
  key: keyof Series;
  direction: "asc" | "desc";
}

const AdminSeriesPage: React.FC = () => {
  const [series, setSeries] = useState<Series[]>([]);
  const [authorsMap, setAuthorsMap] = useState<Record<number, string>>({});
  const [filteredSeries, setFilteredSeries] = useState<Series[]>([]);
  const [search, setSearch] = useState<string>("");
  const [searchBy, setSearchBy] = useState<"title" | "author">("title");
  const [filters, setFilters] = useState<Filters>({
    idFrom: "",
    idTo: "",
    createFrom: "",
    createTo: "",
    updateFrom: "",
    updateTo: "",
  });
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState<boolean>(false);
  const [seriesToDelete, setSeriesToDelete] = useState<number | null>(null);
  const [isFilterOpen, setIsFilterOpen] = useState<boolean>(false);
  const [successModalOpen, setSuccessModalOpen] = useState<boolean>(false);

  const [sortConfig, setSortConfig] = useState<SortConfig>({
    key: "id",
    direction: "asc",
  });
  const [seriesPerPage, setSeriesPerPage] = useState<number | "all">(3);
  const [currentPage, setCurrentPage] = useState<number>(1);

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

        const [allSeries, allUsers] = await Promise.all([
          fetcher<Series[]>("/series", { headers }),
          fetcher<User[]>("/users", { headers }),
        ]);

        const authorMap: Record<number, string> = {};
        allUsers.forEach((user) => {
          authorMap[user.id] = user.name;
        });

        setAuthorsMap(authorMap);
        setSeries(allSeries);
        setFilteredSeries(allSeries);
      } catch (err) {
        console.error("Failed to fetch data", err);
      }
    };

    fetchData();
  }, []);

  const handleSearch = () => {
    const lowerCaseSearch = search.toLowerCase();
    const filtered = series.filter((item) => {
      if (searchBy === "title") {
        return item.title.toLowerCase().includes(lowerCaseSearch);
      } else if (searchBy === "author") {
        return authorsMap[item.authorId]
          ?.toLowerCase()
          .includes(lowerCaseSearch);
      }
      return true;
    });
    setFilteredSeries(filtered);
    setCurrentPage(1);
  };

  const handleFilter = () => {
    const { idFrom, idTo, createFrom, createTo, updateFrom, updateTo } =
      filters;

    let filtered = series.filter((item) => {
      return (
        (!idFrom || item.id >= parseInt(idFrom)) &&
        (!idTo || item.id <= parseInt(idTo)) &&
        (!createFrom || new Date(item.create_at) >= new Date(createFrom)) &&
        (!createTo || new Date(item.create_at) <= new Date(createTo)) &&
        (!updateFrom || new Date(item.update_at) >= new Date(updateFrom)) &&
        (!updateTo || new Date(item.update_at) <= new Date(updateTo))
      );
    });

    setFilteredSeries(filtered);
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
    setFilteredSeries(series);
  };

  const openDeleteModal = (seriesId: number) => {
    setSeriesToDelete(seriesId);
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

      await fetcher(`/series/${seriesToDelete}`, {
        method: "DELETE",
        headers,
      });

      const updatedSeries = series.filter((item) => item.id !== seriesToDelete);
      setSeries(updatedSeries);
      setFilteredSeries(updatedSeries);

      setIsDeleteModalOpen(false);
      setSuccessModalOpen(true);

      setTimeout(() => setSuccessModalOpen(false), 3000);
    } catch (err) {
      console.error("Failed to delete series", err);
    }
  };

  const handleSort = (key: keyof Series) => {
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
    const visiblePages = 5; // Number of visible pages in the pagination
    let pages: (number | "...")[] = [];

    if (totalPages <= visiblePages) {
      // If total pages fit within the visible range
      pages = Array.from({ length: totalPages }, (_, i) => i + 1);
    } else {
      if (currentPage <= Math.ceil(visiblePages / 2)) {
        // Close to the start of the pagination
        pages = [
          ...Array.from({ length: visiblePages - 1 }, (_, i) => i + 1),
          "...",
          totalPages,
        ];
      } else if (currentPage > totalPages - Math.floor(visiblePages / 2)) {
        // Close to the end of the pagination
        pages = [
          1,
          "...",
          ...Array.from(
            { length: visiblePages - 1 },
            (_, i) => totalPages - visiblePages + i + 2
          ),
        ];
      } else {
        // Somewhere in the middle of the pagination
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

  const sortedSeries = [...filteredSeries].sort((a, b) => {
    const { key, direction } = sortConfig;

    const valueA = a[key];
    const valueB = b[key];

    if (valueA < valueB) return direction === "asc" ? -1 : 1;
    if (valueA > valueB) return direction === "asc" ? 1 : -1;
    return 0;
  });

  const totalPages =
    seriesPerPage === "all"
      ? 1
      : Math.ceil(sortedSeries.length / seriesPerPage);
  const paginatedSeries =
    seriesPerPage === "all"
      ? sortedSeries
      : sortedSeries.slice(
          (currentPage - 1) * seriesPerPage,
          currentPage * seriesPerPage
        );

  return (
    <div className="rounded-3xl min-h-screen bg-gradient-to-r from-blue-50 to-indigo-100 p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-700">Manage Series</h1>
        <div className="flex space-x-4">
          <button
            onClick={() => router.push("/admin/dashboard")}
            className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400"
          >
            Back to Dashboard
          </button>
          <button
            onClick={() => router.push("/admin/series/create")}
            className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
          >
            Create New Series
          </button>
        </div>
      </div>

      {/* Search, Filter, View Per Page */}
      <div className="flex justify-between items-center bg-white p-4 shadow-md rounded-lg mb-4">
        <div className="flex items-center gap-4">
          <select
            value={searchBy}
            onChange={(e) => setSearchBy(e.target.value as "title" | "author")}
            className="border border-gray-300 rounded-md p-2"
          >
            <option value="title">Search by Title</option>
            <option value="author">Search by Author</option>
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
          <span>Series per page:</span>
          <select
            value={seriesPerPage}
            onChange={(e) => {
              const value =
                e.target.value === "all" ? "all" : parseInt(e.target.value);
              setSeriesPerPage(value);
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

      {/* Filters */}
      {isFilterOpen && (
        <div className="bg-white p-6 shadow-md rounded-lg mb-6">
          <div className="grid grid-cols-2 gap-4">
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
                placeholder="mm/dd/yyyy"
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
                placeholder="mm/dd/yyyy"
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
                placeholder="mm/dd/yyyy"
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
                placeholder="mm/dd/yyyy"
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

      {/* Table */}
      <div className="bg-white shadow-md rounded-lg overflow-hidden">
        <table className="table-auto w-full text-left">
          <thead className="bg-gray-100">
            <tr>
              {(
                [
                  "id",
                  "title",
                  "description",
                  "author",
                  "create_at",
                  "update_at",
                ] as Array<keyof Series>
              ).map((key) => (
                <th
                  key={key}
                  className={`px-6 py-3 border-b text-sm font-medium text-gray-600 cursor-pointer`}
                  onClick={() => handleSort(key)}
                >
                  <span>{key.replace(/_/g, " ").toUpperCase()}</span>
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
            {paginatedSeries.map((serie) => (
              <tr key={serie.id} className="hover:bg-gray-50">
                <td className="px-6 py-4 border-b text-gray-700">{serie.id}</td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {serie.title}
                </td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {serie.description.length > 100
                    ? `${serie.description.slice(0, 100)}...`
                    : serie.description}
                </td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {authorsMap[serie.authorId] || "Unknown"}
                </td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {serie.create_at}
                </td>
                <td className="px-6 py-4 border-b text-gray-700">
                  {serie.update_at}
                </td>
                <td className="px-6 py-4 border-b">
                  <button
                    onClick={() =>
                      router.push(`/admin/series/edit/${serie.id}`)
                    }
                    className="text-indigo-600 hover:underline mr-4"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => openDeleteModal(serie.id)}
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

      {/* Pagination */}
      {seriesPerPage !== "all" && totalPages > 1 && (
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

      {/* Delete Modal */}
      {isDeleteModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div className="bg-white p-6 rounded-lg shadow-md w-96">
            <h2 className="text-lg font-bold mb-4 text-gray-800">
              Confirm Delete
            </h2>
            <p className="text-sm text-gray-600 mb-6">
              Are you sure you want to delete this series? This action cannot be
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

      {/* Success Modal */}
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

export default AdminSeriesPage;
