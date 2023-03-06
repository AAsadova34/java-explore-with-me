package ru.practicum.explore.ewm.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

@UtilityClass
@Slf4j
public class Logger {
    public static void logWarnException(Throwable e) {
        log.warn(e.getClass().getSimpleName(), e);
    }

    public static void logRequest(HttpMethod method, String uri, String body) {
        log.info("Endpoint request received: '{} {}'. Request body: '{}'", method, uri, body);
    }

    public static void logStorageChanges(String action, String object) {
        log.info("'{}': '{}'", action, object);
    }
}
