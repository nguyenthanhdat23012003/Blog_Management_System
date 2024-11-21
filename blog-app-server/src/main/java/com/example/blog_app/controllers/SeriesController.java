package com.example.blog_app.controllers;

import com.example.blog_app.common.validation.CreateValidationGroup;
import com.example.blog_app.common.validation.UpdateValidationGroup;
import com.example.blog_app.models.dtos.SeriesRequestDto;
import com.example.blog_app.models.dtos.SeriesResponseDto;
import com.example.blog_app.services.SeriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing series in the application.
 *
 * <p>This controller provides endpoints for series creation, retrieval,
 * updating, and deletion. It also includes an endpoint for fetching all series
 * authored by a specific user.</p>
 *
 * <p>Example endpoints:</p>
 * <ul>
 *   <li>POST /api/series - Create a new series</li>
 *   <li>PUT /api/series/{seriesId} - Update an existing series</li>
 *   <li>GET /api/series/{seriesId} - Retrieve a specific series by ID</li>
 *   <li>GET /api/series - Retrieve all series</li>
 *   <li>DELETE /api/series/{seriesId} - Delete a series</li>
 *   <li>GET /api/users/{userId}/series - Retrieve all series authored by a user</li>
 * </ul>
 *
 * @see SeriesService
 * @see SeriesRequestDto
 * @see SeriesResponseDto
 */
@RestController
@RequestMapping("/api/series")
public class SeriesController {

    private final SeriesService seriesService;

    /**
     * Constructs a new {@link SeriesController} with the given {@link SeriesService}.
     *
     * @param seriesService the service to handle series-related business logic
     */
    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    /**
     * Creates a new series.
     *
     * @param seriesDto the DTO containing series details
     * @return the created series as a {@link SeriesResponseDto}
     */
    @PostMapping
    public ResponseEntity<SeriesResponseDto> createSeries(
            @Validated(CreateValidationGroup.class) @RequestBody SeriesRequestDto seriesDto) {
        SeriesResponseDto createdSeries = seriesService.createSeries(seriesDto);
        return ResponseEntity.status(201).body(createdSeries);
    }

    /**
     * Updates an existing series.
     *
     * @param seriesDto the DTO containing updated series details
     * @param seriesId  the ID of the series to update
     * @return the updated series as a {@link SeriesResponseDto}
     */
    @PutMapping("/{seriesId}")
    public ResponseEntity<SeriesResponseDto> updateSeries(
            @Validated(UpdateValidationGroup.class) @RequestBody SeriesRequestDto seriesDto,
            @PathVariable Long seriesId) {
        SeriesResponseDto updatedSeries = seriesService.updateSeries(seriesDto, seriesId);
        return ResponseEntity.status(200).body(updatedSeries);
    }

    /**
     * Retrieves a specific series by its ID.
     *
     * @param seriesId the ID of the series to retrieve
     * @return the series details as a {@link SeriesResponseDto}
     */
    @GetMapping("/{seriesId}")
    public ResponseEntity<SeriesResponseDto> getSeries(@PathVariable Long seriesId) {
        SeriesResponseDto series = seriesService.getSeries(seriesId);
        return ResponseEntity.status(200).body(series);
    }

    /**
     * Retrieves all series.
     *
     * @return a list of series as {@link SeriesResponseDto}
     */
    @GetMapping
    public ResponseEntity<List<SeriesResponseDto>> getAllSeries() {
        List<SeriesResponseDto> series = seriesService.getAllSeries();
        return ResponseEntity.status(200).body(series);
    }

    /**
     * Deletes a specific series by its ID.
     *
     * @param seriesId the ID of the series to delete
     * @return a confirmation message indicating the deletion
     */
    @DeleteMapping("/{seriesId}")
    public ResponseEntity<String> deleteSeries(@PathVariable Long seriesId) {
        seriesService.deleteSeries(seriesId);
        return ResponseEntity.ok("Series deleted successfully");
    }

    /**
     * Retrieves all series authored by a specific user.
     *
     * @param userId the ID of the user whose series are to be retrieved
     * @return a list of series authored by the user as {@link SeriesResponseDto}
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<SeriesResponseDto>> getSeriesByUser(@PathVariable Long userId) {
        List<SeriesResponseDto> series = seriesService.getSeriesByUser(userId);
        return ResponseEntity.status(200).body(series);
    }
}
