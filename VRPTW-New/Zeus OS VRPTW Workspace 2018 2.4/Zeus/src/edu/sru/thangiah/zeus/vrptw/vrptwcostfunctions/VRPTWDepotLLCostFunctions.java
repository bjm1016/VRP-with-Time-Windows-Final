package edu.sru.thangiah.zeus.vrptw.vrptwcostfunctions;

import edu.sru.thangiah.zeus.core.Depot;
import edu.sru.thangiah.zeus.vrptw.VRPTWDepotLinkedList;
import edu.sru.thangiah.zeus.vrptw.VRPTWProblemInfo;

public class VRPTWDepotLLCostFunctions extends VRPTWAbstractCostFunctions
		implements java.io.Serializable {
	
	/**
	* Total Service time - This function will grab the current DepotLinkedList as 
	* a parameter and set the total service time of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepotLinkedList
	*/
	public double getTotalServiceTime(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		setTotalServiceTime(o);

		return depotLL.getAttributes().getTotalServiceTime();
	}
	
	/**
	* Total waiting time - This function will grab the current DepotLinkedList as 
	* a parameter and set the total waiting time of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepotLinkedList
	*/
	public double getTotalWaitingTime(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		setTotalWaitingTime(o);

		return depotLL.getAttributes().getTotalWaitingTime();
	}

	/**
	* Total Cost - This function will grab the current DepotLinkedList as 
	* a parameter and set the total cost of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepotLinkedList
	*/
	public double getTotalCost(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		setTotalCost(o);

		return depotLL.getAttributes().getTotalCost();
	}

	/**
	* Total Demand - This function will grab the current DepotLinkedList as 
	* a parameter and set the total Demand of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepotLinkedList
	*/
	public float getTotalDemand(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		setTotalDemand(o);

		return (int) depotLL.getAttributes().getTotalDemand();
	}
	
	/**
	* Total distance - This function will grab the current DepotLinkedList as 
	* a parameter and set the total distance of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepotLinkedList
	*/
	public double getTotalDistance(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		setTotalDistance(o);

		return depotLL.getAttributes().getTotalDistance();
	}

	/**
	* Total Travel Time - This function will grab the current DepotLinkedList as 
	* a parameter and set the total Travel Time of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepotLinkedList
	*/
	public double getTotalTravelTime(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		setTotalTravelTime(o);

		return depotLL.getAttributes().getTotalTravelTime();
	}
	
	/**
	* Max Travel Time - This function will grab the current DepotLinkedList as 
	* a parameter and set the Max Travel Time of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepotLinkedList
	*/
	public double getMaxTravelTime(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		setMaxTravelTime(o);

		return depotLL.getAttributes().getMaxTravelTime();
	}

	/**
	* Average Travel Time - This function will grab the current DepotLinkedList as 
	* a parameter and set the Average Travel Time of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepotLinkedList
	*/
	public double getAvgTravelTime(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		setAvgTravelTime(o);

		return depotLL.getAttributes().getAvgTravelTime();
	}
	
	/**
	 * Set Total Waiting Time - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then traverse the VRPTWDepotLinkedList and call the Cost Function from the VRPTWDepot Cost Functions 
	 * to get the Total Waiting Time of each VRPTWDepot on the VRPTWDepotLinkedList
	 *
	 * @param o the Object representing the VRPTWDepotLinkedList
	 */
	public void setTotalWaitingTime(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		depotLL.getAttributes().setTotalWaitingTime(0);

		Depot d = depotLL.getHead();

		while (d != depotLL.getTail()) {
			depotLL.getAttributes()
					.setTotalWaitingTime(
							depotLL.getAttributes().getTotalWaitingTime()
									+ ((VRPTWDepotCostFunctions) VRPTWProblemInfo.depotLevelCostF)
											.getTotalWaitingTime(d));
			d = d.getNext();
		}

	}

	/**
	 * Set Total Cost - This function will reset the Total Cost of the VRPTWAttributes class
	 * and then traverse the VRPTWDepotLinkedList and call the Cost Function from the VRPTWDepot Cost Functions 
	 * to get the Total Cost of each VRPTWDepot on the VRPTWDepotLinkedList
	 *
	 * @param o the Object representing the VRPTWDepotLinkedList
	 */
	public void setTotalCost(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		double cost = 0;

		Depot d = depotLL.getHead();

		while (d != depotLL.getTail()) {
			cost += VRPTWProblemInfo.depotLevelCostF.getTotalCost(d);
			d = d.getNext();
		}

		depotLL.getAttributes().setTotalCost(cost);
	}

	/**
	 * Set Total Demand - This function will reset the Total Demand of the VRPTWAttributes class
	 * and then traverse the VRPTWDepotLinkedList and call the Cost Function from the VRPTWDepot Cost Functions 
	 * to get the Total Demand of each VRPTWDepot on the VRPTWDepotLinkedList
	 *
	 * @param o the Object representing the VRPTWDepotLinkedList
	 */
	public void setTotalDemand(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		depotLL.getAttributes().setTotalDemand(0);

		Depot d = depotLL.getHead();

		while (d != depotLL.getTail()) {
			depotLL.getAttributes().setTotalDemand(
					depotLL.getAttributes().getTotalDemand()
							+ (float) VRPTWProblemInfo.depotLevelCostF
									.getTotalDemand(d));
			d = d.getNext();
		}
	}

	/**
	 * Set Total Distance - This function will reset the Total Distance of the VRPTWAttributes class
	 * and then traverse the VRPTWDepotLinkedList and call the Cost Function from the VRPTWDepot Cost Functions 
	 * to get the Total Distance of each VRPTWDepot on the VRPTWDepotLinkedList
	 *
	 * @param o the Object representing the VRPTWDepotLinkedList
	 */
	public void setTotalDistance(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		depotLL.getAttributes().setTotalDistance(0);

		Depot d = depotLL.getHead();

		while (d != depotLL.getTail()) {
			depotLL.getAttributes().setTotalDistance(
					depotLL.getAttributes().getTotalDistance()
							+ (float) VRPTWProblemInfo.depotLevelCostF
									.getTotalDistance(d));
			d = d.getNext();
		}
	}

	/**
	 * Set Total Travel Time - This function will reset the Total Travel Time of the VRPTWAttributes class
	 * and then traverse the VRPTWDepotLinkedList and call the Cost Function from the VRPTWDepot Cost Functions 
	 * to get the Total Travel Time of each VRPTWDepot on the VRPTWDepotLinkedList
	 *
	 * @param o the Object representing the VRPTWDepotLinkedList
	 */
	public void setTotalTravelTime(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		depotLL.getAttributes().setTotalTravelTime(0);

		Depot d = depotLL.getHead();

		while (d != depotLL.getTail()) {
			depotLL.getAttributes().setTotalTravelTime(
					depotLL.getAttributes().getTotalTravelTime()
							+ VRPTWProblemInfo.depotLevelCostF
									.getTotalTravelTime(d));
			d = d.getNext();
		}
	}

	/**
	 * Set Max Travel Time - This function will reset the Max Travel Time of the VRPTWAttributes class
	 * and then traverse the VRPTWDepotLinkedList and call the Cost Function from the VRPTWDepot Cost Functions 
	 * to get the Max Travel Time of each VRPTWDepot on the VRPTWDepotLinkedList
	 *
	 * @param o the Object representing the VRPTWDepotLinkedList
	 */
	public void setMaxTravelTime(Object o) {
		double max = 0;
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		Depot d = depotLL.getHead();

		while (d != depotLL.getTail()) {
			if (VRPTWProblemInfo.depotLevelCostF.getMaxTravelTime(d) > max) {
				max = VRPTWProblemInfo.depotLevelCostF.getMaxTravelTime(d);
			}

			d = d.getNext();
		}

		depotLL.getAttributes().setMaxTravelTime(max);
	}

	/**
	 * Set Avg Travel Time - This function will reset the Avg Travel Time of the VRPTWAttributes class
	 * and then traverse the VRPTWDepotLinkedList and call the Cost Function from the VRPTWDepot Cost Functions 
	 * to get the Avg Travel Time of each VRPTWDepot on the VRPTWDepotLinkedList
	 *
	 * @param o the Object representing the VRPTWDepotLinkedList
	 */
	public void setAvgTravelTime(Object o) {
		double avg = 0;
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		Depot d = depotLL.getHead();

		if (VRPTWProblemInfo.depotLLLevelCostF.getTotalDemand(depotLL) != 0) {
			while (d != depotLL.getTail()) {
				avg += (VRPTWProblemInfo.depotLevelCostF.getAvgTravelTime(d) * VRPTWProblemInfo.depotLevelCostF
						.getTotalDemand(d));
				d = d.getNext();
			}

			depotLL.getAttributes().setAvgTravelTime(
					avg
							/ VRPTWProblemInfo.depotLLLevelCostF
									.getTotalDemand(depotLL));
		} else {
			depotLL.getAttributes().setAvgTravelTime(0);
		}
	}

	/**
	 * Set Total Service Time - This function will reset the Total Service Time of the VRPTWAttributes class
	 * and then traverse the VRPTWDepotLinkedList and call the Cost Function from the VRPTWDepot Cost Functions 
	 * to get the Total Service Time of each VRPTWDepot on the VRPTWDepotLinkedList
	 *
	 * @param o the Object representing the VRPTWDepotLinkedList
	 */
	public void setTotalServiceTime(Object o) {
		VRPTWDepotLinkedList depotLL = (VRPTWDepotLinkedList) o;
		double serviceTime = 0;
		
		Depot d = depotLL.getHead();

		while (d != depotLL.getTail()) {
			serviceTime += VRPTWProblemInfo.depotLevelCostF
					.getTotalServiceTime(d);
			d = d.getNext();
		}

		depotLL.getAttributes().setTotalServiceTime(serviceTime);
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
