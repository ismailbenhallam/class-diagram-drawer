package org.mql.application.business;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	private static Properties properties;
	private static final String PATH = "resources/config.properties";

	private ConfigReader() {
	}

	static {
		properties = new Properties();
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(PATH))) {
			properties.load(bis);
		} catch (IOException e) {
			System.out.println("Erreur : " + e);
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