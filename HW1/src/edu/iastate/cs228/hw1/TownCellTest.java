package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.jupiter.api.Test;

class TownCellTest {
	
	
	//Tests to see if (1,1) on the itr 1 of town changes correctly  
	@Test
	public void testCensus() throws FileNotFoundException 
	{
		//Manually inputs the text file to test if scanner is working  
		Town town = new Town("ISP4x4.txt");
		
		String state = town.grid[0][0].next(town).who().toString();
		
		assertEquals(State.EMPTY.toString(), state); 
	}
}
