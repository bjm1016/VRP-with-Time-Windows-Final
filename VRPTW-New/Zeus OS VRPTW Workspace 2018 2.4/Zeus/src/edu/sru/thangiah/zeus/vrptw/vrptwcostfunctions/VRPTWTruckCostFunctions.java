package edu.sru.thangiah.zeus.vrptw.vrptwcostfunctions;


import edu.sru.thangiah.zeus.vrptw.VRPTWProblemInfo;
import edu.sru.thangiah.zeus.vrptw.VRPTWTruck;
import edu.sru.thangiah.zeus.vrptw.vrptwcostfunctions.VRPTWAbstractCostFunctions;

public class VRPTWTruckCostFunctions extends VRPTWAbstractCostFunctions implements
		java.io.Serializable {

	/**
	* Get Total Cost - This function will grab the current VRPTWTruck as 
	* a parameter and set the total Cost of the current VRPTWTruck and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	public double getTotalCost(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		setTotalCost(o);

		return truck.getAttributes().getTotalCost();
	}

	/**
	* Get Total Demand - This function will grab the current VRPTWTruck as 
	* a parameter and set the total Demand of the current VRPTWTruck and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	public float getTotalDemand(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		setTotalDemand(o);

		return (int) truck.getAttributes().getTotalDemand();
	}
	
	
	/**
	* Get Total Distance - This function will grab the current VRPTWTruck as 
	* a parameter and set the total Distance of the current VRPTWTruck and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	public double getTotalDistance(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		setTotalDistance(o);

		return truck.getAttributes().getTotalDistance();
	}

	/**
	* Get Total Travel Time - This function will grab the current VRPTWTruck as 
	* a parameter and set the total Travel Time of the current VRPTWTruck and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	public double getTotalTravelTime(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		setTotalTravelTime(o);

		return truck.getAttributes().getTotalTravelTime();
	}

	/**
	* Get Max Travel Time - This function will grab the current VRPTWTruck as 
	* a parameter and set the Max Travel Time of the current VRPTWTruck and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	public double getMaxTravelTime(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		setMaxTravelTime(o);

		return truck.getAttributes().getMaxTravelTime();
	}

	/**
	* Get Avg Travel Time - This function will grab the current VRPTWTruck as 
	* a parameter and set the Avg Travel Time of the current VRPTWTruck and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	public double getAvgTravelTime(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		setAvgTravelTime(o);

		return truck.getAttributes().getAvgTravelTime();
	}
	
	/**
	* Get Total Waiting Time - This function will grab the current VRPTWTruck as 
	* a parameter and set the total Waiting Time of the current VRPTWTruck and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	@Override
	public double getTotalWaitingTime(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		setTotalWaitingTime(o);

		return truck.getAttributes().getTotalWaitingTime();
	}
	
	/**
	* Get Total Service Time - This function will grab the current VRPTWTruck as 
	* a parameter and set the total Service Time of the current VRPTWTruck and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWDepot
	*/
	@Override
	public double getTotalServiceTime(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		setTotalServiceTime(o);

		return truck.getAttributes().getTotalServiceTime();
	}

	
	
	/**
	 * Set Total Cost - This function will reset the Total Cost of the VRPTWAttributes class
	 * and then for the VRPTWTruck it will call the Cost Function from the VRPTWNodesLinkedList Cost Functions 
	 * to get the Total Cost for the VRPTWNodesLinkedList
	 *
	 * @param o the Object representing the VRPTWTruck
	 */
	public void setTotalCost(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		truck.getAttributes()
				.setTotalCost(
						VRPTWProblemInfo.nodesLLLevelCostF.getTotalCost(truck
								.getMainNodes()));
	}

	/**
	 * Set Total Demand - This function will reset the Total Demand of the VRPTWAttributes class
	 * and then for the VRPTWTruck it will call the Cost Function from the VRPTWNodesLinkedList Cost Functions 
	 * to get the Total Demand for the VRPTWNodesLinkedList
	 *
	 * @param o the Object representing the VRPTWTruck
	 */
	public void setTotalDemand(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		truck.getAttributes().setTotalDemand(
				VRPTWProblemInfo.nodesLLLevelCostF.getTotalDemand(truck
						.getMainNodes()));
	}

	/**
	 * Set Total Distance - This function will reset the Total Distance of the VRPTWAttributes class
	 * and then for the VRPTWTruck it will call the Cost Function from the VRPTWNodesLinkedList Cost Functions 
	 * to get the Total Distance for the VRPTWNodesLinkedList
	 *
	 * @param o the Object representing the VRPTWTruck
	 */
	public void setTotalDistance(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		truck.getAttributes().setTotalDistance(
				VRPTWProblemInfo.nodesLLLevelCostF.getTotalDistance(truck
						.getMainNodes()));
	}

	/**
	 * Set Total Travel Time - This function will reset the Total Travel Time of the VRPTWAttributes class
	 * and then for the VRPTWTruck it will call the Cost Function from the VRPTWNodesLinkedList Cost Functions 
	 * to get the Total Travel Time for the VRPTWNodesLinkedList
	 *
	 * @param o the Object representing the VRPTWTruck
	 */
	public void setTotalTravelTime(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		truck.getAttributes().setTotalTravelTime(
				VRPTWProblemInfo.nodesLLLevelCostF.getTotalTravelTime(truck
						.getMainNodes()));
	}

	/**
	 * Set Total Waiting Time - This function will reset the Total Waiting Time of the VRPTWAttributes class
	 * and then for the VRPTWTruck it will call the Cost Function from the VRPTWNodesLinkedList Cost Functions 
	 * to get the Total Waiting Time for the VRPTWNodesLinkedList
	 *
	 * @param o the Object representing the VRPTWTruck
	 */
	@Override
	public void setTotalWaitingTime(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		truck.getAttributes().setTotalWaitingTime(
				(VRPTWProblemInfo.nodesLLLevelCostF).getTotalWaitingTime(truck
						.getMainNodes()));
	}

	/**
	 * Set Total Service Time - This function will reset the Total Service Time of the VRPTWAttributes class
	 * and then for the VRPTWTruck it will call the Cost Function from the VRPTWNodesLinkedList Cost Functions 
	 * to get the Total Service Time for the VRPTWNodesLinkedList
	 *
	 * @param o the Object representing the VRPTWTruck
	 */
	@Override
	public void setTotalServiceTime(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		truck.getAttributes().setTotalServiceTime(
				VRPTWProblemInfo.nodesLLLevelCostF.getTotalServiceTime(truck
						.getMainNodes()));
	}
	
	/**
	 * Set Max Travel Time - This function will reset the Max Travel Time of the VRPTWAttributes class
	 * and then for the VRPTWTruck it will call the Cost Function from the VRPTWNodesLinkedList Cost Functions 
	 * to get the Max Time for the VRPTWNodesLinkedList
	 *
	 * @param o the Object representing the VRPTWTruck
	 */
	public void setMaxTravelTime(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		truck.getAttributes().setMaxTravelTime(
				VRPTWProblemInfo.nodesLLLevelCostF.getMaxTravelTime(truck
						.getMainNodes()));
	}

	/**
	 * Set Avg Travel Time - This function will reset the Avg Travel Time of the VRPTWAttributes class
	 * and then for the VRPTWTruck it will call the Cost Function from the VRPTWNodesLinkedList Cost Functions 
	 * to get the Avg Travel Time for the VRPTWNodesLinkedList
	 *
	 * @param o the Object representing the VRPTWTruck
	 */
	public void setAvgTravelTime(Object o) {
		VRPTWTruck truck = (VRPTWTruck) o;
		truck.getAttributes().setAvgTravelTime(
				VRPTWProblemInfo.nodesLLLevelCostF.getAvgTravelTime(truck
						.getMainNodes()));
	}

	/**
	 * This function will call all of the nessecary Cost Functions for the VRPTWDepotLinkedList
	 * 
	 * @param o the Object representing the VRPTWDepot
	 * 
	 */
	public void calculateTotalsStats(Object o) {
		setTotalDemand(o);
		setTotalDistance(o);
		setTotalTravelTime(o);
		setMaxTravelTime(o);
		setAvgTravelTime(o);
		// setTotalCrossRoadPenaltyCost(o);
		// setTotalTurnAroundPenaltyCost(o);
		setTotalWaitingTime(o);
		setTotalCost(o);
		setTotalServiceTime(o);
		// setTotalConstraintCost(o);
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
