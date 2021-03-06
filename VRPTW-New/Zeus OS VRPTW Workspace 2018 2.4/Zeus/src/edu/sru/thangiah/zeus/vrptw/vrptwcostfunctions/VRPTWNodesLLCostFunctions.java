package edu.sru.thangiah.zeus.vrptw.vrptwcostfunctions;


import edu.sru.thangiah.zeus.vrptw.VRPTWNodes;
import edu.sru.thangiah.zeus.vrptw.VRPTWNodesLinkedList;
import edu.sru.thangiah.zeus.vrptw.VRPTWProblemInfo;

public class VRPTWNodesLLCostFunctions extends VRPTWAbstractCostFunctions
		implements java.io.Serializable {

	/**
	* Get Total Cost - This function will grab the current VRPTWNodesLinkedList as 
	* a parameter and set the total Cost of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWNodesLinkedList
	*/
	public double getTotalCost(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		setTotalCost(o);

		return vNodes.getAttributes().getTotalCost();
	}

	/**
	* Get Total Demand - This function will grab the current VRPTWNodesLinkedList as 
	* a parameter and set the total Demand of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWNodesLinkedList
	*/
	public float getTotalDemand(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		setTotalDemand(o);

		return (int) vNodes.getAttributes().getTotalDemand();
	}

	/**
	* Get Total Distance - This function will grab the current VRPTWNodesLinkedList as 
	* a parameter and set the total Distance of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWNodesLinkedList
	*/
	public double getTotalDistance(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		setTotalDistance(o);

		return vNodes.getAttributes().getTotalDistance();
	}

	/**
	* Get Total Travel Time - This function will grab the current VRPTWNodesLinkedList as 
	* a parameter and set the total Travel Time of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWNodesLinkedList
	*/
	public double getTotalTravelTime(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		setTotalTravelTime(o);

		return vNodes.getAttributes().getTotalTravelTime();
	}

	/**
	* Get Total Demand- This function will grab the current VRPTWNodesLinkedList as 
	* a parameter and set the Total Demandof the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWNodesLinkedList
	*/
	public double getTotalWaitingTime(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		setTotalWaitingTime(o);

		return vNodes.getAttributes().getTotalWaitingTime();
	}

	/**
	* Get Total Service Time - This function will grab the current VRPTWNodesLinkedList as 
	* a parameter and set the total Service Time of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWNodesLinkedList
	*/
	public double getTotalServiceTime(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		setTotalServiceTime(o);

		return vNodes.getAttributes().getTotalServiceTime();
	}
	
	/**
	* Get Max Travel Time - This function will grab the current VRPTWNodesLinkedList as 
	* a parameter and set the Max Travel Time of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWNodesLinkedList
	*/
	public double getMaxTravelTime(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		setMaxTravelTime(o);

		return vNodes.getAttributes().getMaxTravelTime();
	}

	/**
	* Get Avg Travel Time - This function will grab the current VRPTWNodesLinkedList as 
	* a parameter and set the Avg Travel Time of the current route and then return 
	* this as an attribute from the VRPTWAttributes class
	* 
	* @param o the Object representing the VRPTWNodesLinkedList
	*/
	public double getAvgTravelTime(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		setAvgTravelTime(o);

		return vNodes.getAttributes().getAvgTravelTime();
	}
	
	public void setDeliveryTime(Object o){
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		
		double earlyArrivalTime = 0,
			distance = 0, waitingTime = 0, 
			previousDeliveryTime = 0, previousDuration = 0,
			currentDeliveryTime = 0, x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		
		VRPTWNodes nodeOne, nodeTwo;
		
		nodeOne = (VRPTWNodes) vNodes.getHead().getNext(); //Grab the first delieverd shipment
		nodeTwo = (VRPTWNodes) nodeOne.getNext(); //Grab the second shipment in the list
		
		while(nodeTwo != vNodes.getTail()){
			//Grab X and Y coordinates of the previous shipment and current shipment
			//These are used to calculate Euclidian distance
			x1 = nodeOne.getPrev().getShipment().getXCoord();
			x2 = nodeOne.getShipment().getXCoord();
			y1 = nodeOne.getPrev().getShipment().getXCoord();
			y2 = nodeOne.getShipment().getXCoord();
			
			distance = Math.sqrt(((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2)));
			
			previousDuration = nodeOne.getPrev().getShipment().getServiceTime();
			
			earlyArrivalTime = nodeOne.getShipment().getEarlyTime();
			
			currentDeliveryTime = distance + previousDuration + previousDeliveryTime;
			
			if(earlyArrivalTime >  currentDeliveryTime){
				currentDeliveryTime = earlyArrivalTime;
			}
			
			previousDeliveryTime = currentDeliveryTime;
			
			nodeOne = nodeTwo;
			
			nodeTwo = (VRPTWNodes) nodeOne.getNext();
			
		}
	}

	
	/**
	 * Set Total Cost - This function will reset the Total Cost of the VRPTWAttributes class
	 * and then traverse the VRPTWNodesLinkedList and calculate the total cost which is a summation of the 
	 * Distance, travel time, and waiting time.
	 *
	 * @param o the Object representing the VRPTWNodesLinkedList
	 */
	public void setTotalCost(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		double cost = 0;
		double dist = getTotalDistance(o);
		double travelTime = getTotalTravelTime(o);
		double waitingTime = getTotalWaitingTime(o);
		cost = dist + travelTime + waitingTime;

		vNodes.getAttributes().setTotalCost(cost);
	}

	/**
	 * Set Total Demand- This function will reset the Total Demandof the VRPTWAttributes class
	 * and then traverse the VRPTWNodesLinkedList and calculate the total Waiting Time. This is the difference between
	 * the delivery time and the service time of a node if it were to arrive early at the customer
	 *
	 * @param o the Object representing the VRPTWNodesLinkedList
	 */
	public void setTotalWaitingTime(Object o) {
		/*VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;

		VRPTWNodes tempCell = (VRPTWNodes) vNodes.getHead().getNext();
		double tempD = 0;

		while (tempCell != vNodes.getTail()) {
			//FIXME: Marc is an idiot, he needs to make this a less than
			if (tempCell.getShipment().getTravelTime() < tempCell.getShipment()
					.getEarlyTime()) {
				tempD += tempCell.getShipment().getEarlyTime() - 
						tempCell.getShipment().getTravelTime();
						
			}

			tempCell = (VRPTWNodes) tempCell.getNext();
		}*/
VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		
		double earlyArrivalTime = 0,
			distance = 0, waitingTime = 0, 
			previousDeliveryTime = 0, previousDuration = 0,
			currentDeliveryTime = 0, x1 = 0, x2 = 0, y1 = 0, y2 = 0,
			totalWaitingTime = 0;
		
		VRPTWNodes nodeOne, nodeTwo;
		
		nodeOne = (VRPTWNodes) vNodes.getHead().getNext(); //Grab the first delieverd shipment
		nodeTwo = (VRPTWNodes) nodeOne.getNext(); //Grab the second shipment in the list
		

		while(nodeOne != vNodes.getTail()){
			//Grab X and Y coordinates of the previous shipment and current shipment
			//These are used to calculate Euclidian distance
			x1 = nodeOne.getPrev().getShipment().getXCoord();
			x2 = nodeOne.getShipment().getXCoord();
			y1 = nodeOne.getPrev().getShipment().getYCoord();
			y2 = nodeOne.getShipment().getYCoord();
			
			distance = Math.sqrt(((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2)));
			
			previousDuration = nodeOne.getPrev().getShipment().getServiceTime();
			
			earlyArrivalTime = nodeOne.getShipment().getEarlyTime();
			
			currentDeliveryTime = distance + previousDuration + previousDeliveryTime;
			
			if(earlyArrivalTime >  currentDeliveryTime){
				waitingTime = earlyArrivalTime - currentDeliveryTime;
				totalWaitingTime += waitingTime;
				currentDeliveryTime = currentDeliveryTime + waitingTime;//earlyArrivalTime;
			}
			
			previousDeliveryTime = currentDeliveryTime;
			
			nodeOne = nodeTwo;
			
			nodeTwo = (VRPTWNodes) nodeOne.getNext();
			
		}

		vNodes.getAttributes().setTotalWaitingTime(totalWaitingTime);
	}

	/**
	 * Set Total Demand- This function will reset the Total Demand of the VRPTWAttributes class
	 * and then traverse the VRPTWNodesLinkedList and calculate the total Waiting Time. This is summation
	 * of the Demand of each node 
	 * 
	 * @param o the Object representing the VRPTWNodesLinkedList
	 */
	public void setTotalDemand(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;

		VRPTWNodes tempCell = (VRPTWNodes) vNodes.getHead();
		int tempD = 0;

		while (tempCell != vNodes.getTail()) {
			tempD += tempCell.getShipment().getDemand();
			tempCell = (VRPTWNodes) tempCell.getNext();
		}

		vNodes.getAttributes().setTotalDemand(tempD);
	}

	/**
	 * Set Total Distance- This function will reset the Total Distance of the VRPTWAttributes class
	 * and then traverse the VRPTWNodesLinkedList and calculate the total Waiting Time. This is summation
	 * of the Distance between each node calculated as the euclidian distance
	 * 
	 * @param o the Object representing the VRPTWNodesLinkedList
	 */
	public void setTotalDistance(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		float distTravelled = 0;
		VRPTWNodes left = vNodes.getHead();
		VRPTWNodes right = (VRPTWNodes) vNodes.getHead().getNext(); // node after head

		while (right != vNodes.getTail()) { // head){

			try {
				distTravelled += (float) Math.sqrt((double) (((right
						.getShipment().getXCoord() - left.getShipment()
						.getXCoord()) * (right.getShipment().getXCoord() - left
						.getShipment().getXCoord())) + ((right.getShipment()
						.getYCoord() - left.getShipment().getYCoord()) * (right
						.getShipment().getYCoord() - left.getShipment()
						.getYCoord()))));
			} catch (ArithmeticException e) {
				System.out.println(e);
			}

			// System.out.println(distTravelled);
			left = right;
			right = (VRPTWNodes) right.getNext();
		}

		distTravelled += (float) Math
				.sqrt((double) (((right.getShipment().getXCoord() - left
						.getShipment().getXCoord()) * (right.getShipment()
						.getXCoord() - left.getShipment().getXCoord())) + ((right
						.getShipment().getYCoord() - left.getShipment()
						.getYCoord()) * (right.getShipment().getYCoord() - left
						.getShipment().getYCoord()))));

		vNodes.getAttributes().setTotalDistance(distTravelled);
	}

	/**
	 * Set Total Service Time- This function will reset the Total Service Time of the VRPTWAttributes class
	 * and then traverse the VRPTWNodesLinkedList and calculate the total Waiting Time. This is summation
	 * of the Service Time of each node plus the total distance of the route
	 * 
	 * @param o the Object representing the VRPTWNodesLinkedList
	 */
	public void setTotalServiceTime(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		double dist = getTotalDistance(o);

		double serviceTime = 0.0;

		VRPTWNodes theCell = (VRPTWNodes) vNodes.getHead().getNext(); // start at the first
		// customer

		while (theCell != vNodes.getTail()) {
			serviceTime += theCell.getShipment().getServiceTime();

			theCell = (VRPTWNodes) theCell.getNext();
		}
		
		vNodes.getAttributes().setTotalServiceTime(serviceTime + dist);

	}

	/**
	 * Set Total Service Time- This function will reset the Total Travel Time of the VRPTWAttributes class
	 * and then traverse the VRPTWNodesLinkedList and calculate the total Travel Time. This is summation
	 * of the Distance of the node since they are a one-to-one correlation
	 * 
	 * @param o the Object representing the VRPTWNodesLinkedList
	 */
	public void setTotalTravelTime(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		double travTime = 0;
		double dist = getTotalDistance(o);
		double milesPerMinute = VRPTWProblemInfo.getAverageBusSpeed() / 60;
		travTime =  /*milesPerMinute * */dist;

		VRPTWNodes theCell = (VRPTWNodes) vNodes.getHead().getNext(); // start at the first
													// customer

		while (theCell != vNodes.getTail()) {
			if (theCell.getShipment().getTruckTypeNeeded()
					.equals(VRPTWProblemInfo.getRegular())) {
				travTime += (VRPTWProblemInfo.getRPickupTime() * theCell.getDemand());
			} else if (theCell.getShipment().getTruckTypeNeeded()
					.equals(VRPTWProblemInfo.getWheelChair())) {
				travTime += (VRPTWProblemInfo.getWPickupTime() * theCell.getDemand());
			} else if (theCell.getShipment().getTruckTypeNeeded()
					.equals(VRPTWProblemInfo.getWheelChairElevator())) {
				travTime += (VRPTWProblemInfo.getWEPickupTime() * theCell.getDemand());
			} else if (theCell.getShipment().getTruckTypeNeeded()
					.equals(VRPTWProblemInfo.getMonitor())) {
				travTime += (VRPTWProblemInfo.getMPickUpTime() * theCell.getDemand());
			}

			theCell = (VRPTWNodes) theCell.getNext();
		}

		vNodes.getAttributes().setTotalTravelTime(travTime);
	}
	
	public void setMaxTravelTime(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;

		double linehaul = 0;

		vNodes.getAttributes().setMaxTravelTime(linehaul);
	}

	public void setAvgTravelTime(Object o) {
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;

		// System.out.println("calculating average travel time");
		double aTT = 0;

		vNodes.getAttributes().setAvgTravelTime(aTT);
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
		setTotalServiceTime(o);
		setTotalCost(o);
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
