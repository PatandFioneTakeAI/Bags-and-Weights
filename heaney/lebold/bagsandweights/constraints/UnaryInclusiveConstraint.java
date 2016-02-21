package heaney.lebold.bagsandweights.constraints;

import heaney.lebold.bagsandweights.Bag;
import heaney.lebold.bagsandweights.Weight;

import java.util.List;

public class UnaryInclusiveConstraint implements IConstraint {

	private char weightID;
	private List<Character> bagIDs;
	
	public UnaryInclusiveConstraint(char weightID, List<Character> bagIDs){
		this.weightID = weightID;
		this.bagIDs = bagIDs;
	}
	
	@Override
	public boolean isValid(List<Bag> bags) {
		for(Bag bag: bags){
			for(Weight weight: bag){
				if(weightID == weight.getID() && !bagIDs.contains(bag.getID()))
					return false;
			}
		}
		return true;
	}

	@Override
	public boolean isFinal(List<Bag> bags) {
		return this.isValid(bags);
	}

}
