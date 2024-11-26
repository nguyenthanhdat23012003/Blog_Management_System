package com.example.blog_app.controllers;

import com.example.blog_app.common.validation.CreateValidationGroup;
import com.example.blog_app.common.validation.UpdateValidationGroup;
import com.example.blog_app.models.dtos.BlogRequestDto;
import com.example.blog_app.models.dtos.BlogResponseDto;
import com.example.blog_app.services.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing blog posts in the application.
 *
 * <p>This controller provides endpoints for creating, retrieving, updating,
 * and deleting blog posts. Additionally, it offers endpoints for filtering
 * blogs by author, series, or category.</p>
 *
 * <p>Example endpoints:</p>
 * <ul>
 *   <li>POST /api/blogs - Create a new blog</li>
 *   <li>PUT /api/blogs/{blogId} - Update an existing blog</li>
 *   <li>GET /api/blogs/{blogId} - Retrieve a specific blog by ID</li>
 *   <li>GET /api/blogs - Retrieve all blogs</li>
 *   <li>DELETE /api/blogs/{blogId} - Delete a blog</li>
 *   <li>GET /api/blogs/users/{userId} - Retrieve blogs by a specific user</li>
 *   <li>GET /api/blogs/series/{seriesId} - Retrieve blogs in a specific series</li>
 *   <li>GET /api/blogs/categories/{categoryId} - Retrieve blogs in a specific category</li>
 * </ul>
 *
 * @see BlogService
 * @see BlogRequestDto
 * @see BlogResponseDto
 */
@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;

    /**
     * Constructs a new {@link BlogController} with the given {@link BlogService}.
     *
     * @param blogService the service to handle blog-related business logic
     */
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    /**
     * Creates a new blog post.
     *
     * @param blogDto the DTO containing blog details
     * @return the created blog as a {@link BlogResponseDto}
     */
    @PostMapping
    public ResponseEntity<BlogResponseDto> createBlog(
            @Validated(CreateValidationGroup.class) @RequestBody BlogRequestDto blogDto) {
        BlogResponseDto createdBlog = blogService.createBlog(blogDto);
        return ResponseEntity.status(201).body(createdBlog);
    }

    /**
     * Updates an existing blog post by its ID.
     *
     * @param blogDto the DTO containing updated blog details
     * @param blogId  the ID of the blog to update
     * @return the updated blog as a {@link BlogResponseDto}
     */
    @PutMapping("/{blogId}")
    public ResponseEntity<BlogResponseDto> updateBlogById(
            @Validated(UpdateValidationGroup.class) @RequestBody BlogRequestDto blogDto,
            @PathVariable Long blogId) {
        BlogResponseDto updatedBlog = blogService.updateBlogById(blogDto, blogId);
        return ResponseEntity.ok(updatedBlog);
    }

    /**
     * Retrieves a specific blog post by its ID.
     *
     * @param blogId the ID of the blog to retrieve
     * @return the blog details as a {@link BlogResponseDto}
     */
    @GetMapping("/{blogId}")
    public ResponseEntity<BlogResponseDto> getBlogById(@PathVariable Long blogId) {
        BlogResponseDto blog = blogService.getBlogById(blogId);
        return ResponseEntity.ok(blog);
    }

    /**
     * Retrieves all blog posts.
     *
     * @return a list of all blogs as {@link BlogResponseDto}
     */
    @GetMapping
    public ResponseEntity<List<BlogResponseDto>> getAllBlogs() {
        List<BlogResponseDto> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogs);
    }

    /**
     * Deletes a specific blog post by its ID.
     *
     * @param blogId the ID of the blog to delete
     * @return a confirmation message indicating the deletion
     */
    @DeleteMapping("/{blogId}")
    public ResponseEntity<String> deleteBlogById(@PathVariable Long blogId) {
        blogService.deleteBlogById(blogId);
        return ResponseEntity.ok("Blog deleted successfully");
    }

    /**
     * Retrieves all blogs authored by a specific user.
     *
     * @param userId the ID of the user
     * @return a list of blogs authored by the user as {@link BlogResponseDto}
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<BlogResponseDto>> getBlogsByUserId(@PathVariable Long userId) {
        List<BlogResponseDto> blogs = blogService.getBlogsByUserId(userId);
        return ResponseEntity.ok(blogs);
    }

    /**
     * Retrieves all blogs in a specific series.
     *
     * @param seriesId the ID of the series
     * @return a list of blogs in the series as {@link BlogResponseDto}
     */
    @GetMapping("/series/{seriesId}")
    public ResponseEntity<List<BlogResponseDto>> getBlogsBySeriesId(@PathVariable Long seriesId) {
        List<BlogResponseDto> blogs = blogService.getBlogsBySeriesId(seriesId);
        return ResponseEntity.ok(blogs);
    }

    /**
     * Retrieves all blogs in a specific category.
     *
     * @param categoryId the ID of the category
     * @return a list of blogs in the category as {@link BlogResponseDto}
     */
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<BlogResponseDto>> getBlogsByCategoryId(@PathVariable Long categoryId) {
        List<BlogResponseDto> blogs = blogService.getBlogsByCategoryId(categoryId);
        return ResponseEntity.ok(blogs);
    }
}
