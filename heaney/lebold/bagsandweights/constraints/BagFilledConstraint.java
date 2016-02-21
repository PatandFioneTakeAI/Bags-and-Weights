package heaney.lebold.bagsandweights.constraints;

import heaney.lebold.bagsandweights.Bag;

import java.util.List;

public class BagFilledConstraint implements IConstraint {
	
	private Bag bag;
	
	public BagFilledConstraint(Bag bag){
		this.bag = bag;
	}

	@Override
	public boolean isValid(List<Bag> bags) {
		return !this.bag.isOverweight();
	}

	@Override
	public boolean isFinal(List<Bag> bags) {
		//System.out.println("[Constraint - bag filled] " + ((int)(this.bag.getMaxWeight()*.9)) + " (" + this.bag.isEligible() + ")");
		return this.bag.isEligible() && !this.bag.isOverweight();
	}

}
