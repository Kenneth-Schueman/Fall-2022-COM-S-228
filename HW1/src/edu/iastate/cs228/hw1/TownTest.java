package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.jupiter.api.Test;

class TownTest {
    
    @Test
    void testLW()
    {
    	Town town = new Town(4, 4);
    	assertEquals(4, town.getLength());
    	assertEquals(4, town.getWidth());
    }
}