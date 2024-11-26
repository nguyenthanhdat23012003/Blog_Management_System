package com.example.blog_app.services;

import com.example.blog_app.models.dtos.blog.BlogRequestDto;
import com.example.blog_app.models.dtos.blog.BlogResponseDto;

import java.util.List;

/**
 * Service interface for managing blogs.
 *
 * <p>This interface defines the contract for operations related to blogs,
 * including creating, updating, retrieving, and deleting blogs.</p>
 *
 * <p>It also provides APIs for querying blogs by user, series, and category.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * BlogService blogService = new BlogServiceImpl();
 * BlogResponseDto blog = blogService.getBlog(1L);
 * }
 * </pre>
 */
public interface BlogService {

    /**
     * Creates a new blog.
     *
     * @param blogDto the DTO containing the details of the blog to be created
     * @return the created blog as a response DTO
     */
    BlogResponseDto createBlog(BlogRequestDto blogDto);

    /**
     * Updates an existing blog.
     *
     * @param blogDto the DTO containing the updated blog details
     * @param blogId  the ID of the blog to update
     * @return the updated blog as a response DTO
     */
    BlogResponseDto updateBlogById(BlogRequestDto blogDto, Long blogId);

    /**
     * Retrieves all blogs.
     *
     * @return a list of blog response DTOs representing all blogs
     */
    List<BlogResponseDto> getAllBlogs();

    /**
     * Retrieves a specific blog by its ID.
     *
     * @param blogId the ID of the blog to retrieve
     * @return the blog as a response DTO
     */
    BlogResponseDto getBlogById(Long blogId);

    /**
     * Deletes a specific blog by its ID.
     *
     * @param blogId the ID of the blog to delete
     */
    void deleteBlogById(Long blogId);

    /**
     * Retrieves all blogs authored by a specific user.
     *
     * @param userId the ID of the user whose blogs are to be retrieved
     * @return a list of blog response DTOs authored by the user
     */
    List<BlogResponseDto> getBlogsByUserId(Long userId);

    /**
     * Retrieves all blogs belonging to a specific series.
     *
     * @param seriesId the ID of the series whose blogs are to be retrieved
     * @return a list of blog response DTOs belonging to the series
     */
    List<BlogResponseDto> getBlogsBySeriesId(Long seriesId);

    /**
     * Retrieves all blogs associated with a specific category.
     *
     * @param categoryId the ID of the category whose blogs are to be retrieved
     * @return a list of blog response DTOs associated with the category
     */
    List<BlogResponseDto> getBlogsByCategoryId(Long categoryId);
}
