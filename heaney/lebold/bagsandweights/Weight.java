package heaney.lebold.bagsandweights;

public class Weight {

	private char id;
	private int weight;
	
	public Weight(char id, int weight){
		this.id = id;
		this.weight = weight;
	}
	
	public char getID(){
		return this.id;
	}
	
	public int getWeight(){
		return this.weight;
	}
	
	@Override
	public String toString(){
		String me =  "";
		me += "[Weight]: " + this.hashCode() + "\n";
		me += "\tchar id = " + this.id + "\n";
		me += "\tint weight = " + this.weight + "\n";
		return me;
	}
	
	@Override
	public int hashCode(){
		return id * 13 + weight * 53;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Weight){
			Weight other = (Weight)o;
			return this.id == other.id && this.weight == other.weight;
		}
		return false;
	}
	
}
