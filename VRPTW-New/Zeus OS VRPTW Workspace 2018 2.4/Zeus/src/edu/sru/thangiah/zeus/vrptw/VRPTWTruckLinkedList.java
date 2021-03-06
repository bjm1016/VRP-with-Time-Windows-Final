package edu.sru.thangiah.zeus.vrptw;

import edu.sru.thangiah.zeus.core.Settings;
import edu.sru.thangiah.zeus.core.TruckLinkedList;


/**
 * VRPTWTruckLinkedList inherits from TruckLinkedList and is
 * adapted to account for time windows and the associated classes
 *
 *
 */
public class VRPTWTruckLinkedList extends TruckLinkedList implements
		java.io.Serializable, java.lang.Cloneable {
	
	/**
	 * Constructor
	 *
	 *
	 */
	public VRPTWTruckLinkedList()
	{
		//Housekeeping for the linked list
		setHead(new VRPTWTruck()); //header node for head
		setTail(new VRPTWTruck()); //tail node for tail
		linkHeadTail();			  //point head and tail to each other
		
		//Assign the attributes	
		setAttributes(new VRPTWAttributes());
	}
	
	/**
	   * Returns the first truck in the linked list
	   * @return first truck
	   */
	  public VRPTWTruck getHead() {
	    return (VRPTWTruck) super.getHead();
	  }
	  
	  /**
	   * Returns the tail node truck in the linked list
	   * @return first truck
	   */
	  public VRPTWTruck getTail() {
	    return (VRPTWTruck) super.getTail();
	  }
	  
	  /**
	   * Returns attributes object for this linked list
	   *
	   * @return VRPTWAttributes object for this linked list
	   */
	  @Override
		public VRPTWAttributes getAttributes(){
			return (VRPTWAttributes) super.getAttributes();
		}
	  

	  //TODO: insertShipment and clone
	  /**
	   * Attempts to insert a given shipment into this list of trucks
	   *
	   * @param theShipment shipment to be inserted
	   * @return true if inserted, false if failed to insert
	   *
	   */
	  public boolean insertShipment(VRPTWShipment theShipment){
		  boolean status = false;
		  
		  VRPTWTruck truck = (VRPTWTruck)this.getHead().getNext();
		  
		  while(truck != this.getTail()){
			  if(truck.compatableWith(theShipment)){
				  status = truck.getVRPTWMainNodes().insertShipment(theShipment);
				  
				  if(status){
					  break;
				  } 
			  }
			  
			  truck = (VRPTWTruck) truck.getNext();
		  }
		  
		  if((!status) && (!Settings.lockTrucks)){
			  VRPTWTruck last = (VRPTWTruck)this.getTail().getPrev();
			  VRPTWTruck first = (VRPTWTruck)this.getHead();
	
		  
			  for(int i = 0; i < VRPTWProblemInfo.getTruckTypesSize(); i++){
				  VRPTWTruckType type = (VRPTWTruckType) VRPTWProblemInfo.getTruckTypesAt(i);
				  
				  if(type.getServiceType().equals(theShipment.getTruckTypeNeeded())){
					  VRPTWTruck newTruck = new VRPTWTruck(type, first.getNext().getDepotX(),
							  first.getNext().getDepotY());
					  
					  status = newTruck.getVRPTWMainNodes().insertShipment(theShipment);
					
					  if(status){
						  System.out.println("** Inserting new Truck");
						  System.out.println("Depot x and y is:"+ first.getNext().getDepotX()+" "+first.getNext().getDepotY());
						  this.insertTruckLast(newTruck);
					  
						  return status;
					  } else {
						  return status;
					  }
				  }
			  }
		  }
		return status;
	  
	  }
}
