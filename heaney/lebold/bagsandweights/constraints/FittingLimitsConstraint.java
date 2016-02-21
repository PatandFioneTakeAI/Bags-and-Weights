package heaney.lebold.bagsandweights.constraints;

import heaney.lebold.bagsandweights.Bag;

import java.util.List;

public class FittingLimitsConstraint implements IConstraint {

	private int minWeights;
	private int maxWeights;
	
	public FittingLimitsConstraint(int minWeights, int maxWeights){
		this.minWeights = minWeights;
		this.maxWeights = maxWeights;
	}
	
	@Override
	public boolean isValid(List<Bag> bags) {
		for(Bag bag: bags){
			int numWeights = bag.size();
			if(numWeights > this.maxWeights){
				//System.out.println("[Constraint - fitting limits] " + this.minWeights + " " + this.maxWeights + " (false)");
				return false;
			}
		}
		//System.out.println("[Constraint - fitting limits] " + this.minWeights + " " + this.maxWeights + " (true)");
		return true;
	}
	
	@Override
	public boolean isFinal(List<Bag> bags){
		for(Bag bag: bags){
			int numWeights = bag.size();
			if(numWeights > maxWeights)
				return false;
			if(numWeights < minWeights)
				return false;
		}
		return true;
	}

}
