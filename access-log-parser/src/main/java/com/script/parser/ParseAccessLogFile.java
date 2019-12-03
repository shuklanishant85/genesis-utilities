package com.script.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParseAccessLogFile {
	private static String EXCEL_FILE_PATH ;
	private static String DATE_PATTERN;
	private static String QUERY_PATTERN;
	private static String DURATION_PATTERN;
	private static String LOG_FILE_PATH;
	private static String sheetName;
	private static Map<String, Map<String, List<String>>> queryExecutionTime = new TreeMap<>();
	
	//LSDS
	static{
		EXCEL_FILE_PATH = "C:\\Project\\Development\\workspace\\parse-access-logs\\comparison-data\\access-log-analysis-lsds.xlsx";
		DATE_PATTERN = "\\d\\d\\d\\d-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d";
		QUERY_PATTERN = "\"(GET|POST) (.*) HTTP/1.1\"";
		DURATION_PATTERN = "[0-9]*\\.[0-9]{1}\\d{2}$";
		LOG_FILE_PATH = "C:\\Project\\Development\\workspace\\parse-access-logs\\comparison-data\\run-2-perf_without-drops-89D-10-07-2019-access-logs.log";
		sheetName = "with-drop";
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			System.out.println("starting log parser...");
			File fragFile = new File(LOG_FILE_PATH);
			List<String> fileListStr;
			if (null != fragFile && fragFile.exists()
					&& !(fileListStr = FileUtils.readLines(fragFile)).isEmpty()) {
				String lineDate = StringUtils.EMPTY;
				String query = StringUtils.EMPTY;
				String time = StringUtils.EMPTY;
				for (String path : fileListStr) {
					Pattern datePattern = Pattern.compile(DATE_PATTERN);
					Matcher dateMatcher = datePattern.matcher(path);
					if (dateMatcher.find()) {
						lineDate = dateMatcher.group(0);
					}
					Pattern queryPattern = Pattern.compile(QUERY_PATTERN);
					Matcher queryMatcher = queryPattern.matcher(path);
					if (queryMatcher.find()) {
						query = queryMatcher.group(0).trim();
					}

					Pattern timePattern = Pattern.compile(DURATION_PATTERN);
					Matcher timeMatcher = timePattern.matcher(path.trim());
					if (timeMatcher.find()) {
						time = timeMatcher.group(0).trim();
					}
					
					if (StringUtils.isNotBlank(time) && StringUtils.isNotBlank(query) /*&& StringUtils.isNotBlank(content)*/) {
						if (queryExecutionTime.containsKey(lineDate)) {
							Map<String, List<String>> dateValue = queryExecutionTime.get(lineDate);
							if (dateValue.containsKey(query)) {
								List<String> queryValue = dateValue.get(query);
								queryValue.add(time);
							} else {
								List<String> queryTime = new ArrayList<>();
								queryTime.add(time);
								dateValue.put(query, queryTime);
							}
						} else {
							Map<String, List<String>> dateValue = new HashMap<>();
							List<String> queryTime = new ArrayList<>();
							queryTime.add(time);
							dateValue.put(query, queryTime);
							queryExecutionTime.put(lineDate, dateValue);
						}
					}

					lineDate = StringUtils.EMPTY;
					query = StringUtils.EMPTY;
					time = StringUtils.EMPTY;
				}
				calcuateTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void calcuateTime() throws Exception {
		FileInputStream file = new FileInputStream(EXCEL_FILE_PATH);
		// Finds the workbook instance for XLSX file
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		// Return first sheet from the XLSX workbook
		XSSFSheet sheet = workbook.createSheet(sheetName);
		int lastRow = 1;
		Iterator itr1 = queryExecutionTime.entrySet().iterator();
		while (itr1.hasNext()) {
			Map.Entry l1 = (Map.Entry) itr1.next();
			String lineDate = (String) l1.getKey();
			Map<String, List<String>> dateValue = (Map<String, List<String>>) l1.getValue();
			Iterator itr2 = dateValue.entrySet().iterator();
			while (itr2.hasNext()) {
				Row row = sheet.createRow(lastRow++);
				Cell cell = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellValue(lineDate);
				Map.Entry l2 = (Map.Entry) itr2.next();
				String query = (String) l2.getKey();
				cell = row.getCell(1,  MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellValue(query);
				List<String> queryTime = (List<String>) l2.getValue();
				System.out.println("lineDate :: " + lineDate);
				System.out.println("query :: " + query);
				System.out.println("queryTime :: " + queryTime);
				int count = 2;
				for (String s : queryTime) {
					cell = row.getCell(count++,  MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellValue(s);
				}
			}
		}
		file.close();
		writeXLSXFile(workbook);

	}

	public static void writeXLSXFile(XSSFWorkbook workbook) throws IOException {
		System.out.println("Enter writeXLSXFile() Method.");
		try {
			FileOutputStream outFile = new FileOutputStream(new java.io.File(EXCEL_FILE_PATH));
			workbook.write(outFile);
			outFile.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException While Processing Assets :: " + e);
		} catch (Exception e) {
			System.out.println("Exception While Processing Assets :: " + e);
		}
		System.out.println("Exit writeXLSXFile() Method.");
	}
}
