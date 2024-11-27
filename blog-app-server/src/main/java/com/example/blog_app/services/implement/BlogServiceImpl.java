package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.exceptions.UnauthorizedException;
import com.example.blog_app.models.dtos.blog.BlogRequestDto;
import com.example.blog_app.models.dtos.blog.BlogResponseDto;
import com.example.blog_app.models.entities.Blog;
import com.example.blog_app.models.entities.Category;
import com.example.blog_app.models.entities.Series;
import com.example.blog_app.models.entities.User;
import com.example.blog_app.models.mappers.BlogMapper;
import com.example.blog_app.repositories.BlogRepository;
import com.example.blog_app.repositories.CategoryRepository;
import com.example.blog_app.repositories.SeriesRepository;
import com.example.blog_app.repositories.UserRepository;
import com.example.blog_app.security.SecurityUtils;
import com.example.blog_app.services.BlogService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link BlogService} interface for managing blogs.
 *
 * <p>This service handles operations such as:</p>
 * <ul>
 *   <li>Creating, updating, retrieving, and deleting blog posts.</li>
 *   <li>Managing blog relationships with authors, categories, and series.</li>
 *   <li>Providing filtered results based on user, series, or category.</li>
 * </ul>
 */
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;
    private final CategoryRepository categoryRepository;
    private final BlogMapper blogMapper;

    /**
     * Constructs the BlogServiceImpl with the required dependencies.
     *
     * @param blogRepository     the repository for managing blogs
     * @param userRepository     the repository for managing users
     * @param seriesRepository   the repository for managing series
     * @param categoryRepository the repository for managing categories
     * @param blogMapper         the mapper for converting Blog entities to/from DTOs
     */
    public BlogServiceImpl(BlogRepository blogRepository, UserRepository userRepository,
                           SeriesRepository seriesRepository, CategoryRepository categoryRepository,
                           BlogMapper blogMapper) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.seriesRepository = seriesRepository;
        this.categoryRepository = categoryRepository;
        this.blogMapper = blogMapper;
    }

    /**
     * Creates a new blog post.
     *
     * @param blogDto the DTO containing blog details for creation
     * @return the created blog's details as a response DTO
     * @throws ResourceNotFoundException if the author, categories, or series do not exist
     * @throws UnauthorizedException if a non-admin user attempts to assign an author
     */
    @Override
    public BlogResponseDto createBlog(BlogRequestDto blogDto) {
        String currentUserEmail = SecurityUtils.getCurrentUserEmail();
        User currentUser = getUserByEmail(currentUserEmail);

        User author;
        if (blogDto.getAuthorId() != null) {
            if (isAdmin(currentUser)) {
                author = getUserById(blogDto.getAuthorId());
            } else {
                throw new UnauthorizedException("Only admins can assign an author.");
            }
        } else {
            author = currentUser;
        }

        Blog blog = blogMapper.toEntity(blogDto);
        blog.setUser(author);

        handleCategory(blog, blogDto.getCategoryIds());
        handleSeries(blog, blogDto.getSeriesId());

        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.toResponseDto(savedBlog);
    }

    /**
     * Updates an existing blog post.
     *
     * @param blogDto the DTO containing updated blog details
     * @param blogId  the ID of the blog to update
     * @return the updated blog's details as a response DTO
     * @throws ResourceNotFoundException if the blog, categories, or series do not exist
     * @throws UnauthorizedException if the current user lacks permission to update
     */
    @Override
    public BlogResponseDto updateBlogById(BlogRequestDto blogDto, Long blogId) {
        Blog blog = getBlogEntityById(blogId);

        User currentUser = getUserByEmail(SecurityUtils.getCurrentUserEmail());
        checkPermission(blog, currentUser);

        blogMapper.updateBlogFromDto(blogDto, blog);

        handleCategory(blog, blogDto.getCategoryIds());
        handleSeries(blog, blogDto.getSeriesId());

        Blog updatedBlog = blogRepository.save(blog);
        return blogMapper.toResponseDto(updatedBlog);
    }

    /**
     * Retrieves all blog posts.
     *
     * @return a list of all blogs as response DTOs
     */
    @Override
    public List<BlogResponseDto> getAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(blogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific blog post by its ID.
     *
     * @param blogId the ID of the blog to retrieve
     * @return the blog's details as a response DTO
     * @throws ResourceNotFoundException if the blog does not exist
     */
    @Override
    public BlogResponseDto getBlogById(Long blogId) {
        Blog blog = getBlogEntityById(blogId);
        return blogMapper.toResponseDto(blog);
    }

    /**
     * Deletes a blog post by its ID.
     *
     * @param blogId the ID of the blog to delete
     * @throws ResourceNotFoundException if the blog does not exist
     * @throws UnauthorizedException if the current user lacks permission to delete
     */
    @Override
    public void deleteBlogById(Long blogId) {
        Blog blog = getBlogEntityById(blogId);

        User currentUser = getUserByEmail(SecurityUtils.getCurrentUserEmail());
        checkPermission(blog, currentUser);

        blogRepository.delete(blog);
    }

    /**
     * Retrieves all blogs authored by a specific user.
     *
     * @param userId the ID of the user
     * @return a list of blogs authored by the user as response DTOs
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    public List<BlogResponseDto> getBlogsByUserId(Long userId) {
        User author = getUserById(userId);
        return blogRepository.findByUser(author)
                .stream()
                .map(blogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all blogs in a specific series.
     *
     * @param seriesId the ID of the series
     * @return a list of blogs in the series as response DTOs
     * @throws ResourceNotFoundException if the series does not exist
     */
    @Override
    public List<BlogResponseDto> getBlogsBySeriesId(Long seriesId) {
        Series series = getSeriesById(seriesId);
        return blogRepository.findBySeries(series)
                .stream()
                .map(blogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all blogs in a specific category.
     *
     * @param categoryId the ID of the category
     * @return a list of blogs in the category as response DTOs
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public List<BlogResponseDto> getBlogsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
        return blogRepository.findByCategoriesContains(category)
                .stream()
                .map(blogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId the ID of the user
     * @return the user entity
     * @throws ResourceNotFoundException if the user does not exist
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    /**
     * Retrieves a series by ID.
     *
     * @param seriesId the ID of the series
     * @return the series entity
     * @throws ResourceNotFoundException if the series does not exist
     */
    private Series getSeriesById(Long seriesId) {
        return seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Series not found with ID: " + seriesId));
    }

    /**
     * Handles category associations for a blog.
     *
     * <p>If categories are provided, validates their existence and associates them with the blog.
     * If no categories are provided, clears any existing associations.</p>
     *
     * @param blog        the blog entity to update
     * @param categoryIds the IDs of the categories to associate
     * @throws ResourceNotFoundException if any category IDs are invalid
     */
    private void handleCategory(Blog blog, Set<Long> categoryIds) {
        if (categoryIds != null && !categoryIds.isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));

            Set<Long> invalidCategoryIds = categoryIds.stream()
                    .filter(categoryId -> categories.stream().noneMatch(category -> category.getId().equals(categoryId)))
                    .collect(Collectors.toSet());

            if (!invalidCategoryIds.isEmpty()) {
                throw new ResourceNotFoundException("Categories not found with IDs: " + invalidCategoryIds);
            }

            blog.setCategories(categories);
        } else {
            blog.setCategories(new HashSet<>());
        }
    }

    /**
     * Handles series association for a blog.
     *
     * <p>If a series ID is provided, validates its existence and associates it with the blog.
     * If no series ID is provided, clears any existing association.</p>
     *
     * @param blog     the blog entity to update
     * @param seriesId the ID of the series to associate
     * @throws ResourceNotFoundException if the series ID is invalid
     */
    private void handleSeries(Blog blog, Long seriesId) {
        if (seriesId != null) {
            Series series = getSeriesById(seriesId);
            blog.setSeries(series);
        } else {
            blog.setSeries(null);
        }
    }

    /**
     * Checks if a user has the ADMIN role.
     *
     * @param user the user entity
     * @return {@code true} if the user has ADMIN role, otherwise {@code false}
     */
    private boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN"));
    }

    /**
     * Validates whether the current user has permission to perform an action on a blog.
     *
     * <p>Admins or the blog's author have permission.</p>
     *
     * @param blog the blog entity
     * @param user the user entity attempting the action
     * @throws UnauthorizedException if the user lacks the required permissions
     */
    private void checkPermission(Blog blog, User user) {
        if (!isAdmin(user) && !(blog.getUser().getId() == user.getId())) {
            throw new UnauthorizedException("You do not have permission to perform this action.");
        }
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user
     * @return the user entity
     * @throws ResourceNotFoundException if the user does not exist
     */
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * Retrieves a blog entity by its ID.
     *
     * @param blogId the ID of the blog
     * @return the blog entity
     * @throws ResourceNotFoundException if the blog does not exist
     */
    private Blog getBlogEntityById(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with ID: " + blogId));
    }
}
