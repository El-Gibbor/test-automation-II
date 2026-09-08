package com.xyzbank.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads test data fixtures from src/test/resources/testdata/*.json into typed POJOs.
 * This is the single place that touches the filesystem for test data - tests and
 * DataProviders never read a JSON file or hardcode a value directly.
 */
public final class TestDataReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Map<String, List<?>> CACHE = new ConcurrentHashMap<>();

    private TestDataReader() {
    }

    /**
     * Reads {@code testdata/<fileName>} from the classpath and deserializes it into a list of {@code type}.
     * Results are cached per file so repeated @DataProvider calls don't re-parse the same JSON.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readList(String fileName, Class<T> type) {
        return (List<T>) CACHE.computeIfAbsent(fileName, name -> {
            String path = "/testdata/" + name;
            try (InputStream stream = TestDataReader.class.getResourceAsStream(path)) {
                if (stream == null) {
                    throw new IllegalArgumentException("Test data file not found on classpath: " + path);
                }
                return MAPPER.readValue(stream,
                        MAPPER.getTypeFactory().constructCollectionType(List.class, type));
            } catch (IOException e) {
                throw new IllegalStateException("Failed to parse test data file: " + path, e);
            }
        });
    }
}
