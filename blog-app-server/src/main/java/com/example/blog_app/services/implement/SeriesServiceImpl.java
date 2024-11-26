package com.example.blog_app.services.implement;

import com.example.blog_app.exceptions.ResourceNotFoundException;
import com.example.blog_app.models.dtos.series.SeriesRequestDto;
import com.example.blog_app.models.dtos.series.SeriesResponseDto;
import com.example.blog_app.models.entities.Series;
import com.example.blog_app.models.entities.User;
import com.example.blog_app.models.mappers.SeriesMapper;
import com.example.blog_app.repositories.SeriesRepository;
import com.example.blog_app.repositories.UserRepository;
import com.example.blog_app.services.SeriesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link SeriesService} interface for managing series.
 *
 * <p>This service provides the business logic for series-related operations such as:</p>
 * <ul>
 *   <li>Creating a new series.</li>
 *   <li>Updating series details.</li>
 *   <li>Retrieving series information.</li>
 *   <li>Deleting a series.</li>
 *   <li>Retrieving series authored by a specific user.</li>
 * </ul>
 */
@Service
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final UserRepository userRepository;
    private final SeriesMapper seriesMapper;

    /**
     * Constructs the SeriesServiceImpl with required dependencies.
     *
     * @param seriesRepository the repository for managing series
     * @param userRepository   the repository for managing users
     * @param seriesMapper     the mapper for converting between entities and DTOs
     */
    public SeriesServiceImpl(SeriesRepository seriesRepository, UserRepository userRepository, SeriesMapper seriesMapper) {
        this.seriesRepository = seriesRepository;
        this.userRepository = userRepository;
        this.seriesMapper = seriesMapper;
    }

    /**
     * Creates a new series.
     *
     * @param seriesDto the DTO containing series details for creation
     * @return the created series's details as a response DTO
     */
    @Override
    public SeriesResponseDto createSeries(SeriesRequestDto seriesDto) {
        User author = userRepository.findById(seriesDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + seriesDto.getAuthorId()));

        Series series = seriesMapper.toEntity(seriesDto);
        series.setUser(author);

        Series savedSeries = seriesRepository.save(series);
        return seriesMapper.toResponseDto(savedSeries);
    }

    /**
     * Updates an existing series's information.
     *
     * @param seriesDto the DTO containing updated series details
     * @param seriesId  the ID of the series to update
     * @return the updated series's details as a response DTO
     * @throws ResourceNotFoundException if the series does not exist
     */
    @Override
    public SeriesResponseDto updateSeriesById(SeriesRequestDto seriesDto, Long seriesId) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Series not found with ID: " + seriesId));

        seriesMapper.updateEntityFromDto(seriesDto, series);
        Series updatedSeries = seriesRepository.save(series);

        return seriesMapper.toResponseDto(updatedSeries);
    }

    /**
     * Retrieves all series.
     *
     * @return a list of series response DTOs representing all series
     */
    @Override
    public List<SeriesResponseDto> getAllSeries() {
        return seriesRepository.findAll()
                .stream()
                .map(seriesMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the details of a specific series.
     *
     * @param seriesId the ID of the series to retrieve
     * @return the series's details as a response DTO
     * @throws ResourceNotFoundException if the series does not exist
     */
    @Override
    public SeriesResponseDto getSeriesById(Long seriesId) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Series not found with ID: " + seriesId));

        return seriesMapper.toResponseDto(series);
    }

    /**
     * Deletes a series by its ID.
     *
     * @param seriesId the ID of the series to delete
     * @throws ResourceNotFoundException if the series does not exist
     */
    @Override
    public void deleteSeriesById(Long seriesId) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Series not found with ID: " + seriesId));

        seriesRepository.delete(series);
    }

    /**
     * Retrieves all series authored by a specific user.
     *
     * @param userId the ID of the user whose series are to be retrieved
     * @return a list of series response DTOs authored by the user
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    public List<SeriesResponseDto> getSeriesByUserId(Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return seriesRepository.findByUser(author)
                .stream()
                .map(seriesMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
