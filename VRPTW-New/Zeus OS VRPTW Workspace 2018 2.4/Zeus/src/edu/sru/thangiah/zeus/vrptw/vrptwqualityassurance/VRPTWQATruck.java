package edu.sru.thangiah.zeus.vrptw.vrptwqualityassurance;

import edu.sru.thangiah.zeus.core.*;
import edu.sru.thangiah.zeus.qualityassurance.*;
import edu.sru.thangiah.zeus.vrptw.VRPTWAttributes;
import edu.sru.thangiah.zeus.vrptw.VRPTWNodesLinkedList;
import edu.sru.thangiah.zeus.vrptw.VRPTWTruck;
import edu.sru.thangiah.zeus.vrptw.vrptwcostfunctions.*;

import java.math.BigDecimal;

/**
 *
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Sam R. Thangiah edit Aaron Weckerly
 * @version 2.0
 */
public class VRPTWQATruck extends QATruck implements java.io.Serializable,
		java.lang.Cloneable {
	private double waitTime;

	public VRPTWQATruck() {
		waitTime = 0.0;
	}

	/**
	 * Sets the wait time for the qatruck
	 * 
	 * @param wt
	 *            - double wait time to set
	 */
	public void setWaitTime(double wt) {
		if (wt >= 0)
			waitTime = wt;
		else
			System.out.println("QATruck wait time not set, negative");
	}

	/**
	 * retrieve the wait time for the QA truck
	 * 
	 * @param truck
	 *            - VRPTWQATruck supplied to retrieve wait time from
	 * @return - double wait time of qatruck
	 */
	private double getWaitTime(VRPTWQATruck truck) {
		return this.waitTime;
	}

	/**
	 * Verify the distance constraint for the supplied truck by iterating
	 * through nodes
	 * 
	 * @param truck
	 *            - VRPTWWATruck supplied to check
	 * @return boolean pass/fail
	 */
	public boolean checkDistanceConstraint(VRPTWQATruck truck) {
		boolean isDiagnostic = true;
		boolean status = true;
		double totalDistance = 0;
		double distance = 0;
		VRPTWQANode node1;
		VRPTWQANode node2;

		// Used in the comparison of distances
		int intDistance1;
		int intDistance2;

		if (isDiagnostic) {
			System.out.println();
			System.out.println("Truck being evaluated is " + truck.getIndex()
					+ " with " + getNodes().size() + " nodes");
			System.out.println("Sequence of the route is:");
			for (int i = 0; i < getNodes().size(); i++) {
				node1 = (VRPTWQANode) getNodes().elementAt(i);
				System.out.print(" " + node1.getIndex());
			}
			System.out.println();
		}

		if (getNodes().size() == 0) {
			return status;
		}

		// compute the distance from the depot to the first node
		node1 = (VRPTWQANode) getNodes().elementAt(0);
		// compute the total travel distance of the nodes in the route
		for (int i = 1; i < getNodes().size(); i++) {
			node2 = (VRPTWQANode) getNodes().elementAt(i);
			// changed cast from float to double
			// sqrt arg must be double, hence the cast
			float x1, x2, y1, y2;
			x1 = node1.getX();
			x2 = node2.getX();
			y1 = node1.getY();
			y2 = node2.getY();

			distance = (float) Math.sqrt(((x1 - x2) * (x1 - x2))
					+ ((y1 - y2) * (y1 - y2)));

			/*
			 * distance = (double) Math.sqrt( (double) (node1.getX() -
			 * node2.getX()) (node1.getX() - node2.getX()) + ( (node1.getY() -
			 * node2.getY()) (node1.getY() - node2.getY())));
			 */
			totalDistance += distance;
			if (isDiagnostic) {
				System.out.println("    Distance from " + node1.getIndex()
						+ " to " + node2.getIndex() + " = " + distance);
			}
			node1 = node2;
		}
		intDistance1 = (int) (truck.getDistance() * 100);
		intDistance2 = (int) (totalDistance * 100);

		if (isDiagnostic) {
			System.out.println("   Truck distance " + truck.getDistance()
					+ " : Computed distance " + totalDistance);

		}

		int errorCheck = Math.abs(intDistance2 - intDistance1);
		// Check if the computed distances are the same
		// if (intDistance1 != intDistance2) {
		if (errorCheck > 1) {
			Settings.printDebug(Settings.ERROR,
					"Truck # " + truck.getIndex()
							+ " distance does not match computed distance "
							+ truck.getDistance() + " " + totalDistance);
			status = false;
			return status;
		}
		return status;
	}

	/**
	 * check the capacity constraint of the VRPTWQATruck by iterating through
	 * the nodes
	 * 
	 * @param truck
	 *            - VRPTWQATruck to verify
	 * @return boolean pass/fail
	 */
	public boolean checkCapacityConstraint(VRPTWQATruck truck) {
		boolean isDiagnostic = true;
		boolean status = true;
		double totalCapacity = 0;
		double capacity = 0;
		VRPTWQANode node;
		int intCap1, intCap2;

		if (isDiagnostic) {
			System.out.println();
			System.out.println("Truck being evaluated is " + truck.getIndex()
					+ " with " + getNodes().size() + " nodes");
			System.out.println("Sequence of the route is:");
			for (int i = 0; i < getNodes().size(); i++) {
				node = (VRPTWQANode) getNodes().elementAt(i);
				System.out.print(" " + node.getIndex());
			}
			System.out.println();
		}

		if (getNodes().size() == 0) {
			return status;
		}
		// compute the total capacity of the route
		for (int i = 1; i < getNodes().size(); i++) {
			node = (VRPTWQANode) getNodes().elementAt(i);
			capacity = node.getDemand();
			if (isDiagnostic) {
				System.out.println("    Capacity " + node.getIndex() + " is "
						+ +capacity);
			}
			totalCapacity += capacity;
		}

		// Convert the capacity to integer values for comparison. The convertion
		// takes up to a precison of 3 decimal places. Comparing floats can lead
		// to inaccurate results as errors start occuring after the 4th decimal
		// place
		intCap1 = (int) (truck.getDemand() * 1000);
		intCap2 = (int) (totalCapacity * 1000);

		if (isDiagnostic) {
			System.out.println("   Truck capacity " + truck.getDemand()
					+ " : Computed capacity " + totalCapacity);
		}
		// Check if the truck capacity is the same as the computed capacity
		if (intCap1 != intCap2) { // check up to 3 decimal placess
			Settings.printDebug(Settings.ERROR,
					"Truck # " + truck.getIndex()
							+ " capacity does not match computed capacity "
							+ truck.getDemand() + " " + totalCapacity);
			status = false;
			return status;
		}
		// check if it exceeds the maximum capacity
		if (intCap2 > intCap1) {
			// if (totalCapacity > truck.getMaxDemand()) {
			Settings.printDebug(Settings.ERROR, "Truck # " + truck.getIndex()
					+ "distance exceeds maximum capacity " + totalCapacity
					+ " " + truck.getMaxDemand());
			status = false;
			return status;
		}
		return status;
	}

	/**
	 * retrieve string info about VRPTWQATruck
	 * 
	 * @return string of info
	 */
	public String getString() {
		String str = "";
		str = str + "Index: " + this.getIndex();
		return str;
	}

	/**
	 * calculate and verify the early and late times of the VRPTWQATruck
	 * 
	 * @param truck
	 *            - supplied VRPTWQATruck to verify
	 * @return boolean pass/fail
	 */
	public boolean checkEarlyLateTimes(VRPTWQATruck truck) {
		boolean isDiagnostic = true;
		boolean status = true;
		double currentTime = 0.0; // the current time so far
		double distance = 0.0; // total distance traveled
		double waitTime = 0.0; // total time waiting
		double previousTime = 0.0;

		VRPTWQANode node1;
		VRPTWQANode node2;

		if (isDiagnostic) {
			System.out.println();
			System.out.println("Truck being evaluated is " + truck.getIndex()
					+ " with " + truck.getNodes().size() + " nodes");
			System.out.println("Sequence of the route is:");
			for (int i = 0; i < truck.getNodes().size(); i++) {
				node1 = (VRPTWQANode) truck.getNodes().elementAt(i);
				System.out.print(" " + node1.getIndex());
			}
			System.out.println();

			int i = 1;
			// compute the distance from the depot to the first node
			node1 = (VRPTWQANode) truck.getNodes().elementAt(i - 1);
			node2 = (VRPTWQANode) truck.getNodes().elementAt((i));
			// compute the total travel distance of the nodes in the route
			System.out.println("Size of Cust List: " + truck.getNodes().size());
			while (node2 != truck.getNodes().elementAt(
					truck.getNodes().size() - 2)) {// for (int i = 0; i <
													// (truck.getNodes().size()-1);
													// i++) {
				node1 = (VRPTWQANode) truck.getNodes().elementAt(i - 1);
				node2 = (VRPTWQANode) truck.getNodes().elementAt((i));

				// changed cast from float to double
				// sqrt arg must be double, hence the cast
				double x1, x2, y1, y2;
				x1 = node1.getX();
				x2 = node2.getX();
				y1 = node1.getY();
				y2 = node2.getY();

				try {
					distance = (double) Math.sqrt(((x1 - x2) * (x1 - x2))
							+ ((y1 - y2) * (y1 - y2)));
				} catch (ArithmeticException e) {
					System.out
							.println("Arithmetic Exception in calculating distance in QAEarlyLate "
									+ e);
				}

				// currentTime+=distance; //add travel distance to var
				// //check to see if the truck is arriving early
				// if(node2.getEarly() > currentTime){
				// waitTime = node2.getEarly()-currentTime;
				// currentTime = currentTime+waitTime;
				// }

				currentTime = distance; // add travel distance to var

				currentTime += node1.getServiceTime();

				currentTime = distance + node1.getServiceTime() + previousTime;// +=
																				// previousTime;
				// check to see if the truck is arriving early
				if (node2.getEarly() > currentTime) {
					waitTime = node2.getEarly() - currentTime;
					currentTime = currentTime + waitTime;
				}

				previousTime = currentTime;

				if (node2.getEarly() > currentTime) {
					System.out
							.println("Error Node serviced before Early Time ");
					System.out.println("startTime " + currentTime
							+ "EarlyTime " + node2.getEarly());
					status = false;
				}
				if (node2.getEarly() <= currentTime) {
					// System.out.println("Node within Early tolerance");
					// System.out.println("startTime "+startTime+"EarlyTime "+node2.getEarly());
				}
				if (node2.getLate() < currentTime) {
					System.out.println("Error Node " + node2.getIndex()
							+ " serviced after late time");
					System.out.println("endTime " + currentTime
							+ " Late Time: " + node2.getLate());
					status = false;
				}
				if (node2.getLate() <= currentTime) {
					// System.out.println("Node within Late tolerance");
					// System.out.println("endTime "+endTime+"EarlyTime "+node2.getLate());
				}
				currentTime += node2.getDuration();
				++i;
			}// for all nodes
			return status;
		}

		if (truck.getNodes().size() == 0) {
			return status;
		}

		return status;

	}

	/**
	 * calculate and verify the wait time of the supplied truck
	 * 
	 * @param truck
	 *            - supplied VRPTWQATruck to verify
	 * @return boolean pass/fail
	 */
	public boolean checkWaitTime(VRPTWQATruck truck) {

		boolean isDiagnostic = true;
		boolean status = true;
		double currentTime = 0.0; // the current time so far
		double distance = 0.0; // total distance traveled
		double waitTime = 0.0; // total time waiting
		double totalWaitTime = 0.0;
		double previousTime = 0.0;

		VRPTWQANode node1;
		VRPTWQANode node2;

		if (isDiagnostic) {
			System.out.println();
			System.out.println("Truck being evaluated is " + truck.getIndex()
					+ " with " + truck.getNodes().size() + " nodes");
			System.out.println("Sequence of the route is:");
			for (int i = 0; i < truck.getNodes().size(); i++) {
				node1 = (VRPTWQANode) truck.getNodes().elementAt(i);
				System.out.print(" " + node1.getIndex());
			}
			System.out.println();

			int i = 1;
			// compute the distance from the depot to the first node
			node1 = (VRPTWQANode) truck.getNodes().elementAt(i - 1);
			node2 = (VRPTWQANode) truck.getNodes().elementAt((i));
			// compute the total travel distance of the nodes in the route
			System.out.println("Size of Cust List: " + truck.getNodes().size());
			while (node2 != truck.getNodes().elementAt(
					truck.getNodes().size() - 1)) {// for (int i = 0; i <
													// (truck.getNodes().size()-1);
													// i++) {
				node1 = (VRPTWQANode) truck.getNodes().elementAt(i - 1);
				node2 = (VRPTWQANode) truck.getNodes().elementAt((i));

				// changed cast from float to double
				// sqrt arg must be double, hence the cast
				double x1, x2, y1, y2;
				x1 = node1.getX();
				x2 = node2.getX();
				y1 = node1.getY();
				y2 = node2.getY();

				try {
					distance = (double) Math.sqrt(((x1 - x2) * (x1 - x2))
							+ ((y1 - y2) * (y1 - y2)));
				} catch (ArithmeticException e) {
					System.out
							.println("Arithmetic Exception in calculating distance in QAWaitCheck "
									+ e);
				}

				currentTime = distance; // add travel distance to var

				currentTime += node1.getServiceTime();

				currentTime = distance + node1.getServiceTime() + previousTime;// +=
																				// previousTime;
				// check to see if the truck is arriving early
				if (node2.getEarly() > currentTime) {
					waitTime = node2.getEarly() - currentTime;
					totalWaitTime += waitTime;
					currentTime = currentTime + waitTime;
				}

				previousTime = currentTime;
				++i;
				// int errorCheck = Math.abs((int)truck.getWaitTime(truck) -
				// (int)totalWaitTime);
				// if(errorCheck > 1){
				// Settings.printDebug(Settings.ERROR,
				// "Truck # " + truck.getIndex() +
				// " WaitTime does not match computed waitTime " +
				// truck.getWaitTime(truck) + " " + totalWaitTime);
				// status = false;
				// return status;
				// }
				// currentTime += node2.getDuration();
			}// for all nodes

			int errorCheck = Math.abs((int) truck.getWaitTime(truck)
					- (int) totalWaitTime);
			if (errorCheck > 1) {
				Settings.printDebug(Settings.ERROR,
						"Truck # " + truck.getIndex()
								+ " WaitTime does not match computed waitTime "
								+ truck.getWaitTime(truck) + " "
								+ totalWaitTime);
				status = false;
				return status;
			}
			return status;
		}

		if (truck.getNodes().size() == 0) {
			return status;
		}

		return status;
	}// checkWaitTime
}
