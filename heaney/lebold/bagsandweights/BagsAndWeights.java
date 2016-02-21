package heaney.lebold.bagsandweights;

import heaney.lebold.bagsandweights.constraints.BagFilledConstraint;
import heaney.lebold.bagsandweights.constraints.IConstraint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BagsAndWeights {

	private List<Weight> weights;
	private List<Bag> bags;
	private List<IConstraint> constraints;

	public BagsAndWeights(List<Weight> weights, List<Bag> bags, List<IConstraint> constraints){
		this.weights = weights;
		this.bags = bags;
		this.constraints = constraints;
	}

	public void init(){		
		//Call backtracking method
		if(this.solve()){  //true if solved
			//Print solution
			for(Bag bag: this.bags){
				System.out.println("Bag: " + bag.getID());
				System.out.print("\tContents:");
				for(Weight weight: bag)
					System.out.print(" " + weight.getID());
				System.out.println();
				System.out.println("\tItem Count: " + bag.size());
				System.out.println("\tTotal Weight: " + bag.getTotalWeight());
				System.out.println("\tWasted Capacity: " + (bag.getMaxWeight() - bag.getTotalWeight()));
			}
		}
		else{
			System.out.println("There are no solutions with the given constraints.");
		}
	}

	private boolean solve(){
	 /**System.out.println("\n-----------------");
		System.out.print("Unplaced Weights:");
		this.weights.forEach((w) -> System.out.print(" " + w.getID()));
		System.out.println();
		for(Bag bag: this.bags)
			System.out.println(bag.toString());*/

		//allFinal remains true if all constraints are satisfied
		boolean allFinal = true;
		for(IConstraint constraint: this.constraints){
			//Check if current state violates constraint directly
			if(!constraint.isValid(this.bags))
				return false;
			//Check if current state violates constraint at all
			if(!constraint.isFinal(this.bags))
				allFinal = false;
		}
		//All constraints satisfied and all weights placed. This is a solution!
		if(allFinal && this.weights.isEmpty())
			return true;

		//Copy weights available at this stage
		List<Weight> weightsToTestBase = new ArrayList<Weight>(this.weights);
		
		for(Bag bag: this.bags){
			// Get local list of weights for this bag
			List<Weight> weightsToTest = new ArrayList<Weight>(weightsToTestBase);
			// Apply heuristic to list to send best weights to the front of the list
			applyHeuristic(weightsToTest);
			
			// For each weight in best -> worst order
			for(Weight weight: weightsToTest){
				if(bag.canFit(weight)){				
					/* Take action (add weight to bag) */
					this.weights.remove(weight);
					weightsToTestBase.remove(weight);
					bag.addWeight(weight);

					//If the problem can be solved by this partial solution, escape
					if(solve())
						return true;

					/* Revoke action (pull weight from bag) */
					bag.removeWeight(weight);
					weightsToTestBase.add(weight);
					this.weights.add(weight);
				}
			}
		}

		//This partial state yields no solutions
		return false;
	}
	
	/* Sort weights in descending order */
	private void applyHeuristic(List<Weight> unsortedWeights){
		unsortedWeights.sort((w1,w2) -> w2.getWeight() - w1.getWeight());
	}

	public static void main(String[] args){
		//Handle arguments 
		if(args.length != 1){
			System.out.println("Invalid syntax: $ java BagsAndWeights.jar <inputdata.txt>");
			return;
		}

		File inputFile = new File(args[0]);
		try {
			//Load the file into buffer
			Scanner scanner = new Scanner(inputFile);
			InputParser[] sections = {InputParser.VARIABLES,
					InputParser.VALUES,InputParser.FITTING_LIMITS,
					InputParser.UNARY_INCLUSIVE,InputParser.UNARY_EXCLUSIVE,
					InputParser.BINARY_EQUALS,InputParser.BINARY_NOT_EQUALS,
					InputParser.MUTUAL_INCLUSIVE};

			scanner.nextLine(); //Bump past first "#####"
			
			/*List of sub-lists of objects (object Type different depending on section) */
			ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
			for(InputParser section:sections){
				/* List of objects to be loaded from input in this section */
				ArrayList<Object> objects = new ArrayList<Object>();
				data.add(objects);

				// Load in each line of data for this section
				while(scanner.hasNextLine()){
					String line = scanner.nextLine();
					if(line.startsWith("#####")) //If true, advance to next section
						break;
					//Obtain object from InputParser
					objects.add(InputParser.getData(section, line));
				}
			}
			scanner.close();

			//Cast weights from Object sub-list to new List
			ArrayList<Weight> weights = new ArrayList<Weight>();
			data.get(0).forEach((w) -> weights.add((Weight)w));

			//Cast bags from Object sub-list to new List
			ArrayList<Bag> bags = new ArrayList<Bag>();
			data.get(1).forEach((b) -> bags.add((Bag)b));

			//Cast all constraints from all other sub-lists to new List
			List<IConstraint> constraints = new ArrayList<IConstraint>();
			for(int n=2; n < data.size(); n++)
				data.get(n).forEach( (c) -> constraints.add((IConstraint)c));

			//Add additional constraints not directly specified in input
			for(Bag bag: bags){
				BagFilledConstraint constraint = new BagFilledConstraint(bag);
				constraints.add(constraint);
			}

			//Solve problem
			BagsAndWeights solver = new BagsAndWeights(weights,bags,constraints);
			solver.init();

		} catch (FileNotFoundException e) {
			System.out.println(" File \"" + args[0] + "\" not found.");
			return;
		}

	}
}
