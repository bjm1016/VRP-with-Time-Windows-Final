package edu.sru.thangiah.zeus.vrptw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTimeUtils;

import edu.sru.thangiah.zeus.core.Shipment;
import edu.sru.thangiah.zeus.core.ShipmentLinkedList;

/**
 * The VRPTWShipmentLinkedList class holds the information pulled from the 
 * data files regarding the customers. This information is stored in single shipments
 * that are then linked.
 * 
 * @author Marc S
 *
 */
public class VRPTWShipmentLinkedList extends ShipmentLinkedList {
	/**
	 * Default constructor for the VRPTWShipmentLinkedList
	 */
	public VRPTWShipmentLinkedList() {
		setHead(new VRPTWShipment());
		setTail(new VRPTWShipment());
		linkHeadTail();
		setNumShipments(0);

	}
	
	/**
	 * Seeks out the shipment based on the index of the shipment
	 * 
	 * @param index the index of the shipment to be found
	 * @return the Shipment if found otherwise it returns null
	 */
	public VRPTWShipment getShipmentByIndex(int index){
		VRPTWShipment current = (VRPTWShipment) this.getHead().getNext();
		VRPTWShipment toReturn = null;
		
		while(current != this.getTail()){
			if(current.getIndex() == index){
				toReturn = current;
				break;
			}
			current = (VRPTWShipment) current.getNext();
		}
		
		return toReturn;
	}

	/**
	 * Returns the head of the VRPTWShipmentLinkedList
	 * 
	 */
	@Override
	public VRPTWShipment getHead() {
		return (VRPTWShipment) super.getHead();
	}

	/**
	 * Returns the tail of the VRPTWShipmentLinkedList
	 */
	@Override
	public VRPTWShipment getTail() {
		return (VRPTWShipment) super.getTail();
	}

	/**
	 * This method will insert the shipment into the VRPTWShipmentLinkedList
	 * at the end of the emerging list
	 * 
	 * @param ship the shipment to insert at the end of the route
	 */
	public void insertLast(VRPTWShipment ship) {
		if (this.getHead().getNext() == this.getTail()) { // list is empty
			this.getHead().setNext(ship);
			this.getTail().setPrev(ship);
			ship.setNext(this.getTail());
			ship.setPrev(this.getHead());
		} else { // list not empty
			ship.setNext(this.getTail()); // put new shipment in
			ship.setPrev(this.getTail().getPrev());
			this.getTail().getPrev().setNext(ship); // change old links
			this.getTail().setPrev(ship);
		}
		setNumShipments(getNumShipments() + 1); // because parent will not allow
												// direct control
		setTotalDemand(getTotalDemand() + ship.getDemand());
		// System.out.println("Added shipment");
	}

	/**
	 * This method will traverse the VRPTWShipmentLinkedList and print each
	 * shipment and the information about the shipment to the Console
	 */
	public void printShipmentsToConsole() {
		Shipment ship = super.getHead().getNext();
		VRPTWShipment shipment;
		int i, x, y, q, e, l, d;

		while (ship != this.getTail()) {
			shipment = (VRPTWShipment) ship;
			i = shipment.getIndex();
			x = (int) shipment.getXCoord();
			y = (int) shipment.getYCoord();
			q = shipment.getDemand();
			e = (int) shipment.getEarlyTime();
			l = (int) shipment.getLateTime();
			d = (int) shipment.getServiceTime();

			System.out.println("Information for VRPTWShipment " + i
					+ ": x-coord: " + x + " y-coord: " + y + " demand: " + q
					+ " earliest arrival time: " + e + " Latest Arrival Time: "
					+ l + " Service Duration:" + d);

			ship = ship.getNext();
		}
	}

	/**
	 * This is the step that will call the correct selection method based on the settings in 
	 * VRPTWProblemInfo
	 * 
	 * @param mainDepots Current Depot Linked List
	 * @param currentDepot The Depot that is currently being used 
	 * @param mainShipments Current Shipment Linked List
	 * @param currentShipment The current Shipment that has been found
	 * @return
	 */
	public VRPTWShipment getNextInsertShipment(VRPTWDepotLinkedList mainDepots,
			VRPTWDepot currentDepot, VRPTWShipmentLinkedList mainShipments,
			VRPTWShipment currentShipment) {

		VRPTWShipmentLinkedList selectShip = (VRPTWShipmentLinkedList) VRPTWProblemInfo.getSelectShipType();
		return selectShip.getSelectShipment(mainDepots, currentDepot,
				mainShipments, currentShipment);
	}

	/**
	 * This is a stub program that should be overridden by the inheriting class
	 * 
	 * @param mainDepots
	 * @param currentDepot
	 * @param mainShipments
	 * @param currentShipment
	 * @return
	 */
	public VRPTWShipment getSelectShipment(VRPTWDepotLinkedList mainDepots,
			VRPTWDepot currentDepot, VRPTWShipmentLinkedList mainShipments,
			VRPTWShipment currentShipment) {
		return null;
	}

	/**
	 * This goes through the the shipment linked list,
	 * The linked list is iterated through, and for each shipment in the list,
	 * information (Index, X, Y, Demand, Ear, Lat, Serv) is written to an excel file
	 * at the specified file path and file name
	 * 
	 * @param filePath - The file path string indicating where to write the file
	 * @param fileName - The file name string which to save the excel file as
	 */
	public void writeShipmentsToFile(String filePath, String fileName) {

		XSSFWorkbook wb = null;
		try {
			InputStream is = new FileInputStream(filePath + fileName);
			try {
				wb = new XSSFWorkbook(is);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (FileNotFoundException exception) {
			wb = new XSSFWorkbook();
		}
		// new sheet or open old
		XSSFSheet sheet = wb.getSheet("Shipments");
		if (sheet == null)
			sheet = wb.createSheet("Shipments");
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(filePath + fileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		VRPTWShipment shipment = this.getHead();
		int i, x, y, q, e, l, d, cnt;
		cnt = 0;
		Row row = sheet.createRow(cnt);
		row.createCell(0).setCellValue("Index");
		row.createCell(1).setCellValue("X-coordinate");
		row.createCell(2).setCellValue("Y-coordinate");
		row.createCell(3).setCellValue("Demand");
		row.createCell(4).setCellValue("EarlyTime");
		row.createCell(5).setCellValue("LateTime");
		row.createCell(6).setCellValue("ServiceTime");
		cnt++;
		while (shipment != this.getTail()) {
			i = shipment.getIndex();
			x = (int) shipment.getXCoord();
			y = (int) shipment.getYCoord();
			q = shipment.getDemand();
			e = (int) shipment.getEarlyTime();
			l = (int) shipment.getLateTime();
			d = (int) shipment.getServiceTime();

			// create rows of data
			row = sheet.createRow(cnt);
			row.createCell(0).setCellValue(i);
			row.createCell(1).setCellValue(x);
			row.createCell(2).setCellValue(y);
			row.createCell(3).setCellValue(q);
			row.createCell(4).setCellValue(e);
			row.createCell(5).setCellValue(l);
			row.createCell(6).setCellValue(d);

			cnt++;
			shipment = (VRPTWShipment) shipment.getNext();
		}
		// close and write to file
		try {
			wb.write(fileOut);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			fileOut.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	protected double calcTimeOrientedDistance(VRPTWShipment shipmentOne,
			VRPTWShipment ShipmentTwo) {
		double distanceToReturn = Double.MAX_VALUE;

		return distanceToReturn;
	}
}

/**
 * This is the Time Oriented Nearest Neighbor Heuristic as outlined by Solomon in
 * his paper "Algorithims for the Vehicle Routing Problems with Time Windows Constraints"
 * 
 * This problem selects the customer "closest" the the last assigned customer. The definition 
 * of "closest" accounts for both temporal and spatial aspects of distance 
 * 
 * @author Marc S
 *
 */
class TimeOrientedNearestNeighbor extends VRPTWShipmentLinkedList {
	VRPTWShipment lastAssigned; //Holds the last assigned shipment

	//The weights utilzed in the calculation 
	float deltaOne = 0.4f, deltaTwo = 0.4f, deltaThree = 0.2f;

	/**
	 * This method is responsible for traversing a VRPTWShipmentLinkedList and 
	 * selecting the unrouted customer "closest" to the last assigned as defined
	 *  and returning this VRPTWShipment to the calling function
	 * 
	 */
	public VRPTWShipment getSelectShipment(VRPTWDepotLinkedList currDepotLL,
			VRPTWDepot currDepot, VRPTWShipmentLinkedList currShipLL,
			VRPTWShipment currShip) {
		VRPTWShipment temp = (VRPTWShipment) currShipLL.getHead().getNext();
		VRPTWShipment foundShipment = null;

		double distance = Double.MAX_VALUE, foundDistance = Double.MAX_VALUE, // initial
																				// distance
		depotX, depotY;

		// Get the X and Y coordinate of the depot
		depotX = currDepot.getXCoord();
		depotY = currDepot.getYCoord();
		
		long currentTimeInMillis = DateTimeUtils.currentTimeMillis();
		long lastCheckedTime;
		
		while (temp != currShipLL.getTail()) {
			lastCheckedTime = currentTimeInMillis - temp.getLastTimeChecked();
			if (temp.getIsAssigned()) {
				temp = (VRPTWShipment) temp.getNext();

				continue;
			}

			// If this is the first shipment, find it by euclidian distance from
			// depot
			if (this.lastAssigned == null) {
				// check if this shipment should be tracked
				if (foundShipment == null) { // this is the first shipment being
												// checked
					foundShipment = temp;
					foundDistance = distance;
				} else {
					double totalCost = 0.0;
					double tempCost = 0.0;
					double distanceTwo = 0.0;

					/* Begin Calculating distanceTwo between each customer */
					distanceTwo = calcDist(currDepot.getXCoord(),
							temp.getXCoord(), currDepot.getYCoord(),
							temp.getYCoord());
					tempCost = distanceTwo;
					
					tempCost *= deltaOne;
					totalCost = tempCost;
					
					/* End calculating distanceTwo between each customer */
					double tempH = Math.max(temp.getEarlyTime(), (VRPTWProblemInfo.depotLevelCostF.getTotalServiceTime(currDepot) + distanceTwo));
					tempCost = Math.max(tempH - (VRPTWProblemInfo.depotLevelCostF.getTotalServiceTime(currDepot) + distanceTwo), 0);
					tempCost *= deltaTwo;
					
					if(tempCost > 0){
						totalCost += tempCost;
					}

					/* Calculate the Urgency of the customers */ 
					tempCost = temp.getLateTime() - (VRPTWProblemInfo.depotLevelCostF.getTotalServiceTime(currDepot) + distanceTwo);
					tempCost *= deltaThree;

					totalCost += tempCost;

					distance = totalCost;
					

					if (distance < foundDistance) { // found an angle that is
													// less
						foundShipment = temp;
						foundDistance = distance;
					}
				}
				temp = (VRPTWShipment) temp.getNext();
			} else {
				if (foundShipment == null) { // this is the first shipment being
					// checked
					foundShipment = temp;
					foundDistance = distance;
				} // else {
				double totalCost = 0.0;
				double tempCost = 0.0;
				double distanceTwo = 0.0;

				/* Begin Calculating distanceTwo between each customer */
				distanceTwo = calcDist(lastAssigned.getXCoord(),
						temp.getXCoord(), lastAssigned.getYCoord(),
						temp.getYCoord());
				
				tempCost = distanceTwo;

				if(tempCost <
						temp.getEarlyTime()){
					temp = (VRPTWShipment) temp.getNext();

					continue;
				}
				
				tempCost *= deltaOne;
				totalCost = tempCost;

				/* End calculating distanceTwo between each customer */
				double tempH = Math.max(temp.getEarlyTime(), (VRPTWProblemInfo.nodesLLLevelCostF.getTotalServiceTime(lastAssigned.getAssignedRoute()) + lastAssigned.getServiceTime() + distanceTwo));
				tempCost = Math.max(tempH - (VRPTWProblemInfo.nodesLLLevelCostF.getTotalServiceTime(lastAssigned.getAssignedRoute()) + distanceTwo),0);
				tempCost *= deltaTwo;
				
				if(tempCost > 0){
					totalCost += tempCost;
				}
				
				tempCost = temp.getLateTime() - (VRPTWProblemInfo.nodesLLLevelCostF.getTotalServiceTime(lastAssigned.getAssignedRoute())  + distanceTwo);
				tempCost *= deltaThree;

				totalCost += tempCost;

				distance = totalCost;
				

				if (distance < foundDistance) { // found an angle that is
												// less
					foundShipment = temp;
					foundDistance = distance;
				}

				temp = (VRPTWShipment) temp.getNext();
			}
		}
		this.lastAssigned = foundShipment;
		return this.lastAssigned;
	}

	// The WhoAmI methods gives the id of the assigned object
	// It is a static method so that it can be accessed without creating an
	// object
	@Override
	public String toString() {
		return ("Selection Type: Time Oriented Nearest Neighbor");
	}
}


/**
 * This class is a selection method that will be utilized by the Insertion 
 * Heuristics to select a seed customer. This one in particular seeks out the 
 * unrouted customer with the earliest deadline.
 * 
 * @author Marc S
 *
 */
class UnroutedCustomerWithEarliestDeadline extends VRPTWShipmentLinkedList {
	/**
	 * This method is responsible for traversing a VRPTWShipmentLinkedList and 
	 * selecting the unrouted customer with the earliest deadline
	 *  and returning this VRPTWShipment to the calling function
	 * 
	 */
	public VRPTWShipment getSelectShipment(VRPTWDepotLinkedList currDepotLL,
			VRPTWDepot currDepot, VRPTWShipmentLinkedList currShipLL,
			VRPTWShipment currShip) {
		VRPTWShipment temp = (VRPTWShipment) currShipLL.getHead().getNext();
		VRPTWShipment foundShipment = null;

		double currentDeadline = 0.0, earliestDeadline = Double.MAX_VALUE;

		while (temp != currShipLL.getTail()) {
			if (temp.getIsAssigned()) {
				temp = (VRPTWShipment) temp.getNext();

				continue;
			}

			currentDeadline = temp.getEarlyTime();

			if (currentDeadline < earliestDeadline) {
				foundShipment = temp;
				earliestDeadline = currentDeadline;
			}
			temp = (VRPTWShipment) temp.getNext();
		}
		return foundShipment;
	}

	// The WhoAmI methods gives the id of the assigned object
	// It is a static method so that it can be accessed without creating an
	// object
	@Override
	public String toString() {
		return ("Selection Type: Unrouted Customer with Earliest Service Time");
	}
}

/**
 * This class will traverse the VRPTWShipmentLinkedList and return the first shipment
 * that it encounters that has been unrouted. 
 * 
 * @author Marc S
 *
 */
class LinearCustomerSelection extends VRPTWShipmentLinkedList {
	/**
	 * This method is responsible for traversing a VRPTWShipmentLinkedList and 
	 * selecting the first unrouted customer
	 * 
	 */
	public VRPTWShipment getSelectShipment(VRPTWDepotLinkedList currDepotLL,
			VRPTWDepot currDepot, VRPTWShipmentLinkedList currShipLL,
			VRPTWShipment currShip) {
		VRPTWShipment foundShipment = null;
		
		foundShipment = (VRPTWShipment) currShipLL.getHead().getNext();
		
		if(foundShipment.getIsAssigned()){
			while(foundShipment.getIsAssigned()){
				foundShipment = (VRPTWShipment) foundShipment.getNext();
			}
		}
		
		return foundShipment;
	}
	
	@Override
	public String toString() {
		return ("Selection Type: Linear Selection");
	}
}

/**
 * This class is a selection method that will be utilized by the Insertion 
 * Heuristics to select a seed customer. This one in particular seeks out the 
 * Unrouted Customer With Furthest Distance From Depot
 * 
 * @author Marc S
 *
 */

class UnroutedCustomerWithFurthestDistanceFromDepot extends
		VRPTWShipmentLinkedList {
	/**
	 * This method is responsible for traversing a VRPTWShipmentLinkedList and 
	 * selecting the Unrouted Customer With Furthest Distance From Depot
	 *  and returning this VRPTWShipment to the calling function
	 * 
	 */
	public VRPTWShipment getSelectShipment(VRPTWDepotLinkedList currDepotLL,
			VRPTWDepot currDepot, VRPTWShipmentLinkedList currShipLL,
			VRPTWShipment currShip) {
		VRPTWShipment temp = (VRPTWShipment) currShipLL.getHead().getNext();
		VRPTWShipment foundShipment = null;

		double currentDistance = 0.0, furthestDistance = 0.0, depotX = currDepot
				.getXCoord(), depotY = currDepot.getYCoord();
		
		long currentTimeInMillis = DateTimeUtils.currentTimeMillis();
		long lastCheckedTime;

		while (temp != currShipLL.getTail()) {
			lastCheckedTime = currentTimeInMillis - temp.getLastTimeChecked();
			if (temp.getIsAssigned() || lastCheckedTime < 1000) {
				temp = (VRPTWShipment) temp.getNext();

				continue;
			}
			currentDistance = calcDist(temp.getXCoord(), depotX,
					temp.getYCoord(), depotY);

			if (currentDistance > furthestDistance) {
				foundShipment = temp;
				furthestDistance = currentDistance;
			}
			temp = (VRPTWShipment) temp.getNext();
		}
		return foundShipment;
	}

	// The WhoAmI methods gives the id of the assigned object
	// It is a static method so that it can be accessed without creating an
	// object
	@Override
	public String toString() {
		return ("Selection Type: Furthest Distance from Depot");
	}
}

/**
 * This is a method that was inherited from the VRP in Zeus. This will 
 * rotate around the plane of the problem set and select the customer that is
 * found with the smallest polar angle from the depot
 * 
 * 
 * @author Dr. Sam R. Thangiah
 *
 */
class SmallestPolarAngleToDepot extends VRPTWShipmentLinkedList {
	/**
	 * This method is responsible for traversing a VRPTWShipmentLinkedList and 
	 * selecting the customer with the Smallest Polar Angle To Depot
	 *  and returning this VRPTWShipment to the calling function
	 * 
	 */
	public VRPTWShipment getSelectShipment(VRPTWDepotLinkedList currDepotLL,
			VRPTWDepot currDepot, VRPTWShipmentLinkedList currShipLL,
			VRPTWShipment currShip) {
		// currDepotLL is the depot linked list of the problem
		// currDepot is the depot under consideration
		// currShipLL is the set of avaialble shipments
		boolean isDiagnostic = false;
		// VRPTWShipment temp = (VRPTWShipment) getHead(); //point to the first
		// shipment
		VRPTWShipment temp = (VRPTWShipment) currShipLL.getHead().getNext(); // point
																			// to
																			// the
																			// first
																			// shipment

		VRPTWShipment foundShipment = null; // the shipment found with the
											// criteria
		double angle;
		double foundAngle = 360; // initial value
		// double distance;
		// double foundDistance = 200; //initial distance
		double depotX, depotY;
		int type = 2;

		// Get the X and Y coordinate of the depot
		depotX = currDepot.getXCoord();
		depotY = currDepot.getYCoord();
		
		long currentTimeInMillis = DateTimeUtils.currentTimeMillis();
		long lastCheckedTime;

		while (temp != currShipLL.getTail()) {

			if (isDiagnostic) {
				System.out.println("Temp is " + temp);
				System.out.println("Tail is " + getTail());
				System.out.print("Shipment " + temp.getIndex() + " ");

				if (((temp.getXCoord() - depotX) >= 0)
						&& ((temp.getYCoord() - depotX) >= 0)) {
					System.out.print("Quadrant I ");
				} else if (((temp.getXCoord() - depotX) <= 0)
						&& ((temp.getYCoord() - depotY) >= 0)) {
					System.out.print("Quadrant II ");
				} else if (((temp.getXCoord()) <= (0 - depotX))
						&& ((temp.getYCoord() - depotY) <= 0)) {
					System.out.print("Quadrant III ");
				} else if (((temp.getXCoord() - depotX) >= 0)
						&& ((temp.getYCoord() - depotY) <= 0)) {
					System.out.print("Quadrant VI ");
				} else {
					System.out.print("No Quadrant");
				}
			}

			// if the shipment is assigned, skip it
			lastCheckedTime = currentTimeInMillis - temp.getLastTimeChecked();
			if (temp.getIsAssigned() || lastCheckedTime < 1000) {
				temp = (VRPTWShipment) temp.getNext();

				continue;
			}

			angle = calcPolarAngle(depotX, depotX, temp.getXCoord(),
					temp.getYCoord());

			if (isDiagnostic) {
				System.out.println("  " + angle);
			}

			// check if this shipment should be tracked
			if (foundShipment == null) { // this is the first shipment being
											// checked
				foundShipment = temp;
				foundAngle = angle;
			} else {
				if (angle < foundAngle) { // found an angle that is less
					foundShipment = temp;
					foundAngle = angle;
				}
			}
			temp = (VRPTWShipment) temp.getNext();
		}
		return foundShipment; // stub
	}

	// The WhoAmI methods gives the id of the assigned object
	// It is a static method so that it can be accessed without creating an
	// object
	@Override
	public String toString() {
		return ("Selection Type: Smallest polar angle to the depot");
	}
}

