# Blog Management System Documentation

## Table of Contents

1. [Introduction](#introduction)

   - Overview
   - Features
   - Technology Stack

2. [Getting Started](#getting-started)

   - Prerequisites
   - Installation
   - Running the Application

3. [Database Design](#database-design)

   - Schema Overview
   - ER Diagram

4. [API Documentation](#api-documentation)

   - Authentication APIs
   - Blog Management APIs
   - User Management APIs
   - Category Management APIs

5. [Frontend Overview](#frontend-overview)

   - Key Pages and Features
   - Folder Structure

6. [Backend Overview](#backend-overview)

   - Key Modules
   - Folder Structure

7. [Deployment](#deployment)

   - Environment Configuration
   - Deployment Steps (Localhost, Server, Docker)

8. [Application Workflow](#application-workflow)

   - Authentication Flow
   - Blog Creation and Management Flow

9. [Connecting the Application](#connecting-the-application)

   - Frontend to Backend
   - Backend to Database

10. [Testing](#testing)

    - Unit Tests
    - Integration Tests

11. [Troubleshooting](#troubleshooting)

    - Common Issues
    - Debugging Tips

12. [Future Improvements](#future-improvements)

    - Planned Features
    - Known Limitations

13. [Contributing](#contributing)

    - Guidelines
    - Pull Request Process

14. [License](#license)

## Introduction

### Overview

The Blog Management System is a full-stack web application designed to simplify the process of creating, managing, and organizing blog posts. It is built for individuals, small businesses, and content creators looking for an efficient platform to handle their blogging needs. The system integrates a powerful backend with an intuitive frontend to ensure a seamless user experience.

### Features

- **User Authentication**: Secure login and registration with role-based access control.
- **Blog Management**: Create, edit, delete, and publish blog posts with rich text formatting.
- **Category Management**: Organize blogs into categories for better navigation.
- **User Management**: Admin capabilities to manage users, roles, and permissions.
- **Responsive Design**: Optimized for all screen sizes, including mobile and desktop.
- **Search and Filters**: Advanced searching and filtering to quickly find content.
- **Commenting System**: Engage readers through a built-in comment section.
- **Analytics Dashboard**: Monitor blog performance with real-time stats (optional).

### Technology Stack

- **Backend**: Spring Boot - A robust and scalable Java framework for building RESTful APIs.
- **Frontend**: Next.js - A modern React framework for building dynamic and fast web interfaces.
- **Database**: MySQL - A reliable relational database for managing data.
- **Others**:
  - **ORM**: Hibernate for database interaction.
  - **Build Tools**: Maven for dependency management.
  - **API Documentation**: Swagger/OpenAPI for detailed API specifications.
  - **Deployment**: Docker for containerization and seamless deployment.
  - **Database visualization**: Use adminer to visually interact with the database

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed on your system:

- **For local setup**:
  - JDK (Java Development Kit) version 17 or higher
  - Node.js version 16 or higher
  - Maven version 3.6.3 or higher
  - MySQL version 8.0 or higher
- **For Docker setup**:
  - Docker version 20.10 or higher
  - Docker Compose version 1.29 or higher

---

### Option 1: Build and Run Locally

Follow these steps to build and run the application on your local machine:

1. **Clone the Repository**  
   `git clone https://github.com/your-repo/blog-management-system.git`  
   `cd blog-management-system`

2. **Set Up the Database**

   - Create a MySQL database:  
     Database name: `blog_spring`  
     Username: `spring`  
     Password: `122002`

3. **Configure Backend**

   - Navigate to the backend directory:  
     `cd blog-app-server`
   - Open `src/main/resources/application.properties` and set up the database connection:
     ```
     spring.datasource.url=jdbc:mysql://localhost:3306/blog_spring
     spring.datasource.username=spring
     spring.datasource.password=122002
     ```

4. **Build and Run Backend**

   - Build the backend:  
     `mvn clean install`
   - Run the backend:  
     `mvn spring-boot:run`

5. **Configure Frontend**

   - Navigate to the frontend directory:  
     `cd blog-app-client`
   - Open `.env.local` and configure the API URL:
     ```
     NEXT_PUBLIC_API_URL=http://localhost:8080/api
     ```

6. **Build and Run Frontend**

   - Install dependencies:  
     `npm install`
   - Start the frontend development server:  
     `npm run dev`

7. **Verify Setup**

   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api

8. **Import Sample Data (Optional)**
   - Import sample data into MySQL:
     ```
     mysql -u root -p122002 -D blog_spring < mysql-fake-data/init.sql
     ```

---

### Option 2: Build and Run with Docker

Follow these steps to build and run the application using Docker:

1. **Clone the Repository**  
   `git clone https://github.com/your-repo/blog-management-system.git`  
   `cd blog-management-system`

2. **Start Services with Docker Compose**

   - Build and start the containers:  
     `docker-compose up --build`
   - This will start the following containers:
     - MySQL Database (`mysql_container`)
     - Spring Boot Backend (`spring_boot_container`)
     - Next.js Frontend (`nextjs_container`)
     - Adminer (`adminer_container`)

3. **Resolve Startup Issues (if any)**

   - If the Spring Boot container fails due to MySQL not being ready, restart the services:  
     `docker-compose up -d`

4. **Import Sample Data (Optional)**

   - Import sample data into the MySQL container:
     ```
     docker exec -i mysql_container mysql -u root -p122002 -D blog_spring < mysql-fake-data/init.sql
     ```

5. **Verify Setup**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api
   - Adminer: http://localhost:8081
     - Server: `mysql_container`
     - Username: `spring`
     - Password: `122002`
     - Database: `blog_spring`

---

### Notes

- **For local setup**, ensure MySQL is running and properly configured before starting the backend.
- **For Docker setup**, the `docker-compose.yml` file manages all dependencies and services automatically.
- Use Adminer (http://localhost:8081) to visually interact with the database.
- For development, frontend hot-reloading is enabled in both setups.
- The code converted to TypeScript is in the master-type-script branch, switch to that branch and do the same steps as above.

## Database Design

### Overview

The database for the Blog Management System consists of multiple tables that manage users, roles, permissions, blogs, categories, and relationships between these entities. Below is an outline of the tables, their fields, and relationships.

---

### 1. Tables and Their Structures

#### 1.1 `users` Table

Stores information about the users of the system.

- **Columns**:
  - `id`: bigint (Primary Key, Auto Increment)
  - `name`: varchar(100)
  - `email`: varchar(255) (Unique)
  - `password`: varchar(255)
  - `about`: varchar(500) (Nullable)
  - `created_at`: datetime(6) (Nullable)
  - `updated_at`: datetime(6) (Nullable)
  - `immutable`: tinyint(1) (Default: 0)
- **Indexes**:
  - Primary Key: `id`
  - Unique: `email`

---

#### 1.2 `roles` Table

Defines user roles.

- **Columns**:
  - `id`: bigint (Primary Key, Auto Increment)
  - `name`: varchar(255) (Unique)
  - `immutable`: tinyint(1) (Default: 0)
- **Indexes**:
  - Primary Key: `id`
  - Unique: `name`

---

#### 1.3 `permissions` Table

Defines specific permissions.

- **Columns**:
  - `id`: bigint (Primary Key, Auto Increment)
  - `name`: varchar(255) (Unique)
  - `immutable`: tinyint(1) (Default: 0)
- **Indexes**:
  - Primary Key: `id`
  - Unique: `name`

---

#### 1.4 `user_roles` Table

Defines the relationship between users and roles (many-to-many).

- **Columns**:
  - `user_id`: bigint (Foreign Key → `users.id`)
  - `role_id`: bigint (Foreign Key → `roles.id`)
- **Indexes**:
  - Primary Key: (`user_id`, `role_id`)

---

#### 1.5 `role_permissions` Table

Defines the relationship between roles and permissions (many-to-many).

- **Columns**:
  - `role_id`: bigint (Foreign Key → `roles.id`)
  - `permission_id`: bigint (Foreign Key → `permissions.id`)
- **Indexes**:
  - Primary Key: (`role_id`, `permission_id`)

---

#### 1.6 `blogs` Table

Stores blog post information.

- **Columns**:
  - `id`: bigint (Primary Key, Auto Increment)
  - `title`: varchar(100)
  - `content`: json (Nullable)
  - `author_id`: bigint (Foreign Key → `users.id`, Nullable)
  - `series_id`: bigint (Foreign Key → `series.id`, Nullable)
  - `created_at`: datetime(6) (Nullable)
  - `updated_at`: datetime(6) (Nullable)
- **Indexes**:
  - Primary Key: `id`
  - Foreign Key: `author_id`, `series_id`

---

#### 1.7 `categories` Table

Defines blog categories.

- **Columns**:
  - `id`: bigint (Primary Key, Auto Increment)
  - `title`: varchar(100)
  - `description`: text (Nullable)
- **Indexes**:
  - Primary Key: `id`

---

#### 1.8 `blog_categories` Table

Defines the relationship between blogs and categories (many-to-many).

- **Columns**:
  - `blog_id`: bigint (Foreign Key → `blogs.id`)
  - `category_id`: bigint (Foreign Key → `categories.id`)
- **Indexes**:
  - Primary Key: (`blog_id`, `category_id`)

---

#### 1.9 `series` Table

Stores information about blog series.

- **Columns**:
  - `id`: bigint (Primary Key, Auto Increment)
  - `title`: varchar(100)
  - `description`: text (Nullable)
  - `author_id`: bigint (Foreign Key → `users.id`, Nullable)
  - `created_at`: datetime(6) (Nullable)
  - `updated_at`: datetime(6) (Nullable)
- **Indexes**:
  - Primary Key: `id`
  - Foreign Key: `author_id`

---

### 2. Relationships

1. **Users and Roles**:
   - Many-to-Many: `users` ↔ `roles` via `user_roles`.
2. **Roles and Permissions**:

   - Many-to-Many: `roles` ↔ `permissions` via `role_permissions`.

3. **Blogs and Categories**:

   - Many-to-Many: `blogs` ↔ `categories` via `blog_categories`.

4. **Blogs and Series**:

   - One-to-Many: `series` → `blogs`.

5. **Users and Blogs**:

   - One-to-Many: `users` → `blogs`.

6. **Users and Series**:
   - One-to-Many: `users` → `series`.

---

## API Documentation

This application provides a comprehensive set of RESTful APIs for managing blogs, users, roles, categories, and series. You can test and explore the APIs using the Postman collection shared below.

### Explore APIs with Postman

Click the button below to import the Postman collection and environment:

<div
  class="postman-run-button"
  data-postman-action="collection/fork"
  data-postman-visibility="public"
  data-postman-var-1="27157288-5c2e7b50-30f7-4147-990d-349eb1ce6b6f"
  data-postman-collection-url="entityId=27157288-5c2e7b50-30f7-4147-990d-349eb1ce6b6f&entityType=collection&workspaceId=4d129c2a-7383-4726-90e0-662629a11f70"
  data-postman-param="env%5BLocal%20(Spring)%5D=W3sia2V5IjoiQmFzZVVSTCIsInZhbHVlIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiZW5hYmxlZCI6dHJ1ZSwidHlwZSI6ImRlZmF1bHQifSx7ImtleSI6InVzZXJJZCIsInZhbHVlIjoiIiwiZW5hYmxlZCI6dHJ1ZSwidHlwZSI6ImFueSJ9LHsia2V5IjoidXNlck5hbWUiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWUsInR5cGUiOiJkZWZhdWx0In0seyJrZXkiOiJ1c2VyRW1haWwiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWUsInR5cGUiOiJkZWZhdWx0In0seyJrZXkiOiJ1c2VyQWJvdXQiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWUsInR5cGUiOiJkZWZhdWx0In0seyJrZXkiOiJyb2xlSWQiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWUsInR5cGUiOiJhbnkifSx7ImtleSI6ImNhdGVnb3J5SWQiLCJ2YWx1ZSI6IjEiLCJlbmFibGVkIjp0cnVlLCJ0eXBlIjoiZGVmYXVsdCJ9LHsia2V5Ijoic2VyaWVzSWQiLCJ2YWx1ZSI6IjEiLCJlbmFibGVkIjp0cnVlLCJ0eXBlIjoiZGVmYXVsdCJ9LHsia2V5IjoicGVybWlzc2lvbklkIiwidmFsdWUiOiIiLCJlbmFibGVkIjp0cnVlLCJ0eXBlIjoiYW55In0seyJrZXkiOiJibG9nSWQiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWUsInR5cGUiOiJhbnkifSx7ImtleSI6InRva2VuQWRtaW4iLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWUsInR5cGUiOiJkZWZhdWx0In0seyJrZXkiOiJ0b2tlblVzZXIiLCJ2YWx1ZSI6IiIsImVuYWJsZWQiOnRydWUsInR5cGUiOiJkZWZhdWx0In0seyJrZXkiOiJ1c2VyRW1haWxcbiIsInZhbHVlIjoiIiwiZW5hYmxlZCI6dHJ1ZSwidHlwZSI6ImRlZmF1bHQifSx7ImtleSI6InVzZXJQYXNzIiwidmFsdWUiOiIiLCJlbmFibGVkIjp0cnVlLCJ0eXBlIjoiZGVmYXVsdCJ9XQ=="
></div>
<script type="text/javascript">
  (function (p, o, s, t, m, a, n) {
    !p[s] &&
      (p[s] = function () {
        (p[t] || (p[t] = [])).push(arguments);
      });
    !o.getElementById(s + t) &&
      o
        .getElementsByTagName("head")[0]
        .appendChild(
          ((n = o.createElement("script")),
          (n.id = s + t),
          (n.async = 1),
          (n.src = m),
          n)
        );
  })(
    window,
    document,
    "_pm",
    "PostmanRunObject",
    "https://run.pstmn.io/button.js"
  );
</script>

---

### How to Use the API

1. **Authentication**:  
   Obtain a token by using the `/api/v1/auth/login` endpoint.

2. **Base URL**:

   - Local Environment: `http://localhost:8080/api/v1`

3. **Endpoints Overview**:
   - **Users**:
     - GET `/users`: Fetch all users
     - POST `/users`: Create a new user
     - PUT `/users/{id}`: Update user details
     - DELETE `/users/{id}`: Delete a user
   - **Blogs**:
     - GET `/blogs`: Fetch all blogs
     - POST `/blogs`: Create a new blog
     - GET `/blogs/{id}`: Fetch a specific blog
     - PUT `/blogs/{id}`: Update a blog
     - DELETE `/blogs/{id}`: Delete a blog
   - **Categories**:
     - GET `/categories`: Fetch all categories
     - POST `/categories`: Create a new category
   - **Roles & Permissions**:
     - GET `/roles`: Fetch all roles
     - POST `/roles`: Create a new role

For a detailed list of all endpoints, headers, and request-response samples, please use the Postman collection.

---

### Notes

- Ensure the backend service is running on `http://localhost:8080`.
- Use the Admin or User token for testing endpoints with authorization.

---
