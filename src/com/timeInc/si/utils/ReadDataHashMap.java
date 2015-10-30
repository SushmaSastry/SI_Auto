/**
 * This class takes data from two tables which are from two different web sites and compare them and write the results to an excel sheet.

 */

/**
 * @author M1031285/Sushma Sastry
 */

package com.timeInc.si.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ReadDataHashMap {
	public WriteLog writelogger = new WriteLog();
	public Properties properties;
	public org.apache.log4j.Logger logger = writelogger.getLogger();
	//final static Logger logger = Logger.getLogger(ReadDataHashMap.class.getName());

	String columnName, rowName, refColumnName, refRowName, tableData, newName;
	LinkedHashMap<String, LinkedHashMap<String, String>> playerInfo = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	LinkedHashMap<String, String> statInfo = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> refStatInfo = new LinkedHashMap<String, String>();
	LinkedHashMap<String, LinkedHashMap<String, String>> playerInfoRef = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	WebDriver driver = new FirefoxDriver();
	WebElement clickNextPage;
	public WriteToExcel excelWriter = new WriteToExcel();

	Date date = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	Path path = Paths.get(ReadProperties.getproperty("excelResult_path")
			+ "/Results" + dateFormat.format(date) + ".xls");

	String[] readColumnHeader;
	private String xpath;
	

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
	 * Used for launching the URL
	 */
	public void launch(String url) {
		driver.get(url);

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
	 * This method reads the table data by taking xpaths of row, column and
	 * pushes into a hash table.
	 * 
	 * @param siLaunchUrl
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */

	@Test(enabled = false, priority = 0)
	public void readTable(String siLaunchUrl, String tablePath)
			throws FileNotFoundException, IOException, InterruptedException {
		logger.info("Launch URL under Test " + siLaunchUrl);
		launch(siLaunchUrl);

		/**
		 * Get number of rows In table.
		 */
		int rowCountSi = 0;
		int colCountSi = 0;
		Thread.sleep(2000);
		rowCountSi = driver.findElements(By.xpath(tablePath)).size();
		logger.info("Number Of Rows in the table to be tested = "
				+ rowCountSi);

		/**
		 * Get number of columns In table.
		 */
		colCountSi = driver.findElements(By.xpath(tablePath + "[1]/td"))
				.size();
		logger.info("Number Of Columns in the table to be tested = "
				+ colCountSi);
		/**
		 * divided xpath In three parts to pass Row_count and Col_count values.
		 */
		String first_part = tablePath + "[";
		String second_part = "]/td[";
		String third_part = "]";
		int i = 0, j = 0;
		/**
		 * Used for loop for number of rows.
		 */
		for (i = 1; i <= rowCountSi; i++) {
	//	for (i = 1; i < 5; i++) {
			statInfo = new LinkedHashMap<String, String>();
			/**
			 * Used for loop for number of columns.
			 */
			readColumnHeader = new String[colCountSi];

			readColumnHeader[0] = "Player Name";
			for (j = 1; j < colCountSi; j++) {

				/**
				 * 
				 Prepared final xpath of specific cell as per values of i and
				 * j.
				 **/
				String first_row_xpath = first_part + i + second_part + 1
						+ third_part;
				String final_xpath = first_part + i + second_part + (j + 1)
						+ third_part;
				WebElement first_column_xpath = driver
						.findElement(By
								.xpath("//span[@class='player-name']/../../../../../../../../tr/../../thead/tr/th["
										+ (j + 1) + "]/div/a"));
				/**
				 * Will retrieve value from located cell and print It.
				 */
				columnName = (first_column_xpath).getText();

				readColumnHeader[j] = columnName;
				rowName = driver.findElement(By.xpath(first_row_xpath))
						.getText();
				tableData = driver.findElement(By.xpath(final_xpath)).getText();

				statInfo.put(columnName, tableData);

			}

			playerInfo.put(rowName, statInfo);

		}

		for (String playerName : playerInfo.keySet()) {
			System.out.println((playerName + "-----FG%-----" + Double
					.valueOf(playerInfo.get(playerName).get("FG%"))));

		}
	}

	/**
	 * This method checks if there is pagination. If yes, then it keeps on
	 * clicking next and performing actions until the next button gets disabled.
	 * 
	 * @param driver
	 * @param clickNextPage
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */

	@Test(enabled = true, priority = 1)
	public void readPaginatedTable(String refLaunchUrl) throws FileNotFoundException, IOException,
			InterruptedException {
		xpath="//div[@class='jcarousel-next']";
			launch(refLaunchUrl);
            Thread.sleep(2000);
			clickNextPage=driver.findElement(By.xpath("//div[@class='jcarousel-next']")); //webElement

			Boolean hasNext = false;
			hasNext=readRefTable();   //reads the Table in the first page
			int i=1;


			while(hasNext==true){
				System.out.println("Next clicked in page "+(i++)); 
				clickTheNextPage();
				hasNext=readRefTable();
			}
			

		}
		
	
	public boolean readRefTable() throws FileNotFoundException, IOException,
			InterruptedException {

		/**
		 * Get number of rows In table.
		 */
		Thread.sleep(2000);
		int rowCountRef = 0;
		int colCountRef = 0;
		String refTablePath=null;
		refTablePath=ReadProperties
				.getproperty("espn_nba_standings_xpath");
		rowCountRef = driver
				.findElements(
						By.xpath(ReadProperties
								.getproperty("espn_nba_standings_xpath")))
				.size();
		

		logger.info("Number Of Rows in reference table = " + rowCountRef);
		logger.info("Launching Reference Table ---- ESPN");

		/**
		 * Get number of columns In table.
		 */
		colCountRef = driver.findElements(By.xpath(refTablePath + "[1]/td"))
		.size();
		logger.info("Number Of Columns in reference table  = " + colCountRef);

		/**
		 * divided xpath In three parts to pass Row_count and Col_count values.
		 */
		String first_part = "//*[@id='my-players-table']/div/div[2]/table/tbody/tr[";
		String second_part = "]/td[";
		String third_part = "]";
		int i = 0, j = 0;

		/**
		 * Used for loop for number of rows.
		 */
		for (i = 1; i < rowCountRef; i++) {

		//	for (i = 1; i < 5; i++) {
			refStatInfo = new LinkedHashMap<String, String>();
			/**
			 * Used for loop for number of columns.
			 */
			for (j = 1; j < colCountRef - 1; j++) {
				/**
				 * Prepared final xpath of specific cell as per values of i and
				 * j
				 */

				String final_xpath = first_part + (i + 1) + second_part
						+ (j + 2) + third_part;

				/**
				 * Will retrieve value from located cell and print It.
				 */
				String reftableData = driver.findElement(By.xpath(final_xpath))
						.getText();
				String first_row_xpath = first_part + (i + 1) + second_part + 2
						+ third_part;
				WebElement first_column_xpath = driver.findElement(By
						.xpath(first_part + 1 + second_part + (j + 2)
								+ third_part));
				/**
				 * Will retrieve value from located cell and print It.
				 */
				retryingFindText(By.xpath(first_part + 1 + second_part
						+ (j + 2) + third_part));
				refColumnName = (first_column_xpath).getText();
				refRowName = getRefTableData(first_row_xpath);
				if (refColumnName.contains("-")) {
					String data[] = splitData(reftableData, "-");
					String arr[] = splitData(refColumnName, "-");

					refStatInfo.put(arr[0], data[0]);
					refStatInfo.put(arr[1], data[1]);

				} else {
					refStatInfo.put(refColumnName, reftableData);
				}

				{

				}

			}
			playerInfoRef.put(refRowName, refStatInfo);

		}
		for (String playerName : playerInfo.keySet()) {

			
			if (playerInfoRef.get(playerName) != null) {
				System.out.println(playerName
						+ Double.valueOf(playerInfoRef.get(playerName).get(
								"FG%")));
			}

		}

		if (i >= colCountRef - 1) {
			{
				try {
					System.out.println("NEXT PAGE-----"
							+ clickNextPage.isEnabled());
				//	isElementEnabled();
					return (clickNextPage.isEnabled());
				} catch (StaleElementReferenceException elementHasDisappeared) {
					return isElementEnabled();
				//	return (clickNextPage.isEnabled());
				}
				
			}
		}
		return false;
	}

	/**
	 * This method reverses the string and splits it.
	 * 
	 * @param xpath
	 * @return
	 */
	public String getRefTableData(String xpath) {
		String data = driver.findElement(By.xpath(xpath)).getText();
		if (data.equalsIgnoreCase("PLAYER")) {
			return data;
		} else {
			newName = org.apache.commons.lang3.StringUtils.reverseDelimited(
					data, ' ');

			String[] arr = newName.split(" ", 2);
			refRowName = arr[1];
			return refRowName;
		}

	}

	/**
	 * This methods takes two Hash tables and compares them. It writes the
	 * results into an excel sheet.
	 * 
	 * @throws IOException
	 */

	@Test(enabled = false, priority = 2)
	public void compareTables() throws IOException {
		logger.info("*****************Comparing two tables***********************");

		int i = 0;
		String[] readResult = new String[statInfo.size() + 1];

		WriteToExcel.writeIntoExcel(path.toFile(), readColumnHeader);

		/**
		 * Iterate through the Key set of first HashMap.
		 */
		for (String playerName : playerInfo.keySet()) {
			LinkedHashMap<String, String> statInfo = playerInfo.get(playerName);
			i = 0;
			readResult[i++] = playerName;
			for (Map.Entry<String, String> entry : statInfo.entrySet()) {
				Double columnValue = null, diffDouble = null, columnValueRef = null;
				Boolean diffString;
				String column = entry.getKey();

				String columnValueStr = entry.getValue();
				if((playerInfoRef).get(playerName)!=null){
				if ((playerInfoRef.get(playerName).get(column) != null)&& (columnValueStr!=null) && (isNumeric(columnValueStr))){
						
				
					columnValue = Double.valueOf(entry.getValue());

					columnValueRef = Double.valueOf(playerInfoRef.get(
							playerName).get(column));
					diffDouble = columnValue - columnValueRef;
					logger.info("Difference in  " + column
							+ " between two table is " + diffDouble);

					logger.info("Player Name is " + playerName + "---"
							+ columnName + "----Test Table--" + columnValue
							+ "-------Ref Table----" + columnValueRef
							+ "---------" + diffDouble);
					Double percentage = ((diffDouble) * 100 / (columnValueRef));
					if (percentage >= Double.valueOf(ReadProperties
							.getproperty("variation"))) {
						readResult[i++] = ("FAIL(" + "Act" + columnValue
								+ ",Exp" + columnValueRef + ")");
					} 
						
				else {
						readResult[i++] = "PASS";

					}
				} else if (playerInfoRef.get(playerName).get(column) == null) {
					logger.info(column + "--Column does not exist");

					readResult[i++] = "N/A";
				}

				else if ((columnValueStr!=null) && !(isNumeric(columnValueStr))) {
					String valueInColumn = entry.getValue();
					String valueInRefColumn = (playerInfoRef.get(playerName)
							.get(column));

					diffString = valueInColumn
							.equalsIgnoreCase(valueInRefColumn);
					logger.info("Player Name is " + playerName + "-----"
							+ columnName + "----Test Table----" + valueInColumn
							+ "-------Ref Table---" + valueInRefColumn
							+ "---------" + diffString);
					if (diffString) {

						readResult[i++] = "PASS";
					} else {
						readResult[i++] = ("FAIL(" + "Act" + valueInColumn
								+ ",Exp" + valueInRefColumn + ")");

					}

				}
				else{
					System.out.println("*******ELSE*******ColumnName:"+playerInfoRef.get(playerName).get(column)+"ColumnValue:"+columnValueStr+"ISColumnString"+(isNumeric(columnValueStr)));
				}

			}
				else{
					System.out.println("Player : "+playerName+"does not exist in reference table");
				}
			}

			WriteToExcel.writeIntoExcel(path.toFile(), readResult);

		}
	}

	/**
	 * This method gets the string and a delimiter and splits it to return an
	 * array of words divided based upon the delimiter.
	 * 
	 * @param splitTheString
	 * @param delimiter
	 * @return
	 */

	public String[] splitData(String splitTheString, String delimiter) {
		String[] arr = splitTheString.split(delimiter);
		return arr;
	}

	/**
	 * This function will handle stale element reference exception
	 * 
	 * @param elementName
	 */
	public void handleStaleElement(String elementName) {
		int count = 0;
		/**
		 * It will try 4 times to find same element using name.
		 */
		while (count < 4) {
			try {
				/**
				 * If exception generated that means It Is not able to find
				 * element then catch block will handle It.
				 * 
				 */
				WebElement staledElement = driver.findElement(By
						.name(elementName));
				/**
				 * If exception not generated that means element found and
				 * element text get cleared.
				 */

				staledElement.clear();
			} catch (StaleElementReferenceException e) {
				e.toString();
				logger.info("Trying to recover from a stale element :"
						+ e.getMessage());
				count = count + 1;
			}
			count = count + 4;
		}
	}

	/**
	 * This method is to retry element click event in case the stale element is
	 * encountered.
	 * 
	 * @param by
	 * @return
	 */
	public boolean retryingFindClick(By by) {
		boolean result = false;
		int attempts = 0;
		while (attempts < 3) {
			try {
				driver.findElement(by).click();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
				System.out.println("Stale element Encountered");
				rebuildElement();

			}
			attempts++;
		}
		return result;
	}

	/**
	 * This method is to retry element get text in case the stale element is
	 * encountered.
	 * 
	 * @param by
	 * @return
	 */
	public boolean retryingFindText(By by) {
		boolean result = false;
		int attempts = 0;
		while (attempts < 2) {
			try {
				driver.findElement(by).getText();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
				System.out.println("Stale exception have come");

			}
			attempts++;
		}
		return result;
	}

	public void WebTable(WebDriver driver, String xpath) {
		this.driver = driver;
		this.xpath = xpath;
		clickNextPage = driver.findElement(By.xpath(xpath));
		
	}

	public WebElement rebuildElement() {
		WebTable(this.driver, this.xpath);
		System.out.println("Stale exception handled");
		return clickNextPage;

	}
	
	/**
	 *This takes care of clicking the next page
	 *In case the element is stale, it would be in a continuous loop until the 'next' button can be clicked
	 **/
	public void clickTheNextPage(){
		Boolean stale=true;
		while(stale==true){
			try{
				System.out.println("Found Stale Element");
				clickNextPage.click();
				stale=false;
			}
			catch(StaleElementReferenceException ex){
				System.out.println("Exception Handled");
				stale=true;
				clickNextPage = driver.findElement(By.xpath(xpath)); 
			}
		}
	}
	
	public boolean isElementEnabled(){
		Boolean stale=true;
		while(stale==true){
			try{
				System.out.println("Found Stale Element");
				clickNextPage.isEnabled();
				stale=false;
			}
			catch(StaleElementReferenceException ex){
				
				System.out.println("Exception Handled");
				stale=true;
				try{
				clickNextPage = driver.findElement(By.xpath(xpath)); 
				}catch(NoSuchElementException noElement)
				{
					System.out.println("Reached Last Page");
					return false;
				}
				}
			}
		return clickNextPage.isEnabled();
		}
	

	public static boolean isNumeric(String str)  
	{  
	  
		try  
	  {  
		  Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException exception)  
	  {  
	    return false;  
	  }  
	  return true;  
	}

}
