package edu.iastate.cs228.hw1;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

/**
 * @author <<KenSchue>>
 *
 * The ISPBusiness class performs simulation over a grid 
 * plain with cells occupied by different TownCell types.
 *
 */
public class ISPBusiness {
	
	/**
	 * Returns a new Town object with updated grid value for next billing cycle.
	 * @param tOld: old/current Town object.
	 * @return: New town object.
	 */
	public static Town updatePlain(Town tOld) 
	{
		//Initiating tNew with same parm as grid
		Town tNew = new Town(tOld.getLength(), tOld.getWidth());
		
		//Iterates through last plain (tOld)
		for (int i = 0; i <= tOld.getLength() - 1; i++)
			for (int j = 0; j <= tOld.getWidth() - 1; j++) 
				
				//Creates a new plain when 'next' method is called
				tNew.grid[i][j] = tOld.grid[i][j].next(tNew);
		
		return (tNew);
	}
	
	/**
	 * Returns the profit for the current state in the town grid.
	 * @param town
	 * @return
	 */
	public static int getProfit(Town town) 
	{
		int profit = 0;
		
		//2D Iterator using get methods for L & W
		for (int i = 0; i <= town.getLength() - 1; i++)
			for (int j = 0; j <= town.getWidth() - 1; j++)
				
				//Checks grid to find casual consumers, then adds to counter (profit)
				if(town.grid[i][j].who().equals(State.CASUAL)) 
					profit++;
		
		return (profit);
	}
	

	/**
	 *  Main method. Interact with the user and ask if user wants to specify elements of grid
	 *  via an input file (option: 1) or wants to generate it randomly (option: 2).
	 *  
	 *  Depending on the user choice, create the Town object using respective constructor and
	 *  if user choice is to populate it randomly, then populate the grid here.
	 *  
	 *  Finally: For 12 billing cycle calculate the profit and update town object (for each cycle).
	 *  Print the final profit in terms of %. You should print the profit percentage
	 *  with two digits after the decimal point:  Example if profit is 35.5600004, your output
	 *  should be:
	 *
	 *	35.56%
	 *  
	 * Note that this method does not throw any exception, so you need to handle all the exceptions
	 * in it.
	 * 
	 * @param args
	 * 
	 */
	public static void main(String []args) {
		int length, width, seed;
		int pgrmType = 0, currProfit = 0;
		double totalProfit = 0.0;
		Town town = null;
		
		//Scanner generation used for user selection
		Scanner userSelect = new Scanner(System.in); 
		
		//Program START prompts 	
		System.out.println("Methods to populate grid,");
		System.out.println("1: From a file");
		System.out.println("2: Randomly with a seed");
		System.out.println("(Please select 1 or 2): ");
				
		//Scanner for program type selection
		pgrmType = userSelect.nextInt(); 
		
		//Path for pgrmType 1 || File Input
		if (pgrmType == 1) 
		{			
			//Scanner to read file
			Scanner fileInput = new Scanner (System.in); 
			
			System.out.println("Enter filename:");
			
			String filename = fileInput.nextLine();
			
			//Try & Catch to ensure user selects readable file
			try
			{
				town = new Town(filename);
			}
			
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				fileInput.close();
				
				return;
			}
			
			fileInput.close();
		}
			
		//path for pgrmType 2 || Random with Seed
		else if (pgrmType == 2) 
		{	
			System.out.println("\nProvide Row, Col and seed integer separated by spaces:");
			
			//Scanning using already initiated scanner for user parms
			length = userSelect.nextInt();
			width = userSelect.nextInt();
			seed = userSelect.nextInt();	
				
			userSelect.close();
				
			//Initiating Town & passing length, width; also passing seed into randomInit() 
			town = new Town(length, width); 
			town.randomInit(seed);	
		}
			
		else
		{
			//Test case to warn user of input out of bounds
			System.out.println("Error");

		}
		
		//Extra insurance that scanner is closed
		userSelect.close();  
		
		for (int i = 1; i <= 12; i++)
		{
			//Print statements for the iterator & grid as a string
			System.out.print("\nAfter itr: " + i);
			System.out.print(town.toString() + "\n");
			
			//Sets getProfit() as current profit; then prints current profit
			currProfit = getProfit(town); 
			System.out.println("\nProfit:" + currProfit);
			
			//Adds current profit to total sum; updates town to next plain; +1 itr 
			totalProfit += getProfit(town);
			town = updatePlain(town);
		}		
		
		//Initializes potential profit as the size of grid (x,y) multiplied by the number of iterations
		int potentProfit = (town.getLength() * town.getWidth() * 12);
		
		//Initializes percent as the total profit divided by the potential profit 
		double percent = (totalProfit * 100) / potentProfit;
		
		//Pints percent of profit and formats to x.xx
		System.out.println("\n" + String.format("%.2f", percent) + "%");
	}
}
