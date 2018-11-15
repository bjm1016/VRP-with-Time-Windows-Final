package edu.sru.thangiah.zeus.vrptw.vrptwqualityassurance;

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

public class VRPTWQANode
    extends QANode
    implements java.io.Serializable, java.lang.Cloneable {
	private int earlyTime = 0;
	private int lateTime = 0;
	private int serviceTime = 0;
	private int duration = 0;
	private int waitTime = 0;
	/**
	 * VRPTWQANode
	 * extends QANode
	 */
  public VRPTWQANode() {
  }
  /**
   * set early time of VRPTWQANode
   * @param ear - int value of early time to set
   */
  public void setEarly(int ear){
	  if(ear >= 0)
		  earlyTime = ear;
	  else
		  System.out.println("Early time Negative, not added");
  }//set Early
  /**
   * set late time of VRPTWQANode
   * @param lat - int value of late time to set
   */
  public void setLate(int lat){
	  //change to throw errors?
	  if(lat >= 0)
		  lateTime = lat;
	  else
		  System.out.println("Late time Negative, not added");
  }//set Late
  /**
   * set service time of VRPTWQANode
   * @param servTime - int value to set as service time
   */
  public void setService(int servTime){
	  if(servTime >=0){
		  serviceTime = servTime;
	  }
	  else
		  System.out.println("Service Time Negative, not added");
  }
  /**
   * set the wait time for the VRPTWQANode
   * @param wait - the integer wait time for the specific node
   */
  public void setWaitTime(int wait){
	  if(wait > 0){
		  waitTime = wait;
	  }
	  else System.out.println("Wait time not set, negative");
  }
  /**
   * sets the duration of the VRPTWQADuration
   * 
   * @param dur duration to set to node
   */
  public void setDuration(int dur){
	  if(dur > 0){
		  duration = dur;
	  }
  }
  /**
   * retrieve early time for VRPTWQANode
   * @return int early time
   */
  public int getEarly(){
	  return earlyTime;
  }
  /**
   * retrieve early time for VRPTWQANode
   * @return int late time
   */
  public int getLate(){
	  return lateTime;
  }
  /**
   * retrieve early time for VRPTWQANode
   * @return int service time
   */
  public int getServiceTime(){
	  return serviceTime;
  }
  /**
   * retrieves the wait time for the specific VRPTWQANode
   * @return integer wait time value
   */
  public int getWaitTime(){
	  return waitTime;
  }
  /**
   * retrieve the duration for the specific VRPTWQANode
   * 
   * @return integer duration value
   */
  public int getDuration(){
	  return duration;
  }
}