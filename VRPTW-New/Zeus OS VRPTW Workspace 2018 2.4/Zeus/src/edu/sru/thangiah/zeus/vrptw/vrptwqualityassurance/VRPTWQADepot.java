package edu.sru.thangiah.zeus.vrptw.vrptwqualityassurance;

import java.util.Vector;

import edu.sru.thangiah.zeus.qualityassurance.*;

/**
 *
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sam R. Thangiah
 * Edit - Aaron Weckerly
 * @version 2.0
 */
/**
 * VRPTWQADepot a test Depot to run QA tests on
 * Extends QADepot
 */
public class VRPTWQADepot
    extends QADepot
    implements java.io.Serializable, java.lang.Cloneable {
	private double travelTime;
	private double capacity;
/**
 * VRPTWQADepot extends QADepot,
 * includes traveltime and capacity
 */
  public VRPTWQADepot() {
	  travelTime = 0;
	  capacity = 0;
  }

  /**
   * iterates through trucks of depot checking distance
   * @param depot supplied depot to check constraints 
   * @return boolean status if pass or fail
   */
  public boolean checkDistanceConstraint(VRPTWQADepot depot) {
    boolean status = true;
    for (int i = 0; i < getTrucks().size(); i++) {
      VRPTWQATruck truck = (VRPTWQATruck) getTrucks().elementAt(i);
      boolean stat = truck.checkDistanceConstraint(truck);
      status = stat && status;
    }
    return status;
  }

  /**
   * iterates through trucks of depot checking capacity
   * @return boolean status if pass or fail
   */
  public boolean checkCapacityConstraint() {
    boolean status = true;
    for (int i = 0; i < getTrucks().size(); i++) {
      VRPTWQATruck truck = (VRPTWQATruck) getTrucks().elementAt(i);
      boolean stat = truck.checkCapacityConstraint(truck);
      status = stat && status;
    }
    return status;
  }
  /**
   * Stubbed out travel time check/not called
   * handled individually by checking duration, distance, and wait time
   * @return
   */
  public boolean checkTravelTime(){
	  boolean status = true;
	  return status;
  }
  /**
   * Iterates through trucks, calling check early late per truck
   * @return boolean pass/fail
   */
  public boolean checkEarlyLateTimes(){
	  boolean status = true;
	  int numTrucks = getTrucks().size();
	  Vector truckVector =  getTrucks();
	  for(int i = 0; i < numTrucks; i++){
		  VRPTWQATruck truck = (VRPTWQATruck) getTrucks().elementAt(i);
		  boolean stat = truck.checkEarlyLateTimes(truck);
		  if(stat==false)
			  status = status&&stat;
	  }//for number of Trucks  
	  return status;
  }
  /**
   * iterates through trucks, calling check wait time per truck
   * @return boolean pass/fail
   */
  public boolean checkWaitTime(){
	  boolean status = true;
	  int numTrucks = getTrucks().size();
	  Vector truckVector =  getTrucks();
	  for(int i = 0; i < numTrucks; i++){
		  VRPTWQATruck truck = (VRPTWQATruck) getTrucks().elementAt(i);
		  boolean stat = truck.checkWaitTime(truck);
		  if(stat==false)
			  status = status&&stat;
	  }//for number of Trucks  
	  return status;
  }
  /**
   * Sets the travel time for VRPTWQADepot to param t
   * @param t - time to set
   */
  public void setTravelTime(double t){
	  if(t>0)
		  travelTime = t;
	  else
		  System.out.println("Travel Time not set");
  }//set travel time
  /**
   * retrieves travel time of VRPTWQADepot
   * @return double traveltime
   */
  public double getTravelTime(){
	  return travelTime;
  }
  /**
   * Sets capacity of VRPTWQADepot to param t
   * @param t - capacity to set
   */
  public void setCapacity(double t){
	  if(t>0)
		  capacity = t;
	  else
		  System.out.println("Capacity not set");
  }//set travel time
  /**
   * retrieves capacity of VRPTWQADepot
   * @return double capacity of VRPTWQADepot
   */
  public double getCapacity(){
	  return capacity;
  }

}
