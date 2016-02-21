package heaney.lebold.bagsandweights.constraints;

import java.util.List;

import heaney.lebold.bagsandweights.Bag;

public interface IConstraint {
	public boolean isValid(List<Bag> bags);
	public boolean isFinal(List<Bag> bags);
}
