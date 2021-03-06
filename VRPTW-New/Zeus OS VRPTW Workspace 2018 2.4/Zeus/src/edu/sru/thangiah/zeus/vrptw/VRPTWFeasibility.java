package edu.sru.thangiah.zeus.vrptw;

import edu.sru.thangiah.zeus.core.Feasibility;
/**
 * The VRPTWFeasiblity class is responsible for 
 * verifying if the route being constructed is feasible based on the constraints 
 * set by the VRPTW
 * 
 * @author Marc S
 *
 */
public class VRPTWFeasibility extends Feasibility implements
		java.io.Serializable, java.lang.Cloneable {

	public VRPTWFeasibility() {
	}

	/**
	 * Constructor for the feasibilty, will send the constants as well as a
	 * pointer to the route that will be checked
	 * 
	 * @param maxd
	 *            max duration
	 * @param maxq
	 *            max capacity
	 * @param thisR
	 *            the route
	 */
	public VRPTWFeasibility(double maxd, float maxq, VRPTWNodesLinkedList thisR) {
		super(thisR);
		setMaxDuration(maxd);
		setMaxCapacity(maxq);
		// thisRoute = thisR;
	}

	/**
	 * This function checks the feasibility of the route.
	 * 
	 * @return true if feasible, false if not.
	 */
	public boolean isFeasible() {
		double currentDistance;
		double currentDemand;
		double currentTravelTime;
		currentDistance = VRPTWProblemInfo.nodesLLLevelCostF
				.getTotalDistance(getRoute());
		currentDemand = VRPTWProblemInfo.nodesLLLevelCostF
				.getTotalDemand(getRoute());
		currentTravelTime = VRPTWProblemInfo.nodesLLLevelCostF
				.getTotalTravelTime(getRoute());
		/*double currentWaitingTime = VRPTWProblemInfo.nodesLLLevelCostF
				.getTotalWaitingTime(thisRoute);*/

		// System.out.println("Current Distance ="+ currentDistance);
		// System.out.println("Current Demand ="+ currentDemand);
		
		boolean lateArrival = false;
		VRPTWNodes tempCell = (VRPTWNodes) getRoute().getHead().getNext();
		
		double tempD = 0;
		
		if(getRoute().getTail().getPrev().getShipment().getIndex() == 6){
			System.out.print("Please wait...");
		}

		while (tempCell != getRoute().getTail()) {
			if(getRoute().getTail().getPrev().getShipment().getIndex() == 58 && tempCell.getShipment().getIndex() == 11){
				System.out.print("Please wait...");
			}
			//FIXME: Marc is an idiot, he needs to make this a less than
			if (tempCell.getShipment().getTravelTime() > tempCell.getShipment()
					.getLateTime()) {
				lateArrival = true;
				//break;
			}

			tempCell = (VRPTWNodes) tempCell.getNext();
		}

		if ((currentTravelTime/* + currentWaitingTime*/ <= getMaxDuration())
				&& (currentDemand <= getMaxCapacity()) && (!lateArrival)) {
			return true;
		} else {
			return false;
		}
	}
	  
	  /**
	   * Overrides parent clone
	   * 
	   * @return copy of VRPTWFeasibility that called this
	   * @author Eric McAlpine
	   */
	public Object clone() {
		VRPTWFeasibility cloned = new VRPTWFeasibility();
		cloned.setMaxCapacity(this.getMaxCapacity());
		cloned.setMaxDuration(this.getMaxDuration());

		return cloned;
	}

}
