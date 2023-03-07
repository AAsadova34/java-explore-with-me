package ru.practicum.explore.ewm.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.ewm.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
public class ConverterLocalDateTime {
    public static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime toLocalDateTime(String strDataTime) {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(strDataTime, CUSTOM_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ValidationException(e.getMessage());
        }
        return localDateTime;
    }
}
