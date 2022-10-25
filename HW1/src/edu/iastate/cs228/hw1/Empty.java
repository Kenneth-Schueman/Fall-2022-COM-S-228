package edu.iastate.cs228.hw1;

public class Empty extends TownCell {
	
	public Empty (Town p, int l, int w)
	{
		//Grabs values from TownCell
		super(p, l, w); 
	}
	
	//Performs logic for census and neighbors 
	@Override 
	public TownCell next(Town tNew)
	{
		int nCensus[] = new int[NUM_CELL_TYPE];
		
		census(nCensus);
		
		//Subtracts self from being counted in the census
		nCensus[EMPTY]--;
		
		//Rule 6a
		if (nCensus[EMPTY] + nCensus[OUTAGE] <= 1)
			return new Reseller(tNew, row, col);
		
		//Rule 7
		else			
			return new Casual(tNew, row, col);
	}
	
	//Get method to check cell type
	@Override 
	public State who()
	{
		return State.EMPTY;
	}
	
}
