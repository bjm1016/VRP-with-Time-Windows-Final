package edu.sru.thangiah.zeus.vrptw.vrptwqualityassurance;

import edu.sru.thangiah.zeus.core.*;
import edu.sru.thangiah.zeus.vrptw.*;
import edu.sru.thangiah.zeus.qualityassurance.*;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Author: Aaron Weckerly
 * Based off QA written by Dr. Sam Thangiah
 * @version 2.0
 */

public class VRPTWQualityAssurance extends QualityAssurance {
	VRPTWDepotLinkedList mainDepots;
	VRPTWShipmentLinkedList mainShipments;
	VRPTWQADepotLinkedList vrptwQADepots;
	VRPTWQAShipmentLinkedList vrptwQAShipments;

	File shipFile;
	File solFile;

	public VRPTWQualityAssurance() {
	}

	/**
	 * Creates a VRPTWQA object
	 * writes files, reads shipment, depot, and solution files
	 * 
	 * @param md VRPTWDepotLinkedList from problem (mainDepots)
	 * @param ms VRPTWShipmentLinkedList from problem (mainShipments)
	 * @throws IOException
	 */
	public VRPTWQualityAssurance(VRPTWDepotLinkedList md,
			VRPTWShipmentLinkedList ms) throws IOException {
		// used for writing out the files
		mainDepots = md;
		mainShipments = ms;
		// used for reading in the information
		vrptwQAShipments = new VRPTWQAShipmentLinkedList();
		vrptwQADepots = new VRPTWQADepotLinkedList();

		try {
			clearTempFiles(VRPTWProblemInfo.getProbFileName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writeFiles();
		String file = VRPTWProblemInfo.getProbFileName();
		readShipmentFile(VRPTWProblemInfo.getProbFileName());
		readDepotFile(VRPTWProblemInfo.getProbFileName());
		readSolutionFile(VRPTWProblemInfo.getProbFileName());

	}// VRPTWQA

	/**
	 * runs the QA process, verifies service, distance, capacity, wait time, early and late time
	 * 
	 * @return the end result of all QA checks
	 */
	public boolean runQA() {
		boolean isGood = true;
		boolean serv = true, dist = true, demand = true, time = true, waitTime = true;
	
		// Area all the customer being serviced and are they serviced only once
		System.out
				.print("Check on all customers being serviced and serviced no more than once:");
		serv = vrptwQAShipments.customerServicedOnlyOnce(vrptwQADepots);
		VRPTWQADepot d = (VRPTWQADepot) vrptwQADepots.getDepots().elementAt(0);
		System.out.println("Number of trucks is: " + d.getTrucks().size());

		if (serv) {
			System.out.println("Passed");
		} else {
			System.out.println("Failed");
		}

		System.out.print("Check on maximum travel time of trucks:");
		dist = vrptwQADepots.checkDistanceConstraint();
		
		if (dist) {
			System.out.println("Distance Passed");
		} else {
			System.out.println("Distance Failed");
		}

		System.out.print("Check on maximum demand of trucks:");

		demand = vrptwQADepots.checkCapacityConstraint();
		
		if (demand) {
			System.out.println("Capacity Passed");
		} else {
			System.out.println("Capacity Failed");
		}

		System.out.print("Check on Early/Late /time delivery:");

		time = vrptwQADepots.checkEarlyLateTimes();
		if (time) {
			System.out.println("Early/Late Passed");
		} else {
			System.out.println("Early/Late Failed");
		}

		System.out.print("Check on TruckWaitTime:");

		waitTime = vrptwQADepots.checkWaitTime();

		if (waitTime) {
			System.out.println("Wait Time Passed");
		} else {
			System.out.println("Wait Time Failed");
		}
		isGood = (serv && dist && demand && time && waitTime);

		String id = VRPTWProblemInfo.heurStr;

		try {
			writeLogFile(VRPTWProblemInfo.getProbFileName(), id, isGood, mainDepots);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clearTempFiles(VRPTWProblemInfo.getProbFileName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isGood;
	}// runQA

	/**
	 * Write information to a log file present in the ProblemInfo.outputPath
	 * updates with current run of file information
	 * Within this workbook there are also various comparison tables
	 * 
	 * @param file VRPTWProblemInfo.fileName (data set)
	 * @param huerType Short string correlating to heuristic being used
	 * @param passFail the result of QA
	 * @param mdepot depot linked list from problem to gather results from
	 * @throws FileNotFoundException
	 */
	public void writeLogFile(String file, String huerType, boolean passFail,
			VRPTWDepotLinkedList mdepot) throws FileNotFoundException {

		// public void writeLogFile(String file, String selType, String insType,
		// boolean passFail, VRPTWDepotLinkedList mdepot) throws
		// FileNotFoundException {

		// all data is stored within 'blocks' for each data set

		InputStream is = null;
		XSSFWorkbook wb = null;

		try {
			is = new FileInputStream(VRPTWProblemInfo.getOutputPath() + "Log\\Log.xlsx");
			wb = new XSSFWorkbook(is);
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println("Input Stream for log file not created");
			wb = new XSSFWorkbook();
			// e.printStackTrace();
		}
		// open the sheets within the workbook
		//log of all runs of VRPTW
		XSSFSheet resultsSheet = wb.getSheet("All Group 4 Results"); 
		//contains best results for VRPTW via Group 4
		XSSFSheet g4BestSheet = wb.getSheet("Group 4 Best Results");
		//Contains avg per heuristic to compare to solomon
		XSSFSheet avgSheet = wb.getSheet("Group 4 Average Results");
		//comparison of group 4 avg vs solomon
		XSSFSheet solSheet = wb.getSheet("Solomon Comparison"); 
		//comparison of best known to Group 4
		XSSFSheet bestKnown = wb.getSheet("Best Known vs. Group 4");
		//formula evaluator used for dist calcs within excel
		XSSFFormulaEvaluator evaluator = wb.getCreationHelper()
				.createFormulaEvaluator();

		int cnt = 0;
		// Update "All group 4 results"

		// header
		if (resultsSheet.getLastRowNum() == 0) {
			Row row = resultsSheet.createRow(cnt);
			row.createCell(0).setCellValue("Data Set");
			row.createCell(1).setCellValue("Data Sub Set");
			row.createCell(2).setCellValue("Selection");
			row.createCell(3).setCellValue("Insertion");
			row.createCell(4).setCellValue("QA P/F");
			row.createCell(5).setCellValue("Travel Time");
			row.createCell(6).setCellValue("Distance");
			row.createCell(7).setCellValue("Waiting Time");
			row.createCell(8).setCellValue("Number of Trucks");

			cnt++;
			row = resultsSheet.createRow(cnt);

			// get large data set string

			String str = VRPTWProblemInfo.getInputPath();
			int indexA = str.lastIndexOf("data");
			str = str.substring(0, indexA);
			int indexB = str.lastIndexOf("\\");
			str = str.substring((indexB + 1));

			row.createCell(0).setCellValue(str);
			row.createCell(1).setCellValue(
					file.substring(0, (file.length() - 5)));
			row.createCell(2).setCellValue(huerType);
			row.createCell(3).setCellValue(huerType);
			row.createCell(4).setCellValue(passFail);
			row.createCell(5).setCellValue(mainDepots.getAttributes().getTotalServiceTime()+
					  mainDepots.getAttributes().getTotalWaitingTime());
			row.createCell(6).setCellValue(
					mainDepots.getAttributes().getTotalDistance());
			row.createCell(7).setCellValue(
					mainDepots.getAttributes().getTotalWaitingTime());
			row.createCell(8).setCellValue(mainDepots.getNumTrucksUsed());
		}// if
		else {
			cnt = resultsSheet.getLastRowNum() + 1;
			Row row = resultsSheet.createRow(cnt);
			// get large data set string

			String str = VRPTWProblemInfo.getInputPath();
			int indexA = str.lastIndexOf("data");
			str = str.substring(0, indexA);
			int indexB = str.lastIndexOf("\\");
			str = str.substring((indexB + 1));

			row.createCell(0).setCellValue(str);
			row.createCell(1).setCellValue(
					file.substring(0, (file.length() - 5)));
			row.createCell(2).setCellValue(huerType);
			row.createCell(3).setCellValue(huerType);
			row.createCell(4).setCellValue(passFail);
			row.createCell(5).setCellValue(mainDepots.getAttributes().getTotalServiceTime()+
					  mainDepots.getAttributes().getTotalWaitingTime());
			row.createCell(6).setCellValue(
					mainDepots.getAttributes().getTotalDistance());
			row.createCell(7).setCellValue(
					mainDepots.getAttributes().getTotalWaitingTime());
			row.createCell(8).setCellValue(mainDepots.getNumTrucksUsed());
		}
		// Auto-size columns to fit text within cells
		Row row = resultsSheet.getRow(0);
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++)
			resultsSheet.autoSizeColumn(i);

		// Below the average sheet

		String end = resultsSheet.getPhysicalNumberOfRows() + "";
		String A = "IFERROR(AVERAGEIFS('All Group 4 Results'!";// var
		String B = ",'All Group 4 Results'!A2:A" + end + ","; // data
		String C = "'All Group 4 Results'!C2:C" + end + ",";// heur
		String D = "),0)";
		XSSFSheet grab = avgSheet;

		SheetWriter(2, grab, evaluator, end, A, B, C, D);

		// update best of results
		end = resultsSheet.getPhysicalNumberOfRows() + "";
		A = "MIN(IF('All Group 4 Results'!A2:A" + end + "="; // data
		B = "IF('All Group 4 Results'!C2:C" + end + "="; // heur
		C = ",'All Group 4 Results'!"; // variable
		D = ")))";
		grab = g4BestSheet;

		SheetWriter(1, grab, evaluator, end, A, B, C, D);

		// update comparison
		A = "('Group 4 Average Results'!";
		B = "-'Solomon Results'!";
		C = ")/IF('Solomon Results'!";
		D = ">0,'Solomon Results'!";
		end = "";
		grab = solSheet;

		SheetWriter(3, grab, evaluator, end, A, B, C, D);

		// update best known comparison

		int numRows = bestKnown.getPhysicalNumberOfRows();
		// add formulas for best known sheet for comparison
		for (int i = 0; i < (numRows - 2); i++) {
			// in column H calculate percent difference between best results and
			// group 4
			String percentError = "(B" + (i + 3) + "-" + "E" + (i + 3) + ")/B"
					+ (i + 3);
			row = bestKnown.getRow(i + 2);
			row.createCell(7);
			row.getCell(7).setCellType(2);
			row.getCell(7).setCellFormula(percentError);

			// in column E get group 4 best distance
			String dist = "MIN(IF('All Group 4 Results'!B2:B"
					+ (resultsSheet.getPhysicalNumberOfRows());
			String b = "=A" + (i + 3);
			String c = ",'All Group 4 Results'!G2:G"
					+ (resultsSheet.getPhysicalNumberOfRows()) + "))";
			dist = dist + b + c;
			row.createCell(4);
			row.getCell(4).setCellType(2);
			row.getCell(4).setCellFormula(dist);

			// in column f get group 4 get best truck count
			String truck = "MIN(IF('All Group 4 Results'!B2:B"
					+ (resultsSheet.getPhysicalNumberOfRows());
			String d = "=A" + (i + 3);
			String e = ",'All Group 4 Results'!I2:I"
					+ (resultsSheet.getPhysicalNumberOfRows()) + "))";
			truck = truck + d + e;
			row.createCell(5);
			row.getCell(5).setCellType(2);
			row.getCell(5).setCellFormula(truck);

			// in column G get group 4 best heuristic
			String heur = "MIN(IF('All Group 4 Results'!B2:B"
					+ (resultsSheet.getPhysicalNumberOfRows());
			String f = "=A" + (i + 3);
			String g = ",'All Group 4 Results'!C2:C"
					+ (resultsSheet.getPhysicalNumberOfRows()) + "))";
			heur = heur + f + g;
			row.createCell(6);
			row.getCell(6).setCellType(2);
			row.getCell(6).setCellFormula(heur);

		}// iterates through all of the rows in the best row sheet

		FileOutputStream fileOut = new FileOutputStream(VRPTWProblemInfo.getOutputPath()
				+ "Log\\Log.xlsx");
		// close and write to file
		try {
			wb.write(fileOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// log

	/**
	 * sheetWriter is a helper function to assist with the writing of functions
	 * to the results and comparison file within this class. This will only work
	 * with the predetermined format found within this excel file. This function
	 * will access a sheet, and have a string parameters broken up into 4 parts.
	 * The first parameter is an integer flag, if it is equal to one it will be
	 * used for the BestResults Sheet if it is equal to two it will be used for
	 * the average sheet if it is equal to 3 it will be used for the comparison
	 * sheet
	 * 
	 * This function is intended to increase conciseness of code
	 * very specific to log sheet
	 * @param grabSheet
	 * @param formA first section of formula
	 * @param formB second section of formula
	 * @param formC third section of formula
	 * @param formD fourth section of formula
	 */
	private void SheetWriter(int flag, XSSFSheet grabSheet,
			XSSFFormulaEvaluator evaluator, String end, String formA,
			String formB, String formC, String formD) {

		XSSFWorkbook wb = new XSSFWorkbook(); // simply used to generate formula
												// evaluator
		// XSSFFormulaEvaluator evaluator =
		// wb.getCreationHelper().createFormulaEvaluator();

		String timeRange = "F2:F" + end;
		String distRange = "G2:G" + end;
		String waitRange = "H2:H" + end;
		String truckRange = "I2:I" + end;
		int j = 0;
		String data = "";
		String heur = "";
		Row row = null;
		// for loop to iterate through the different average blocks
		for (int i = 0; i < 6; i++) {
			// j is the offset for the row
			switch (i) {
			case 0:
				data = "\"r1\",";
				break;
			case 1:
				data = "\"c1\",";
				break;
			case 2:
				data = "\"rc1\",";
				break;
			case 3:
				data = "\"r2\",";
				break;
			case 4:
				data = "\"c1\",";
				break;
			case 5:
				data = "\"rc2\",";
				break;
			default:
				System.out
						.println("ERROR data set not found when writing comparison file");
				data = "";
				break;
			}// end switch

			for (int k = 0; k < 7; k++) {
				switch (k) {
				case 0:
					heur = "\"SAV\"";
					row = grabSheet.getRow(j + 1);
					break;
				case 1:
					heur = "\"SWT\"";
					row = grabSheet.getRow(j + 2);
					break;
				case 2:
					heur = "\"I1\"";
					row = grabSheet.getRow(j + 3);
					break;
				case 3:
					heur = "\"I2\"";
					row = grabSheet.getRow(j + 4);
					break;
				case 4:
					heur = "\"I3\"";
					row = grabSheet.getRow(j + 5);
					break;
				case 5:
					heur = "\"NN\"";
					row = grabSheet.getRow(j + 6);
					break;
				case 6:
					heur = "\"S\"";
					row = grabSheet.getRow(j + 7);
					break;
				default:
					System.out
							.println("ERROR heuristic not found when writing comparison file");
					data = "";
					break;
				}// end switch
				if (flag == 1) {
					// schedule time
					row.createCell(1).setCellType(2);
					row.getCell(1).setCellFormula(
							formA + data + formB + heur + formC + timeRange
									+ formD);
					// distance
					row.createCell(2).setCellType(2);
					row.getCell(2).setCellFormula(
							formA + data + formB + heur + formC + distRange
									+ formD);
					// waiting time
					row.createCell(3).setCellType(2);
					row.getCell(3).setCellFormula(
							formA + data + formB + heur + formC + waitRange
									+ formD);
					// number of routes
					row.createCell(4).setCellType(2);
					row.getCell(4).setCellFormula(
							formA + data + formB + heur + formC + truckRange
									+ formD);
				} else if (flag == 2) {
					row.createCell(1).setCellType(2);
					row.getCell(1).setCellFormula(
							formA + timeRange + formB + data + formC + heur
									+ formD);
					// distance
					row.createCell(2).setCellType(2);
					row.getCell(2).setCellFormula(
							formA + distRange + formB + data + formC + heur
									+ formD);
					// waiting time
					row.createCell(3).setCellType(2);
					row.getCell(3).setCellFormula(
							formA + waitRange + formB + data + formC + heur
									+ formD);
					// number of routes
					row.createCell(4).setCellType(2);
					row.getCell(4).setCellFormula(
							formA + truckRange + formB + data + formC + heur
									+ formD);
				} else if (flag == 3) {

					String cell = "B" + "" + (row.getRowNum() + 1) + "";
					// time
					row.createCell(1).setCellType(2);
					row.getCell(1).setCellFormula(
							formA + cell + formB + cell + formC + cell + formD
									+ cell + ",1)");

					// distance
					cell = "C" + "" + (row.getRowNum() + 1);
					row.createCell(2).setCellType(2);
					row.getCell(2).setCellFormula(
							formA + cell + formB + cell + formC + cell + formD
									+ cell + ",1)");

					// waiting time
					cell = "D" + "" + (row.getRowNum() + 1);
					row.createCell(3).setCellType(2);
					row.getCell(3).setCellFormula(
							formA + cell + formB + cell + formC + cell + formD
									+ cell + ",1)");

					// number of routes
					cell = "E" + "" + (row.getRowNum() + 1);
					row.createCell(4).setCellType(2);
					row.getCell(4).setCellFormula(
							formA + cell + formB + cell + formC + cell + formD
									+ cell + ",1)");
				}

			}// for heuristics
			j = j + 9;
		}// end the for loop iterating through data sets
	}

	// will need to call appropriate write files for mainShipments and
	// mainDepots
	/**
	 * called to write temp files for shipments and depot to temp filepath
	 * used by QA
	 */
	public void writeFiles() {

		mainShipments.writeShipmentsToFile(VRPTWProblemInfo.getTempFileLocation()
				+ "//shipments//", VRPTWProblemInfo.getProbFileName());

		try {
			mainDepots.writeDepotInformationToXlsx(
					VRPTWProblemInfo.getTempFileLocation() + "//depots//",
					VRPTWProblemInfo.getProbFileName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// write files

	/**
	 * Clears the temp files with specified filename to avoid overwriting issues
	 * 
	 * @param VRPTWFileName
	 * @throws IOException
	 */
	public void clearTempFiles(String VRPTWFileName) throws IOException {
		// Open the requested file
		FileInputStream thisxls = null;
		XSSFWorkbook wb;
		XSSFSheet sheet;
		XSSFRow curRow;
		try {
			thisxls = new FileInputStream(VRPTWProblemInfo.getTempFileLocation()
					+ "//shipments//" + VRPTWFileName);
			wb = new XSSFWorkbook(thisxls);
			sheet = wb.getSheetAt(0);
			int numRows = sheet.getPhysicalNumberOfRows();
			for (int i = 0; i < numRows; i++) {
				sheet.removeRow(sheet.getRow(i));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			thisxls.close();
		}

		try {
			thisxls = new FileInputStream(VRPTWProblemInfo.getTempFileLocation()
					+ "//depots//" + VRPTWFileName);
			wb = new XSSFWorkbook(thisxls);
			sheet = wb.getSheetAt(0);
			int numRows = sheet.getPhysicalNumberOfRows();
			for (int i = 0; i < numRows; i++) {
				sheet.removeRow(sheet.getRow(i));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			thisxls.close();
		}

	}

	/**
	 * reads shipment file from temp location, writefiles() must be called first
	 * 
	 * @param VRPTWFileName specified filename from VRPTWProblemInfo.fileName
	 * @throws IOException
	 */
	public void readShipmentFile(String VRPTWFileName) throws IOException {
		// this will be used to 'reread' the provided problem (shipment) file

		// Open the requested file
		FileInputStream thisxls = null;
		XSSFWorkbook wb;
		XSSFSheet sheet;
		XSSFRow curRow;
		try {
			thisxls = new FileInputStream(VRPTWProblemInfo.getTempFileLocation()
					+ "//shipments//" + VRPTWFileName);
			wb = new XSSFWorkbook(thisxls);
			sheet = wb.getSheetAt(0);

			VRPTWQADepot qaDepot = new VRPTWQADepot();

			int count = 1;
			curRow = sheet.getRow(count);
			while (curRow.getRowNum() < sheet.getLastRowNum()) {
				VRPTWQAShipment s = new VRPTWQAShipment();
				s.setIndex((int) curRow.getCell(0).getNumericCellValue()); // index
				s.setX((float) curRow.getCell(1).getNumericCellValue()); //x
				s.setY((float) curRow.getCell(2).getNumericCellValue()); //y
				s.setDemand(curRow.getCell(3).getNumericCellValue()); // demand
				s.setEarly((int) curRow.getCell(4).getNumericCellValue()); // early
				s.setLate((int) curRow.getCell(5).getNumericCellValue()); // late
				s.setDuration((int) curRow.getCell(6).getNumericCellValue()); // duration
				s.setCustId((int) curRow.getCell(0).getNumericCellValue());
				;
				vrptwQAShipments.getShipments().add(s);

				count++;
				curRow = sheet.getRow(count);
			}// while shipments available

		} catch (Exception e) {
			e.printStackTrace();
			// throw e;
		} finally {
			thisxls.close();
		}
	}// read shipment file

	/**
	 * Read in the depot file for QA, writefiles() must be called first
	 * 
	 * @param VRPTWFileName specified filename from VRPTWProblemInfo.fileName
	 * @throws IOException
	 */
	public void readDepotFile(String VRPTWFileName) throws IOException {
		// Open the requested file
		FileInputStream thisxls = null;
		XSSFWorkbook wb;
		XSSFSheet sheet;
		XSSFRow curRow;
		try {
			thisxls = new FileInputStream(VRPTWProblemInfo.getTempFileLocation()
					+ "//depots//" + VRPTWFileName);
			wb = new XSSFWorkbook(thisxls);
			sheet = wb.getSheetAt(0);

			VRPTWQADepot qaDepot = new VRPTWQADepot();

			int count = 1;
			curRow = sheet.getRow(count);
			while (curRow.getRowNum() < sheet.getLastRowNum()) {
				int x, y, i, t;
				x = (int) curRow.getCell(0).getNumericCellValue();
				y = (int) curRow.getCell(1).getNumericCellValue();
				i = (int) curRow.getCell(2).getNumericCellValue();
				t = (int) curRow.getCell(3).getNumericCellValue();
				qaDepot.setX(x);
				qaDepot.setY(y);
				qaDepot.setIndex(i);
				qaDepot.setTravelTime(t);

				System.out.println(x + "  " + y + "  " + i);

				vrptwQADepots.getDepots().add(qaDepot);
				count++;
				curRow = sheet.getRow(count);
			}// while
		} catch (Exception e) {
		}

		try {
			thisxls.close();
		} catch (IOException e) {
			e.printStackTrace();
		}// catch
	}

	/**
	 * reads in the information from longResults output to run QA and verify results from
	 * This function is depended on the format specified in the writing of long results
	 * any changes to this function, or the long results write will cause issues
	 * 
	 * @param VRPTWFileName specified filename from VRPTWProblemInfo.fileName
	 * @throws IOException
	 */
	public void readSolutionFile(String VRPTWFileName) throws IOException {

		String dataSet = VRPTWFileName.substring(0, (VRPTWFileName.length()-5));
		// Open the requested file
		FileInputStream thisxls = null;
		XSSFWorkbook wb;
		XSSFSheet sheet;
		try {
			thisxls = new FileInputStream(VRPTWProblemInfo.getOutputPath()
					+ dataSet +"_"+VRPTWProblemInfo.heurStr+"_"+"LongResults.xlsx");
			wb = new XSSFWorkbook(thisxls);
			sheet = wb.getSheetAt(0);

			int cnt, numTrucks, numNodes;
			cnt = 1; // by setting cnt to 1, begin access at row 2, skip header

			Row row = sheet.getRow(cnt);
			// get number of trucks
			row.getCell(6).setCellType(0);
			numTrucks = (int) row.getCell(6).getNumericCellValue();

			// create QA data structures for storage
			VRPTWQADepot qaDepot = new VRPTWQADepot();

			// for the number of trucks, get the nodes for each truck
			for (int i = 0; i < numTrucks; i++) {
				// increment count to skip over truck header
				cnt = cnt + 2;
				row = sheet.getRow(cnt);
				VRPTWQATruck qaTruck = new VRPTWQATruck();

				// get QATruck info
				qaTruck.setIndex((int) row.getCell(0).getNumericCellValue());
				qaTruck.setDemand((int) row.getCell(1).getNumericCellValue());
				qaTruck.setDistance(row.getCell(2).getNumericCellValue());
				qaTruck.setWaitTime(row.getCell(5).getNumericCellValue());
				numNodes = (int) row.getCell(7).getNumericCellValue();

				cnt++;// will currently be pointing at header row
				// for each node on the truck, get the node information
				for (int j = 0; j < (numNodes + 2); j++) {
					cnt++; // move to next node, in the case of j==0, moves from
							// header to cust #1
					row = sheet.getRow(cnt);

					VRPTWQANode qaNode = new VRPTWQANode();

					// get node info
					float x, y;
					double demand;
					int ear, lat, index, serviceTime;
					index = (int) row.getCell(1).getNumericCellValue();
					x = (float) row.getCell(2).getNumericCellValue();
					y = (float) row.getCell(3).getNumericCellValue();
					row.getCell(4).setCellType(0);
					ear = (int) row.getCell(4).getNumericCellValue();
					row.getCell(5).setCellType(0);
					lat = (int) row.getCell(5).getNumericCellValue();
					row.getCell(6).setCellType(0);
					demand = (double) row.getCell(6).getNumericCellValue();
					row.getCell(7).setCellType(0);
					serviceTime = (int) row.getCell(7).getNumericCellValue();

					// setNode info to node
					qaNode.setIndex(index);
					qaNode.setDemand(demand);
					qaNode.setX(x);
					qaNode.setY(y);
					qaNode.setEarly(ear);
					qaNode.setLate(lat);
					qaNode.setService(serviceTime);

					// at the end of this inner for loop cnt is pointing at the
					// last node on the truck
					qaNode = qaNode;
					// add the node to the nodes vector on the truck
					qaTruck.getNodes().add(qaNode);
				}// numNodes on truck
					// add the trucks to the vector on the depot
				qaDepot.getTrucks().add(qaTruck);
			}// for num truck
			vrptwQADepots.getDepots().add(qaDepot);

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			thisxls.close();
		} catch (IOException e) {
			e.printStackTrace();
		}// catch
	}
}
