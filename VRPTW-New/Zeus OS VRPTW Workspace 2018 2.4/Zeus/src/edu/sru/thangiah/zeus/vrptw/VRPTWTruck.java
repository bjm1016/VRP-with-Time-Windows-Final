package edu.sru.thangiah.zeus.vrptw;


import edu.sru.thangiah.zeus.core.Truck;

/**
 * VRPTWTruck extends Truck class and is adapted to handle
 * time windows problem and the inherited classes associated with it
 *
 *
 */
public class VRPTWTruck extends Truck implements java.io.Serializable,
		java.lang.Cloneable {
	
	/**
	 * Constructor
	 *
	 */
	public VRPTWTruck(){
		setMainNodes(new VRPTWNodesLinkedList());
		setAttributes(new VRPTWAttributes());
	}
	
	/**
	   * Constructor
	   * @param tt truck type
	   * @param depotX depot's x coordinate
	   * @param depotY depot's y coordinate
	   */
	  public VRPTWTruck(VRPTWTruckType tt, double depX, double depY) {
	    //super(tt, depX, depY);
	    setAttributes(new VRPTWAttributes());
	    setDepotX(depX);
	    setDepotY(depY);
	    setTruckNum(VRPTWProblemInfo.getNumTrucks() + 1);
	    setTruckType(tt);

	    //TODO: implement this constructor
	    setMainNodes(new VRPTWNodesLinkedList(tt, depX, depY, getTruckNum()));

	  }
	  
	  /**
	   * Returns the attributes object associated with this truck
	   *
	   * @return VRPTWAttributes object
	   */
	  @Override
		public VRPTWAttributes getAttributes(){
			return (VRPTWAttributes) super.getAttributes();
		}
	  
	  /**
	   * Returns the NodesLinkedList for this truck
	   *
	   * @return VRPTWNodesLinkedList
	   */
	  public VRPTWNodesLinkedList getVRPTWMainNodes(){
		  return (VRPTWNodesLinkedList) getMainNodes();
	  }
	  
	  /**
	   * Returns next truck in the list
	   *
	   * @return VRPTWTruck which appears next in list
	   */
	  public VRPTWTruck getVRPTWNext(){
		  return (VRPTWTruck)getNext();
	  }
	  
	  /**
	   * Returns a copy of this truck
	   *
	   * @return VRPTWTruck object identical to this one
	   */
	  public Object clone(){
		  VRPTWTruck cloned = new VRPTWTruck();
		  
		  cloned.setAttributes(this.getAttributes());
		  cloned.setDepotX(this.getDepotX());
		  cloned.setDepotY(this.getDepotY());
		  cloned.setNext(this.getNext());
		  cloned.setPrev(this.getPrev());
		  cloned.setMainNodes(this.getVRPTWMainNodes());
		  return cloned;
	  }

}
