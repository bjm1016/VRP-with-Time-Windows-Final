package edu.sru.thangiah.zeus;

import edu.sru.thangiah.zeus.vrp.VRPRoot;
import edu.sru.thangiah.zeus.vrptw.VRPTWRoot;

import javax.swing.*;

/**
 * Calls the main functions of Zeus.
 * Title: Zeus
 * Description: This class calls the main root method for the problem to be solved
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: </p>
 * @author Sam R. Thangiah
 * @version 2.0
 */

public class Zeus {
  /**
   * Main function for Zeus
   * @param args command line arguments (not used)
 * @throws Exception 
   */
  public static void main(String[] args) throws Exception {
	  
	  
    //Solve the VRPTW Problem
    VRPTWRoot theRoot = new VRPTWRoot();
    //TOPRoot theRoot = new TOPRoot();
    //MDVRPRoot theRoot = new MDVRPRoot();
	//HDMDVRPRoot theRoot = new HDMDVRPRoot();
	  

	//int minCapacity=0;
	//int maxCapacity=0; 
	//int maxMiles=0;
	//int maxStops=0;
	
	System.gc();

  }
}
