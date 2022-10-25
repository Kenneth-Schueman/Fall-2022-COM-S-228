package edu.iastate.cs228.hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 *  @author <<KenSchue>>
 *
 */
public class Town {
	
	//Row and col (first and second indices)
	private int length, width;  
	public TownCell[][] grid;
	
	/**
	 * Constructor to be used when user wants to generate grid randomly, with the given seed.
	 * This constructor does not populate each cell of the grid (but should assign a 2D array to it).
	 * @param length
	 * @param width
	 */
	public Town(int length, int width) 
	{
		//Sets length from pgramType 2 path as class stated integers
		this.length = length;
		this.width = width;
		
		//System.out.println(length + " " + width);
		//Assigning the passed int's to this class and the limits of grid
		grid = new TownCell[length][width];
		
	}
	
	/**
	 * Constructor to be used when user wants to populate grid based on a file.
	 * Please see that it simple throws FileNotFoundException exception instead of catching it.
	 * Ensure that you close any resources (like file or scanner) which is opened in this function.
	 * @param inputFileName
	 * @throws FileNotFoundException
	 */
	public Town(String inputFileName) throws FileNotFoundException 
	{
		//Scanner initialization for file input
		File f = new File(inputFileName); 
		
		Scanner input = new Scanner(f);
		
		//Grabbing length and width from first line of file
		int length = input.nextInt();
		int width = input.nextInt();
		
		//Sets length from pgramType 1 path as class stated integers 
		this.length = length;
		this.width = width;
		
		//Initialize new grid
		grid = new TownCell[length][width];
		
		input.nextLine();
		
		for (int i = 0; i < length; i++)
		{
			//Used to split each character in to a temporary char array
			String[] line = input.nextLine().split(" ");
			
			//'line' length is equal to row but ensures j never breaks bounds
			for (int j = 0; j < line.length; j++) 
				
				//Assigning a TownCell descendant classes to the respective cell in grid
				switch (line[j])
				{
					case "R":
						grid[i][j] = new Reseller(this, i, j);
						break;

					case "E":
						grid[i][j] = new Empty(this, i, j);
						break;
 
					case "C":
						grid[i][j] = new Casual(this ,i, j);
						break;

					case "O":
	                	grid[i][j] = new Outage(this, i, j);
	                	break;
	                    
					case "S":
						grid[i][j] = new Streamer(this ,i, j);
						break;
	                   
					//Specific cases added for cells at the end of the line
					case "R\t":
						grid[i][j] = new Reseller(this, i, j);
						break;
						
					case "E\t":
						grid[i][j] = new Empty(this, i, j);
						break;  

					case "C\t":
						grid[i][j] = new Casual(this, i, j);
						break;
	                    
					case "O\t":
						grid[i][j] = new Outage(this, i, j);
						break;
						
					case "S\t":
						grid[i][j] = new Streamer(this, i, j);
						break;
	                    
					default:
						break;
				}
			}
		
		input.close();
	}
	
	/**
	 * Returns width of the grid.
	 * @return
	 */
	public int getWidth() {
		return (width);
	}
	
	/**
	 * Returns length of the grid.
	 * @return
	 */
	public int getLength() {
		return (length);
	}

	/**
	 * Initialize the grid by randomly assigning a cell with one of the following class object:
	 * Casual, Empty, Outage, Reseller OR Streamer
	 */
	public void randomInit(int seed) 
	{
		Random rand = new Random(seed);
		
		//Iterates through newly created grid
		for (int i = 0; i <= (length - 1); i++)
			for (int j = 0; j <= (width - 1); j++) {
			
				//Initializes r as a random integer every j iteration
				int r = rand.nextInt(5);
	
				//Assigns grid to a random descendant class
				switch(r) 
				{
					case 0:
						grid[i][j] = new Reseller(this, i, j);
						break;
						
					case 1:
						grid[i][j] = new Empty(this, i, j);
						break;
						
					case 2:
						grid[i][j] = new Casual(this, i, j);
						break;
						
					case 3:
						grid[i][j] = new Outage(this, i, j);
						break;
						
					case 4:
						grid[i][j] = new Streamer(this, i, j);
						break;
					
					default:
						break;
				}
		}
	}
	
	/**
	 * Output the town grid. For each square, output the first letter of the cell type.
	 * Each letter should be separated either by a single space or a tab.
	 * And each row should be in a new line. There should not be any extra line between 
	 * the rows.
	 */
	@Override
	public String toString() 
	{
		String s = "";
		
		//Iterates through the grid
		for (int i = 0; i <= getLength() - 1; i++)
		{
			//Generates a new line every i iteration
			s = s + "\n";
			
			for (int j = 0; j <= getWidth() - 1; j++) {
				
				//Checks to see what object then adds respective char to String 's'
				if (grid[i][j].who().equals(State.CASUAL))
					s = s + "C" + " ";
		
				else if (grid[i][j].who().equals(State.EMPTY))
					s = s + "E" + " ";
		
				else if (grid[i][j].who().equals(State.RESELLER))
					s = s + "R" + " ";
		
				else if (grid[i][j].who().equals(State.OUTAGE))
					s = s + "O" + " ";
		
				else if (grid[i][j].who().equals(State.STREAMER))
					s = s + "S" + " ";
			}
		}
		
		return (s);
	}
}
