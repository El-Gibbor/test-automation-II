package com.xyzbank.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads config.properties (environment/runtime settings only).
 * Any key can be overridden at runtime via a matching -D system property, e.g.
 * {@code mvn test -Dbrowser=firefox -Dheadless=false} without touching the file.
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = load();

    private ConfigReader() {
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream stream = ConfigReader.class.getResourceAsStream("/config.properties")) {
            if (stream == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
        return properties;
    }

    public static String get(String key) {
        return System.getProperty(key, PROPERTIES.getProperty(key));
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String browser() {
        return get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static int implicitWaitSeconds() {
        return Integer.parseInt(get("implicit.wait.seconds"));
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(get("explicit.wait.seconds"));
    }

    public static int pageLoadTimeoutSeconds() {
        return Integer.parseInt(get("page.load.timeout.seconds"));
    }
}
