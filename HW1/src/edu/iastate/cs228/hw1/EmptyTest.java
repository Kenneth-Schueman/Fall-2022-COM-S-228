package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmptyTest {

    @Test
    void emptyTest() 
    {
    	//Initializing a 2x2 grid to check is Empty(0,0) turns into Reseller
        Town t = new Town(2,2);
        
        t.grid[0][0] = new Empty(t,0,0);
        t.grid[0][1] = new Casual(t,0,1);
        t.grid[1][0] = new Empty(t,1,0);
        t.grid[1][1] = new Casual(t,1,1);
        
        assertEquals(State.RESELLER, t.grid[0][0].next(t).who());
    }


}