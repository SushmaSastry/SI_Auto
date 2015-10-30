/**
 * This class reads the properties file , loads it and also returns the value for the key property passed.
 */
/**
 * @author M1031285/ Sushma Sastry
 */

package com.timeInc.si.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.testng.annotations.Test;

public class ReadProperties {

	/**
	 * This method is for setting property values in the properties file.
	 * 
	 * @param key
	 * @param value
	 */
	@Test(priority = 0)
	public static void setproperty(String key, String value) {

		Properties prop = new Properties();
		OutputStream output = null;

		try {

			output = new FileOutputStream("config.properties");

			/**
			 * set the properties value
			 */
			prop.setProperty(key, value);

			/**
			 * save properties to project root folder
			 */
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * This method takes in a key and returns corresponding value from the
	 * properties file.
	 * 
	 * @param key
	 * @return
	 */

	public static String getproperty(String key) {
		String value = null;
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("conf/config.properties");

			/**
			 * load a properties file
			 */
			prop.load(input);

			/**
			 * get the property value and print it out
			 */
			value = prop.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;

	}
}