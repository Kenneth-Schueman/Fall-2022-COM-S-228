package edu.iastate.cs228.hw2;

import java.io.File;

/**
 * 
 * @author <KenSchue>
 *
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * 
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class PointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;   
	
	private String fileName;
	
		
	protected long scanTime; 	       // execution time in nanoseconds. 
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		if (pts == null || pts.length == 0)
		{
			throw new IllegalArgumentException();
		}
		
		//Checks if pts has valid values and updates pts and algo
		else
		{
			points = pts;
			sortingAlgorithm = algo;
		}
	}

	
	/**
	 * This constructor reads points from a file. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		//Updates algo and inputFileName 
		sortingAlgorithm = algo;
		fileName = inputFileName;
		
		//Counter
		int ctr = 0;
		//Try and Catch to make sure file is readable and has valid data
		try
		{
			File f = new File(inputFileName);
			Scanner fileInput = new Scanner(f);
			
			//This while loop counts how many int's are in the file
			while (fileInput.hasNext())
			{
				int v = fileInput.nextInt();
				ctr++;
			}
			
			fileInput.close();
			
			//Throws IME if there are odd amount of int's
			if (ctr % 2 != 0)
			{
				throw new InputMismatchException();
			}
			
			points = new Point[ctr / 2];
			
			Scanner fileInput2 = new Scanner(f);
			
			int i = 0;
			
			//Created new point array above and creating new points below
			while (fileInput2.hasNext())
			{
				int x = fileInput2.nextInt();
				int y = fileInput2.nextInt();
				
				//New points are initialized in array
				points[i] = new Point(x, y);
				
				i++;
			}
			
			fileInput2.close();
		}
		
		catch (FileNotFoundException e)
		{
			System.out.println("File Not Found");
			throw new FileNotFoundException();
		}
	}

	
	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.  
	 *     //aSorter.AbstractSorter(points);
	*	
	*	// create an object to be referenced by aSorter according to sortingAlgorithm. for each of the two 
	*	// rounds of sorting, have aSorter do the following: 
	*	// 
	*	//     a) call setComparator() with an argument 0 or 1. 
	*	//
	*	//     b) call sort(). 		
	*	// 
	*	//     c) use a new Point object to store the coordinates of the medianCoordinatePoint
	*	//
	*	//     d) set the medianCoordinatePoint reference to the object with the correct coordinates.
	*	//
	*	//     e) sum up the times spent on the two sorting rounds and set the instance variable scanTime.    
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.       
	 * @param algo
	 * @return
	 */
	public void scan()
	{
		AbstractSorter aSorter = null;

		//Checks what sortingAlgorithm is set too and initializes the sorters
		if (sortingAlgorithm == Algorithm.MergeSort)
		{
			aSorter = new MergeSorter(points);
		}
		
		if (sortingAlgorithm == Algorithm.InsertionSort)
		{
			aSorter = new InsertionSorter(points);
		}
		
		if (sortingAlgorithm == Algorithm.SelectionSort)
		{
			aSorter = new SelectionSorter(points);
		}
		
		if (sortingAlgorithm == Algorithm.QuickSort)
		{
			aSorter = new QuickSorter(points);
		}
		
		long timeStart;
		long timeEnd;
		
		//Compare via X
		aSorter.setComparator(0);
		timeStart = System.nanoTime();
		aSorter.sort();
		timeEnd = System.nanoTime();
		
		long xTime = timeEnd - timeStart;
		
		int x = aSorter.getMedian().getX();
		
		//Compare via Y
		aSorter.setComparator(1);
		timeStart = System.nanoTime();
		aSorter.sort();
		timeEnd = System.nanoTime();
		
		long yTime = timeEnd - timeStart;
		
		//Final logic for time and medianCoordinatePoint
		scanTime = xTime + yTime;
		
		int y = aSorter.getMedian().getY();
		
		medianCoordinatePoint = new Point(x, y);
	}
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{
		String stats = null;
		
		//Checks sortingAlgorithm and passes creates the correct string 
		if (sortingAlgorithm == Algorithm.MergeSort)
			stats = "MergeSort" + "	" + points.length + "	" + scanTime;
		
		if (sortingAlgorithm == Algorithm.InsertionSort)
			stats = "InsertionSort" + "	" + points.length + "	" + scanTime;
		
		if (sortingAlgorithm == Algorithm.SelectionSort)
			stats = "SelectionSort" + "	" + points.length + "	" + scanTime;
		
		if (sortingAlgorithm == Algorithm.QuickSort)
			stats = "QuickSort" + "	" + points.length + "	" + scanTime;
		
		return (stats);
	}
	
	
	/**
	 * Write MCP after a call to scan(),  in the format "MCP: (x, y)"   The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{
		if (medianCoordinatePoint == null)
		{
			System.out.println("MCP is null");
			return "";
		}
		
		//Prints MCP point when called
		return medianCoordinatePoint.toString();
	}

	
	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException
	{	
		try 
		{
			FileWriter f = new FileWriter(fileName);
			
			f.write(toString());
			
			f.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
}
