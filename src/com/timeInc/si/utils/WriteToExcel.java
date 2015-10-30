/**
 * This class takes in FileName and String[] and writes it down to an excel sheet using apache poi.
 */
/**
 * @author M1031285/ Sushma Sastry
 */

package com.timeInc.si.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class WriteToExcel {
	public static WriteLog writelogger = new WriteLog();
	public static org.apache.log4j.Logger logger = writelogger.getLogger();
	static int currentRow = 0;
	static HSSFWorkbook wb = new HSSFWorkbook();
	static HSSFSheet sheet = null;
	static HSSFRow row = null;

	/**
	 * This method creates a sheet name Result if not already existing.And writes
	 * the data into the excel sheet and closed the file.
	 * 
	 * @param fileName
	 * @param data
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeIntoExcel(File fileName, String[] data)
			throws FileNotFoundException, IOException {

		int columnCount = data.length;
		if (sheet != null) {
			sheet = wb.getSheet("Result");
		} else {
			sheet = wb.createSheet("Result");
		}
		System.out.println(" Sheet is "+sheet);
		currentRow = sheet.getLastRowNum() + 1;
		row = sheet.createRow(currentRow++);
		System.out.println("-------current row --------"+currentRow+" row ---------"+row);

		//logger.info(currentRow + "---Current Row ");

		for (int i = 0; i < (columnCount); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(data[i]);
			
			logger.info("----data-----" + data[i]);
		}

		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		if (fileOut!=null) {
		fileOut.close();
		}
	}

}
