package edu.sru.thangiah.zeus.vrptw.vrptwqualityassurance;

import edu.sru.thangiah.zeus.qualityassurance.*;

/**
 *
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sam R. Thangiah
 * Edit Aaron Weckerly
 * @version 2.0
 */

public class VRPTWQADepotLinkedList
    extends QADepotLinkedList
    implements java.io.Serializable, java.lang.Cloneable {
	/**
	 * VRPTWWADepotLinkedList contains VRPTWQADepots to check constraints upon
	 * extends QADepotLinkedList
	 */
  public VRPTWQADepotLinkedList() {
  }

  /**
   * iterates through depot linked list calling depot check for each depot
   * @return boolean pass/fail
   */
  public boolean checkDistanceConstraint() {
    boolean status = true;
    for (int i = 0; i < getDepots().size(); i++) {
      VRPTWQADepot depot = (VRPTWQADepot) getDepots().elementAt(i);
      status = status && depot.checkDistanceConstraint(depot);
    }
    return status;
  }

  /**
   * iterates through depot linked list calling capacity check for each depot
   * @return boolean pass/fail
   */
  public boolean checkCapacityConstraint() {
   boolean status = true;
   for (int i = 0; i < getDepots().size(); i++) {
     VRPTWQADepot depot = (VRPTWQADepot) getDepots().elementAt(i);
     status = status && depot.checkCapacityConstraint();
   }
   return status;
 }
  /**
   * iterates through depot linked list calling early late check for each depot
   * @return boolean pass/fail
   */
  public boolean checkEarlyLateTimes(){
	  boolean status = true;
	   for (int i = 0; i < getDepots().size(); i++) {
	     VRPTWQADepot depot = (VRPTWQADepot) getDepots().elementAt(i);
	     status = status && depot.checkEarlyLateTimes();
	   }
	   return status;
  }//check early late times
  /**
   * iterates through depot linked list calling travel time check for each depot
   * @return boolean pass/fail
   */
  public boolean checkTravelTime(){
	  boolean status = true;
	  for (int i = 0; i < getDepots().size(); i++) {
		     VRPTWQADepot depot = (VRPTWQADepot) getDepots().elementAt(i);
		     status = status && depot.checkTravelTime();
		   }
	  return status;
  }//check travel time
  /**
   * iterates through depot linked list calling wait time check for each depot
   * @return boolean pass/fail
   */
  public boolean checkWaitTime(){
	  boolean status = true;
	  for (int i = 0; i < getDepots().size(); i++) {
		     VRPTWQADepot depot = (VRPTWQADepot) getDepots().elementAt(i);
		     status = status && depot.checkWaitTime();
		   }
	  return status;
  }//check travel time
}
