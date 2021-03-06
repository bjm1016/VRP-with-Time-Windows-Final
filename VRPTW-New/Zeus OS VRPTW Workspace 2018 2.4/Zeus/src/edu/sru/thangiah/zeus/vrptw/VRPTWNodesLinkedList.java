package edu.sru.thangiah.zeus.vrptw;

import java.util.ArrayList;

import org.joda.time.DateTimeUtils;

import edu.sru.thangiah.zeus.core.NodesLinkedList;
import edu.sru.thangiah.zeus.core.Shipment;
import edu.sru.thangiah.zeus.vrptw.vrptwcostfunctions.VRPTWNodesLLCostFunctions;

/**
 * VRPTWNodesLinkedList inherits NodesLinkedList and contains
 * methods adapted to the VRPTW problem
 *
 *
 */
public class VRPTWNodesLinkedList extends NodesLinkedList implements
		java.io.Serializable, java.lang.Cloneable {

	/**
	 * Constructor
	 *
	 *
	 */
	public VRPTWNodesLinkedList() {
		setHead(new VRPTWNodes());
		setTail(new VRPTWNodes());
		linkHeadTail();
		setAttributes(new VRPTWAttributes());
	}

	/**
	 * Constructor
	 *
	 * @param tT truck type of truck to which this linked list is assigned
	 * @param depotX x-coordinate of depot from which the truck originates
	 * @param depotY y-coordinate of depot from which the truck originates
	 * @param tN truck number
	 *
	 */
	public VRPTWNodesLinkedList(VRPTWTruckType tT, double depotX,
			double depotY, int tN) {
		setTruckType(tT);
		setTruckNum(tN);

		setFeasibility(new VRPTWFeasibility(getTruckType().getMaxDuration(),
				getTruckType().getMaxCapacity(), this));
		setHead(new VRPTWNodes(new VRPTWShipment((float) depotX,
				(float) depotY, 0, 0, 0, 0, 0)));
		setTail(new VRPTWNodes(new VRPTWShipment((float) depotX,
				(float) depotY, 0, -1, 0, 0, 0)));
		linkHeadTail();

		setAttributes(new VRPTWAttributes());
	}

	/**
	 * Return attributes object held by this linked list
	 *
	 * @return VRPTWAttributes object
	 */
	@Override
	public VRPTWAttributes getAttributes() {
		return (VRPTWAttributes) super.getAttributes();
	}

	/**
	 * Returns a reference to the first node in the list
	 *
	 * @return VRPTWNodes reference
	 */
	public VRPTWNodes getHead() {
		return (VRPTWNodes) super.getHead();
	}

	/**
	 * Inserts the shipment using the insertion method described by problem info
	 *
	 * @param theShipment shipment to be inserted into list
	 * @return boolean stating the shipment has been inserted
	 *
	 */
	public boolean insertShipment(Shipment theShipment) {
		VRPTWNodesLinkedList status = (VRPTWNodesLinkedList) VRPTWProblemInfo.getInsertShipType();
		return status.getInsertShipment(this, (VRPTWShipment) theShipment);
	}

	/**
	 * Finds the nodes object matching a given shipment
	 *
	 * @param toFind shipment to be found
	 * @return reference to corresponding nodes or null if not found
	 *
	 */
	public VRPTWNodes findNodeByShipment(VRPTWShipment toFind) {

		VRPTWNodes currentNode = (VRPTWNodes) getHead();

		while (currentNode != getTail()) {
			if (currentNode.getShipment().getIndex() == toFind.getIndex()) {
				return currentNode;
			}
			currentNode = (VRPTWNodes) currentNode.getNext();
		}

		if (currentNode == getTail()) {
			return currentNode;
		}

		return null;
	}
	
	/**
	 * overrides NodesLinkedList clone, uses child classes
	 * @author Eric McAlpine
	 * 
	 */
	@Override
	public Object clone(){
		VRPTWNodesLinkedList clonedLinkedList=new VRPTWNodesLinkedList();
		
		clonedLinkedList.setAttributes((VRPTWAttributes)this.getAttributes().clone());
		clonedLinkedList.setFeasibility((VRPTWFeasibility) this.getFeasibility().clone());
		clonedLinkedList.setTruckType((VRPTWTruckType) this.getTruckType().clone());
		clonedLinkedList.setTruckNum(this.getTruckNum());
		clonedLinkedList.setHead((VRPTWNodes) this.getHead().clone());
		
		//this.expandRoute();
		
		if(this.getHead() != this.getTail()){
			VRPTWNodes currNodes = (VRPTWNodes) clonedLinkedList.getHead();
			VRPTWNodes nextNodes = (VRPTWNodes) this.getHead().getNext();
			
			while(nextNodes != null){
				currNodes.setNext((VRPTWNodes)nextNodes.clone());
				currNodes.getNext().setPrev(currNodes);
				currNodes=(VRPTWNodes) currNodes.getNext();
				nextNodes=(VRPTWNodes) nextNodes.getNext();
				
				if(nextNodes==null){
					clonedLinkedList.setTail(currNodes);
					currNodes.setNext(null);
				}
			}
		}
		clonedLinkedList.getFeasibility().setRoute(clonedLinkedList);
		
		return clonedLinkedList;
	}

	/**
	 * This is a stub - Leave it as it is The concrete getInsertShipment will be
	 * declared by the class inheriting this class and implementing the actual
	 * insertion of shipment *
	 * 
	 * @param currNodesLL
	 *            current nodes linked list
	 * @param theShipment
	 *            shipment to be inserted
	 * @return true if inserted, false if not
	 */
	public boolean getInsertShipment(VRPTWNodesLinkedList currNodeLL,
			VRPTWShipment theShipment) {
		return false;
	}

	protected double calcDist(double x1, double x2, double y1, double y2) {
		double d = 0;

		try {
			d = (double) Math
					.sqrt((double) (((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1))));
		} catch (ArithmeticException e) {
			System.out
					.println("Arithmetic Exception in calculating distance in ShipmentLinkedList "
							+ e);
		}
		return d;
	}
	
	public double setArrivalTime(Object o){
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		
		double earlyArrivalTime = 0,
			distance = 0, waitingTime = 0, 
			previousDeliveryTime = 0, previousDuration = 0,
			currentDeliveryTime = 0, x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		
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
			
			/*if(earlyArrivalTime >  currentDeliveryTime){
				currentDeliveryTime = earlyArrivalTime;
			}*/
			
			nodeOne.getShipment().setActualArrivalTime((float) currentDeliveryTime);
			
			
			previousDeliveryTime = currentDeliveryTime;
			nodeOne = nodeTwo;
			
			nodeTwo = (VRPTWNodes) nodeOne.getNext();
			
		}
		return currentDeliveryTime;
	}
	
	public double setDeliveryTime(Object o){
		VRPTWNodesLinkedList vNodes = (VRPTWNodesLinkedList) o;
		
		double earlyArrivalTime = 0,
			distance = 0, waitingTime = 0, 
			previousDeliveryTime = 0, previousDuration = 0,
			currentDeliveryTime = 0, x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		
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
				currentDeliveryTime = earlyArrivalTime;
			}
			
			nodeOne.getShipment().setTravelTime((float) currentDeliveryTime);
			
			previousDeliveryTime = currentDeliveryTime;
			nodeOne = nodeTwo;
			
			nodeTwo = (VRPTWNodes) nodeOne.getNext();
			
		}
		return currentDeliveryTime;
	}
}


/**
 * The Time Oriented Nearest Neighbour (TONN) heuristic insertion method. As 
 * outlined in "Algorithms for the Vehicle Routing and Scheduling 
 * Problem with Time Window Constraints" the insertion utilized by 
 * the TONN inserts the last assigned customer into the end of the emerging route
 * 
 *
 * @author Marc S
 * 
 */
class TimeOrientedNearestNeighborInsert extends VRPTWNodesLinkedList {
	/**
	 * This method will insert the shipment selected by the TONN selection 
	 * method into the emerging route at the end of this route, unless the 
	 * route is currently empty it will become the head of the route. If 
	 * the insertion is not feasible the shipment will be removed from the
	 * route and assigned to a new route
	 * 
	 * @param currNodeLL This represents the current route the shipment
	 *	will be assigned to 
	 * 
	 * @param theShipment This represents the shipment to be inserted 
	 * 
	 * @author Marc S
	 */
	public boolean getInsertShipment(VRPTWNodesLinkedList currNodeLL,
			VRPTWShipment theShipment) {

		/** */
		VRPTWNodes tmpPtr;
		/** A new node that holds the shipment to be inserted */
		VRPTWNodes cellToInsert = new VRPTWNodes(theShipment); 

		//If the route is empty, insert the shipment as the first 
		if (currNodeLL.getHead().getNext() == currNodeLL.getTail()) {
			//Insert the new node and set the pointers to update the route
			currNodeLL.setHeadNext(cellToInsert);
			currNodeLL.getTail().setPrev(cellToInsert);
			cellToInsert.setPrev(currNodeLL.getHead());
			cellToInsert.setNext(currNodeLL.getTail());
			//Set the current travel time to assist with calculating wait time
//			cellToInsert.getShipment().setTravelTime(
//					(float) VRPTWProblemInfo.nodesLLLevelCostF
//							.getTotalTravelTime(currNodeLL));
			
			cellToInsert.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
			cellToInsert.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));


			//If the route is not feasible, remove the inserted shipment to 
			//return the route to the previous state 
			if (!currNodeLL.getFeasibility().isFeasible()) {
				// remove the inserted node
				tmpPtr = (VRPTWNodes) currNodeLL.getHead().getNext();
				tmpPtr.setNext(null);
				tmpPtr.setPrev(null);
				//Reset the travel time of the shipment
				cellToInsert.getShipment().setTravelTime(0.0f);
				cellToInsert.getShipment().setLastTimeChecked(DateTimeUtils.currentTimeMillis());

				// point the head and tail to each other
				currNodeLL.setHeadNext(currNodeLL.getTail());
				currNodeLL.getTail().setPrev(currNodeLL.getHead());

				return false;
			}
		} else { // The route is not empty, and we insert at the end of the emerging route
			
			/** Node representing the last actual shipment on the route */
			VRPTWNodes lastShipmentNode = (VRPTWNodes) currNodeLL.getTail()
					.getPrev();
			//Check for an error
			if (lastShipmentNode == null) {
				lastShipmentNode = (VRPTWNodes) currNodeLL.getTail().getPrev();
			}
			//Set the pointers to insert the node into the end of the route
			lastShipmentNode.setNext(cellToInsert);
			cellToInsert.setPrev(lastShipmentNode);
			cellToInsert.setNext(currNodeLL.getTail());
			currNodeLL.getTail().setPrev(cellToInsert);
			//Update the travel time for wait time calculation
//			cellToInsert.getShipment().setTravelTime(
//					(float) VRPTWProblemInfo.nodesLLLevelCostF
//							.getTotalTravelTime(currNodeLL));
			
			cellToInsert.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
			cellToInsert.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));
			
			// If the route is not feasible, reuturn route to previous
			// state and return false
			if (!currNodeLL.getFeasibility().isFeasible()) {
				//Clear the pointers of the node that was inserted
				cellToInsert.setNext(null);
				cellToInsert.setPrev(null);
				cellToInsert.getShipment().setTravelTime(0.0f);
				//Reset the route to the previous state
				currNodeLL.getTail().setPrev(lastShipmentNode);
				lastShipmentNode.setNext(currNodeLL.getTail());
				cellToInsert.getShipment().setLastTimeChecked(DateTimeUtils.currentTimeMillis());
				return false;
			}
		}

		//Set the shipment to be assigned
		theShipment.setIsAssigned(true);
		//Store the current route of the shipment for later use
		theShipment.setAssignedRoute(currNodeLL);
		//Update the problem statistics 
		VRPTWProblemInfo.nodesLLLevelCostF.calculateTotalsStats(currNodeLL);

		theShipment.setLastTimeChecked(DateTimeUtils.currentTimeMillis());
		
		//Print to the console the current route
		{
			System.out.println("Route is:");
			VRPTWNodes tempPtr = (VRPTWNodes) currNodeLL.getHead();
			while (tempPtr != (VRPTWNodes) currNodeLL.getTail()) {
				System.out.print(tempPtr.getIndex() + "(" + tempPtr.getDemand()
						+ ")-");
				tempPtr = (VRPTWNodes) tempPtr.getNext();
			}
			System.out.println();
		}

		return true;
	}

	// The WhoAmI methods gives the id of the assigned object
	// It is a static method so that it can be accessed without creating an
	// object
	@Override
	public String toString() {
		return ("Insertion Type: Time Oriented Nearest Neighbor Insert");
	}
	
	public static String getID(){
		return ("NN");
	}
}

/**
 * This is the insertion method utilized by the Sweep selection method 
 * 
 * @author Marc S
 *
 */
class SweepInsert extends VRPTWNodesLinkedList {
	/**
	 * This method will insert the shipment selected by the Sweep selection 
	 * method into the emerging route at the end of this route, unless the 
	 * route is currently empty it will become the head of the route. If 
	 * the insertion is not feasible the shipment will be removed from the
	 * route and assigned to a new route
	 * 
	 * @param currNodeLL This represents the current route the shipment
	 *	will be assigned to 
	 * 
	 * @param theShipment This represents the shipment to be inserted 
	 * 
	 * @author Marc S
	 */
	public boolean getInsertShipment(VRPTWNodesLinkedList currNodeLL,
			VRPTWShipment theShipment) {

		VRPTWNodes tmpPtr;
		VRPTWNodes cellToInsert = new VRPTWNodes(theShipment);

		
		if(theShipment.getIndex() == 49){
			System.out.println("ieieio");
		}
		// The route is currently empty, handle first insert into route
		if (currNodeLL.getHead().getNext() == currNodeLL.getTail()) {
//			cellToInsert.getShipment().setTravelTime(
//					(float) VRPTWProblemInfo.nodesLLLevelCostF
//							.getTotalTravelTime(currNodeLL));
			currNodeLL.setHeadNext(cellToInsert);
			currNodeLL.getTail().setPrev(cellToInsert);
			cellToInsert.setPrev(currNodeLL.getHead());
			cellToInsert.setNext(currNodeLL.getTail());

//			currNodeLL.setDeliveryTime(currNodeLL);
			cellToInsert.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
			cellToInsert.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));
			
			// If the route is not feasible, reuturn route to previous
			// state and return false
			if (!currNodeLL.getFeasibility().isFeasible()) {
				// remove the inserted node
				tmpPtr = (VRPTWNodes) currNodeLL.getHead().getNext();
				tmpPtr.setNext(null);
				tmpPtr.setPrev(null);

				// point the head and tail to each other
				currNodeLL.setHeadNext(currNodeLL.getTail());
				currNodeLL.getTail().setPrev(currNodeLL.getHead());

				return false;
			}
		} else { // The route is not empty, and we insert at the end of the
					// emerging route

			VRPTWNodes lastShipmentNode = (VRPTWNodes) currNodeLL.getTail()
					.getPrev();
			if (lastShipmentNode == null) {
				lastShipmentNode = (VRPTWNodes) currNodeLL.getTail().getPrev();
			}
			/*cellToInsert.getShipment().setTravelTime(
					(float) VRPTWProblemInfo.nodesLLLevelCostF
							.getTotalTravelTime(currNodeLL));*/

			lastShipmentNode.setNext(cellToInsert);
			cellToInsert.setPrev(lastShipmentNode);
			cellToInsert.setNext(currNodeLL.getTail());

			currNodeLL.getTail().setPrev(cellToInsert);
		
			
//			currNodeLL.setDeliveryTime(currNodeLL);
			cellToInsert.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
			cellToInsert.getShipment().setTravelTime((float) currNodeLL.setDeliveryTime(currNodeLL));
			// If the route is not feasible, reuturn route to previous
			// state and return false
			if (!currNodeLL.getFeasibility().isFeasible()) {

				cellToInsert.setNext(null);
				cellToInsert.setPrev(null);
				cellToInsert.getShipment().setTravelTime(0.0f);
				cellToInsert.getShipment().setActualArrivalTime(0.0);

				currNodeLL.getTail().setPrev(lastShipmentNode);
				lastShipmentNode.setNext(currNodeLL.getTail());
				return false;
			}
		}

		theShipment.setIsAssigned(true);
		theShipment.setAssignedRoute(currNodeLL);
		VRPTWProblemInfo.nodesLLLevelCostF.calculateTotalsStats(currNodeLL);

		{
			System.out.println("Route is:");
			VRPTWNodes tempPtr = (VRPTWNodes) currNodeLL.getHead();
			while (tempPtr != (VRPTWNodes) currNodeLL.getTail()) {
				System.out.print(tempPtr.getIndex() + "(" + tempPtr.getDemand()
						+ ")-");
				tempPtr = (VRPTWNodes) tempPtr.getNext();
			}
			System.out.println();
		}

		return true;
	}

	// The WhoAmI methods gives the id of the assigned object
	// It is a static method so that it can be accessed without creating an
	// object
	@Override
	public String toString() {
		return ("Insertion Type: Sweep Insert");
	}
	
	public static String getID(){
		return ("S");
	}
}

/**
 * This class represents the first of the Insertion Heuristics presented by
 * Solomon. This heuristic tries to maximize the benefit of servicing the customer
 * on the current partial route rather than on its own direct route. 
 * 
 * @author Marc S
 *
 */
class InsertionCriterionI extends VRPTWNodesLinkedList {
	/** Represents all of the shipments potentially available*/
	private VRPTWShipmentLinkedList allShipments;
	/** Holds all shipments that potentially can be inserted */
	private ArrayList<VRPTWNodes> feasibleShipments;

	/**
	 * Argumented constructor that takes in the main shipments to keep 
	 * track of all shipments in the problem set
	 * 
	 * @param sLL represents the main shipments from the problem set
	 */
	public InsertionCriterionI(VRPTWShipmentLinkedList sLL) {
		super();
		this.allShipments = sLL;
		feasibleShipments = new ArrayList<VRPTWNodes>();
	}

	/**
	 * This is the main insertion function that handles the Insertion I
	 * heuristic. For each unrouted customer on the route it calculates
	 * the best insertion point for this customer on the route and if this
	 * insertion is feasible it is stored in the list for the next step. 
	 * In the second step, we find the optimal insertable customer that is 
	 * both unrouted and feasible. At this point we then insert the found 
	 * customer into its best insertion position in the route.
	 * 
	 * 
	 * @param currNodeLL This represents the current route the shipment
	 *	will be assigned to 
	 * 
	 * @param theShipment This represents the shipment to be inserted 
	 * 
	 * @author Marc S
	 */
	public boolean getInsertShipment(VRPTWNodesLinkedList currNodeLL,
			VRPTWShipment theShipment) {
		VRPTWShipment seedShipment = theShipment;
		VRPTWNodes tmpPtr;
		
		/** The shipment that is to be inserted will be stored in this node */ 
		VRPTWNodes cellToInsert = null,
			/** Holds the node that represents the shipment in front of the tested shipment */
			i = null, 
			/** Holds the node that represents the shipment behind the tested shipment */
			j = null, 
			/** This will represent the node being tested for the nessecary criteria */
			u = new VRPTWNodes(seedShipment);
		
		VRPTWNodes testingNode = null;
		
		/** These represent the weights utilized within the calculations */ 
		float deltaOne = 0.1f, 
			deltaTwo = 0.9f, 
			mew = 1, 
			lambda = 2;

		/** These variables hold the calculated values in the problem */
		double C11 = 0.0, 
			C12 = 0.0,
			C1 = 0.0,
			C2 = 0.0;
		
		double bestValue = Double.MAX_VALUE;
		while (true) {
			/* This is an error check for the problem due to the nature of the heuristic
			 * an already assigned shipment may come back into the heuristic */
			if(u.getShipment().getIsAssigned()){
				return true;
			}
			
			// Handle the insertion of the seed customer into the route
			if (currNodeLL.getHead().getNext() == currNodeLL.getTail()) {
				currNodeLL.setHeadNext(u);
				currNodeLL.getTail().setPrev(u);
				u.setPrev(currNodeLL.getHead());
				u.setNext(currNodeLL.getTail());
//				u.getShipment().setTravelTime(
//						(float) VRPTWProblemInfo.nodesLLLevelCostF
//								.getTotalTravelTime(currNodeLL));
				
				u.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
				u.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));

				// If the route is not feasible, reuturn route to previous
				// state and return false
				
				if (!currNodeLL.getFeasibility().isFeasible()) {
					// remove the inserted node
					tmpPtr = (VRPTWNodes) currNodeLL.getHead().getNext();
					tmpPtr.setNext(null);
					tmpPtr.setPrev(null);

					// point the head and tail to each other
					currNodeLL.setHeadNext(currNodeLL.getTail());
					currNodeLL.getTail().setPrev(currNodeLL.getHead());

					return false;
				}
				//Let the program know that this shipment has been assigned
				allShipments.getShipmentByIndex(u.getIndex()).setIsAssigned(true);
			} else { // The route is not empty, and we insert at the end of the
						// emerging route

				testingNode = new VRPTWNodes((VRPTWShipment) allShipments
						.getHead().getNext());// Grab The first Shipment
												// available
				int feasibleListPosition = 0;
				// Calculate the best insertion cost and position for each
				// possible shipment
				while (testingNode.getShipment() != allShipments.getTail()) {
					if (testingNode.getShipment().getIsAssigned()) { // If this
																		// shipment
																		// has
																		// already
																		// been
																		// assigned
																		// skip
																		// it
						 //testingNode.setShipment(testingNode.getShipment().getNext());
						testingNode = new VRPTWNodes(
								(VRPTWShipment) testingNode.getShipment()
										.getNext());
						continue;
					}
					boolean feasibleFound = false;
					// Grab the starting two nodes of the emerging route
					i = (VRPTWNodes) currNodeLL.getHead(); // The head of the
															// list,
					j = (VRPTWNodes) i.getNext(); //

					while (i != currNodeLL.getTail()) {
						double distBtwIU = 0.0, distBtwUJ = 0.0, distBtwIJ = 0.0, currentServiceTime = 0.0, updatedServiceTime = 0.0;

						// Begin calculating C1.1 = Diu + Duj - mew*Dij, mew >=
						// 0
						distBtwIU = calcDist(i.getShipment().getXCoord(),
								testingNode.getShipment().getXCoord(), i
										.getShipment().getXCoord(), testingNode
										.getShipment().getYCoord());

						distBtwUJ = calcDist(testingNode.getShipment()
								.getXCoord(), j.getShipment().getXCoord(),
								testingNode.getShipment().getXCoord(), j
										.getShipment().getYCoord());

						distBtwIJ = calcDist(i.getShipment().getXCoord(), j
								.getShipment().getXCoord(), i.getShipment()
								.getXCoord(), j.getShipment().getYCoord());

						// C1.1 = Diu + Duj - mew*Dij, mew >= 0
						C11 = distBtwIU + distBtwUJ - (mew * distBtwIJ);

						// Begin calculating C1.2 = Bju - Bj; Bju is service
						// time including insertion of u
						//currentServiceTime = VRPTWProblemInfo.nodesLLLevelCostF
						//		.getTotalTravelTime(currNodeLL);

						currentServiceTime = (float)currNodeLL.setDeliveryTime(currNodeLL);
//						testingNode.setPrev(i);
//						testingNode.setNext(j);
//						i.setNext(testingNode);
//						j.setPrev(testingNode);
						

						//updatedServiceTime = VRPTWProblemInfo.nodesLLLevelCostF
						//		.getTotalTravelTime(currNodeLL);

						updatedServiceTime = (float)currNodeLL.setDeliveryTime(currNodeLL);
						C12 = updatedServiceTime - currentServiceTime;

						// Calculate total C1
						C1 = (deltaOne * C11) + (deltaTwo * C12);

						// If this route will be feasible, check to see if this
						// is the best cost
						if (currNodeLL.getFeasibility().isFeasible()) {
							if (C1 < bestValue) {
								// Store this node into a list of feasible nodes
								// for this route
								testingNode.setBestInsertionCost(C1);
								testingNode.setCellToInsertInFront(i);
								testingNode.setCellToInsertBehind(j);
								feasibleShipments.add(feasibleListPosition,
										testingNode);
								feasibleFound = true;
								bestValue = C1;
							}
						} else {
							// Reset the values of i and j
							i.setNext(j);
							j.setPrev(i);

							return false;
						}
						// Reset the values of i and j
						testingNode.setPrev(null);
						testingNode.setNext(null);
						i.setNext(j);
						j.setPrev(i);

						i = j;
						j = (VRPTWNodes) i.getNext();
					}
					if (feasibleFound) ++feasibleListPosition;
					// Update currentShipmentValues
					testingNode = new VRPTWNodes((VRPTWShipment) testingNode
							.getShipment().getNext());
				}

				/* Begin calculation of C2, find the optimal value of C2 for 
				* all unrouted and feasible shipments
				*/
				double best = Double.NEGATIVE_INFINITY;
				for (VRPTWNodes currentNode : feasibleShipments) {
					double localC2 = 0.0;
					if(currentNode.getShipment().getIsAssigned()){
						continue;
					}
					localC2 = (lambda * calcDist(allShipments.getHead()
							.getXCoord(),
							currentNode.getShipment().getXCoord(), allShipments
									.getHead().getYCoord(), currentNode
									.getShipment().getYCoord()))
							- currentNode.getBestInsertionCost();

					if (localC2 > best) {
						cellToInsert = currentNode;
						best = localC2;
					}
				}
				
				//Empty out the list for the next round of calculations
				feasibleShipments.clear();

				/*
				 * If there was a shipment that was able to be inserted into the 
				 * take care of the nessecary insertion into the route
				 * */
				if (cellToInsert != null) {
					cellToInsert.setPrev(cellToInsert.getCellToInsertInFront()); // i
					cellToInsert.setNext(cellToInsert.getCellToInsertBehind()); // j
					//Set the travel time for the wait time calculations
//					cellToInsert.getShipment().setTravelTime(
//							(float) VRPTWProblemInfo.nodesLLLevelCostF
//									.getTotalTravelTime(currNodeLL));
					
					cellToInsert.getCellToInsertInFront().setNext(cellToInsert);
					cellToInsert.getCellToInsertBehind().setPrev(cellToInsert);
				
					cellToInsert.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
					cellToInsert.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));
				}

				// If the route is not feasible, return route to previous
				// state and return false
				if (!currNodeLL.getFeasibility().isFeasible()) {

					cellToInsert.setNext(null);
					cellToInsert.setPrev(null);
					cellToInsert.getShipment().setTravelTime(0.0f);
					cellToInsert.getCellToInsertInFront().setNext(
							cellToInsert.getCellToInsertBehind());
					cellToInsert.getCellToInsertBehind().setPrev(
							cellToInsert.getCellToInsertInFront());
					
					return false;
				}
			}

			
			/*
			 * If there was a shipment that was found insertable, set it 
			 * to be assigned for the problem to know about. Otherwise, exit the
			 * loop and find the next seed shipment
			 * */
			if (cellToInsert != null) {
				cellToInsert.getShipment().setIsAssigned(true);
				theShipment = (VRPTWShipment) cellToInsert.getShipment();
				theShipment.setIsAssigned(true);
			} else {
				return true;
			}
			
			//Calculate total stats and clear out cell to insert
			VRPTWProblemInfo.nodesLLLevelCostF.calculateTotalsStats(currNodeLL);
			cellToInsert = null;
			
			
			//Print the current route to the console
			{
				System.out.println("Route is:");
				VRPTWNodes tempPtr = (VRPTWNodes) currNodeLL.getHead();
				while (tempPtr != (VRPTWNodes) currNodeLL.getTail()) {
					System.out.print(tempPtr.getShipment().getIndex() + "("
							+ tempPtr.getShipment().getDemand() + ")-");
					tempPtr = (VRPTWNodes) tempPtr.getNext();
				}
				System.out.println();
			}
		}
	}

	// The WhoAmI methods gives the id of the assigned object
	// It is a static method so that it can be accessed without creating an
	// object
	@Override
	public String toString() {
		return ("Insertion Type: Insertion Criterion i");
	}

	public static String getID(){
		return ("I1");
	}
}

/**
 * This class is a representation of the second insertion method proposed by
 * Solomon. This is an extension of the first insertion heuristic that accounts for
 * more than I1. In this case this insertion method aims to select customers whose
 * insertion costs minimize the total measure of route distance and route time.
 * 
 * @author Marc S
 *
 */
class InsertionCriterionII extends VRPTWNodesLinkedList {
	/** Represents all of the shipments potentially available*/
	private VRPTWShipmentLinkedList allShipments;
	/** Holds all shipments that potentially can be inserted */
	private ArrayList<VRPTWNodes> feasibleShipments;

	/**
	 * Argumented constructor that takes in the main shipments to keep 
	 * track of all shipments in the problem set
	 * 
	 * @param sLL represents the main shipments from the problem set
	 */
	public InsertionCriterionII(VRPTWShipmentLinkedList sLL) {
		super();
		this.allShipments = sLL;
		feasibleShipments = new ArrayList<VRPTWNodes>();
	}
 
	/**
	 * This is the main insertion function that handles the Insertion II
	 * heuristic. For each unrouted customer on the route it calculates
	 * the best insertion point for this customer on the route and if this
	 * insertion is feasible it is stored in the list for the next step. 
	 * In the second step, we find the optimal insertable customer that is 
	 * both unrouted and feasible. This is found by calculating and finding the 
	 * minimum value of the route duration summed with the route time. At this 
	 * point we then insert the found customer into its best insertion position 
	 * in the route.
	 * 
	 * 
	 * @param currNodeLL This represents the current route the shipment
	 *	will be assigned to 
	 * 
	 * @param theShipment This represents the shipment to be inserted 
	 * 
	 * @author Marc S
	 */
	public boolean getInsertShipment(VRPTWNodesLinkedList currNodeLL,
			VRPTWShipment theShipment) {
		VRPTWShipment seedShipment = theShipment;
		VRPTWNodes tmpPtr;
		VRPTWNodes cellToInsert = null, 
			i = null, 
			j = null, 
			u = new VRPTWNodes(seedShipment),
			testingNode = null;
		
		float deltaOne = 0.1f, 
			deltaTwo = 0.9f,
			mew = 1,
			betaOne = 0.5f, 
			betaTwo = 0.5f;

		double C11 = 0.0, 
			C12 = 0.0,
			C1 = 0.0,
			C2 = 0.0,
			bestValue = Double.MAX_VALUE;
		
		long currentTimeInMillis = DateTimeUtils.currentTimeMillis();
		long lastCheckedTime = 0;
		
		while (true) {
			//currentTimeInMillis = DateTimeUtils.currentTimeMillis();
			/* This is an error check for the problem due to the nature of the heuristic
			 * an already assigned shipment may come back into the heuristic */
			if(u == null){
				return false;
				//break;
			}
			
			if (u.getShipment().getIsAssigned()) {
				u =  (VRPTWNodes) u.getNext();

				continue;
			}
			
			// Handle the insertion of the seed customer into the route
			if (currNodeLL.getHead().getNext() == currNodeLL.getTail()) {
				currNodeLL.setHeadNext(u);
				currNodeLL.getTail().setPrev(u);
				u.setPrev(currNodeLL.getHead());
				u.setNext(currNodeLL.getTail());
//				u.getShipment().setTravelTime(
//						(float) VRPTWProblemInfo.nodesLLLevelCostF
//								.getTotalTravelTime(currNodeLL));
				
				u.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
				u.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));

				// If the route is not feasible, reuturn route to previous
				// state and return false
				
				if (!currNodeLL.getFeasibility().isFeasible()) {
					// remove the inserted node
					tmpPtr = (VRPTWNodes) currNodeLL.getHead().getNext();
					tmpPtr.setNext(null);
					tmpPtr.setPrev(null);

					// point the head and tail to each other
					currNodeLL.setHeadNext(currNodeLL.getTail());
					currNodeLL.getTail().setPrev(currNodeLL.getHead());
					u.getShipment().setLastTimeChecked(DateTimeUtils.currentTimeMillis());

					return false;
				}
				//Let the program know that this shipment has been assigned
				allShipments.getShipmentByIndex(u.getIndex()).setIsAssigned(true);
			} else { // The route is not empty, and we insert at the end of the
				// emerging route

				testingNode = new VRPTWNodes((VRPTWShipment) allShipments
						.getHead().getNext());// Grab The first Shipment
				// available
				int feasibleListPosition = 0;
				// Calculate the best insertion cost and position for each
				// possible shipment
				while (testingNode.getShipment() != allShipments.getTail()) {
					lastCheckedTime = currentTimeInMillis - testingNode.getShipment().getLastTimeChecked();
					if (testingNode.getShipment().getIsAssigned()) {
						testingNode = new VRPTWNodes(
								(VRPTWShipment) testingNode.getShipment()
								.getNext());
						continue;
					}
					boolean feasibleFound = false;
					// Grab the starting two nodes of the emerging route
					i = (VRPTWNodes) currNodeLL.getHead(); // The head of the
					// list,
					j = (VRPTWNodes) i.getNext(); //

					while (i != currNodeLL.getTail()) {
						double distBtwIU = 0.0, distBtwUJ = 0.0, distBtwIJ = 0.0, currentServiceTime = 0.0, updatedServiceTime = 0.0;

						// Begin calculating C1.1 = Diu + Duj - mew*Dij, mew >=
						// 0
						distBtwIU = calcDist(i.getShipment().getXCoord(),
								testingNode.getShipment().getXCoord(), i
								.getShipment().getXCoord(), testingNode
								.getShipment().getYCoord());

						distBtwUJ = calcDist(testingNode.getShipment()
								.getXCoord(), j.getShipment().getXCoord(),
								testingNode.getShipment().getXCoord(), j
								.getShipment().getYCoord());

						distBtwIJ = calcDist(i.getShipment().getXCoord(), j
								.getShipment().getXCoord(), i.getShipment()
								.getXCoord(), j.getShipment().getYCoord());

						// C1.1 = Diu + Duj - mew*Dij, mew >= 0
						C11 = distBtwIU + distBtwUJ - (mew * distBtwIJ);

						// Begin calculating C1.2 = Bju - Bj; Bju is service
						// time including insertion of u
						currentServiceTime = VRPTWProblemInfo.nodesLLLevelCostF
								.getTotalTravelTime(currNodeLL);

						testingNode.setPrev(i);
						testingNode.setNext(j);
						i.setNext(testingNode);
						j.setPrev(testingNode);
						
						testingNode.getShipment().setLastTimeChecked(DateTimeUtils.currentTimeMillis());
						if (currNodeLL.getFeasibility().isFeasible()){
						updatedServiceTime = VRPTWProblemInfo.nodesLLLevelCostF
								.getTotalTravelTime(currNodeLL);
						} else {
							
							i.setNext(j);
							j.setPrev(i);

							return false;
						//	continue;
						}

						C12 = updatedServiceTime - currentServiceTime;

						// Calculate total C1
						C1 = (deltaOne * C11) + (deltaTwo * C12);

						// If this route will be feasible, check to see if this
						// is the best cost
						if (currNodeLL.getFeasibility().isFeasible()) {
							if (C1 < bestValue) {
								// Store this node into a list of feasible nodes
								// for this route
								testingNode.setBestInsertionCost(C1);
								testingNode.setCellToInsertInFront(i);
								testingNode.setCellToInsertBehind(j);
								testingNode.setRouteTime(VRPTWProblemInfo.nodesLLLevelCostF
										.getTotalTravelTime(currNodeLL));
								testingNode.setRouteDistance(VRPTWProblemInfo.nodesLLLevelCostF
										.getTotalDistance(currNodeLL));

								testingNode.getShipment().setLastTimeChecked(DateTimeUtils.currentTimeMillis());

								feasibleShipments.add(feasibleListPosition,
										testingNode);
								feasibleFound = true;
								bestValue = C1;
							}
						} else {
							testingNode.setCellToInsertInFront(null);
							testingNode.setCellToInsertBehind(null);
							// Reset the values of i and j
							i.setNext(j);
							j.setPrev(i);

							return false;
						}
						// Reset the values of i and j
						i.setNext(j);
						j.setPrev(i);

						i = j;
						j = (VRPTWNodes) i.getNext();
					}
					if (feasibleFound)
						++feasibleListPosition;
					// Update currentShipmentValues
					testingNode = new VRPTWNodes((VRPTWShipment) testingNode
							.getShipment().getNext());
				}

				/* Begin calculation of C2
				 * This finds the minimal cost of the route duration and route time and stores
				 * that shipment until completed with all of the feasible shipments available
				 * 
				 */
				double best = Double.POSITIVE_INFINITY;
				for (VRPTWNodes currentNode : feasibleShipments) {
					double localC2 = 0.0;
					if(currentNode.getShipment().getIsAssigned()){
						continue;
					}

					localC2 = betaOne*currentNode.getRouteDistance() + betaTwo*currentNode.getRouteTime();

					if (localC2 < best) {
						cellToInsert = currentNode;
						best = localC2;
					}
				}

				//Empty out the list for the next round of calculations
				feasibleShipments.clear();

				/*
				 * If there was a shipment that was able to be inserted into the 
				 * take care of the nessecary insertion into the route
				 * */
				if (cellToInsert != null) {
					cellToInsert.setPrev(cellToInsert.getCellToInsertInFront()); // i
					cellToInsert.setNext(cellToInsert.getCellToInsertBehind()); // j
					//Set the travel time for the wait time calculations
					
					cellToInsert.getCellToInsertInFront().setNext(cellToInsert);
					cellToInsert.getCellToInsertBehind().setPrev(cellToInsert);
					
					cellToInsert.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
					cellToInsert.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));
				
					// If the route is not feasible, return route to previous
					// state and return false
					if (!currNodeLL.getFeasibility().isFeasible()) {

						cellToInsert.setNext(null);
						cellToInsert.setPrev(null);
						cellToInsert.getShipment().setTravelTime(0.0f);
						
						cellToInsert.getCellToInsertInFront().setNext(
								cellToInsert.getCellToInsertBehind());
						cellToInsert.getCellToInsertBehind().setPrev(
								cellToInsert.getCellToInsertInFront());
						
						cellToInsert.getShipment().setLastTimeChecked(DateTimeUtils.currentTimeMillis());

						return false;
					}
				}
			}

			/*
			 * If there was a shipment that was found insertable, set it 
			 * to be assigned for the problem to know about. Otherwise, exit the
			 * loop and find the next seed shipment
			 * */
			if (cellToInsert != null) {
				cellToInsert.getShipment().setIsAssigned(true);
				cellToInsert.getShipment().setLastTimeChecked(DateTimeUtils.currentTimeMillis());
				theShipment = (VRPTWShipment) cellToInsert.getShipment();
				theShipment.setIsAssigned(true);
			} else {
				return true;
			}
			//Calculate total stats and clear out cell to insert
			VRPTWProblemInfo.nodesLLLevelCostF.calculateTotalsStats(currNodeLL);
			cellToInsert = null;

			//Print the current route to the console
			{
				System.out.println("Route is:");
				VRPTWNodes tempPtr = (VRPTWNodes) currNodeLL.getHead();
				while (tempPtr != (VRPTWNodes) currNodeLL.getTail()) {
					System.out.print(tempPtr.getShipment().getIndex() + "("
							+ tempPtr.getShipment().getDemand() + ")-");
					tempPtr = (VRPTWNodes) tempPtr.getNext();
				}
				System.out.println();
			}
		}
	}
	// The WhoAmI methods gives the id of the assigned object
	// It is a static method so that it can be accessed without creating an
	// object
	@Override
	public String toString() {
		return ("Insertion Type: Insertion Criterion ii");
	}
	
	public static String getID(){
		return ("I2");
	}
}

/**
 * This class is a representation of the second insertion method proposed by
 * Solomon. This is an extension of the first insertion heuristic that accounts for
 * more than I1. In this case this insertion method aims to select customers whose
 * insertion costs minimize the total measure of route distance and route time. On top
 * of these it now accounts for the temporal aspect of servicing a customer as well as 
 * the urgency of delivery to the customer. This heuristic ends up being a generalization
 * of the Time Oriented Nearest Neighbor
 * 
 * @author Marc S
 *
 */
class InsertionCriterionIII extends VRPTWNodesLinkedList {
	/** Represents all of the shipments potentially available*/
	private VRPTWShipmentLinkedList allShipments;
	/** Holds all shipments that potentially can be inserted */
	private ArrayList<VRPTWNodes> feasibleShipments;

	/**
	 * Argumented constructor that takes in the main shipments to keep 
	 * track of all shipments in the problem set
	 * 
	 * @param sLL represents the main shipments from the problem set
	 */
	public InsertionCriterionIII(VRPTWShipmentLinkedList sLL) {
		super();
		this.allShipments = sLL;
		feasibleShipments = new ArrayList<VRPTWNodes>();
	}

	/**
	 * This is the main insertion function that handles the Insertion III
	 * heuristic. For each unrouted customer on the route it calculates
	 * the best insertion point for this customer on the route and if this
	 * insertion is feasible it is stored in the list for the next step. 
	 * In the second step, we find the optimal insertable customer that is 
	 * both unrouted and feasible. At this point we then insert the found 
	 * customer into its best insertion position in the route.
	 * 
	 * 
	 * @param currNodeLL This represents the current route the shipment
	 *	will be assigned to 
	 * 
	 * @param theShipment This represents the shipment to be inserted 
	 * 
	 * @author Marc S
	 */
	public boolean getInsertShipment(VRPTWNodesLinkedList currNodeLL,
			VRPTWShipment theShipment) {
		VRPTWShipment seedShipment = theShipment;
		VRPTWNodes tmpPtr;
		VRPTWNodes cellToInsert = null,
			i = null,
			j = null,
			u = new VRPTWNodes(seedShipment),
			testingNode = null;
		
		float deltaOne = 0.1f,
			deltaTwo = 0.9f,
			deltaThree = 0.1f, 
			mew = 1, lambda = 2;

		double C11 = 0.0, 
			C12 = 0.0,
			C13 = 0.0,
			C1 = 0.0,
			C2 = 0.0;
		
		double bestValue = Double.MAX_VALUE;
		while (true) {
			/* This is an error check for the problem due to the nature of the heuristic
			 * an already assigned shipment may come back into the heuristic */
			if(u.getShipment().getIsAssigned()){
				return true;
			}
			
			// Handle the insertion of the seed customer into the route
			if (currNodeLL.getHead().getNext() == currNodeLL.getTail()) {
				currNodeLL.setHeadNext(u);
				currNodeLL.getTail().setPrev(u);
				u.setPrev(currNodeLL.getHead());
				u.setNext(currNodeLL.getTail());
				
				u.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
				u.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));
				// If the route is not feasible, reuturn route to previous
				// state and return false
				
				if (!currNodeLL.getFeasibility().isFeasible()) {
					// remove the inserted node
					tmpPtr = (VRPTWNodes) currNodeLL.getHead().getNext();
					tmpPtr.setNext(null);
					tmpPtr.setPrev(null);
					u.getShipment().setTravelTime((float) 0.0);

					// point the head and tail to each other
					currNodeLL.setHeadNext(currNodeLL.getTail());
					currNodeLL.getTail().setPrev(currNodeLL.getHead());

					return false;
				}
				//Let the program know that this shipment has been assigned
				allShipments.getShipmentByIndex(u.getIndex()).setIsAssigned(true);
			} else { // The route is not empty, and we insert at the end of the
						// emerging route

				testingNode = new VRPTWNodes((VRPTWShipment) allShipments
						.getHead().getNext());// Grab The first Shipment
												// available
				int feasibleListPosition = 0;
				// Calculate the best insertion cost and position for each
				// possible shipment
				while (testingNode.getShipment() != allShipments.getTail()) {
					if (testingNode.getShipment().getIsAssigned()) { // If this
																		// shipment
																		// has
																		// already
																		// been
																		// assigned
																		// skip
																		// it
						testingNode = new VRPTWNodes(
								(VRPTWShipment) testingNode.getShipment()
										.getNext());
						continue;
					}
					boolean feasibleFound = false;
					// Grab the starting two nodes of the emerging route
					i = (VRPTWNodes) currNodeLL.getHead(); // The head of the
															// list,
					j = (VRPTWNodes) i.getNext(); //

					while (i != currNodeLL.getTail()) {
						double distBtwIU = 0.0, distBtwUJ = 0.0, distBtwIJ = 0.0, currentServiceTime = 0.0, updatedServiceTime = 0.0;

						// Begin calculating C1.1 = Diu + Duj - mew*Dij, mew >=
						// 0
						distBtwIU = calcDist(i.getShipment().getXCoord(),
								testingNode.getShipment().getXCoord(), i
										.getShipment().getXCoord(), testingNode
										.getShipment().getYCoord());

						distBtwUJ = calcDist(testingNode.getShipment()
								.getXCoord(), j.getShipment().getXCoord(),
								testingNode.getShipment().getXCoord(), j
										.getShipment().getYCoord());

						distBtwIJ = calcDist(i.getShipment().getXCoord(), j
								.getShipment().getXCoord(), i.getShipment()
								.getXCoord(), j.getShipment().getYCoord());

						// C1.1 = Diu + Duj - mew*Dij, mew >= 0
						C11 = distBtwIU + distBtwUJ - (mew * distBtwIJ);

						// Begin calculating C1.2 = Bju - Bj; Bju is service
						// time including insertion of u
						currentServiceTime = VRPTWProblemInfo.nodesLLLevelCostF
								.getTotalTravelTime(currNodeLL);

						testingNode.setPrev(i);
						testingNode.setNext(j);
						i.setNext(testingNode);
						j.setPrev(testingNode);

						// if (currNodeLL.getFeasibility().isFeasible()){
						updatedServiceTime = VRPTWProblemInfo.nodesLLLevelCostF
								.getTotalTravelTime(currNodeLL);
						// }

						C12 = updatedServiceTime - currentServiceTime;
						
						//Calculation of C1.3 Utilizing the urgency of the customer in the route
						C13 = testingNode.getShipment().getLateTime() - updatedServiceTime;

						// Calculate total C1
						C1 = (deltaOne * C11) + (deltaTwo * C12) + (deltaThree * C13);

						// If this route will be feasible, check to see if this
						// is the best cost
						if (currNodeLL.getFeasibility().isFeasible()) {
							if (C1 < bestValue) {
								// Store this node into a list of feasible nodes
								// for this route
								testingNode.setBestInsertionCost(C1);
								testingNode.setCellToInsertInFront(i);
								testingNode.setCellToInsertBehind(j);
								feasibleShipments.add(feasibleListPosition,
										testingNode.clone());
								feasibleFound = true;
								bestValue = C1;
							}
						} else {
							// Reset the values of i and j
							i.setNext(j);
							j.setPrev(i);

							return false;
						}
						// Reset the values of i and j
						i.setNext(j);
						j.setPrev(i);

						i = j;
						j = (VRPTWNodes) i.getNext();
					}
					if (feasibleFound)
						++feasibleListPosition;
					// Update currentShipmentValues
					testingNode = new VRPTWNodes((VRPTWShipment) testingNode
							.getShipment().getNext());
				}

				/* Begin calculation of C2, find the optimal value of C2 for 
				* all unrouted and feasible shipments
				*/
				double best = Double.NEGATIVE_INFINITY;
				for (VRPTWNodes currentNode : feasibleShipments) {
					double localC2 = 0.0;
					if(currentNode.getShipment().getIsAssigned()){
						continue;
					}
					localC2 = (lambda * calcDist(allShipments.getHead()
							.getXCoord(),
							currentNode.getShipment().getXCoord(), allShipments
									.getHead().getYCoord(), currentNode
									.getShipment().getYCoord()))
							- currentNode.getBestInsertionCost();

					if (localC2 > best) {
						cellToInsert = currentNode;
						best = localC2;
					}
				}
				
				//Empty out the list for the next round of calculations
				feasibleShipments.clear();

				/*
				 * If there was a shipment that was able to be inserted into the 
				 * take care of the nessecary insertion into the route
				 * */
				if (cellToInsert != null) {
					cellToInsert.setPrev(cellToInsert.getCellToInsertInFront()); // i
					cellToInsert.setNext(cellToInsert.getCellToInsertBehind()); // j
					//Set the travel time for the wait time calculations
					
					cellToInsert.getCellToInsertInFront().setNext(cellToInsert);
					cellToInsert.getCellToInsertBehind().setPrev(cellToInsert);
					
					cellToInsert.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
					cellToInsert.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));
				}

				// If the route is not feasible, return route to previous
				// state and return false
				if (!currNodeLL.getFeasibility().isFeasible()) {

					cellToInsert.setNext(null);
					cellToInsert.setPrev(null);
					cellToInsert.getShipment().setTravelTime(0.0f);

					cellToInsert.getCellToInsertInFront().setNext(
							cellToInsert.getCellToInsertBehind());
					cellToInsert.getCellToInsertBehind().setPrev(
							cellToInsert.getCellToInsertInFront());
					
					return false;
				}
			}

			/*
			 * If there was a shipment that was found insertable, set it 
			 * to be assigned for the problem to know about. Otherwise, exit the
			 * loop and find the next seed shipment
			 * */
			if (cellToInsert != null) {
				cellToInsert.getShipment().setIsAssigned(true);
				theShipment = (VRPTWShipment) cellToInsert.getShipment();
				theShipment.setIsAssigned(true);
			} else {
				return true;
			}
			
			//Calculate total stats and clear out cell to insert
			VRPTWProblemInfo.nodesLLLevelCostF.calculateTotalsStats(currNodeLL);
			cellToInsert = null;
			
			
			//Print the current route to the console
			{
				System.out.println("Route is:");
				VRPTWNodes tempPtr = (VRPTWNodes) currNodeLL.getHead();
				while (tempPtr != (VRPTWNodes) currNodeLL.getTail()) {
					System.out.print(tempPtr.getShipment().getIndex() + "("
							+ tempPtr.getShipment().getDemand() + ")-");
					tempPtr = (VRPTWNodes) tempPtr.getNext();
				}
				System.out.println();
			}
		}
	}

	// The WhoAmI methods gives the id of the assigned object
	// It is a static method so that it can be accessed without creating an
	// object
	@Override
	public String toString() {
		return ("Insertion Type: Insertion Criterion iii");
	}
	
	public static String getID(){
		return ("I3");
	}
}


/**
* SavingsInsertHeuristic - 
* Mimics Savings Heuristic outlined in Solomon's paper
* "This procedure begins with n distinct routes in which each customer is served by a dedicated vehicle" (Solomon 4).
* This heuristic does not exactly fit into Zeus's selection-insertion model, so this is an approximation of it using a dumby route for each shipment.
* @author Eric McAlpine
*
*/
class SavingsInsertHeuristic extends VRPTWNodesLinkedList {
	//similar to linear greedy insert shipment, but compares results to empty truck
	//to simulate every node is in their own truck to start
	/**
	* Tries to insert shipment into NodesLinkedList.
	* If infeasible or not cost efficient, will refuse to insert.
	*
	* @author Eric McAlpine
	* @param currNodeLL the VRPTWNodesLinkedList that the shipment is to be inserted into
	* @param theShipment VRPTWShipment to be inserted
	* @return boolean value indicating whether or not shipment was inserted
	*/
	public boolean getInsertShipment(VRPTWNodesLinkedList currNodeLL,
			VRPTWShipment theShipment) {
		VRPTWNodes tmpPtr;
		VRPTWNodes theCell = new VRPTWNodes(theShipment);
		System.out.println("theShimpment:"+theShipment.getDemand());
		//determine if route is empty
		if(currNodeLL.getHead().getNext() == currNodeLL.getTail()){
			currNodeLL.setHeadNext(theCell);
			currNodeLL.getTail().setPrev(theCell);
			theCell.setPrev(currNodeLL.getHead());
			theCell.setNext(currNodeLL.getTail());
//			theCell.getShipment().setTravelTime(
//					(float) VRPTWProblemInfo.nodesLLLevelCostF
//							.getTotalTravelTime(currNodeLL));
			
			theCell.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
			theCell.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));
			
			//check feasibility
			/*if(!currNodeLL.getFeasibility().isFeasible()){
				//remove node
				tmpPtr= (VRPTWNodes) currNodeLL.getHead().getNext();
				tmpPtr.setNext(null);
				tmpPtr.setPrev(null);
				//repoint head/tail
				currNodeLL.setHeadNext(currNodeLL.getTail());
				currNodeLL.getTail().setPrev(currNodeLL.getHead());
			}*/
		}
		//route not empty
		else{
			double cost = Double.MAX_VALUE;
			VRPTWNodes costCell = null;
			VRPTWNodes prevCell = (VRPTWNodes) currNodeLL.getHead();
			VRPTWNodes nextCell = (VRPTWNodes) currNodeLL.getHead().getNext();
			
			while(nextCell != currNodeLL.getTail()){
				prevCell.setNext(theCell);	//insert node into list
				theCell.setPrev(prevCell);
				theCell.setNext(nextCell);
				nextCell.setPrev(theCell);
				theCell.getShipment().setTravelTime(
						(float) VRPTWProblemInfo.nodesLLLevelCostF
								.getTotalTravelTime(currNodeLL));
				
				if(currNodeLL.getFeasibility().isFeasible()){
					double tempCost = VRPTWProblemInfo.nodesLLLevelCostF.
							getTotalCost(currNodeLL);
					if(tempCost<cost){
						cost = tempCost;
						costCell=prevCell;
					}
				}
				//remove node
				prevCell.setNext(nextCell);
				nextCell.setPrev(prevCell);
				theCell.setNext(null);
				theCell.setPrev(null);
				
				prevCell=nextCell;
				nextCell= (VRPTWNodes) prevCell.getNext();				
			}//end while
			
			if(costCell != null){
				//create route with just node-to-insert to simulate every shipment
				//having its own route to start
				VRPTWNodesLinkedList tempNLL;
				try{
					tempNLL = (VRPTWNodesLinkedList) currNodeLL.clone();	//will start with empty list
				}
				catch(Exception e){
					currNodeLL.setAttributes(new VRPTWAttributes());
					tempNLL = (VRPTWNodesLinkedList) currNodeLL.clone();
				}
//				tempNLL.setHead((Nodes) currNodeLL.getHead().clone());
//				tempNLL.setTail((Nodes) currNodeLL.getTail().clone());
//				tempNLL.setHeadNext(tempNLL.getTail());
//				tempNLL.setFeasibility(currNodeLL.getFeasibility());
//				tempNLL.getInsertShipment(tempNLL , theShipment);			//meaning shipment will be alone
				theShipment.setIsAssigned(false);	//would be set assigned in temporary linked list
				double tempCost = VRPTWProblemInfo.nodesLLLevelCostF.
						getTotalCost(tempNLL);			//get cost of solo-route
				if(tempCost > cost)						//if cheaper than any feasible solution here,
					return false;						//not feasible to be inserted here
					//otherwise, insert as usual
				prevCell = costCell;
				nextCell = (VRPTWNodes) prevCell.getNext();
				prevCell.setNext(theCell);
				theCell.setPrev(prevCell);
				theCell.setNext(nextCell);
				nextCell.setPrev(theCell);
//				theCell.getShipment().setTravelTime(
//						(float) VRPTWProblemInfo.nodesLLLevelCostF
//								.getTotalTravelTime(currNodeLL));
				
				theCell.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
				theCell.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));
			}
			else {				//if better off in its own route,
				return false;	//it will be placed in its own automatically
			}
		}

		theShipment.setIsAssigned(true);
		VRPTWProblemInfo.nodesLLLevelCostF.calculateTotalsStats(currNodeLL);

		{
			System.out.println("Route is:");
			VRPTWNodes tempPtr = (VRPTWNodes) currNodeLL.getHead();
			while (tempPtr != (VRPTWNodes) currNodeLL.getTail()) {
				System.out.print(tempPtr.getIndex() + "(" + tempPtr.getDemand()
						+ ")-");
				tempPtr = (VRPTWNodes) tempPtr.getNext();
			}
			System.out.println();
		}

		return true;
		
		
	}
	
	public String toString() {
		return ("Insertion Type: Savings Insertion Heuristic");
	}
	
	public static String getID(){
		return ("SAV");
	}
	
}

/**
 * Savings Heuristic with additional considerations to time windows
 * Before, it may have been beneficial to combine two routes even if resulted in large wait times.
 * This heuristic ensures that the savings will not let the waiting time be too large of a portion
 * @author Eric McAlpine
 *
 */
class SavingsTimeOriented extends VRPTWNodesLinkedList {
	//similar to linear greedy insert shipment, but compares results to empty truck
	//to simulate every node is in their own truck to start
	/**
	 * Tries to insert the shipment into the most cost effective point in the route given, while maintaining the feasibility and keeping wait time below 20%
	 * @author Eric McAlpine
	 * @param currNodeLL NodesLinkedList to insert shipment into
	 * @param theShipment
	 */
	public boolean getInsertShipment(VRPTWNodesLinkedList currNodeLL,
			VRPTWShipment theShipment) {
		VRPTWNodes tmpPtr;
		VRPTWNodes theCell = new VRPTWNodes(theShipment);
		System.out.println("theShimpment:"+theShipment.getDemand());
		//determine if route is empty
		if(currNodeLL.getHead().getNext() == currNodeLL.getTail()){
			currNodeLL.setHeadNext(theCell);
			currNodeLL.getTail().setPrev(theCell);
			theCell.setPrev(currNodeLL.getHead());
			theCell.setNext(currNodeLL.getTail());
			theCell.getShipment().setTravelTime(
					(float) VRPTWProblemInfo.nodesLLLevelCostF
							.getTotalTravelTime(currNodeLL));
			
			//check feasibility
			/*if(!currNodeLL.getFeasibility().isFeasible()){
				//remove node
				tmpPtr= (VRPTWNodes) currNodeLL.getHead().getNext();
				tmpPtr.setNext(null);
				tmpPtr.setPrev(null);
				//repoint head/tail
				currNodeLL.setHeadNext(currNodeLL.getTail());
				currNodeLL.getTail().setPrev(currNodeLL.getHead());
			}*/
		}
		//route not empty
		else{
			double cost = Double.MAX_VALUE;
			VRPTWNodes costCell = null;
			VRPTWNodes prevCell = (VRPTWNodes) currNodeLL.getHead();
			VRPTWNodes nextCell = (VRPTWNodes) currNodeLL.getHead().getNext();
			
			while(nextCell != currNodeLL.getTail()){
				prevCell.setNext(theCell);	//insert node into list
				theCell.setPrev(prevCell);
				theCell.setNext(nextCell);
				nextCell.setPrev(theCell);
//				theCell.getShipment().setTravelTime(
//						(float) VRPTWProblemInfo.nodesLLLevelCostF
//								.getTotalTravelTime(currNodeLL));
				
				theCell.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
				theCell.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));
				
				if(currNodeLL.getFeasibility().isFeasible()){
					double tempCost = VRPTWProblemInfo.nodesLLLevelCostF.
							getTotalTravelTime(currNodeLL);
					VRPTWNodesLLCostFunctions tempCF = new VRPTWNodesLLCostFunctions();
					double waitTime = tempCF.getTotalWaitingTime(currNodeLL);
					if(tempCost<cost && waitTime<(tempCost/5)){		//now also checks to make sure waitTime 
						cost = tempCost;							//is less than 20% of the cost
						costCell=prevCell;
					}
				}
				//remove node
				prevCell.setNext(nextCell);
				nextCell.setPrev(prevCell);
				theCell.setNext(null);
				theCell.setPrev(null);
				
				prevCell=nextCell;
				nextCell= (VRPTWNodes) prevCell.getNext();				
			}//end while
			
			if(costCell != null){
				//create route with just node-to-insert to simulate every shipment
				//having its own route to start
				VRPTWNodesLinkedList tempNLL;
				try{
					tempNLL = (VRPTWNodesLinkedList) currNodeLL.clone();	//will start with empty list
				}
				catch(Exception e){
					currNodeLL.setAttributes(new VRPTWAttributes());
					tempNLL = (VRPTWNodesLinkedList) currNodeLL.clone();
				}
//				tempNLL.setHead((Nodes) currNodeLL.getHead().clone());
//				tempNLL.setTail((Nodes) currNodeLL.getTail().clone());
//				tempNLL.setHeadNext(tempNLL.getTail());
//				tempNLL.setFeasibility(currNodeLL.getFeasibility());
//				tempNLL.getInsertShipment(tempNLL , theShipment);			//meaning shipment will be alone
				theShipment.setIsAssigned(false);	//would be set assigned in temporary linked list
				double tempCost = VRPTWProblemInfo.nodesLLLevelCostF.
						getTotalTravelTime(tempNLL);			//get cost of solo-route
				if(tempCost > cost)						//if cheaper than any feasible solution here,
					return false;						//not feasible to be inserted here
					//otherwise, insert as usual
				prevCell = costCell;
				nextCell = (VRPTWNodes) prevCell.getNext();
				prevCell.setNext(theCell);
				theCell.setPrev(prevCell);
				theCell.setNext(nextCell);
				nextCell.setPrev(theCell);
//				theCell.getShipment().setTravelTime(
//						(float) VRPTWProblemInfo.nodesLLLevelCostF
//								.getTotalTravelTime(currNodeLL));
				
				theCell.getShipment().setActualArrivalTime((float)currNodeLL.setArrivalTime(currNodeLL));
				theCell.getShipment().setTravelTime((float)currNodeLL.setDeliveryTime(currNodeLL));
			}
			else {				//if better off in its own route,
				return false;	//it will be placed in its own automatically
			}
		}

		theShipment.setIsAssigned(true);
		VRPTWProblemInfo.nodesLLLevelCostF.calculateTotalsStats(currNodeLL);

		{
			System.out.println("Route is:");
			VRPTWNodes tempPtr = (VRPTWNodes) currNodeLL.getHead();
			while (tempPtr != (VRPTWNodes) currNodeLL.getTail()) {
				System.out.print(tempPtr.getIndex() + "(" + tempPtr.getDemand()
						+ ")-");
				tempPtr = (VRPTWNodes) tempPtr.getNext();
			}
			System.out.println();
		}

		return true;
		
		
	}
	
	public String toString() {
		return ("Insertion Type: Savings Time Oriented");
	}
	
	public static String getID(){
		return ("SWT");
	}
	
}