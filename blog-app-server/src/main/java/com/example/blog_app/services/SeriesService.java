package com.example.blog_app.services;

import com.example.blog_app.models.dtos.series.SeriesRequestDto;
import com.example.blog_app.models.dtos.series.SeriesResponseDto;

import java.util.List;

/**
 * Service interface for managing series.
 *
 * <p>This interface defines the contract for operations related to blog series,
 * including creating, updating, retrieving, and deleting series.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * SeriesService seriesService = new SeriesServiceImpl();
 * SeriesResponseDto series = seriesService.getSeries(1L);
 * }
 * </pre>
 */
public interface SeriesService {

    /**
     * Creates a new series.
     *
     * @param seriesDto the DTO containing the details of the series to be created
     * @return the created series as a response DTO
     */
    SeriesResponseDto createSeries(SeriesRequestDto seriesDto);

    /**
     * Updates an existing series.
     *
     * @param seriesDto the DTO containing the updated series details
     * @param seriesId  the ID of the series to update
     * @return the updated series as a response DTO
     */
    SeriesResponseDto updateSeriesById(SeriesRequestDto seriesDto, Long seriesId);

    /**
     * Retrieves all series.
     *
     * @return a list of series response DTOs representing all series
     */
    List<SeriesResponseDto> getAllSeries();

    /**
     * Retrieves a specific series by its ID.
     *
     * @param seriesId the ID of the series to retrieve
     * @return the series as a response DTO
     */
    SeriesResponseDto getSeriesById(Long seriesId);

    /**
     * Deletes a specific series by its ID.
     *
     * @param seriesId the ID of the series to delete
     */
    void deleteSeriesById(Long seriesId);

    /**
     * Retrieves all series authored by a specific user.
     *
     * @param userId the ID of the user whose series are to be retrieved
     * @return a list of series response DTOs authored by the user
     */
    List<SeriesResponseDto> getSeriesByUserId(Long userId);
}
