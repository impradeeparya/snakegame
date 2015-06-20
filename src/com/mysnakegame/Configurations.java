package com.mysnakegame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configurations {

	private static Configurations config = null;
	private static Boolean isInstanceCreated = false;
	private InputStream inputStream = null;
	private static Properties prop = new Properties();

	private Configurations() {
		String propFileName = "resource/game.properties";
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream(
					propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public static Configurations getInstance() {
		if (!isInstanceCreated) {
			config = new Configurations();
			isInstanceCreated = true;
		}
		return config;
	}

	public String getProperty(String property) {
		return prop.getProperty(property);
	}

}
