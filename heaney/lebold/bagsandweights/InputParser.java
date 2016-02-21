package heaney.lebold.bagsandweights;

import java.util.ArrayList;
import java.util.List;

import heaney.lebold.bagsandweights.constraints.BinaryEqualsConstraint;
import heaney.lebold.bagsandweights.constraints.BinaryNotEqualsConstraint;
import heaney.lebold.bagsandweights.constraints.FittingLimitsConstraint;
import heaney.lebold.bagsandweights.constraints.MutualInclusiveConstraint;
import heaney.lebold.bagsandweights.constraints.UnaryExclusiveConstraint;
import heaney.lebold.bagsandweights.constraints.UnaryInclusiveConstraint;

public enum InputParser {

	/* The different sections of parsable input */
	VARIABLES,
	VALUES,
	FITTING_LIMITS,
	UNARY_INCLUSIVE,
	UNARY_EXCLUSIVE,
	BINARY_EQUALS,
	BINARY_NOT_EQUALS,
	MUTUAL_INCLUSIVE;
	
	/* Converts input string into object corresponding to section */
	public static Object getData(InputParser section, String input){
		if(section == InputParser.VARIABLES)
			return InputParser.getDataVariables(input);
		else if(section == InputParser.VALUES)
			return InputParser.getDataValues(input);
		else if(section == InputParser.FITTING_LIMITS)
			return InputParser.getDataFittingLimits(input);
		else if(section == InputParser.UNARY_INCLUSIVE)
			return InputParser.getDataUnaryInclusive(input);
		else if(section == InputParser.UNARY_EXCLUSIVE)
			return InputParser.getDataUnaryExclusive(input);
		else if(section == InputParser.BINARY_EQUALS)
			return InputParser.getDataBinaryEquals(input);
		else if(section == InputParser.BINARY_NOT_EQUALS)
			return InputParser.getDataBinaryNotEquals(input);
		else if(section == InputParser.MUTUAL_INCLUSIVE)
			return InputParser.getDataMutualInclusive(input);
		
		return null;
	}
	
	private static Object getDataVariables(String input){
		String splitInput[] = input.split(" ");
		char id = splitInput[0].charAt(0);
		int weight = Integer.parseInt(splitInput[1]);
		
		return new Weight(id,weight);	
	}
	
	private static Object getDataValues(String input){
		String splitInput[] = input.split(" ");
		char id = splitInput[0].charAt(0);
		int maxWeight = Integer.parseInt(splitInput[1]);
		
		return new Bag(id,maxWeight);
	}
	
	private static Object getDataFittingLimits(String input){
		String splitInput[] = input.split(" ");
		int min = Integer.parseInt(splitInput[0]);
		int max = Integer.parseInt(splitInput[1]);
		FittingLimitsConstraint constraint = new FittingLimitsConstraint(min,max);
		
		return constraint;
	}
	
	private static Object getDataUnaryInclusive(String input){
		String[] splitInput = input.split(" ");
		
		char weightID = splitInput[0].charAt(0);
		List<Character> bagIDs = new ArrayList<Character>();
		for(int n=1; n < splitInput.length; n++){
			bagIDs.add(splitInput[n].charAt(0));
		}
		UnaryInclusiveConstraint constraint = new UnaryInclusiveConstraint(weightID,bagIDs);
		return constraint;
	}
	
	private static Object getDataUnaryExclusive(String input){
		String[] splitInput = input.split(" ");
		
		char weightID = splitInput[0].charAt(0);
		List<Character> bagIDs = new ArrayList<Character>();
		for(int n=1; n < splitInput.length; n++){
			bagIDs.add(splitInput[n].charAt(0));
		}
		UnaryExclusiveConstraint constraint = new UnaryExclusiveConstraint(weightID,bagIDs);
		return constraint;
	}
	
	private static Object getDataBinaryEquals(String input){
		String[] splitInput = input.split(" ");
		char weight1ID = splitInput[0].charAt(0);
		char weight2ID = splitInput[1].charAt(0);
		
		BinaryEqualsConstraint constraint = new BinaryEqualsConstraint(weight1ID,weight2ID);
		return constraint;
	}
	
	private static Object getDataBinaryNotEquals(String input){
		String[] splitInput = input.split(" ");
		char weight1ID = splitInput[0].charAt(0);
		char weight2ID = splitInput[1].charAt(0);
		
		BinaryNotEqualsConstraint constraint = new BinaryNotEqualsConstraint(weight1ID,weight2ID);
		return constraint;
	}
	
	private static Object getDataMutualInclusive(String input){
		String[] splitInput = input.split(" ");
		char weight1ID = splitInput[0].charAt(0);
		char weight2ID = splitInput[1].charAt(0);
		char bag1ID = splitInput[2].charAt(0);
		char bag2ID = splitInput[3].charAt(0);
		
		MutualInclusiveConstraint constraint = new MutualInclusiveConstraint(weight1ID,weight2ID,bag1ID,bag2ID);
		return constraint;
	}
}
