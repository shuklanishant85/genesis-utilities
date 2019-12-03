package com.utility.constants;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ParserProperties {
	private static final Log LOGGER = LogFactory.getLog(ParserProperties.class);
	private static String propertyFilePath;
	private static ParserProperties instance = null;
	private static Properties properties;

	private ParserProperties() {
		// do nothing
	}

	public static void setPropertyFilePath(String path) {
		propertyFilePath = path;
	}
	private static synchronized ParserProperties getInstance() {
		if (null == instance) {
			synchronized (ParserProperties.class) {
				if (null == instance) {
					instance = new ParserProperties();
					properties = new Properties();
					try {
						InputStream stream = Files.newInputStream(Paths.get(propertyFilePath));
						properties.load(stream);
					} catch (IOException e) {
						LOGGER.error("could not read properties file : " + e.getMessage());
					}

				}
			}

		}
		return instance;
	}

	public static String getProperty(String key) {
		if (properties == null) {
			instance = getInstance();
		}

		return properties.getProperty(key);
	}

}
