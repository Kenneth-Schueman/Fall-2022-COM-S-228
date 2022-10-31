package edu.iastate.cs228.hw3;

import static org.junit.Assert.*;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 *
 * @author Ryan Krause
 *
 *         A lot of unit tests for the project that I'm sure you all are working
 *         super hard on and don't have time to write unit tests. Enjoy!
 *
 */
public class StoutListTests {

	StoutList<String> stout;
	StoutList<String> stout6;
	StoutList<String> stoutBad;
	ListIterator<String> stoutIter;
	ListIterator<String> stoutIterPos;
	ListIterator<String> stoutIterEnd;
	ListIterator<String> stoutIterBad;
	String msg;

	@Before
	public void setup() {
		stout = new StoutList<String>();
		stout.add("A");
		stout.add("B");
		stout.add("X");
		stout.add("X");
		stout.add("C");
		stout.add("D");
		stout.add("E");
		stout.remove(2); // these are so we can have spaces in nodes.
		stout.remove(2); // don't really know another way to do that.

		stout6 = new StoutList<String>(6);

		stoutIter = stout.listIterator();
		stoutIterPos = stout.listIterator(2);
		stoutIterEnd = stout.listIterator(5);

	}

	@After
	public void cleanup() {
		stout = null;
		stout6 = null;
		stoutBad = null;
		msg = null;
		stoutIter = null;
		stoutIterPos = null;
		stoutIterBad = null;
		stoutIterEnd = null;
	}

	@Test
	public void setupTest() {
		msg = "Your setup method is broken, you should check out my PDFexample on blackboard.";
		assertEquals(msg, "[(A, B, -, -), (C, D, E, -)]",
				stout.toStringInternal());
	}

	@Test
	public void bubbleSortTest() {
		msg = "When sort reverse is called, the items are sorted decreasingly";
		stout.sortReverse();
		assertEquals(msg, "[(E, D, C, B), (A, -, -, -)]",
				stout.toStringInternal());
	}

	@Test
	public void insertionSortTest() {
		msg = "When sort is called, the terms should be sorted increasingly.";
		stout.sort();
		assertEquals(msg, "[(A, B, C, D), (E, -, -, -)]",
				stout.toStringInternal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void StoutListOddConstructorTest() {
		// msg =
		// "A Stout list must be constructed with M elements were M is an even positive number";
		stoutBad = new StoutList<String>(5);
	}

	@Test
	public void checkSize() {
		msg = "The current size of the list is 5 elements";
		assertEquals(msg, 5, stout.size());
	}

	@Test
	public void createListIterNoArg() {
		msg = "List iterator with no arguments should have a cursor at pos 0.";
		assertEquals(msg, "[(| A, B, -, -), (C, D, E, -)]",
				stout.toStringInternal(stoutIter));
	}

	@Test
	public void createListIterWithArg() {
		msg = "List iterator with a pos argument of 2 should have a cursor at cursor pos 2.";
		assertEquals(msg, "[(A, B, -, -), (| C, D, E, -)]",
				stout.toStringInternal(stoutIterPos));
	}

	@Test
	public void createListIterWithArgEnd() {
		msg = "List iterator with a pos argument of 5 should have a cursor at cursor pos 5.";
		assertEquals(msg, "[(A, B, -, -), (C, D, E |, -)]",
				stout.toStringInternal(stoutIterEnd));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void createListIterOutOfBounds() {
		msg = "Your listIterator(super big number) should throw an outofbounds exception.";
		stoutIterBad = stout.listIterator(500);
	}

	@Test
	public void hasNextTest0() {
		msg = "When starting at pos 0, has next should return true";
		assertTrue(msg, stoutIter.hasNext());
	}

	@Test
	public void hasNextTest2() {
		msg = "When starting at pos 2, has next should return true";
		assertTrue(msg, stoutIter.hasNext());
	}

	@Test
	public void hasNextEndTest() {
		msg = "When starting at pos 5, has next should return false";
		assertFalse(msg, stoutIterEnd.hasNext());
	}

	@Test
	public void hasPreviousStartTest() {
		msg = "When starting at pos 0, has previous should return false";
		assertFalse(msg, stoutIter.hasPrevious());
	}

	@Test
	public void hasPreviousTest2() {
		msg = "When starting at pos 2, hasprevious should return true";
		assertTrue(msg, stoutIterPos.hasPrevious());
	}

	@Test
	public void hasPreviousTestEnd() {
		msg = "When starting at the end of the list, has previous should return True";
		assertTrue(msg, stoutIterEnd.hasPrevious());
	}

	@Test
	public void nextIterStartTest() {
		msg = "When starting at pos 0, next should return 'A'";
		assertEquals(msg, "A", stoutIter.next());
	}

	@Test
	public void nextIterPos2Test() {
		msg = "When starting at pos 2, next should return 'C'";
		assertEquals(msg, "C", stoutIterPos.next());
	}

	@Test(expected = NoSuchElementException.class)
	public void nextIterEndTest() {
		msg = "When starting at the end of the list, next should throw a NoSuchElementException";
		stoutIterEnd.next();
	}

	@Test
	public void nextIndexIter2Test() {
		msg = "When starting at pos 2, nextIndex should return 2";
		assertEquals(msg, 2, stoutIterPos.nextIndex());
	}

	@Test
	public void nextIndexIterStartTest() {
		msg = "When starting at pos end, nextIndex should return end";
		assertEquals(msg, 5, stoutIterEnd.nextIndex());
	}

	@Test
	public void prevousIndexIter2Test() {
		msg = "When starting at pos 2, nextIndex should return 1";
		assertEquals(msg, 1, stoutIterPos.previousIndex());
	}

	@Test
	public void prevousIndexIterStartTest() {
		msg = "When starting at pos 0, nextIndex should return -1";
		assertEquals(msg, -1, stoutIter.previousIndex());
	}

	@Test(expected = NoSuchElementException.class)
	public void prevIterStartTest() {
		msg = "When starting at the start of the list, previous should throw a NoSuchElementException";
		stoutIter.previous();
	}

	@Test
	public void prevIterPos2Test() {
		msg = "When starting at pos 2, previous should return 'B'";
		assertEquals(msg, "B", stoutIterPos.previous());
	}

	@Test
	public void prevtIterEndTest() {
		msg = "When starting at the end of the list, previous should return 'E'";
		assertEquals(msg, "E", stoutIterEnd.previous());
	}

	@Test(expected = IllegalStateException.class)
	public void setIterfailTest() {
		msg = "when calling set on a new list, IllegalStateException is thrown";
		stoutIter.set("false");
	}

	@Test
	public void setIterNextTest() {
		msg = "When calling next then set, the item replaces the item just returned by next";
		stoutIter.next();
		stoutIter.set("a");
		assertEquals(msg, "[(a, | B, -, -), (C, D, E, -)]",
				stout.toStringInternal(stoutIter));
	}

	@Test
	public void setIterPreviousTest() {
		msg = "When calling previous then set, the item replaces the item just returned by prevous";
		stoutIterEnd.previous();
		stoutIterEnd.set("e");
		assertEquals(msg, "[(A, B, -, -), (C, D, | e, -)]",
				stout.toStringInternal(stoutIterEnd));
	}

	@Test
	public void addStartTest() {
		msg = "When adding using the iterator at pos 0, the item should be added at the start of the list and the cursor moved";
		stoutIter.add("X");
		assertEquals(msg, "[(X, | A, B, -), (C, D, E, -)]",
				stout.toStringInternal(stoutIter));
	}

	@Test
	public void addIterPos2Test() {
		msg = "When adding using the iterator at pos 2, the item should be added before the cursor";
		stoutIterPos.add("X");
		assertEquals(msg, "[(A, B, X, -), (| C, D, E, -)]",
				stout.toStringInternal(stoutIterPos));
	}

	@Test
	public void addIterEndTest() {
		msg = "When adding using the iterator at pos end, the item should be added to the end of the list";
		stoutIterEnd.add("X");
		assertEquals(msg, "[(A, B, -, -), (C, D, E, X |)]",
				stout.toStringInternal(stoutIterEnd));
	}

	@Test(expected = IllegalStateException.class)
	public void removeErrorTest() {
		msg = "When calling a remove function without calling next or previous, it should throw an IllegalStateException";
		stoutIter.remove();
	}

	@Test
	public void removeTest1() {
		msg = "When calling next and then remove from pos 0, remove the element that was returned from next";
		stoutIter.next();
		stoutIter.remove();
		assertEquals(msg, "[(| B, C, -, -), (D, E, -, -)]",
				stout.toStringInternal(stoutIter));
	}

	@Test
	public void removeTest2() {
		msg = "When calling next and then remove from pos 2, remove the element that was returned from next";
		stoutIterPos.next();
		stoutIterPos.remove();
		assertEquals(msg, "[(A, B, -, -), (| D, E, -, -)]",
				stout.toStringInternal(stoutIterPos));
	}

	@Test
	public void ExamplePDFIteratorTest() {
		// non traditional unit test sorry... gotta go step by step.
		StoutList<String> stout = new StoutList<String>();
		ListIterator<String> stoutIter = stout.listIterator();
		stoutIter.add("A");
		stoutIter.add("B");
		stoutIter.add("X");
		stoutIter.add("X");
		stoutIter.add("C");
		stoutIter.add("D");
		stoutIter.add("E");
		stoutIter.previous();
		stoutIter.previous();
		stoutIter.previous();
		stoutIter.previous();
		stoutIter.remove();
		stoutIter.previous();
		stoutIter.remove();
		assertEquals("[(A, B, -, -), (| C, D, E, -)]",
				stout.toStringInternal(stoutIter));

		stoutIter.next();
		stoutIter.next();
		stoutIter.next();
		stoutIter.add("V");
		assertEquals("[(A, B, -, -), (C, D, E, V |)]",
				stout.toStringInternal(stoutIter));

		stoutIter.add("W");
		assertEquals("[(A, B, -, -), (C, D, E, V), (W |, -, -, -)]",
				stout.toStringInternal(stoutIter));

		stoutIter.previous();
		stoutIter.previous();
		stoutIter.previous();
		stoutIter.previous();
		stoutIter.previous();
		stoutIter.add("X");
		assertEquals("[(A, B, X, -), (| C, D, E, V), (W, -, -, -)]",
				stout.toStringInternal(stoutIter));

		stoutIter.previous();
		stoutIter.add("Y");
		assertEquals("[(A, B, Y, | X), (C, D, E, V), (W, -, -, -)]",
				stout.toStringInternal(stoutIter));

		stoutIter.previous();
		stoutIter.add("Z");
		assertEquals(
				"[(A, B, Z, -), (| Y, X, -, -), (C, D, E, V), (W, -, -, -)]",
				stout.toStringInternal(stoutIter));

		// this is so tediuous...

		stoutIter.next();
		stoutIter.next();
		stoutIter.next();
		stoutIter.next();
		stoutIter.next();
		stoutIter.next();
		stoutIter.next();
		stoutIter.remove();
		assertEquals("[(A, B, Z, -), (Y, X, -, -), (C, D, E, V |)]",
				stout.toStringInternal(stoutIter));

		stoutIter.previous();
		stoutIter.previous();
		stoutIter.previous();
		stoutIter.previous();
		stoutIter.previous();
		stoutIter.previous();
		stoutIter.remove();
		assertEquals("[(A, B, Z, -), (| X, C, -, -), (D, E, V, -)]",
				stout.toStringInternal(stoutIter));

		stoutIter.next();
		stoutIter.remove();
		assertEquals("[(A, B, Z, -), (| C, D, -, -), (E, V, -, -)]",
				stout.toStringInternal(stoutIter));

		stoutIter.next();
		stoutIter.next();
		stoutIter.next();
		stoutIter.remove();
		assertEquals("[(A, B, Z, -), (C, D, -, -), (| V, -, -, -)]",
				stout.toStringInternal(stoutIter));

		stoutIter.previous();
		stoutIter.previous();
		stoutIter.remove();
		assertEquals("[(A, B, Z, -), (| D, V, -, -)]",
				stout.toStringInternal(stoutIter));

	}

}