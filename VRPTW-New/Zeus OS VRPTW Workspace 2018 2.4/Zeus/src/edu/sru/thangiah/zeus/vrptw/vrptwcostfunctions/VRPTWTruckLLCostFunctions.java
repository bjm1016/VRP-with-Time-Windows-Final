package edu.sru.thangiah.zeus.vrptw.vrptwcostfunctions;

import edu.sru.thangiah.zeus.core.*;
import edu.sru.thangiah.zeus.vrptw.VRPTWDepot;
import edu.sru.thangiah.zeus.vrptw.VRPTWProblemInfo;
import edu.sru.thangiah.zeus.vrptw.VRPTWTruckLinkedList;

/**
 * Cost functions specific to VRPTW Truck Linked List level
 * <p>
 * Title: Zeus - A Unified Object Oriented Model for VRPTW's
 * </p>
 * <p>
 * Description: cost functions specific to VRPTW Truck Linked List level
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Sam R. Thangiah
 * @version 2.0
 */
public class VRPTWTruckLLCostFunctions extends VRPTWAbstractCostFunctions
		implements java.io.Serializable {

	/**
	* Total Cost - This function will grab the current VRPTWTruckLinkedList as 
	* a parameter and set the total Cost of the VRPTWTruckLinkedList and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWTruckLinkedList
	*/
	public double getTotalCost(Object o) {
		VRPTWTruckLinkedList tLL = (VRPTWTruckLinkedList) o;
		setTotalCost(o);

		return tLL.getAttributes().getTotalCost();
	}

	/**
	* Total Demand - This function will grab the current VRPTWTruckLinkedList as 
	* a parameter and set the total Demand of the VRPTWTruckLinkedList and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWTruckLinkedList
	*/
	public float getTotalDemand(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		setTotalDemand(o);

		return (int) truckLL.getAttributes().getTotalDemand();
	}

	/**
	* Total Distance - This function will grab the current VRPTWTruckLinkedList as 
	* a parameter and set the total Distance of the VRPTWTruckLinkedList and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWTruckLinkedList
	*/
	public double getTotalDistance(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		setTotalDistance(o);

		return truckLL.getAttributes().getTotalDistance();
	}

	/**
	* Total Travel Time - This function will grab the current VRPTWTruckLinkedList as 
	* a parameter and set the total Travel Time of the VRPTWTruckLinkedList and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWTruckLinkedList
	*/
	public double getTotalTravelTime(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		setTotalTravelTime(o);

		return truckLL.getAttributes().getTotalTravelTime();
	}

	/**
	* Max Travel Time - This function will grab the current VRPTWTruckLinkedList as 
	* a parameter and set the Max Travel Time of the VRPTWTruckLinkedList and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWTruckLinkedList
	*/
	public double getMaxTravelTime(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		setMaxTravelTime(o);

		return truckLL.getAttributes().getMaxTravelTime();
	}
	
	/**
	* Total Service Time - This function will grab the current VRPTWTruckLinkedList as 
	* a parameter and set the total Service Time of the VRPTWTruckLinkedList and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWTruckLinkedList
	*/
	public double getTotalServiceTime(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		setTotalServiceTime(o);

		return truckLL.getAttributes().getTotalServiceTime();
	}

	/**
	* Avg Travel Time - This function will grab the current VRPTWTruckLinkedList as 
	* a parameter and set the Avg Travel Time of the VRPTWTruckLinkedList and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWTruckLinkedList
	*/
	public double getAvgTravelTime(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		setAvgTravelTime(o);

		return truckLL.getAttributes().getAvgTravelTime();
	}
	
	/**
	* Total Waiting Time - This function will grab the current VRPTWTruckLinkedList as 
	* a parameter and set the Total Waiting Time of the VRPTWTruckLinkedList and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWTruckLinkedList
	*/
	@Override
	public double getTotalWaitingTime(Object o) {
		VRPTWTruckLinkedList tLL = (VRPTWTruckLinkedList) o;
		setTotalWaitingTime(o);

		return tLL.getAttributes().getTotalWaitingTime();
	}

	/**
	 * Set Total Cost - This function will reset the Total Cost of the VRPTWAttributes class
	 * and then traverse the VRPTWTruckLinkedList and call the Cost Function from the VRPTWTruck Cost Functions 
	 * to get the Total Cost of each VRPTWTruck on the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWTruckLinkedList
	 */
	public void setTotalCost(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		truckLL.getAttributes().setTotalCost(0);

		Truck t = truckLL.getHead();

		while (t != truckLL.getTail()) {
			if (!t.isEmptyMainNodes()) {
				truckLL.getAttributes().setTotalCost(
						truckLL.getAttributes().getTotalCost()
								+ VRPTWProblemInfo.truckLevelCostF.getTotalCost(t));
			}

			t = t.getNext();
		}
	}

	/**
	 * Set Total Demand - This function will reset the Total Demand of the VRPTWAttributes class
	 * and then traverse the VRPTWTruckLinkedList and call the Cost Function from the VRPTWTruck Cost Functions 
	 * to get the Total Demand of each VRPTWTruck on the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWTruckLinkedList
	 */
	public void setTotalDemand(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		truckLL.getAttributes().setTotalDemand(0);

		Truck t = truckLL.getHead();

		while (t != truckLL.getTail()) {
			if (!t.isEmptyMainNodes()) {
				truckLL.getAttributes()
						.setTotalDemand(
								truckLL.getAttributes().getTotalDemand()
										+ VRPTWProblemInfo.truckLevelCostF
												.getTotalDemand(t));
			}

			t = t.getNext();
		}
	}

	/**
	 * Set Total Distance - This function will reset the Total Distance of the VRPTWAttributes class
	 * and then traverse the VRPTWTruckLinkedList and call the Cost Function from the VRPTWTruck Cost Functions 
	 * to get the Total Distance of each VRPTWTruck on the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWTruckLinkedList
	 */
	public void setTotalDistance(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		truckLL.getAttributes().setTotalDistance(0);

		Truck t = truckLL.getHead();

		while (t != truckLL.getTail()) {
			if (!t.isEmptyMainNodes()) {
				truckLL.getAttributes().setTotalDistance(
						truckLL.getAttributes().getTotalDistance()
								+ VRPTWProblemInfo.truckLevelCostF
										.getTotalDistance(t));
			}

			t = t.getNext();
		}
	}

	/**
	 * Set Total Travel Time - This function will reset the Total Travel Time of the VRPTWAttributes class
	 * and then traverse the VRPTWTruckLinkedList and call the Cost Function from the VRPTWTruck Cost Functions 
	 * to get the Total Travel Time of each VRPTWTruck on the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWTruckLinkedList
	 */
	public void setTotalTravelTime(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		truckLL.getAttributes().setTotalTravelTime(0);

		Truck t = truckLL.getHead();

		while (t != truckLL.getTail()) {
			if (!t.isEmptyMainNodes()) {
				truckLL.getAttributes().setTotalTravelTime(
						truckLL.getAttributes().getTotalTravelTime()
								+ VRPTWProblemInfo.truckLevelCostF
										.getTotalTravelTime(t));
			}

			t = t.getNext();
		}
	}

	/**
	 * Set Total Service Time - This function will reset the Total Service Time of the VRPTWAttributes class
	 * and then traverse the VRPTWTruckLinkedList and call the Cost Function from the VRPTWTruck Cost Functions 
	 * to get the Total Service Time of each VRPTWTruck on the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWTruckLinkedList
	 */
	public void setTotalServiceTime(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		truckLL.getAttributes().setTotalServiceTime(0);
		
		Truck t = truckLL.getHead();
		
		while (t != truckLL.getTail()) {
			if (!t.isEmptyMainNodes()) {
				truckLL.getAttributes().setTotalServiceTime(
						truckLL.getAttributes().getTotalServiceTime()
								+ VRPTWProblemInfo.truckLevelCostF
										.getTotalServiceTime(t));
			}

			t = t.getNext();
		}
	}

	/**
	 * Set Max Travel Time - This function will reset the Max Travel Time of the VRPTWAttributes class
	 * and then traverse the VRPTWTruckLinkedList and call the Cost Function from the VRPTWTruck Cost Functions 
	 * to get the Max Travel Time of each VRPTWTruck on the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWTruckLinkedList
	 */
	public void setMaxTravelTime(Object o) {
		double max = 0;
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		Truck t = truckLL.getHead();

		while (t != truckLL.getTail()) {
			if (!t.isEmptyMainNodes()) {
				if (VRPTWProblemInfo.truckLevelCostF.getMaxTravelTime(t) > max) {
					max = VRPTWProblemInfo.truckLevelCostF.getMaxTravelTime(t);
				}
			}

			t = t.getNext();
		}

		truckLL.getAttributes().setMaxTravelTime(max);
	}

	/**
	 * Set Avg Travel Time - This function will reset the Avg Travel Time of the VRPTWAttributes class
	 * and then traverse the VRPTWTruckLinkedList and call the Cost Function from the VRPTWTruck Cost Functions 
	 * to get the Avg Travel Time of each VRPTWTruck on the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWTruckLinkedList
	 */
	public void setAvgTravelTime(Object o) {
		double avg = 0;
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		Truck t = truckLL.getHead();

		if ((truckLL.getSize() != 0)
				&& (VRPTWProblemInfo.truckLLLevelCostF.getTotalDemand(truckLL) != 0)) {
			while (t != truckLL.getTail()) {
				if (!t.isEmptyMainNodes()) {
					avg += (VRPTWProblemInfo.truckLevelCostF.getAvgTravelTime(t) * VRPTWProblemInfo.truckLevelCostF
							.getTotalDemand(t));
				}

				t = t.getNext();
			}

			truckLL.getAttributes()
					.setAvgTravelTime(
							avg
									/ VRPTWProblemInfo.truckLLLevelCostF
											.getTotalDemand(truckLL));
		} else {
			truckLL.getAttributes().setAvgTravelTime(0);
		}
	}
	
	/**
	 * Set Total Waiting Time - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then traverse the VRPTWTruckLinkedList and call the Cost Function from the VRPTWTruck Cost Functions 
	 * to get the Total Waiting Time of each VRPTWTruck on the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWTruckLinkedList
	 */
	@Override
	public void setTotalWaitingTime(Object o) {
		VRPTWTruckLinkedList truckLL = (VRPTWTruckLinkedList) o;
		truckLL.getAttributes().setTotalWaitingTime(0);

		Truck t = truckLL.getHead();

		while (t != truckLL.getTail()) {
			if (!t.isEmptyMainNodes()) {
				truckLL.getAttributes().setTotalWaitingTime(
						truckLL.getAttributes().getTotalWaitingTime()
								+ ((VRPTWTruckCostFunctions) VRPTWProblemInfo.truckLevelCostF)
										.getTotalWaitingTime(t));
			}

			t = t.getNext();
		}
	}

	/**
	 * This function will call all of the nessecary Cost Functions for the VRPTWDepotLinkedList
	 * 
	 * @param o the Object representing the VRPTWDepotLinkedList
	 * 
	 */
	public void calculateTotalsStats(Object o) {
		setTotalDemand(o);
		setTotalDistance(o);
		setTotalTravelTime(o);
		setMaxTravelTime(o);
		setAvgTravelTime(o);
		setTotalWaitingTime(o);
		setTotalCost(o);
		setTotalServiceTime(o);
	}

	@Override
	public int getTotalDays(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalStops(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTotalDays(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTotalStops(Object arg0) {
		// TODO Auto-generated method stub
		
	}

}
