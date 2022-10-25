package edu.iastate.cs228.hw2;

/**
 *  
 * @author <KenSchue>
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random; 


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{		
		PointScanner[] scanners = new PointScanner[4];
		Random rand = new Random();
		Scanner input = new Scanner(System.in);


		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");

		int userChoice = 0;
		int itr = 1;

		while(userChoice != 3)
		{
			System.out.println("Choices:  1) Random Points 2) Input File  3) Exit");

			userChoice = input.nextInt();

			if(userChoice == 1)
			{
				System.out.println("Trial " + itr + ": " + userChoice);
				System.out.println("Enter number of random points: ");
				
				int randNumPoints = input.nextInt();

				//Moved to own class
				randomPoints(randNumPoints, rand, scanners);

				itr++;
			}
			
			if(userChoice == 2)
			{
				System.out.println("Trial " + itr + ": " + userChoice);
				System.out.println("Points From a File");
				System.out.println("File Name: ");
				String fileString = input.next();

				//Moved to own class
				fileInputPoints(fileString, scanners);

				scanners[3].writeMCPToFile();

				itr++;
			}

		}
	}
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] � [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	private static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{
		if(numPts < 1) 
		{
			throw new IllegalArgumentException();
		}

		//Checks for points and then generates desired amount of points inbetween -50 and 50
		Point[] randPoints = new Point[numPts];

		for(int i = 0; i < numPts; i++)
		{
			Point p = new Point(rand.nextInt(101)-50, rand.nextInt(101)-50);
			randPoints[i] = p;

		}
		
		return randPoints;
	}

	/*
	 * Created own classes for different choices of point input because if user decided to input file after generating random points errors would occur
	 */
	public static void randomPoints(int numPoints, Random rand, PointScanner[] scanners)
	{
		scanners[0] = new PointScanner(generateRandomPoints(numPoints,rand), Algorithm.SelectionSort);
		scanners[1] = new PointScanner(generateRandomPoints(numPoints,rand), Algorithm.InsertionSort);
		scanners[2] = new PointScanner(generateRandomPoints(numPoints,rand), Algorithm.MergeSort);
		scanners[3] = new PointScanner(generateRandomPoints(numPoints,rand), Algorithm.QuickSort);

		System.out.println("\nAlgorithm	Size  Time (ns)");
		System.out.println("----------------------------------");
		for(int i = 0; i < 4; i++)
		{
			scanners[i].scan();
			System.out.println(scanners[i].stats());
		}
		System.out.println("----------------------------------\n");
	}

	public static void fileInputPoints(String fileName,PointScanner[] scanners) throws FileNotFoundException 
	{
		scanners[0] = new PointScanner(fileName, Algorithm.SelectionSort);
		scanners[1] = new PointScanner(fileName, Algorithm.InsertionSort);
		scanners[2] = new PointScanner(fileName, Algorithm.MergeSort);
		scanners[3] = new PointScanner(fileName, Algorithm.QuickSort);

		System.out.println("\nAlgorithm	Size  Time (ns)");
		System.out.println("----------------------------------");
		for(int i = 0; i < 4; i++)
		{
			scanners[i].scan();
			System.out.println(scanners[i].stats());
		}
		System.out.println("----------------------------------\n");
	}
	
}
