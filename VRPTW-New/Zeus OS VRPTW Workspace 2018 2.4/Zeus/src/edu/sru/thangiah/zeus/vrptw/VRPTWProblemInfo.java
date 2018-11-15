package edu.sru.thangiah.zeus.vrptw;

import edu.sru.thangiah.zeus.core.ZeusProblemInfo;
import edu.sru.thangiah.zeus.vrptw.vrptwcostfunctions.VRPTWAbstractCostFunctions;

/**
 * The VRPTWProblemInfo class holds additional variables and cost functions that
 * are to be utilized by the VRPTW
 * 
 * @author Marc S
 *
 */
public class VRPTWProblemInfo extends ZeusProblemInfo {
	public VRPTWProblemInfo() {
	}

	public static int heuristicType = 1;
	public static String heurStr = "";

	// public static VRPTWNodesLinkedList insertShipType;

	public static VRPTWAbstractCostFunctions nodesLLLevelCostF;
	public static VRPTWAbstractCostFunctions truckLevelCostF;
	public static VRPTWAbstractCostFunctions truckLLLevelCostF;
	public static VRPTWAbstractCostFunctions depotLevelCostF;
	public static VRPTWAbstractCostFunctions depotLLLevelCostF;
}
