package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CasualTest {

	//Initializing a 2x2 grid to check is Casual(0,0) turns into Reseller
    @Test
    void casualTest() 
    {
        Town t = new Town(2,2);
        
        t.grid[0][0] = new Casual(t,0,0);
        t.grid[0][1] = new Streamer(t,0,1);
        t.grid[1][0] = new Casual(t,1,0);
        t.grid[1][1] = new Casual(t,1,1);
        
        assertEquals(State.RESELLER, t.grid[0][0].next(t).who());
    }

  //Initializing a 2x2 grid to check is Casual(0,0) turns into Reseller
    @Test
    void casualTest1()
    {
        Town t = new Town(2,2);
        
        t.grid[0][0] = new Casual(t,0,0);
        t.grid[0][1] = new Casual(t,0,1);
        t.grid[1][0] = new Casual(t,1,0);
        t.grid[1][1] = new Casual(t,1,1);
        
        assertEquals(State.RESELLER, t.grid[0][0].next(t).who());
        
      //Initializing a 2x2 grid to check if all 4 Casual cells are counted for getProfit()
        Town tt = new Town(2,2);
        
        for(int i = 0; i < tt.getLength();i++) 
        	for(int j = 0; j < tt.getWidth(); j++) 
        		tt.grid[i][j]= new Casual(tt, i, j);
        	
        tt.toString();
        
        assertEquals(4, ISPBusiness.getProfit(tt));
        
    }

}