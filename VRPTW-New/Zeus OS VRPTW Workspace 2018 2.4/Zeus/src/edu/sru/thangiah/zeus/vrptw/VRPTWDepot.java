package edu.sru.thangiah.zeus.vrptw;

import edu.sru.thangiah.zeus.core.Depot;

/** The class VRPTWDepot extends and utilizes the parent
 * Depot class. To fit the VPRTW problem this class includes 
 * additional methods and attributes to assist with the problem
 * 
 * @author mas1029
 *
 */
public class VRPTWDepot extends Depot implements java.io.Serializable,
		java.lang.Cloneable {
	/** The expected time the trucks are due at the depot */
	private double depotDueTime;
	
	/**
	 * Returns the due time of the depot
	 * 
	 * @return the current depot due time
	 */
	public double getDepotDueTime() {
		return depotDueTime;
	}

	/**Updates the time for the trucks to be expected to complete
	 * their deliveries
	 * 
	 * @param depotDueTime the time to be updated
	 */
	public void setDepotDueTime(double depotDueTime) {
		this.depotDueTime = depotDueTime;
	}

	/**
	 * Default constructor for the VRPTWDepot creates the Depot with 
	 * empty values and trucks
	 */
	public VRPTWDepot() {
		setAttributes(new VRPTWAttributes());
		setMainTrucks(new VRPTWTruckLinkedList());
	}

	/** Argumented constructor for the VRPTWDepot it initializes the 
	 * depot with its index, due time, and coordinates 
	 * 
	 * @param i index
	 * @param x x-coord
	 * @param y y-coord
	 * @param d due time
	 */
	public VRPTWDepot(int i, float x, float y, float d) {
		setDepotNum(i);
		setXCoord(x);
		setYCoord(y);
		this.depotDueTime = d;

		setAttributes(new VRPTWAttributes());
		setMainTrucks(new VRPTWTruckLinkedList());
	}

	/**
	 * Returns the truck linked list
	 * 
	 * @return main trucks
	 */
	public VRPTWTruckLinkedList getMainTrucks() {
		return (VRPTWTruckLinkedList) super.getMainTrucks();
	}

	/**
	 * Returns the next depot in the linked list
	 * 
	 * @return next depot
	 */
	public VRPTWDepot getNext() {
		return (VRPTWDepot) super.getNext();
	}

	@Override
	public VRPTWAttributes getAttributes() {
		return (VRPTWAttributes) super.getAttributes();
	}
	/**
	 * Override parent clone function
	 *
	 * @return copy of VRPTWDepot 
	 * @author Eric McAlpine 
	 */
	@Override
	public Object clone(){
		VRPTWDepot clonedDepot = new VRPTWDepot();
		
		clonedDepot.setDepotNum(this.getDepotNum());
		clonedDepot.setxCoord(this.getxCoord());
		clonedDepot.setyCoord(this.getyCoord());
		clonedDepot.setAttributes(this.getAttributes());
		clonedDepot.setMainTrucks(this.getMainTrucks());
		clonedDepot.setDepotDueTime(this.getDepotDueTime());
		
		return clonedDepot;
	}
}
