package edu.iastate.cs228.hw1;

public class Reseller extends TownCell {

	public Reseller (Town p, int l, int w) 
	{
		super(p, l, w); //Grabs values from TownCell
	}
	
	@Override //Performs logic for census and neighbors
	public TownCell next(Town tNew)
	{
		int nCensus[] = new int[NUM_CELL_TYPE];
		
		//Subtracts self from being counted in the census
		nCensus[RESELLER]--;
		
		//Rule 6a
		if (nCensus[CASUAL] <= 3 || nCensus[EMPTY] >= 3)
			return new Empty(tNew, row, col);
		
		//Rule 6b
		else if (nCensus[CASUAL] >= 5)
			return  new Streamer(tNew, row, col);
		
		//Rule 7
		else
			return new Reseller(tNew, row, col);
	}

	@Override //get method to check cell type
	public State who()
	{
		return State.RESELLER;
	}
	
}
