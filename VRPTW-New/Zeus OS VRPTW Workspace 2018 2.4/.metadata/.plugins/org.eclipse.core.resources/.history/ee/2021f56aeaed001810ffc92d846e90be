package edu.sru.thangiah.zeus.vrptw;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class VRPTWRootOutputSummary {
		
	private static XSSFWorkbook wb = new XSSFWorkbook();
	private static XSSFSheet sheet = wb.createSheet("Results");
	private static boolean headerCreated = false;
	private static int rowCount = 0;
	//FileOutputStream fileOut = new FileOutputStream(
	//		VRPTWProblemInfo.outputPath + fileName +"_"+VRPTWProblemInfo.heurStr+"_"+"SummaryResults.xlsx");
	public static void addToResultsList(String fileName, String insertType, String selectType, double runTime)
	{
		if(!headerCreated)
		{
			Row row = sheet.createRow(rowCount);
			row.createCell(0).setCellType(1);
			row.getCell(0).setCellValue("Problem Set");
			row.createCell(1).setCellType(1);
			row.getCell(1).setCellValue("Selection Type");
			row.createCell(2).setCellType(1);
			row.getCell(2).setCellValue("Insertion Type");
			row.createCell(3).setCellType(1);
			row.getCell(3).setCellValue("CPU Time");
			headerCreated = true;
			rowCount++;
		}
		
		
		Row row = sheet.createRow(rowCount);
		row.createCell(0).setCellType(1);
		row.getCell(0).setCellValue(fileName);
		row.createCell(1).setCellType(1);
		row.getCell(1).setCellValue(insertType);
		row.createCell(2).setCellType(1);
		row.getCell(2).setCellValue(selectType);
		row.createCell(3).setCellType(1);
		row.getCell(3).setCellValue(runTime);

		rowCount++;
	}
	public static void createRunTimeFile() throws FileNotFoundException
	{
		/*try {
			InputStream is = new FileInputStream(VRPTWProblemInfo.outputPath
					 + fileName +"_"+VRPTWProblemInfo.heurStr+"_"+"SummaryResults.xlsx");
			wb = new XSSFWorkbook(is);
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
			wb = new XSSFWorkbook();
		}*/
		FileOutputStream fileOut = new FileOutputStream(
				VRPTWProblemInfo.getOutputPath() + VRPTWProblemInfo.heurStr+"_"+"ResultsSummary.xlsx");
		
		
		//XSSFFont font = wb.createFont();
		//XSSFCellStyle style = wb.createCellStyle();
		//font.setBold(true);
		//style.setFont(font);
		// header

		
		
		
		try {
			wb.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileOut.close();
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
}
