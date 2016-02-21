package heaney.lebold.bagsandweights.constraints;

import heaney.lebold.bagsandweights.Bag;
import heaney.lebold.bagsandweights.Weight;

import java.util.List;

public class MutualInclusiveConstraint implements IConstraint {

	private char weight1ID;
	private char weight2ID;
	private char bag1ID;
	private char bag2ID;
	
	public MutualInclusiveConstraint(char weight1ID, char weight2ID, char bag1ID, char bag2ID){
		this.weight1ID = weight1ID;
		this.weight2ID = weight2ID;
		this.bag1ID = bag1ID;
		this.bag2ID = bag2ID;
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
		
		if(weight1bag.getID() == bag1ID)
			return weight2bag.getID() == bag2ID;
		else if(weight1bag.getID() == bag2ID)
			return weight2bag.getID() == bag1ID;
		else if(weight2bag.getID() == bag1ID)
			return weight1bag.getID() == bag2ID;
		else if(weight2bag.getID() == bag2ID)
			return weight1bag.getID() == bag1ID;
		return true;
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
		
		if(weight1bag == null || weight2bag == null)
			return false;
		
		if(weight1bag.getID() == bag1ID)
			return weight2bag.getID() == bag2ID;
		else if(weight1bag.getID() == bag2ID)
			return weight2bag.getID() == bag1ID;
		else if(weight2bag.getID() == bag1ID)
			return weight1bag.getID() == bag2ID;
		else if(weight2bag.getID() == bag2ID)
			return weight1bag.getID() == bag1ID;
		return true;
	}

}
