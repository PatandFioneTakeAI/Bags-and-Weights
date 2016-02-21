package heaney.lebold.bagsandweights.constraints;

import heaney.lebold.bagsandweights.Bag;
import heaney.lebold.bagsandweights.Weight;

import java.util.List;

public class BinaryNotEqualsConstraint implements IConstraint {

	private char weight1ID;
	private char weight2ID;
	
	public BinaryNotEqualsConstraint(char weight1ID, char weight2ID){
		this.weight1ID = weight1ID;
		this.weight2ID = weight2ID;
	}
	
	@Override
	public boolean isValid(List<Bag> bags) {
		Bag weight1bag = null;
		Bag weight2bag = null;
		for(Bag bag: bags){
			for(Weight weight: bag){
				if(weight1bag == null && weight.getID() == this.weight1ID)
					weight1bag = bag;
				else if(weight2bag == null && weight.getID() == this.weight2ID)
					weight2bag = bag;
			}
		}
		if(weight1bag == null || weight2bag == null)
			return true;
		return weight1bag.getID() != weight2bag.getID();
	}

	@Override
	public boolean isFinal(List<Bag> bags) {
		Bag weight1bag = null;
		Bag weight2bag = null;
		for(Bag bag: bags){
			for(Weight weight: bag){
				if(weight1bag == null && weight.getID() == this.weight1ID)
					weight1bag = bag;
				else if(weight2bag == null && weight.getID() == this.weight2ID)
					weight2bag = bag;
			}
		}
		return weight1bag != null && weight2bag != null && weight1bag.getID() != weight2bag.getID();
	}

}
