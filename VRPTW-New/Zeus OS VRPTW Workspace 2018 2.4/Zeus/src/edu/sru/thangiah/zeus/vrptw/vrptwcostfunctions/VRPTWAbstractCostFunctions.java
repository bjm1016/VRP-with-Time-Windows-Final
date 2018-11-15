package edu.sru.thangiah.zeus.vrptw.vrptwcostfunctions;

import edu.sru.thangiah.zeus.core.CostFunctions;
import edu.sru.thangiah.zeus.vrptw.VRPTWNodesLinkedList;
abstract public class VRPTWAbstractCostFunctions implements CostFunctions {
	
	 /** Methods not used by VRP in the computation of cost can be declared
	   * here with dummy methods. Then have the cost functions in the VRP
	   * inherit this class instead of the CostFunctions class.
	   */

	  public float getTotalScore(Object o) {
	    return 0;
	  }

	  public void setTotalScore(Object o) {

	  }  
	  
	  public abstract void setTotalWaitingTime(Object o);
	  
	  public abstract double getTotalWaitingTime(Object o);

	  public abstract void setTotalServiceTime(Object o);
	  
	  public abstract double getTotalServiceTime(Object o);
	  
}
