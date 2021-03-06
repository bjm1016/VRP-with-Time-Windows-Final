package edu.sru.thangiah.zeus.vrptw.vrptwcostfunctions;

import edu.sru.thangiah.zeus.core.Depot;
import edu.sru.thangiah.zeus.vrptw.VRPTWDepot;
import edu.sru.thangiah.zeus.vrptw.VRPTWDepotLinkedList;
import edu.sru.thangiah.zeus.vrptw.VRPTWProblemInfo;

public class VRPTWDepotCostFunctions extends VRPTWAbstractCostFunctions
		implements java.io.Serializable {
	
	/**
	* Total waiting time - This function will grab the current Depot as 
	* a parameter and set the total waiting time of the current Depot and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	public double getTotalWaitingTime(Object o){
		VRPTWDepot depotLL = (VRPTWDepot) o;
	    setTotalWaitingTime(o);

	    return depotLL.getAttributes().getTotalWaitingTime();
	}

	/**
	* Total Avg Travel time - This function will grab the current Depot as 
	* a parameter and set the Avg Travel time of the current Depot and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	@Override
	public double getAvgTravelTime(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		setAvgTravelTime(o);

		return depot.getAttributes().getAvgTravelTime();
	}

	/**
	* Total Max Travel time - This function will grab the current Depot as 
	* a parameter and set the Max Travel time of the current Depot and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	@Override
	public double getMaxTravelTime(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		setMaxTravelTime(o);

		return depot.getAttributes().getMaxTravelTime();
	}

	/**
	* Total Cost - This function will grab the current Depot as 
	* a parameter and set the Total Cost of the current Depot and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	@Override
	public double getTotalCost(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		setTotalCost(o);

		return depot.getAttributes().getTotalCost();
	}

	/**
	* Total Demand - This function will grab the current Depot as 
	* a parameter and set the Total Demand of the current Depot and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	@Override
	public float getTotalDemand(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		setTotalDemand(o);

		return (int) depot.getAttributes().getTotalDemand();
	}

	/**
	* Total Distance - This function will grab the current Depot as 
	* a parameter and set the Total Distance of the current Depot and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	@Override
	public double getTotalDistance(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		setTotalDistance(o);

		return depot.getAttributes().getTotalDistance();
	}

	/**
	* Total Travel Time - This function will grab the current Depot as 
	* a parameter and set the Total Travel Time of the current Depot and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	@Override
	public double getTotalTravelTime(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		setTotalTravelTime(o);

		return depot.getAttributes().getTotalTravelTime();
	}
	
	/**
	* Total Service Time - This function will grab the current Depot as 
	* a parameter and set the Total Service Time of the current Depot and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	public double getTotalServiceTime(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		setTotalServiceTime(o);

		return depot.getAttributes().getTotalServiceTime();
	}

	/**
	 * Set Avg Travel Time - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then for the VRPTWDepot it will call the Cost Function from the VRPTWTruckLinkedList Cost Functions 
	 * to get the Avg Travel Time for the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWDepot
	 */
	@Override
	public void setAvgTravelTime(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		depot.getAttributes().setAvgTravelTime(
				VRPTWProblemInfo.truckLLLevelCostF.getAvgTravelTime(depot
						.getMainTrucks()));
	}

	/**
	 * Set Max Travel Time - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then for the VRPTWDepot it will call the Cost Function from the VRPTWTruckLinkedList Cost Functions 
	 * to get the Max Travel Time for the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWDepot
	 */
	@Override
	public void setMaxTravelTime(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		depot.getAttributes().setMaxTravelTime(
				VRPTWProblemInfo.truckLLLevelCostF.getMaxTravelTime(depot
						.getMainTrucks()));
	}

	/**
	 * Set Total Cost - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then for the VRPTWDepot it will call the Cost Function from the VRPTWTruckLinkedList Cost Functions 
	 * to get the Total Cost for the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWDepot
	 */
	@Override
	public void setTotalCost(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		depot.getAttributes().setTotalCost(
				VRPTWProblemInfo.truckLLLevelCostF.getTotalCost(depot
						.getMainTrucks()));

	}

	/**
	 * Set Total Demand - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then for the VRPTWDepot it will call the Cost Function from the VRPTWTruckLinkedList Cost Functions 
	 * to get the Total Demand for the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWDepot
	 */
	@Override
	public void setTotalDemand(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		depot.getAttributes().setTotalDemand(
				(int) VRPTWProblemInfo.truckLLLevelCostF.getTotalDemand(depot
						.getMainTrucks()));

	}

	/**
	 * Set Total Distance - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then for the VRPTWDepot it will call the Cost Function from the VRPTWTruckLinkedList Cost Functions 
	 * to get the Total Distance for the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWDepot
	 */
	@Override
	public void setTotalDistance(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		depot.getAttributes().setTotalDistance(
				(float) VRPTWProblemInfo.truckLLLevelCostF.getTotalDistance(depot
						.getMainTrucks()));

	}

	/**
	 * Set Total Travel Time - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then for the VRPTWDepot it will call the Cost Function from the VRPTWTruckLinkedList Cost Functions 
	 * to get the Total Travel Time for the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWDepot
	 */
	@Override
	public void setTotalTravelTime(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
		depot.getAttributes().setTotalTravelTime(
				VRPTWProblemInfo.truckLLLevelCostF.getTotalTravelTime(depot
						.getMainTrucks()));
	}

	/**
	 * Set Total Waiting Time - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then for the VRPTWDepot it will call the Cost Function from the VRPTWTruckLinkedList Cost Functions 
	 * to get the Total Waiting Time for the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWDepot
	 */
	public void setTotalServiceTime(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;

		depot.getAttributes().setTotalServiceTime(VRPTWProblemInfo.truckLLLevelCostF.getTotalServiceTime(depot.getMainTrucks()));
	}
	
	/**
	 * Set Total Waiting Time - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then for the VRPTWDepot it will call the Cost Function from the VRPTWTruckLinkedList Cost Functions 
	 * to get the Total Waiting Time for the VRPTWTruckLinkedList
	 *
	 * @param o the Object representing the VRPTWDepot
	 */
	public void setTotalWaitingTime(Object o) {
		VRPTWDepot depot = (VRPTWDepot) o;
	     depot.getAttributes().setTotalWaitingTime(0);

	      depot.getAttributes().setTotalWaitingTime(
	    		  ((VRPTWTruckLLCostFunctions) VRPTWProblemInfo.truckLLLevelCostF).getTotalWaitingTime(
	    				  depot.getMainTrucks()));
		
	}
	
	/**
	 * This function will call all of the nessecary Cost Functions for the VRPTWDepotLinkedList
	 * 
	 * @param o the Object representing the VRPTWDepot
	 * 
	 */
	@Override
	public void calculateTotalsStats(Object o) {
		setTotalDemand(o);
		setTotalDistance(o);
		setTotalTravelTime(o);
		setMaxTravelTime(o);
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
