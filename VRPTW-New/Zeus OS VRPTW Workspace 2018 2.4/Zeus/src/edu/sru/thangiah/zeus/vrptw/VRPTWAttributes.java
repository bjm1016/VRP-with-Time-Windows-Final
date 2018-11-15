package edu.sru.thangiah.zeus.vrptw;

import java.text.DecimalFormat;

/** I'VE BEEN DOCUMENTED!!!!!!!!!!!!!!!!!!!! */

import edu.sru.thangiah.zeus.core.Attributes;
/** The class VRPTWAttributes extends and utilizes the parent
 * Attributes class. To fit the VPRTW problem this class includes 
 * additional methods and attributes to assist with the problem
 * 
 * @author mas1029
 *
 */
public class VRPTWAttributes extends Attributes
	implements java.io.Serializable, java.lang.Cloneable{
	/** The waiting time accumulated for the current route */ 
	private double totalWaitingTime = 0;
	/** The total service time and distance for the route */
	private double totalServiceTime = 0;
	
	/** Default constructor of VRPTWAttributes utilizes the 
	 * parent to generate nessecary attributes
	 */
	public VRPTWAttributes(){
		super();
	}

	/**
	 * Gets the total waiting time for the route
	 * 
	 * @return the total waiting time of the route
	 */
	public double getTotalWaitingTime() {
		return totalWaitingTime;
	}

	/**
	 * Updates the total waiting time of the route
	 * 
	 * @param totalWaitingTime the updated waiting time
	 */
	public void setTotalWaitingTime(double totalWaitingTime) {
		this.totalWaitingTime = totalWaitingTime;
	}
	
	/**
	 * Gets the total service time for the route
	 * 
	 * @return the total service time of the route
	 */
	public double getTotalServiceTime() {
		return totalServiceTime;
	}
	
	/**
	 * Updates the total service time of the route
	 * 
	 * @param totalServiceTime the updated service time
	 */
	public void setTotalServiceTime(double totalServiceTime) {
		this.totalServiceTime = totalServiceTime;
	}
	
	
	/**
	 * Utilizing cost functions this will allow for the problem to display information
	 * of a String type
	 * 
	 * @return The string representing the results received from the current route
	 */
	@Override
	public String toDetailedString() {
	    DecimalFormat df = new DecimalFormat("0.00");

	    return "Total Demand = " + df.format(getTotalDemand()) +
	        "| Total Distance = " + df.format(getTotalDistance()) +
	        "| Total Travel Time = " + df.format(getTotalTravelTime()) + 
	        "| Total Waiting Time = "+ df.format(getTotalWaitingTime()) + 
	        "| Total Service Time = " + df.format(getTotalServiceTime() + getTotalWaitingTime()) +
	        "| Total Cost = $" + df.format(getTotalServiceTime() + getTotalWaitingTime()) +"|";
	  }
	
	/**This clone method allows for an instance of VRPTWAttributes 
	 * to be copied to another vessel. 
	 * 
	 * @return An instance of the cloned VRPTWAttributes
	 */
	public Object clone(){
		VRPTWAttributes cloned = new VRPTWAttributes();
		cloned.setAvgTravelTime(this.getAvgTravelTime());
		cloned.setMaxTravelTime(this.getMaxTravelTime());
		cloned.setTotalCost(this.getTotalCost());
		cloned.setTotalDemand(this.getTotalDemand());
		cloned.setTotalDistance(this.getTotalDistance());
		cloned.setTotalTravelTime(this.getTotalTravelTime());
		cloned.setTotalWaitingTime(this.getTotalWaitingTime());
		return cloned;
	}

	
}
