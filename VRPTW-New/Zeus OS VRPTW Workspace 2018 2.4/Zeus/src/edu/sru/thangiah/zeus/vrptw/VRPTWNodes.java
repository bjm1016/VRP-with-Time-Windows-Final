package edu.sru.thangiah.zeus.vrptw;

import edu.sru.thangiah.zeus.core.Nodes;

/** VRPTWNodes inherits from the Nodes class, but adds information
 * relevant to time windows, such as wait time.
 *
 */
public class VRPTWNodes extends Nodes implements java.io.Serializable,
		java.lang.Cloneable {
	//private wait time variable for Node
	private double waitTime;
	private VRPTWShipment theShipment;
	private double bestInsertionCost = Double.MAX_VALUE;
	private VRPTWNodes cellToInsertInFront;
	private VRPTWNodes cellToInsertBehind;
	private double routeDistance = 0;
	private double routeTime = 0;
	
	/**
	 * Returns the index of the shipment used to create the node
	 *
	 * @return integer index of node
	 *
	 */
	public int getIndex(){
		return theShipment.getIndex();
	}
	
	/**
	 * Returns demand of the shipment used to create the node
	 *
	 * @return demand of the node
	 *
	 */
	public int getDemand(){
		return theShipment.getDemand();
	}
	
	/**
	 * Returns the current best insertion cost found
	 *
	 * @return best insertion cost
	 *
	 */
	public double getBestInsertionCost() {
		return bestInsertionCost;
	}
	
	/**
	 * Sets the best insertion cost. Assumes nothing about values
	 *
	 * @param bestInsertionCost double value of the best cost found 
	 * 
	 */
	public void setBestInsertionCost(double bestInsertionCost) {
		this.bestInsertionCost = bestInsertionCost;
	}

	/**
	 * Constructor
	 *
	 *
	 *
	 */
	public VRPTWNodes() {
	}

	/**
	 * Constructor
	 * 
	 * @param s
	 *            shipment conatining this cells information
	 */
	public VRPTWNodes(VRPTWShipment s) {
		// super(s);
		//super.setShipment(s);
		this.theShipment = (s);
		waitTime = 0.0;
	}

	/**
	 * Returns the next point cell in the linked list
	 * 
	 * @return next point cell
	 */
	public VRPTWNodes getVRPTWNext() {
		return (VRPTWNodes) getNext();
	}
	
	/**
	 * Returns shipment used to create node
	 *
	 * @return VRPTWShipment used in this node
	 */
	@Override
	public VRPTWShipment getShipment(){
		return this.theShipment;//(VRPTWShipment) super.getShipment();
	}

	/**
	 * Sets the shipment used by this node
	 *
	 * @param ship VRPTWShipment to base this node on
	 *
	 */
	public void setShipment(VRPTWShipment ship){
		this.theShipment = ship;
	}
	/**
	 * Creates a copy of this node and returns it. It will not create the next
	 * and prev links, because this would cause infinite recursion. These are
	 * set in the nodes linked list clone() function.
	 * 
	 * @return Object node clone
	 */
	public VRPTWNodes clone() {
		VRPTWNodes clonedNode = new VRPTWNodes();

		clonedNode.theShipment = (VRPTWShipment) this.theShipment;
		clonedNode.cellToInsertInFront = this.cellToInsertInFront;
		clonedNode.cellToInsertBehind = this.cellToInsertBehind;
		clonedNode.bestInsertionCost = this.bestInsertionCost;

		return clonedNode;
	}
	
	/**
	 * Sets the waiting time to a node
	 * bounds checks for greater or equal to zero
	 *
	 * @param t new wait time for node
	 *
	 */
	public void setWaitTime(double t){
		if(t>=0){
			waitTime = t;
		}
		else{
			//how would we like to handle this?
			System.out.println("The wait time was not added, recieved negative value");
			waitTime = 0.0;
		}
	}//set wait time
	
	/**
	 * Returns wait time stored in node
	 *
	 * @return waitTime
	 *
	 */
	public double getWaittime(){
		return waitTime;
	}//get wait time

	/**
	 * Returns reference to which this is to be inserted in front of
	 *
	 * @return reference to Nodes object 
	 */
	public VRPTWNodes getCellToInsertInFront() {
		return cellToInsertInFront;
	}

	/**
	 * Sets the cell which this is to be inserted in front of
	 *
	 * @param cellToInsertInFront reference to VRPTWNodes
	 *
	 */
	public void setCellToInsertInFront(VRPTWNodes cellToInsertInFront) {
		this.cellToInsertInFront = cellToInsertInFront;
	}

	/**
	 * Returns cell which this is to be inserted behind in the linked list
	 *
	 * @return reference to VRPTWNodes
	 *
	 */
	public VRPTWNodes getCellToInsertBehind() {
		return cellToInsertBehind;
	}

	/**
	 * Sets the cell which this is be be inserted behind
	 *
	 * @param cellToInsertBehind VRPTWNodes reference to insert behind
	 *
	 */
	public void setCellToInsertBehind(VRPTWNodes cellToInsertBehind) {
		this.cellToInsertBehind = cellToInsertBehind;
	}

	/**
	 * Returns the time at which this node is visited
	 *
	 * @return route time
	 *
	 */
	public double getRouteTime() {
		return routeTime;
	}

	/**
	 * Sets the time at which this node is visited
	 *
	 * @param routeTime time at which node is serviced
	 *
	 */
	public void setRouteTime(double routeTime) {
		this.routeTime = routeTime;
	}

	/**
	 * Returns the distance into the route this node occurs
	 *
	 * @return route distance 
	 *
	 */
	public double getRouteDistance() {
		return routeDistance;
	}

	/**
	 * Sets the route distance for this node
	 *
	 * @param routeDistance distance into the route at which this node is serviced
	 *
	 */
	public void setRouteDistance(double routeDistance) {
		this.routeDistance = routeDistance;
	}

}
