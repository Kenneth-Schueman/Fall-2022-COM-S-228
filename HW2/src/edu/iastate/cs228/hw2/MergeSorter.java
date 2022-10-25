package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 *  
 * @author <KenSchue>
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	// Other private instance variables if needed
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		super(pts);
		super.algorithm = "mergesort";  
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		mergeSortRec(points); 
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		int n = pts.length;
		
		if (n <= 1)
		{
			return;
		}
		
		int k = n / 2;
		
		//Creating Left anb Right points
		Point[] ptsL = new Point[k];
		Point[] ptsR = new Point[n - k];
		
		int i = 0;
		
		for (int j = 0; j < ptsL.length; j++)
		{
			ptsL[j] = pts[i];
			i++;
		}
		
		for (int j = 0; j < ptsR.length; j++)
		{
			ptsR[j] = pts[i];
			i++;
		}
		
		//Subarrays
		mergeSortRec(ptsL);
		mergeSortRec(ptsR);
		
		//Created own merge for ease
		merge(pts, ptsL, ptsR);
	}


	private void merge(Point[] pts, Point[] ptsL, Point[] ptsR) 
	{
		int i = 0, j = 0, k = 0;
		int L = ptsL.length, R = ptsR.length;
		
		//Loop will break when points are sorted
		while (i < L && j < R)
		{
			//Primary condition
			if (ptsL[i].compareTo(ptsR[j]) < 0)
			{
				pts[k] = ptsL[i];
				i++;
				k++;
			}
			
			else
			{
				pts[k] = ptsR[j];
				j++;
				k++;
			}
		}
		
		//Secondary condition
		while (j < R)
		{
			pts[k] = ptsR[j];
			j++;
			k++;
		}
		
		//Third condition
		while (i < L)
		{
			pts[k] = ptsL[i];
			i++;
			k++;
		}
	}
}
