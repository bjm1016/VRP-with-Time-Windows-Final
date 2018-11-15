package edu.sru.thangiah.zeus.vrptw;

import edu.sru.thangiah.zeus.core.TruckType;

/**
 * VRPTWTruckType inherits from TruckType and is adapted
 * to handle time window problem and related classes
 *
 *
 */
public class VRPTWTruckType extends TruckType
implements java.io.Serializable, java.lang.Cloneable{
	
	/**
	 * Constructor
	 *
	 */
	public VRPTWTruckType() {
	  }
	  
	/**
	 * Constructor
	 *
	 * @param N truck number
	 * @param D max duration allowed for this type of truck
	 * @param Q max capacity allowed for this type of truck
	 * @param s service type for this truck type
	 *
	 */
	public VRPTWTruckType(int N, float D, float Q, String s) {
		super.setTruckNo(N);

	    super.setServiceType(s);

	    if (D == 0) {
	    	super.setMaxDuration(Integer.MAX_VALUE);
	    }
	    else {
	    	super.setMaxDuration(D);
	    }

	    if (Q == 0) {
	    	super.setMaxCapacity(Integer.MAX_VALUE);
	    }
	    else {
	    	super.setMaxCapacity(Q);
	    }
	    super.setFixedCost(super.getMaxCapacity());
	    super.setVariableCost((double)super.getMaxCapacity()/1000);
	  }
	
	/**
	 * Returns the truck number
	 *
	 * @return truck number
	 */
	public int VRPTWgetTruckNum(){
		return super.getTruckNo();
	}
	/**
	 * Returns maximum duration allowed
	 *
	 * @return max duration
	 */
	public double VRPTWgetTruckDuration(){
		return super.getMaxDuration();
	}
	
	/**
	 * returns maximum capacity allowed
	 *
	 * @return max capacity
	 */
	public float VRPTWgetTruckMaxCapacity(){
		return super.getMaxCapacity();
	}
	
	/**
	 * Returns service type
	 *
	 * @return service type
	 */
	public String VRPTWgetTruckServiceType(){
		return super.getServiceType();
	}
	
	/**
	 * Returns the fixed cost for this truck
	 *
	 * @return fixed cost
	 */
	public double VRPTWgetFixedCost(){
		return super.getFixedCost();
	}
	
	/**
	 * Returns varying cost for this truck type
	 *
	 * @return variable cost
	 */
	public double VRPTWgetVariableCost(){
		return super.getVariableCost();
	}
	
	/**
	 * Overrides parent clone function
	 * 
	 * @return copy of VRPTWTruckType that called this
	 * @author Eric McAlpine
	 */
	@Override
	public Object clone(){
		VRPTWTruckType cloned = new VRPTWTruckType();
		cloned.setFixedCost(this.getFixedCost());
		cloned.setMaxCapacity(this.getMaxCapacity());
		cloned.setMaxDuration(this.getMaxDuration());
		cloned.setServiceType(this.getServiceType());
		cloned.setTruckNo(this.getTruckNo());
		cloned.setVariableCost(this.getVariableCost());
		return cloned;
	}
}
