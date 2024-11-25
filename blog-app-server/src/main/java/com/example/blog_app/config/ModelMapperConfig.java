package com.example.blog_app.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the {@link ModelMapper} bean.
 *
 * <p>This class defines the configuration for the {@link ModelMapper},
 * setting the matching strategy to {@link MatchingStrategies#STRICT},
 * which ensures that fields must match exactly between source and target objects.</p>
 *
 * <p>ModelMapper is a library used for mapping objects between different layers,
 * such as DTOs and Entities in the application.</p>
 *
 * <p>Example usage in a service or controller:</p>
 * <pre>
 * {@code
 * @Autowired
 * private ModelMapper modelMapper;
 *
 * UserResponseDto responseDto = modelMapper.map(userEntity, UserResponseDto.class);
 * }
 * </pre>
 *
 * @see ModelMapper
 * @see MatchingStrategies
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Creates and configures a {@link ModelMapper} bean with strict matching strategy.
     *
     * @return the configured {@link ModelMapper} instance
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }
}
