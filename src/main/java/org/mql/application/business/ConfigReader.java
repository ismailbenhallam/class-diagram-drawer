package org.mql.application.business;

import java.io.BufferedInputStream;
import java.util.Objects;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties;
    private static final String PATH = "config/config.properties";
    private static final String PATH_DEFAULT = "config/default-config.properties";

    private ConfigReader() {
    }

    static {
        properties = new Properties();
        try (BufferedInputStream bis = new BufferedInputStream(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(PATH)))) {
            properties.load(bis);
        } catch (Exception ignored) {
            try (BufferedInputStream bis = new BufferedInputStream(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(PATH_DEFAULT)))) {
                properties.load(bis);
            } catch (Exception e) {
                System.err.println("Erreur : " + e);
            }
        }
    }

    public static Properties getProperties() {
        return properties;
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}