package edu.iastate.cs228.hw1;

public class Outage extends TownCell {
	
	public Outage (Town p, int l, int w)
	{
		//Grabs values from TownCell
		super (p, l, w); 
	}
	
	//Performs logic for census and neighbors
	@Override 
	public TownCell next(Town tNew)
	{
		int nCensus[] = new int[NUM_CELL_TYPE];
		
		census(nCensus);
		
		//Subtracts self from being counted in the census
		nCensus[OUTAGE]--;
		
		//Rule 7
		return new Empty(tNew, row, col);
	}

	//Get method to check cell type
	@Override 
	public State who()
	{
		return State.OUTAGE;
	}
	
}
