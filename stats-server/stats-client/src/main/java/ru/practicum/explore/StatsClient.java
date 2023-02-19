package ru.practicum.explore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${STATS_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addStats(StatsInnerDto statsInnerDto) {
        return post("/hit", statsInnerDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        if (uris != null && uris.length != 0) {
            parameters.put("uris", uris);
        }
        parameters.put("unique", unique);

        if (parameters.containsKey("uris")) {
            return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);

        } else {
            return get("/stats?start={start}&end={end}&unique={unique}", parameters);
        }
    }
}
