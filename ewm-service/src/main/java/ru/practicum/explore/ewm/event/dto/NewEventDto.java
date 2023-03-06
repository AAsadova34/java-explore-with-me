package ru.practicum.explore.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.explore.ewm.event.location.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class NewEventDto {
    @NotNull(message = "Field: annotation. Error: must not be blank. Value: null")
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull(message = "Field: category. Error: must not be blank. Value: null")
    private Integer category;

    @NotNull(message = "Field: description. Error: must not be blank. Value: null")
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull(message = "Field: eventDate. Error: must not be blank. Value: null")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Field: location. Error: must not be blank. Value: null")
    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotNull(message = "Field: title. Error: must not be blank. Value: null")
    @Size(min = 3, max = 120)
    private String title;
}
