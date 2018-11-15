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

public class VRPTWQAShipmentLinkedList
    extends QAShipmentLinkedList
    implements java.io.Serializable, java.lang.Cloneable {
/**
 * VRPTWQAShipmentLinkedList allows for checking of customer services
 */
  public VRPTWQAShipmentLinkedList() {
  }
/**
 * verifies customer is serviced only once for the supplied VRPTWQADepotLinkedList
 * @param qaDepots - VRPTWQADepotLinkedList supplied to check
 * @return boolean pass/fail
 */
  public boolean customerServicedOnlyOnce(VRPTWQADepotLinkedList qaDepots) {
    //loop through all the shipments and mark which are serviced and count the number of times
    //the customers are serviced. Each shipment should be serviced no more than once
    for (int i = 0; i < qaDepots.getDepots().size(); i++) {
      VRPTWQADepot d = (VRPTWQADepot) qaDepots.getDepots().elementAt(i);
      for (int j = 0; j < d.getTrucks().size(); j++) {
        VRPTWQATruck t = (VRPTWQATruck) d.getTrucks().elementAt(j);
        for (int k = 0; k < t.getNodes().size(); k++) {
          VRPTWQANode n = (VRPTWQANode) t.getNodes().elementAt(k);
          for (int l = 0; l < getShipments().size(); l++) {
            VRPTWQAShipment s = (VRPTWQAShipment) getShipments().elementAt(l);
            if (s.getIndex() == n.getIndex()) {
              s.setServecount(s.getServecount()+1);
              break;
            }
          }
        }
      }
    }

    boolean ret = true;
    //loop through shipments and look for anomalies
    for (int l = 1; l < getShipments().size(); l++) {
      VRPTWQAShipment s = (VRPTWQAShipment) getShipments().elementAt(l);
      if (s.getServecount() != 1) {
        edu.sru.thangiah.zeus.core.Settings.printDebug(edu.sru.thangiah.zeus.
            core.Settings.ERROR,
            "Shipment " + s.getIndex() + " is serviced " + s.getServecount() + " time(s)");
        ret = false;
      }
    }

    return ret;
  }

}
