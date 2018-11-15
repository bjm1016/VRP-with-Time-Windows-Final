package edu.sru.thangiah.zeus.vrptw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


//import the parent class
import edu.sru.thangiah.zeus.core.DepotLinkedList;
import edu.sru.thangiah.zeus.core.Settings;
/**
 * VRPTWDepotLinkedList inherits from the DepotLinkedList but 
 * uses new VRPTW classes in its declarations.  Included functions
 * suited to these new classes, and contains functions to write
 * its contents to file.
 * 
 */
public class VRPTWDepotLinkedList extends DepotLinkedList implements
		java.io.Serializable, java.lang.Cloneable {

	public VRPTWDepotLinkedList() {
		setHead(new VRPTWDepot());
		setTail(new VRPTWDepot());
		linkHeadTail();

		setAttributes(new VRPTWAttributes());
	}
	/**
	 * Returns calling object's attributes
	 *
	 * @return attributes object
	 * 
	 */
	@Override
	public VRPTWAttributes getAttributes(){
		return (VRPTWAttributes) super.getAttributes();
	}

	/**
	 * Attempts to insert a shipment into a depot.
	 * Returns false if unsuccessful
	 *
	 *
	 * @param theShipment shipment to be inserted into depot linked list
	 * @return boolean stating if the insertion was successful
	 *
	 */
	public boolean insertShipment(VRPTWShipment theShipment) {
		boolean status = false;

		VRPTWDepot depot = (VRPTWDepot) super.getHead().getNext();
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) depot
				.getMainTrucks();

		while (depot != this.getTail()) {
			truckLL = (VRPTWTruckLinkedList) depot.getMainTrucks();

			// TODO: VRPTWTruckLinkedList still needs implemented
			status = truckLL.insertShipment(theShipment);

			if (status) {
				break;
			}
			
			depot = (VRPTWDepot)depot.getNext();
		}
		return status;
	}
	/**
	 *	Returns the head depot of the list
	 *
	 * @return depot at head of list
	 *
	 */
	public VRPTWDepot getVRPTWDepotHead() {
		return (VRPTWDepot) getHead();
	}

	/**
	 * Prints information of the depots in the list to the console.
	 *
	 *
	 */
	public void printDepotsToConsole() {
		VRPTWDepot depot = (VRPTWDepot) this.getHead().getNext();
		double x, y;
		int n;

		while (depot != this.getTail()) {
			x = depot.getXCoord();
			y = depot.getYCoord();
			n = depot.getDepotNum();

			System.out.println("Information for VRPTWDepot " + n
					+ ": X-Coord: " + x + " Y-Coord: " + y);

			depot = depot.getNext();
		}
	}
	/**
	 * Writes information about each depot to a given excel file
	 * 
	 * @param filePath relative path to the file location
	 * @param fileName name of the file including extension
	 * @exception error regarding file operations
	 *
	 */
	public void writeDepotInformationToXlsx(String filePath, String fileName) throws Exception {
		File excelFile;
		FileOutputStream thisxlsx = null;
		XSSFWorkbook wb;
		XSSFSheet sheet;
		XSSFRow curRow;
		XSSFCell curCell;
		int cellIndex = 0;
		int rowIndex = 0;
		try {
			Settings.printDebug(Settings.COMMENT, "Creating a new workbook in ");
			wb = new XSSFWorkbook();
			sheet = wb.createSheet(fileName);
			curRow = sheet.createRow(rowIndex);

			VRPTWDepot depot = (VRPTWDepot) this.getHead().getNext();
			while (depot != this.getTail()) {
				curRow.createCell(cellIndex++).setCellValue(
						depot.getXCoord());
				curRow.createCell(cellIndex++).setCellValue(
						depot.getYCoord());
				curRow.createCell(cellIndex++).setCellValue(
						depot.getDepotNum());
				cellIndex = 0;
				curRow = sheet.createRow(++rowIndex);
				depot = depot.getNext();
			}

			excelFile = new File(filePath + fileName + ".xlsx");

			thisxlsx = new FileOutputStream(excelFile);
			wb.write(thisxlsx);
			thisxlsx.close();

			Settings.printDebug(Settings.COMMENT,
					"Completed creating a new workbook in ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			thisxlsx.close();
		}

	}

	/**currently this function writes depot data to excel for verification
	 * it will later be modified to write results
	 * 
	 * @param file name of the file including file path
	 * @exception error regarding file operations
	 */
	public void writeDepotsToFile(String file) throws IOException{

		String resultsFile = file;
		resultsFile = resultsFile.replace(resultsFile.substring(resultsFile.length()-5), "Data.xlsx");

		//create new workbook or open old
		XSSFWorkbook wb;
		try{
			InputStream is = new FileInputStream(VRPTWProblemInfo.getOutputPath()+resultsFile);
			wb = new XSSFWorkbook(is);
		}
		catch(FileNotFoundException exception)
		{
			wb = new XSSFWorkbook();
		}

		//new sheet or open old
		XSSFSheet sheet = wb.getSheet("Depots");
		if(sheet == null)
			sheet = wb.createSheet("Depots");

		FileOutputStream fileOut = new FileOutputStream(VRPTWProblemInfo.getOutputPath()+resultsFile);

		VRPTWDepot depot = (VRPTWDepot) this.getHead().getNext();
		double x, y;
		int n, cnt;
		cnt = 0;

		while (depot != this.getTail()) {
			x = depot.getXCoord();
			y = depot.getYCoord();
			n = depot.getDepotNum();

			Row row = sheet.createRow(cnt);
			row.createCell(0).setCellValue("X-Coordinate");
			row.createCell(1).setCellValue("Y-Coordinate");
			row.createCell(2).setCellValue("DepotNumber");
			cnt++;
			//create rows of data
			row = sheet.createRow(cnt);
			row.createCell(0).setCellValue(x);
			row.createCell(1).setCellValue(y);
			row.createCell(2).setCellValue(n);

			cnt++;
			depot = depot.getNext();
		}

		//close and write to file
		wb.write(fileOut);
		fileOut.close();
	}
}
