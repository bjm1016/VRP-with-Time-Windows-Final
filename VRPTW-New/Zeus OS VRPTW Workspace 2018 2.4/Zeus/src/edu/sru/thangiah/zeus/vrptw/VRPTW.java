//VRP.java referenced for creation
package edu.sru.thangiah.zeus.vrptw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.sru.thangiah.zeus.core.Settings;
import edu.sru.thangiah.zeus.gui.ZeusGui;
import edu.sru.thangiah.zeus.vrptw.vrptwqualityassurance.*;

/**
 * This is the main calling function of the VPRTW. This function call those functions 
 * handling input and output, selection of the heuristics, creating the routes, running quality assurance,
 * and launching the GUI. 
 */
public class VRPTW {
	private final boolean debug = true;
	int x = 0, // x-coordinate of the depots
			y = 0, // y-coordinate of the depots
			n = 0, // number of customers
			t = 0, // number of days(or depots)
			Q = 0, // maximum capacity of vehicle
			D = 0; // maximum duration of route
	public double runTime;

	VRPTWShipmentLinkedList mainShipments = new VRPTWShipmentLinkedList();
	VRPTWDepotLinkedList mainDepots = new VRPTWDepotLinkedList();
	ArrayList<VRPTWShipment> shipArrList = new ArrayList<VRPTWShipment>();
	ArrayList<VRPTWTruckType> ttArrList = new ArrayList<VRPTWTruckType>();
	// constructor for VRPTW Class
	public VRPTW(String fileName, int heuristicType) throws Exception {
		Object selectionType = null, insertionType = null; 
		switch(heuristicType){
		case 1:
			selectionType = new UnroutedCustomerWithFurthestDistanceFromDepot();
			insertionType = new SavingsInsertHeuristic();
			break;
		case 2:
			selectionType = new UnroutedCustomerWithFurthestDistanceFromDepot();
			insertionType = new SavingsTimeOriented();
			break;
		case 3:
			selectionType = new TimeOrientedNearestNeighbor();
			insertionType = new TimeOrientedNearestNeighborInsert();
			break;
		case 4:
			selectionType = new UnroutedCustomerWithEarliestDeadline();
			insertionType = new InsertionCriterionI(mainShipments);
			break;
		case 5:
			selectionType = new UnroutedCustomerWithEarliestDeadline();
			insertionType = new InsertionCriterionII(mainShipments);
			break;
		case 6:
			selectionType = new UnroutedCustomerWithEarliestDeadline();
			insertionType = new InsertionCriterionIII(mainShipments);
			break;
		case 7:
			selectionType = new SmallestPolarAngleToDepot();
			insertionType = new SweepInsert();
			break;
		default:
			Settings.printDebug(Settings.ERROR, "An error occured when selecting an "
					+ "heuristic. System Terminating.");
			System.exit(-1);			
		}
		


		VRPTWProblemInfo.setTruckTypes(new Vector());
		readDataFromFile(VRPTWProblemInfo.getInputPath() + fileName);
		VRPTWProblemInfo.uvList = new VRPTWUnviableList(shipArrList, ttArrList, mainDepots.getVRPTWDepotHead());

		Settings.printDebug(Settings.COMMENT, "Read Data File: "
				+ VRPTWProblemInfo.getInputPath() + fileName);
		mainShipments.printShipmentsToConsole();

		// Ensure that the shipment linked list has been loaded with the data
		if (mainShipments.getHead() == null) {
			Settings.printDebug(Settings.ERROR,
					"VRPTW: Shipment linked list is empty");
		}

		VRPTWProblemInfo.setSelectShipType(selectionType);
		Settings.printDebug(Settings.COMMENT,
				VRPTWProblemInfo.getSelectShipType().toString());

		// set up the shipment insertion type
		VRPTWProblemInfo.setInsertShipType(insertionType);
		Settings.printDebug(Settings.COMMENT,
				VRPTWProblemInfo.getInsertShipType().toString());
		
		
		String id = "";
		String insType = VRPTWProblemInfo.getInsertShipType().toString();
		switch (insType) {
		case "Insertion Type: Time Oriented Nearest Neighbor Insert":
			id = "NN";
			break;
		case "Insertion Type: Sweep Insert":
			id = "S";
			break;
		case "Insertion Type: Insertion Criterion i":
			id = "I1";
			break;
		case "Insertion Type: Insertion Criterion ii":
			id = "I2";
			break;
		case "Insertion Type: Insertion Criterion iii":
			id = "I3";
			break;
		case "Insertion Type: Savings Insertion Heuristic":
			id = "SAV";
			break;
		case "Insertion Type: Savings Time Oriented":
			id = "SWT";
			break;
		default:
			Settings.printDebug(Settings.ERROR, "An error occurred when selecting an "
					+ "heuristic Str. System Terminating.");
			System.exit(-1);		
		}// end of get appropriate heuristic ID switch
		VRPTWProblemInfo.heurStr = id; 
		
		double startTime = System.currentTimeMillis();

		createInitialRoutes();
		double endTime = System.currentTimeMillis();
		runTime = endTime - startTime;
		System.out.println("Completed initial routes");

		Settings.printDebug(Settings.COMMENT, "Created Initial Routes ");
		Settings.printDebug(Settings.COMMENT,
				"Initial Stats: " + mainDepots.getSolutionString());

		if (mainShipments.isAllShipsAssigned()) {
			writeShortResultsFile(fileName);
			writeLongResultsFile(fileName);
		} 

		VRPTWQualityAssurance vrptwqa = new VRPTWQualityAssurance(mainDepots,
				mainShipments);
		boolean didItWork;
		didItWork = vrptwqa.runQA();
		System.out.println("The problem passed QA:  " + didItWork);

		
		//ZeusGui guiPost = new ZeusGui(mainDepots, mainShipments);
	}

	/**
	 * This void function writes results in long form to a file as specified.
	 * The file name will be 'fileName'_'heur'_LongResults.xlsx
	 * This file will be written to the VRPTWProblemInfo output path
	 * 
	 * This output file contains information about the depots, as well as
	 * detailed information about each truck
	 * 
	 * Note - Quality assurance references this file specifically, therefore any modifications
	 * to the format of this output, will directly impact the reading in of information in QA
	 * 
	 * @param file  a string containing the name of the file being processed (data set)
	 * @throws FileNotFoundException
	 */
	public void writeLongResultsFile(String file) throws FileNotFoundException {

		String dataSet = file.substring(0, (file.length()-5));
		VRPTWDepotLinkedList depotll = new VRPTWDepotLinkedList();
		depotll = mainDepots;
		// create new workbook or open old
		XSSFWorkbook wb;
		try {
			InputStream is = new FileInputStream(VRPTWProblemInfo.getOutputPath()
					+ dataSet+"_"+VRPTWProblemInfo.heurStr+"_"+"LongResults.xlsx");
			wb = new XSSFWorkbook(is);
		} catch (IOException exception) {
			wb = new XSSFWorkbook();
		}
		try{

		// new sheet or open old
		XSSFSheet sheet = wb.getSheet("Results");
		if (sheet == null)
			sheet = wb.createSheet("Results");
		FileOutputStream fileOut = new FileOutputStream(
				VRPTWProblemInfo.getOutputPath()+ dataSet +"_"+VRPTWProblemInfo.heurStr+"_"+"LongResults.xlsx");

		int cnt = 0;

		XSSFFont font = wb.createFont();
		XSSFCellStyle style = wb.createCellStyle();
		font.setBold(true);
		style.setFont(font);

		// header
		Row row = sheet.createRow(cnt);
		row.setRowStyle(style);
		row.createCell(0).setCellType(1);
		row.getCell(0).setCellValue("DEPOT INFO");
		row.createCell(1).setCellType(1);
		row.getCell(1).setCellValue("Total Demand");
		row.createCell(2).setCellType(1);
		row.getCell(2).setCellValue("Total Distance");
		row.createCell(3).setCellType(1);
		row.getCell(3).setCellValue("Tot. Wait Time");
		row.createCell(4).setCellType(1);
		row.getCell(4).setCellValue("Total Time");
		row.createCell(5).setCellType(1);
		row.getCell(5).setCellValue("MaxTravelTime");
		row.createCell(6).setCellType(1);
		row.getCell(6).setCellValue("Number Of Trucks");
		// sets all of the cells within the header to have a bold style
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
			row.getCell(i).setCellStyle(style);
		}
		cnt++;
		row = sheet.createRow(cnt);
		row.createCell(0).setCellType(1);
		row.getCell(0).setCellValue("Heuristic - "+VRPTWProblemInfo.heurStr);
		row.createCell(1).setCellType(0);
		row.getCell(1).setCellValue(VRPTWProblemInfo.depotLLLevelCostF.getTotalDemand(depotll));
		row.createCell(2).setCellType(0);
		row.getCell(2).setCellValue(VRPTWProblemInfo.depotLLLevelCostF.getTotalDistance(depotll));
		row.createCell(3).setCellType(0);
		row.getCell(3).setCellValue(VRPTWProblemInfo.depotLLLevelCostF.getTotalWaitingTime(depotll));
		row.createCell(4).setCellType(0);
		depotll.getAttributes().getTotalServiceTime();
		row.getCell(4).setCellValue(VRPTWProblemInfo.depotLLLevelCostF.getTotalWaitingTime(depotll)+VRPTWProblemInfo.depotLLLevelCostF.getTotalServiceTime(depotll));//+
		row.createCell(5).setCellType(0);
		row.getCell(5).setCellValue(
				depotll.getAttributes().getMaxTravelTime());
		row.createCell(6).setCellType(0);
		row.getCell(6).setCellValue(depotll.getNumTrucksUsed());
		cnt++;

		VRPTWDepot Depot = new VRPTWDepot();
		Depot = (VRPTWDepot) depotll.getHead().getNext();
		VRPTWTruckLinkedList truckll = Depot.getMainTrucks();
		VRPTWTruck truck = (VRPTWTruck) truckll.getHead();
		int numTrucks = depotll.getNumTrucksUsed();

		row = sheet.createRow(cnt);

		for (int j = 0; j < numTrucks; j++) {
			// information about the trucks
			truck = truck.getVRPTWNext();

			VRPTWNodesLinkedList nodell = (VRPTWNodesLinkedList) truck
					.getVRPTWMainNodes();
			row = sheet.createRow(cnt);
			row.createCell(0).setCellValue("TruckNumber");
			row.createCell(1).setCellValue("Capacity Q");
			row.createCell(2).setCellValue("Distance");
			row.createCell(3).setCellValue("FixedCost");
			row.createCell(4).setCellValue("VariableCost");
			row.createCell(5).setCellValue("Wait Time");
			row.createCell(6).setCellValue("Total Truck Route Time");
			row.createCell(7).setCellValue("# of Customers");
			// set all the cells within the header to have a bold style
			for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
				row.getCell(i).setCellStyle(style);
			}
			cnt++;

			// Truck Header
			row = sheet.createRow(cnt);
			row.createCell(0).setCellValue(j);
			row.createCell(1).setCellType(0);
			row.getCell(1)
					.setCellValue(nodell.getAttributes().getTotalDemand());
			row.createCell(2).setCellType(0);
			row.getCell(2).setCellValue(
					VRPTWProblemInfo.truckLevelCostF.getTotalDistance(truck));
			row.createCell(5).setCellValue(
					VRPTWProblemInfo.truckLevelCostF.getTotalWaitingTime(truck));
			row.createCell(6).setCellValue(
					VRPTWProblemInfo.truckLevelCostF.getTotalWaitingTime(truck)+
					VRPTWProblemInfo.truckLevelCostF.getTotalServiceTime(truck));
			row.createCell(7).setCellValue(nodell.getSize());
			cnt++;
			// Customer node headers
			row = sheet.createRow(cnt);
			row.createCell(1).setCellValue("CustID");
			row.createCell(2).setCellValue("X");
			row.createCell(3).setCellValue("Y");
			row.createCell(4).setCellValue("EAR");
			row.createCell(5).setCellValue("LAT");
			row.createCell(6).setCellValue("Demand");
			row.createCell(7).setCellValue("Duration");
			row.createCell(8).setCellValue("Distance");
			// set all the cells within the header to style of bold
			for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
				row.getCell((i + 1)).setCellStyle(style);
			}
			cnt++;
			// Go through customers per truck
			int nodellSize = 0;
			nodellSize = nodell.getSize();
			for (int j1 = 0; j1 <= (nodellSize + 1); j1++) {
				row = sheet.createRow(cnt);
				VRPTWNodes myNode;
				if (j1 == 0)
					myNode = (VRPTWNodes) nodell.getHead();
				else
					myNode = (VRPTWNodes) nodell.getNodesAtPosition((j1 - 1));

				row.createCell(1).setCellType(0);
				row.getCell(1).setCellValue(myNode.getShipment().getIndex());
				row.createCell(2)
						.setCellValue(myNode.getShipment().getXCoord());
				row.createCell(3)
						.setCellValue(myNode.getShipment().getYCoord());
				row.createCell(4).setCellValue(
						myNode.getShipment().getEarlyTime());
				row.createCell(5).setCellValue(
						myNode.getShipment().getLateTime());
				row.createCell(6)
						.setCellValue(myNode.getShipment().getDemand());
				row.createCell(7).setCellValue(
						myNode.getShipment().getServiceTime());
				row.createCell(11).setCellValue(myNode.getShipment().getTravelTime());

				// calculating the distance from the previous node, via an excel
				// function
				// recall x value held in C, y value held in D
				// distance formula = ((x1-x2)^2+(y1-y2)^2)^(.5)

				row.createCell(8).setCellType(2);
				String str = "((C" + (cnt + 1) + "-C" + cnt + ")^2+(D"
						+ (cnt + 1) + "- D" + cnt + ")^2)^.5";

				// make no distance calculation on the first node, the depot
				if (j1 != 0) {
					row.getCell(8).setCellFormula(str);
				} else {
					row.getCell(8).setCellValue(0.0);
				}

				// after all nodes have been written and distances calculated
				// show a total beside the last node
				if (j1 == (nodellSize + 1)) {
					row = sheet.getRow((cnt - 1));
					row.createCell(9).setCellStyle(style);
					row.getCell(9).setCellValue("Distance");
					row.createCell(10).setCellStyle(style);
					row.getCell(10).setCellValue("Confirm");
					row = sheet.getRow(cnt);
					row.createCell(9).setCellFormula(
							"SUM(I" + (cnt + 1) + ":I" + (cnt - nodellSize)
									+ ")");

					// set up formula evaluator
					XSSFFormulaEvaluator evaluator = wb.getCreationHelper()
							.createFormulaEvaluator();
					// get cell containing formula to be evaluated
					if (row.getCell(9).getCellType() == 2)
						evaluator.evaluateFormulaCell(row.getCell(9));

					// confirm the excel calculated distances found in cell(9)
					double excelCalc = row.getCell(9).getNumericCellValue(); 
					double zeusCalc = VRPTWProblemInfo.truckLevelCostF
							.getTotalDistance(truck); // retrieves distance as
														// calculated by Zeus
					excelCalc = excelCalc * 100;
					zeusCalc = zeusCalc * 100;

					boolean result = true;
					if ((int) excelCalc != (int) zeusCalc)
						result = false;
					row.createCell(10).setCellValue(result);
				}
				cnt++;

			}// nested, customer list
		}// end for, trucks

		// Auto-size columns to fit text within cells
		row = sheet.getRow(2);
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++)
			sheet.autoSizeColumn(i);

		// close and write to file
			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}// writeLongResultsFile
	
	/** This void function writes results in short form to a file as specified.
	* The file name will be 'fileName'_'heur'_ShortResults.xlsx
	* This file will be written to the VRPTWProblemInfo output path
	* 
	* This output file contains information about the depots, as well as
	* brief information about the nodes, and a route list
	* 
	* @param file - the data set file to write
	* @throws FileNotFoundException
	*/
	public void writeShortResultsFile(String file) throws FileNotFoundException {

		String dataSet = file.substring(0, (file.length()-5));
		VRPTWDepotLinkedList depotll = new VRPTWDepotLinkedList();
		depotll = mainDepots;
		// create new workbook or open old
		XSSFWorkbook wb;
		try {
			InputStream is = new FileInputStream(VRPTWProblemInfo.getOutputPath()
					 + dataSet +"_"+VRPTWProblemInfo.heurStr+"_"+"ShortResults.xlsx");
			wb = new XSSFWorkbook(is);
		} catch (IOException exception) {
			wb = new XSSFWorkbook();
		}

		// new sheet or open old
		XSSFSheet sheet = wb.getSheet("Results");
		if (sheet == null)
			sheet = wb.createSheet("Results");

		FileOutputStream fileOut = new FileOutputStream(
				VRPTWProblemInfo.getOutputPath() + dataSet +"_"+VRPTWProblemInfo.heurStr+"_"+"ShortResults.xlsx");

		XSSFFont font = wb.createFont();
		XSSFCellStyle style = wb.createCellStyle();
		font.setBold(true);
		style.setFont(font);

		int cnt = 0;

		// header
		Row row = sheet.createRow(cnt);
		row.createCell(0).setCellType(1);
		row.getCell(0).setCellValue("Depot Stats");
		row.createCell(1).setCellType(1);
		row.getCell(1).setCellValue("Total Demand");
		row.createCell(2).setCellType(1);
		row.getCell(2).setCellValue("Total Distance");
		row.createCell(3).setCellType(1);
		row.getCell(3).setCellValue("Total Waiting Time");
		row.createCell(4).setCellType(1);
		row.getCell(4).setCellValue("Total Service Time");
		row.createCell(5).setCellType(1);
		row.getCell(5).setCellValue("MaxTravelTime");
		row.createCell(6).setCellType(1);
		row.getCell(6).setCellValue("Number Of Trucks");
		// sets all of the cells within the header to have a bold style
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
			row.getCell(i).setCellStyle(style);
		}
		cnt++;
		row = sheet.createRow(cnt);
		row.createCell(1).setCellType(0);
		row.getCell(1)
				.setCellValue(VRPTWProblemInfo.depotLLLevelCostF.getTotalDemand(depotll));
		row.createCell(2).setCellType(0);
		row.getCell(2).setCellValue(VRPTWProblemInfo.depotLLLevelCostF.getTotalDistance(depotll));
		row.createCell(3).setCellType(0);
		row.getCell(3).setCellValue(VRPTWProblemInfo.depotLLLevelCostF.getTotalWaitingTime(depotll));
		row.createCell(4).setCellType(0);
		row.getCell(4).setCellValue(VRPTWProblemInfo.depotLLLevelCostF.getTotalWaitingTime(depotll)+VRPTWProblemInfo.depotLLLevelCostF.getTotalServiceTime(depotll));
		row.createCell(5).setCellType(0);
		row.getCell(5).setCellValue(
				depotll.getAttributes().getMaxTravelTime());
		row.createCell(6).setCellType(0);
		row.getCell(6).setCellValue(depotll.getNumTrucksUsed());
		cnt++;

		VRPTWDepot Depot = new VRPTWDepot();
		Depot = (VRPTWDepot) depotll.getHead().getNext();
		VRPTWTruckLinkedList truckll = Depot.getMainTrucks();
		VRPTWTruck truck = (VRPTWTruck) truckll.getHead();
		int numTrucks = depotll.getNumTrucksUsed();

		cnt++;// add a space after depot header

		// prints the truck header
		row = sheet.createRow(cnt);
		row.createCell(0).setCellValue("TruckNumber");
		row.createCell(1).setCellValue("Demand");
		row.createCell(2).setCellValue("Distance");
		row.createCell(3).setCellValue("Wait Time");
		row.createCell(4).setCellValue("RouteCost");
		row.createCell(5).setCellValue("# of Customers");
		row.createCell(6).setCellValue("Truck Route:");
		// add bold to header
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
			row.getCell(i).setCellStyle(style);
		}
		cnt++;
		for (int j = 0; j < numTrucks; j++) {
			// information about the trucks
			truck = truck.getVRPTWNext();

			VRPTWNodesLinkedList nodell = (VRPTWNodesLinkedList) truck
					.getVRPTWMainNodes();
			row = sheet.createRow(cnt);
			row.createCell(0).setCellValue(j);
			row.createCell(1).setCellType(0);
			row.getCell(1)
					.setCellValue(VRPTWProblemInfo.truckLevelCostF.getTotalDemand(truck));
			row.createCell(2).setCellType(0);
			row.getCell(2).setCellValue(VRPTWProblemInfo.truckLevelCostF.getTotalDistance(truck));
			row.createCell(3).setCellValue(VRPTWProblemInfo.truckLevelCostF.getTotalWaitingTime(truck));
			row.createCell(4).setCellValue(VRPTWProblemInfo.truckLevelCostF.getTotalServiceTime(truck)+
					VRPTWProblemInfo.truckLevelCostF.getTotalWaitingTime(truck));
			row.createCell(5).setCellValue(nodell.getSize());
			int listSize = nodell.getSize();
			for (int i = 0; i < listSize + 2; i++) {
				VRPTWNodes myNode;
				if (i == 0)
					myNode = (VRPTWNodes) nodell.getHead();
				else
					myNode = (VRPTWNodes) nodell.getNodesAtPosition((i - 1));

				row.createCell(i + 6).setCellType(0);
				row.getCell(i + 6)
						.setCellValue(myNode.getShipment().getIndex());
			}

			cnt++;

		}// end for, trucks

		// auto-size the cells to fit contents
		row = sheet.getRow(0);
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++)
			sheet.autoSizeColumn(i);
		// close and write to file
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
	}// writeShortResultsFile

	/**
	 * Verifies the selection and insertion statements are assigned
	 * while all shipments are not assigned, the next shipment to be
	 * inserted will be found, and inserted into the emerging route
	 * After all shipments are assigned, calculations of total stats are done
	 */
	public void createInitialRoutes() {
		VRPTWDepot currentDepot = null;
		VRPTWShipment currentShipment = null;

		// check if selection and insertion type methods have been selected
		if (VRPTWProblemInfo.getSelectShipType() == null) {
			Settings.printDebug(Settings.ERROR,
					"No selection shipment type has been assigned");

		}
		if (VRPTWProblemInfo.getInsertShipType() == null) {
			Settings.printDebug(Settings.ERROR,
					"No insertion shipment type has been assigned");
		}

		while (!mainShipments.isAllShipsAssigned()) {
			currentDepot = (VRPTWDepot) mainDepots.getHead().getNext();

			VRPTWShipment theShipment = mainShipments.getNextInsertShipment(
					mainDepots, currentDepot, mainShipments, currentShipment);

			if (theShipment == null) { // shipment is null, print error message
				Settings.printDebug(Settings.COMMENT,
						"No shipment was selected");
			}

			// TESTING: passing the shipments alongside of the shipment
			theShipment.setShipments(mainShipments);

			// The selected shipment will be inserted into the route
			if (!mainDepots.insertShipment(theShipment)) {
				Settings.printDebug(Settings.COMMENT, "The Shipment: <"
						+ theShipment.getIndex() + "> cannot be routed");
			} else {
				Settings.printDebug(Settings.COMMENT, "The Shipment: <"
						+ theShipment.getIndex() + // " " + theShipment +
						"> was routed");
				// tag the shipment as being routed
				if (!VRPTWProblemInfo.getInsertShipType().toString().substring(VRPTWProblemInfo.getInsertShipType().toString().length() - 1).equals(
						"i")) {
					theShipment.setIsAssigned(true);
				}
			}
		}
		VRPTWProblemInfo.depotLLLevelCostF.calculateTotalsStats(mainDepots);

	}

	/**
	 * Reads in information of a predetermined format containing data sets for the VRPTW problem
	 * Information pertaining to depots, and shipments is loaded into mainDepots and mainShipments
	 * 
	 * @param VRPTWFileName data will be read from this file, it should contain both the path and file name
	 * @return
	 * @throws IOException
	 */
	public int readDataFromFile(String VRPTWFileName) throws IOException {
		// TODO: Stub of main VRPTW readDataFromFile

		// Open the requested file
		FileInputStream thisxls = null;
		XSSFWorkbook wb;
		XSSFSheet sheet;
		XSSFRow curRow;
		try {
			thisxls = new FileInputStream(VRPTWFileName);
			wb = new XSSFWorkbook(thisxls);
			sheet = wb.getSheetAt(0);
			curRow = sheet.getRow(1);

			x = checkIntType(curRow.getCell(0));
			y = checkIntType(curRow.getCell(1));
			n = checkIntType(curRow.getCell(2));
			t = checkIntType(curRow.getCell(3));
			Q = checkIntType(curRow.getCell(4));
			D = checkIntType(curRow.getCell(5));
			VRPTWDepot depot = new VRPTWDepot(t, x, y, D);
			depot.setDepotDueTime(D);
			
			mainDepots.insertDepotLast(depot);

			// problem info into problem info class
			VRPTWProblemInfo.setNumDepots(1);
			//VRPTWProblemInfo.fileName = VRPTWFileName;
			VRPTWProblemInfo.setNoOfShips(n);
			VRPTWProblemInfo.setNoOfDays(t);
			if (Q == 0) { // if there is no maximum capacity, set it to a very
							// large
							// number
				Q = 999999999;
			}
			if (D == 0) { // if there is no travel time, set it to a very large
							// number
				D = 999999999; // if there is not maximum distance, set it to a
								// very
								// large number
				// VRPTWProblemInfo.maxCapacity = Q; //maximum capacity of a
				// vehicle
				// VRPTWProblemInfo.maxDistance = D; //maximum distance of a
				// vehicle
			}

			String serviceType = "1";
			float maxCapacity = Q; // maximum capacity of a vehicle
			float maxDistance = D; // maximum distance of a vehicle

			int numTruckTypes = 1;
			for (int i = 0; i < numTruckTypes; i++) {
				VRPTWTruckType truckType = new VRPTWTruckType(i, maxDistance,
						maxCapacity, serviceType);
				VRPTWProblemInfo.addTruckTypes(truckType);
			}

			for (int i = 0; i < VRPTWProblemInfo.getTruckTypesSize(); i++) {
				VRPTWTruckType tType = (VRPTWTruckType) VRPTWProblemInfo.getTruckTypesAt(i);
				depot.getMainTrucks().insertTruckLast(
						new VRPTWTruck(tType, depot.getXCoord(), depot
								.getYCoord()));
				ttArrList.add(tType);

			}

			/** This section begins the collection of the shipment data **/
			float x = 0, // x-coordinate of customer
			y = 0; // y-coordinate of customer
			int q = 0, // Demand
			i = 0, // Customer Number
			e = 0, // EAR?
			l = 0, // LAT?
			d = 0; // Service duration

			int count = 3;
			curRow = sheet.getRow(count);
			while (curRow.getRowNum() < sheet.getLastRowNum()) {
				x = (int) curRow.getCell(0).getNumericCellValue();
				y = (int) curRow.getCell(1).getNumericCellValue();
				q = (int) curRow.getCell(2).getNumericCellValue();
				i = (int) curRow.getCell(3).getNumericCellValue();
				curRow.getCell(4).setCellType(0);
				e = (int) curRow.getCell(4).getNumericCellValue();
				curRow.getCell(5).setCellType(0);
				l = (int) curRow.getCell(5).getNumericCellValue();
				curRow.getCell(6).setCellType(0);
				d = (int) curRow.getCell(6).getNumericCellValue();
				count++;
				curRow = sheet.getRow(count);

				mainShipments
						.insertLast(new VRPTWShipment(x, y, q, i, e, l, d));
				shipArrList.add(new VRPTWShipment(x, y, q, i, e, l, d));

			} // end while

			// TODO: Should we initially insert a truck into the depots
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			thisxls.close();
		}
	}


/**
 * referenced from Eric Corrado
 * 
 * returns integer value from cell
 * 
 * @param testCell XSSFCell provided to return integer value from
 * @return
 */
	private int checkIntType(XSSFCell testCell) {
		if (testCell.getCellType() == 1) {
			return Integer.parseInt(testCell.getStringCellValue());
		}
		return (int) testCell.getNumericCellValue();
	}

}