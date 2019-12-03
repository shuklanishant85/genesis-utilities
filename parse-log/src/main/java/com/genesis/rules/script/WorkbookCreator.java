package com.genesis.rules.script;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.genesis.script.perf.model.ResultSet;
import com.genesis.script.perf.run.ReportGenerator;

@PropertySource("script.properties")
public class WorkbookCreator {

	@Value("${root.path}")
	private String root;

	@Value("${output.file}")
	private String outputFile;

	@Value("${output.sheetName}")
	private String sheetName;

	@Autowired
	private ReportGenerator reportGenerator;

	public void createExcelReport() {
		List<ResultSet> reportList = reportGenerator.generateReportMap();
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet(sheetName);
		Font headerFont = workbook.createFont();
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("THREAD COUNT");
		headerRow.createCell(1).setCellValue("THREADS TOTAL TIME");
		headerRow.createCell(2).setCellValue("THREADS PER SECOND");
		headerRow.createCell(3).setCellValue("THREAD AVERAGE TIME IN SECOND");
		headerRow.createCell(4).setCellValue("THREAD MINIMUM TIME");
		headerRow.createCell(5).setCellValue("THREAD MAXIMUM TIME");
		headerRow.createCell(6).setCellValue("ERROR COUNT");
		headerRow.createCell(7).setCellValue("ACTIVE THREAD COUNT");
		headerRow.createCell(8).setCellValue("STARTED THREAD COUNT");
		headerRow.createCell(9).setCellValue("FINISHED THREAD COUNT");

		for (int i = 0; i < reportList.size(); i++) {
			System.out.println(reportList.get(i));
			Row row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(reportList.get(i).getThreadCount());
			row.createCell(1).setCellValue(reportList.get(i).getTime());
			row.createCell(2).setCellValue(reportList.get(i).getThreadPerSec());
			row.createCell(3).setCellValue(reportList.get(i).getAverageTimeInSec());
			row.createCell(4).setCellValue(reportList.get(i).getMinTime());
			row.createCell(5).setCellValue(reportList.get(i).getMaxTime());
			row.createCell(6).setCellValue(reportList.get(i).getErrorCount());
			row.createCell(7).setCellValue(reportList.get(i).getActiveThreads());
			row.createCell(8).setCellValue(reportList.get(i).getStartedThreads());
			row.createCell(9).setCellValue(reportList.get(i).getFinishedThreads());
		}
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(root+outputFile);
			System.out.println("file found :: " + outputFile);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
			System.out.println("written successfully");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}