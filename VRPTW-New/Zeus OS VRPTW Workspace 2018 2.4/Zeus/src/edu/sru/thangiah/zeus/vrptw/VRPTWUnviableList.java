package edu.sru.thangiah.zeus.vrptw;

import java.util.ArrayList;

//import edu.sru.thangiah.zeus.core.ZeusProblemInfo;

public class VRPTWUnviableList {
	private ArrayList<VRPTWUnviable> badRoutes = new ArrayList<VRPTWUnviable>();
	private double maxDistance = 0;
	private double depotX = 0; 
	private double depotY = 0;
	public VRPTWUnviableList()
	{
		
	}

	public VRPTWUnviableList(ArrayList<VRPTWShipment> shipList, ArrayList<VRPTWTruckType> ttList, VRPTWDepot depot)
	{
		//VRPTWTruck truck = (VRPTWTruck)ZeusProblemInfo.getTruckTypes();
		//VRPTW
		depotX = depot.getXCoord();
		depotY = depot.getYCoord();
		VRPTWTruckType truck = ttList.get(0);
		maxDistance = truck.getMaxDuration();
		findInfeasibleRoutes(shipList);	
	}
	
	public int length() { return badRoutes.size(); }
	private void findInfeasibleRoutes(ArrayList<VRPTWShipment> shipList)
	{
		ArrayList<VRPTWShipment> a = shipList;
		for(VRPTWShipment testShip : shipList)
		{
			VRPTWUnviable badRoute = new VRPTWUnviable(testShip.getIndex());
			
			for(VRPTWShipment ship: a)
			{
				if(isFeasible(testShip, ship) == false)
				{
					//System.out.println("running");

					badRoute.addToList(ship.getIndex());
				}
				
			}
			badRoutes.add(badRoute);
			
		}
	}
	
	private boolean isFeasible(VRPTWShipment a, VRPTWShipment b)
	{	
		boolean feasible = true;
		double routeDistance = 0;
		double ptpdistance = 0;
		//System.out.println(maxDistance + "\n");
		ptpdistance += calculateDistance(a.getXCoord(), b.getXCoord(), a.getYCoord(), b.getYCoord());
		//System.out.println(routeDistance + "\n");

		routeDistance = ptpdistance +  calculateDistance(b.getXCoord(), depotX, b.getYCoord(), depotY);
		//System.out.println(maxDistance + "\n");

		if(routeDistance > maxDistance)
		{
		feasible = false;
		}
		
		if((a.getEarlyTime() + a.getServiceTime()) + ptpdistance > b.getLateTime())
		{
		    feasible = false;
		}
		//if(a)

		return feasible;
	}
	
	private double calculateDistance(double x1, double x2, double y1, double y2)
	{
		return Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2), 2));
	}
	public String toString()
	{
		String ret = ""; 
		for(VRPTWUnviable a: badRoutes)
		{
			ret += a.toString() + "\n";
		}
		return ret;
	}
	public void print()
	{
		System.out.print(this.toString());
	}
	
	public boolean contains(int shipNum, int secShipNum)
	{
		//VRPTWUnviable find = new VRPTWUnviable(shipNum, secShipNum);
		for(VRPTWUnviable a : badRoutes)
		{
			if(a.getShipNo() == shipNum)
			{
				if(a.has(secShipNum))
				{
				return true;
				}
			}
		}
		return false;
	}
}
