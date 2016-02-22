package heaney.lebold.bagsandweights;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bag implements Iterable<Weight>{

	private List<Weight> weights;
	
	private char id;
	private int maxWeight;
	
	public Bag(Bag bag){
		this.weights = new ArrayList<Weight>(bag.weights);
		this.id = bag.id;
		this.maxWeight = bag.maxWeight;
	}
	
	public Bag(char id, int maxWeight){		
		this.id = id;
		this.maxWeight = maxWeight;
		
		this.weights = new ArrayList<Weight>();
	}
	
	public int size(){
		return this.weights.size();
	}
	
	public List<Weight> getContents(){
		List<Weight> contents = new ArrayList<Weight>();
		contents.addAll(this.weights);
		return contents;
	}
	
	public void addWeight(Weight weight){
		this.weights.add(weight);
	}
	
	public void removeWeight(Weight weight){
		this.weights.remove(weight);
	}
	
	public boolean isEligible(){
		return this.getTotalWeight() >= (int)(this.maxWeight * .9);
	}
	
	public boolean isOverweight(){
		return this.getTotalWeight() > this.maxWeight;
	}
	
	public boolean canFit(Weight weight){
		return this.getTotalWeight() + weight.getWeight() <= this.maxWeight;
	}
	
	public int getTotalWeight(){
		int total = 0;
		for(Weight weight:this.weights)
			total += weight.getWeight();
		return total;
	}
	
	public char getID(){
		return this.id;
	}
	
	public int getMaxWeight(){
		return this.maxWeight;
	}

	@Override
	public Iterator<Weight> iterator() {
		return this.weights.iterator();
	}
	
	@Override
	public int hashCode(){
		return getID() * 13 + weights.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Bag){
			Bag other = (Bag)o;
			return other.weights.containsAll(this.weights) && other.id == this.id;
		}
		return false;
	}
	
	@Override
	public String toString(){
		String me = "";
		me += "[Bag]:";
		for(Weight weight: this)
			me += " " + weight.getID();
		me += "\n";
		me += "\tchar id = " + this.id + "\n";
		me += "\tint max = " + this.maxWeight + "\n";
		me += "\tint weight = " + this.getTotalWeight() + "\n";
		return me;
	}
	
}
