package com.example.blog_app.models.dtos;

import com.example.blog_app.common.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private int id;
    private String name;
    private String email;
    private String about;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    @JsonProperty("create_at")
    public String getCreatedAtFormatted() {
        return DateTimeUtils.format(createdAt);
    }

    @JsonProperty("update_at")
    public String getUpdatedAtFormatted() {
        return DateTimeUtils.format(updatedAt);
    }
}
