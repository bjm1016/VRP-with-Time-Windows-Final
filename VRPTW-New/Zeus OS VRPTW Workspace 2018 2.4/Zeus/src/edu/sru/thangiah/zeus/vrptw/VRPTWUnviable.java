package edu.sru.thangiah.zeus.vrptw;

import java.util.ArrayList;
import java.util.Comparator;

public class VRPTWUnviable {

	private int shipNum;
	private ArrayList<Integer> unviableList = new ArrayList<Integer>();
	private int shipNum2;
	public VRPTWUnviable(int shipmentNo)
	{
		shipNum = shipmentNo;
	}
	public int getShipNo()
	{
		return shipNum;
	}
	
	public String toString()
	{
		String rtStr = "";
		for(Integer i : unviableList)
		{
			rtStr += shipNum + "-> " + i + "\n";
		}
		return rtStr;
	}
	public boolean has(int num)
	{
		return unviableList.contains(num);
	}
	
	public void addToList(int num)
	{
		unviableList.add(num);
	}
	
}
