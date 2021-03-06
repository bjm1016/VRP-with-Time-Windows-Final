package edu.sru.thangiah.zeus.vrptw;

//import the parent class
import edu.sru.thangiah.zeus.core.Shipment;

/**
 * 
 * @author Marc S / Eric M
 * @version 1.0
 */

public class VRPTWShipment 
	extends Shipment 
	implements java.io.Serializable, java.lang.Cloneable {

	private int early;	//additional time window parameters
	private int late;
	private VRPTWNodesLinkedList assignedRoute;
	private VRPTWShipmentLinkedList shipments;
	
	private VRPTWNodes nextNode;
	private VRPTWNodes prevNode;
	
	
	private long lastTimeChecked;
	private double actualDeliveryTime;
	private double actualArrivalTime;
	
	//private VRPTWShipment next;
	//private VRPTWShipment prev;
	
	public VRPTWShipment(){
		super();
	}
	
	

	/**
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param q demand
	 * @param i customer number 
	 * @param e EAR
	 * @param l LAT
	 * @param d Service duration
	 */
	public VRPTWShipment(float x, float y, int q, int i, int e, int l, int d){
		//super();
		setIndex(i);
		setXCoord(x);
		setYCoord(y);
		setDemand(q);
		setEarlyTime(e);
		setLateTime(l);
		setServiceTime(d);
		setIsAssigned(false);
		setIsSelected(false);
		setTruckTypeNeeded("1");
		//System.out.println("Shipment created");
	}
	
	/**
	 * Override parent clone function
	 * 
	 * @return copy of VRPTWShipment that called this
	 * @author Eric McAlpine
	 */
	public VRPTWShipment clone(){
		VRPTWShipment clonedShipment = new VRPTWShipment();
		clonedShipment.setIndex(this.getIndex());
		clonedShipment.setXCoord(this.getXCoord());
		clonedShipment.setYCoord(this.getYCoord());
		clonedShipment.setDemand(this.getDemand());
		clonedShipment.setEarlyTime((int) this.getEarlyTime());
		clonedShipment.setLateTime((int) this.getLateTime());
		clonedShipment.setServiceTime(this.getServiceTime());
		clonedShipment.setIsAssigned(this.getIsAssigned());
		clonedShipment.setIsSelected(this.getIsSelected());
		clonedShipment.setTruckTypeNeeded(this.getTruckTypeNeeded());
		
		
		return clonedShipment;
	}
	/**
	*return the float early time of the VRPTWShipment
	*
	*@return float early time of shipment
	*/
	public float getEarlyTime(){
		return early;
	}
	/**
	*set the float early time of the VRPTWShipment
	*
	*@param e early time is set for the shipment
	*/
	public void setEarlyTime(int e){
		if(e>0)
			early=e;
	}
	/**
	*return the float late time of the VRPTWShipment
	*
	*@return float late time of shipment
	*/
	public float getLateTime(){
		return late;
	}
	/**
	*set the float late time of the VRPTWShipment
	*
	*@param l late time is set for the shipment
	*/
	public void setLateTime(int l){
		if(l>early && l>0)
			late=l;
	}
	/**
	*Set the VRPTWShipment linked list to a supplied shipment linked list
	*
	*@param shipLL - Supplied shipment linked list to set this VRPTW
	*/
	public void setShipments(VRPTWShipmentLinkedList shipLL){
		this.shipments = shipLL;
	}
	/**
	*This will return the main shipments that are tie to a shipment
	*
	*/
	public VRPTWShipmentLinkedList getShipments() {
		return shipments;
	}
	/**
	* This returns the VRPTWNodesLinkedList that the shipment is assigned to
	*
	*/
	public VRPTWNodesLinkedList getAssignedRoute() {
		return assignedRoute;
	}
	/**
	*This will update the route that the shipment is assigned to
	*
	*/
	public void setAssignedRoute(VRPTWNodesLinkedList assignedRoute) {
		this.assignedRoute = assignedRoute;
	}
	/**
	*This returns the node that represents the next shipment in the route
	*
	*/
	public VRPTWNodes getNextNode() {
		return nextNode;
	}
	/**
	*This will set the node that represents the next shipment in the route
	*
	*/
	public void setNextNode(VRPTWNodes nextNode) {
		this.nextNode = nextNode;
	}
	/**
	*This returns the node that represents the previous shipment in the route
	*
	*/
	public VRPTWNodes getPrevNode() {
		return prevNode;
	}
	/**
	*This will set the node that represents the previous shipment in the route
	*
	*/
	public void setPrevNode(VRPTWNodes prevNode) {
		this.prevNode = prevNode;
	}



	public long getLastTimeChecked() {
		return lastTimeChecked;
	}



	public void setLastTimeChecked(long lastTimeChecked) {
		this.lastTimeChecked = lastTimeChecked;
	}



	public double getActualDeliveryTime() {
		return actualDeliveryTime;
	}



	public void setActualDeliveryTime(double actualDeliveryTime) {
		this.actualDeliveryTime = actualDeliveryTime;
	}



	public double getActualArrivalTime() {
		return actualArrivalTime;
	}



	public void setActualArrivalTime(double actualArrivalTime) {
		this.actualArrivalTime = actualArrivalTime;
	}
}
