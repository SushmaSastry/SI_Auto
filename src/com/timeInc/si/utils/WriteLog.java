/**
 * This class uses apache logger to log the information.
 * @author M1031285/Sushma Sastry
 */

package com.timeInc.si.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class WriteLog {
	public String logfile = "conf/log4j.properties";
	public Logger logger = Logger.getLogger("SI");

	{
		try {

			PropertyConfigurator.configure(logfile);

			getLogger();

		} catch (Exception e) {
			logger.error("Error:" + e.getMessage());
		}
	}

	public org.apache.log4j.Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger("SI");

		}
		return logger;
	}
}
