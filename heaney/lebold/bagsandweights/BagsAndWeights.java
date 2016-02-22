package heaney.lebold.bagsandweights;

import heaney.lebold.bagsandweights.constraints.BagFilledConstraint;
import heaney.lebold.bagsandweights.constraints.IConstraint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class BagsAndWeights {
	
	//private int stepsTaken;

	private List<Weight> weights;
	private List<Bag> bags;
	private List<IConstraint> constraints;
	
	private List<List<Bag>> memoizationList;
	private boolean topLevelValid;
	private boolean subLevelValid;

	public BagsAndWeights(List<Weight> weights, List<Bag> bags, List<IConstraint> constraints){
		this.weights = weights;
		this.bags = bags;
		this.constraints = constraints;
		
		this.topLevelValid = true;
		this.subLevelValid = true;
		//this.stepsTaken = 0;
		this.memoizationList = new ArrayList<List<Bag>>();
	}

	public void init(){		
		//Call backtracking method
		if(this.solve()){  //true if solved
			//Print solution
			for(Bag bag: this.bags){
				System.out.print(bag.getID());
				bag.forEach((w) -> System.out.print(" " + w.getID()));
				System.out.println();
				System.out.println("number of items: " + bag.size());
				System.out.println("total weight: " + bag.getTotalWeight() + "/" + bag.getMaxWeight());
				System.out.println("wasted capacity: " + (bag.getMaxWeight() - bag.getTotalWeight()));
				System.out.println();
			}
			
			//System.out.println("\n\nTotal Steps Taken: " + this.stepsTaken);
		}
		else{
			System.out.println("There are no solutions with the given constraints.");
		}
	}

	private boolean solve(){
		//this.stepsTaken++;
		
		//Memoization
		for(List<Bag> localBagList: this.memoizationList){
			boolean isCopy = true;
			for(int n=0; n< localBagList.size(); n++){
				Bag localBag = localBagList.get(n);
				Bag globalBag = this.bags.get(n);
				if(!localBag.getContents().containsAll(globalBag.getContents()))
					isCopy = false;
				if(!globalBag.getContents().containsAll(localBag.getContents()))
					isCopy = false;
			}
			if(isCopy)
				return false;
			else
				this.memoizationList.add(new ArrayList<Bag>(this.bags));
		}

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
			// Apply heuristic to remove weights that are too heavy
			applyMRVHeuristic(bag,weightsToTest);
			// Apply heuristic to order weights based on forward checking
			applyLCVHeuristic(bag,weightsToTest);
			
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
	
	/* Remove weights that put bags over capacity */
	private void applyMRVHeuristic(Bag bag, List<Weight> unsortedWeights){
		unsortedWeights.sort((w1,w2) -> {
			return w2.getWeight() - w1.getWeight();
		});
		while(!unsortedWeights.isEmpty() && bag.getTotalWeight() + unsortedWeights.get(0).getWeight() > bag.getMaxWeight()){
			unsortedWeights.remove(0);
		}
	}
	
	/* Order weights based on heuristic/forward checking */
	private void applyLCVHeuristic(Bag bag, List<Weight> unsortedWeightsBase){
		//Get local copy of unplaced weights
		List<Weight> unsortedWeights = new ArrayList<Weight>(unsortedWeightsBase);
		
		//Map used to rank each weight based on number of valid sub-placements
		HashMap<Weight,Integer> weightValues = new HashMap<Weight,Integer>();
		for(int n = unsortedWeights.size()-1; n >= 0; n--){
			Weight weight = unsortedWeights.get(n);
			if(!weightValues.containsKey(weight))
				weightValues.put(weight, 0);
			
			//Place weight in bag, check to see if new state is valid. (if not, dump weight from list)
			bag.addWeight(weight);
			this.topLevelValid = true;
			this.constraints.forEach((c) -> {
				if(!c.isValid(this.bags)) 
					this.topLevelValid = false;
			});
			if(!topLevelValid){
				unsortedWeightsBase.remove(weight);
			}
			
			// subList -> weights placed in turn after placing last weight
			List<Weight> subList = new ArrayList<Weight>(unsortedWeights);
			subList.remove(weight);
			
			// validCount -> number of valid moves after last weight place
			int validCount = 0;
			for(Bag b: this.bags){
				for(Weight subWeight: subList){
					//Test weight in bag, if valid state, increment validCount
					b.addWeight(subWeight);
					this.subLevelValid = true;
					this.constraints.forEach((c) -> {
						if(!c.isValid(this.bags))
							this.subLevelValid = false;
					});
					b.removeWeight(subWeight);
					
					if(this.subLevelValid)
						validCount++;
				}
			}
			//Push to map validCount with key of the placed weight
			weightValues.put(weight, validCount);
			
			//Undo weight placement
			bag.removeWeight(weight);
		}
		
		//Sort list based on validCount for each weight
		unsortedWeightsBase.sort((w1,w2) -> {
			return weightValues.get(w2) - weightValues.get(w1);
		});
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
