package vrptw;

import edu.sru.thangiah.zeus.vrp.VRPDepot;
import edu.sru.thangiah.zeus.vrp.VRPDepotLinkedList;
import edu.sru.thangiah.zeus.vrp.VRPShipment;
import edu.sru.thangiah.zeus.vrp.VRPShipmentLinkedList;

class ClosestEuclideanDistToShip extends 
VRPTWShipmentLinkedList{
	public double calcSavings (VRPTWDepotList currDepotLL,
							   VRPTWDepot currDepot,
						   	   VRPTWShipmentLinkedList currShipLL,
						   	   VRPTWShipment currShip,
						   	   VRPTWShipmentLinkedList nextShipLL,
						   	   VRPTWShipment nextShip){
		double savings, distanceA, distanceB, distanceC,
		depotX, depotY, shipX, shipY, nextShipX, nextShipY;
		
	    depotX = currDepot.getXCoord();
	    depotY = currDepot.getYCoord();
	    shipX = currShip.getXCoord();
	    shipY = currShip.getYCoord();
		nextShipX = nextShip.getXCoord();
		nextShipY = nextShip.getYCoord();		
		
		distanceA = calcDist(shipX, depotX, shipY, depotY);
		distanceB = calcDist(nextShipX, depotX, nextShipX, depotY);
		distanceC = calcDist(nextShipX, shipX, nextShipX, shipX);
		
		savings = distanceA + distanceB - distanceC;
		
		return savings;
	}
	
	public VRPTWShipment getSelectShipment(VRPTWDepotList currDepotLL,
										   VRPTWDepot currDepot,
										   VRPTWShipmentLinkedList currShipLL,
										   VRPTWShipment currShip,
										   VRPTWShipmentLinkedList nextShipLL,
										   VRPTWShipment nextShip){
		boolean isDiagnostic = false;
		VRPTWShipment temp = (VRPTWShipment) currShipLL.getVRPTWHead().getNext(); //point to the first shipment
		VRPTWShipment foundShipment = null; //the shipment found with the criteria
		double savings;
		double foundDistance = 200; //initial distance

		while (temp != currShipLL.getVRPTWTail()) {
			if (isDiagnostic) {
				System.out.print("Shipment " + temp.getIndex() + " ");
			}

			//if the shipment is assigned, skip it
			if (temp.getIsAssigned()) {
				if (isDiagnostic) {
					System.out.println("has been assigned");
				}

				temp = (VRPTWShipment) temp.getNext();

				continue;
			}

		savings = calcSavings (currDepotLL, currDepot, currShipLL, currShip, nextShipLL, nextShip);

		if (isDiagnostic) {
			System.out.println("  " + savings);
		}

		//check if this shipment should be tracked
		if (foundShipment == null) { //this is the first shipment being checked
			foundShipment = temp;
			foundDistance = savings;
		}
		else {
			if (savings > foundDistance) { //found an angle that is less
				foundShipment = temp;
				foundDistance = savings;
			}
		}
		temp = (VRPTWShipment) temp.getNext();
		}
		return foundShipment; //stub
	}
}
