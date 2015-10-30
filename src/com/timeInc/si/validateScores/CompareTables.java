/**
 * This class provides paths for two tables and calls the corresponding methods to fetch data from the tables and calls compare
 *  method to compare both the data and fill the results into an excel sheet.
 */
/**
 * @author M1031285/Sushma Sastry
 */

package com.timeInc.si.validateScores;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.timeInc.si.utils.ReadDataHashMap;
import com.timeInc.si.utils.ReadProperties;
import com.timeInc.si.utils.WriteLog;

public class CompareTables {

	public WriteLog writelogger = new WriteLog();

	public org.apache.log4j.Logger logger = writelogger.getLogger();

	WebDriver driver = new FirefoxDriver();

	/**
	 * This method initializes the driver and
	 * 
	 * @throws Exception
	 */

	@BeforeClass
	public void setup() throws Exception {
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	/**
	 * This method is run after each test.
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public void tearDown() throws Exception {
		driver.quit();
	}

	/**
	 * This method reads two tables and compare them and stores the result into
	 * excel sheet.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(priority = 0)
	public void verifyCompareTables() throws FileNotFoundException,
			IOException, InterruptedException {
		ReadDataHashMap readMap = new ReadDataHashMap();
		String siLaunchUrl = ReadProperties
				.getproperty("si_nba_standings_overview");
		String tableXpath = ReadProperties
				.getproperty("si_nba_standings_xpath");
		String refLaunchUrl = ReadProperties
				.getproperty("espn_nba_standings_overview");
		
		readMap.readTable(siLaunchUrl, tableXpath);
		readMap.readPaginatedTable(refLaunchUrl);
	    readMap.compareTables();

	}
}
