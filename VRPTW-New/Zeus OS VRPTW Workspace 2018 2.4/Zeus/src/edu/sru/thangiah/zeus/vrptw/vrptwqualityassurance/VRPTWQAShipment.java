package edu.sru.thangiah.zeus.vrptw.vrptwqualityassurance;

import edu.sru.thangiah.zeus.qualityassurance.*;

/**
 *
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sam R. Thangiah
 * edit Aaron Weckerly
 * @version 2.0
 */
public class VRPTWQAShipment
    extends QAShipment
    implements java.io.Serializable, java.lang.Cloneable {
	private int early = 0;
	private int late = 0;
	private int duration = 0;
	/**
	 * creates VRPTWQAShipment object containing
	 * early, late, duration, and QAShipment inherited varibale
	 * used to store Shipment info to verify in QA
	 */
  public VRPTWQAShipment() {
  }
  /**
   * Set VRPTWWAShipment early time
   * @param e - int early time to set
   */
  public void setEarly(int e){
	  early = e;
  }//early time
  /**
   * retrieve early time
   * @return - int early time
   */
  public int getEarly(){
	  return early;
  }
  /**
   * set VRPTWQAShipment late time
   * @param l - int late time to set
   */
  public void setLate(int l){
	  late = l;
  }//late time
  /**
   * retrieve late time of VRPTWQAShipment
   * @return int late time
   */
  public int getLate(){
	  return late;
  }
  /**
   * set the duration for VRPTWQAShipment
   * @param d - int duration to set
   */
  public void setDuration(int d){
	  duration = d;
  }//duration
  /**
   * retrieve duration of VRPTWQAShipment
   * @return - int duration
   */
  public int getDuration(){
	  return duration;
  }
  /**
   * get string of info pertaining to VRPTWQAShipment
   * @return string of information
   */
  public String getString(){
		String str = "";
		str= str+"Index: "+this.getIndex()+" X: "+this.getX()+" Y: "+this.getY()+" CustID: "+this.getCustId();
		str = str+" Demand: "+this.getDemand()+" Ear: "+this.getEarly()+" Lat: "+this.getLate()+" ServT: "+this.getDuration();
		return str;
	}//get string
}
