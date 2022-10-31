package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
/**
 *  
 * @author <KenSchue>
 *
 */

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends
		AbstractSequentialList<E> {
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	 /**
	   * Constructs an empty list with the given node size.
	   * @param nodeSize number of elements that may be stored in each node, must be 
	   *   an even number
	   */
	public StoutList(int nodeSize) 
	{
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) 
	{
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}

	
	@Override
	public int size() 
	{
		if (size > Integer.MAX_VALUE)
			return (Integer.MAX_VALUE);
		return size;
	}

	@Override
	public boolean add(E item) throws NullPointerException 
	{
		if (item == null)
			throw new NullPointerException();
		
		add(size, item);
		return true;
	}

	@Override
	public void add(int pos, E item) 
	{
		if (item == null)
			throw new NullPointerException();
		
		if (pos < 0 || pos > size)
			throw new IndexOutOfBoundsException();
		
		NodeInfo nodeInfo = find(pos);
		add(nodeInfo.node, nodeInfo.offset, item);
	}

	
	@Override
	public E remove(int pos) 
	{
		if (pos < 0 || pos > size)
			throw new IndexOutOfBoundsException();
		
		NodeInfo nodeInfo = find(pos);
		return remove(nodeInfo);
	}

	/**
	   * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
	   * Traverse the list and copy its elements into an array, deleting every visited node along 
	   * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
	   * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
	   * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
	   * (possibly) the last one must be full of elements.  
	   *  
	   * Comparator<E> must have been implemented for calling insertionSort().    
	   */
	public void sort() 
	{
		Iterator<E> iter = iterator();
		
		E[] sortDataList = (E[]) new Comparable[size()];
		
		for (int i = 0; i < size; i++) 
			sortDataList[i] = iter.next();
		
		head.next = tail;
		tail.previous = head;
		size = 0;
		
		Comparator<E> comp = new genericComparator();
		insertionSort(sortDataList, comp);
	}

	/**
	 * Sort all elements in the Stout list in the NON-INCREASING order. Call
	 * the bubbleSort() method. After sorting, all but (possibly) the last nodes
	 * must be filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() 
	{
		Iterator<E> iter = iterator();
		E[] ReverseSortDataList = (E[]) new Comparable[size];
		
		for (int i = 0; i < size; i++) 
			ReverseSortDataList[i] = iter.next();
		
		head.next = tail;
		tail.previous = head;
		size = 0;
		
		bubbleSort(ReverseSortDataList);
		
		for(int i = 0 ; i < ReverseSortDataList.length; i++)
			this.add(ReverseSortDataList[i]);
	}

	@Override
	public Iterator<E> iterator() {
		return listIterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) throws IndexOutOfBoundsException 
	{
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException();
		
		StoutListIterator iter = new StoutListIterator(index);
		return iter;
	}

	/**
	   * Returns a string representation of this list showing
	   * the internal structure of the nodes.
	   */
	public String toStringInternal() 
	{
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal
	 * structure of the nodes and the position of the iterator.
	 * 
	 * @param iter
	 *            an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];

			for (int i = 0; i < nodeSize; ++i) {
				if (i != 0)
					sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	   * Node type for this list.  Each node holds a maximum
	   * of nodeSize elements in an array.  Empty slots
	   * are null.
	   */
	private class Node {
		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * unite to next node.
		 */
		public Node next;

		/**
		 * unite to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the
		 * number of elements in this node.
		 */
		public int count;

		/**
		 * Adds an item to this node at the first available offset.
		 * Precondition: count < nodeSize
		 * 
		 * @param item
		 *            element to be added
		 */
		void addItem(E item) {
			if (count >= nodeSize) {
				return;
			}
			data[count++] = item;
			//useful for debugging
		    //      System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
		}

		/**
	     * Adds an item to this node at the indicated offset, shifting
	     * elements to the right as necessary.
	     * 
	     * Precondition: count < nodeSize
	     * @param offset array index at which to put the new element
	     * @param item element to be added
	     */
		void addItem(int offset, E item) 
		{
			if (count >= nodeSize) 
				return;
			for (int i = count - 1; i >= offset; --i)
				data[i + 1] = data[i];
			
			++count;
			data[offset] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + offset + " to node: " + Arrays.toString(data));
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting
		 * elements left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void removeItem(int offset) {
			E item = data[offset];
			
			for (int i = offset + 1; i < nodeSize; ++i)
				data[i - 1] = data[i];
			
			data[count - 1] = null;
			--count;
		}
	}
	
	private class NodeInfo 
	{
		
		public Node node;
		
		public int offset;

		//Helper class to represent a specific point on the list
		public NodeInfo(Node node, int offset) 
		{
			this.node = node;
			this.offset = offset;
		}
	}

	//Helper method to locate a specific item
	NodeInfo find(int pos) 
	{
		if (pos == -1)
			return new NodeInfo(head, 0);
		
		if (pos == size)
			return new NodeInfo(tail, 0);

		Node current = head.next;
		int index = current.count - 1;
		
		while (current != tail && pos > index) 
		{
			current = current.next;
			index += current.count;
		}
		
		int offset = current.count + pos - index - 1;
		
		return new NodeInfo(current, offset);
	}

	private NodeInfo add(Node n, int offset, E item) 
	{
		if (item == null)
			throw new NullPointerException();
		
		NodeInfo NodeInfo = null;
		
		if (size == 0) 
		{
			Node Node = new Node();
			
			Node.addItem(item);
			unite(head, Node);
			NodeInfo = new NodeInfo(Node, 0);
		} 
		
		else if (offset == 0 && n.previous.count < nodeSize && n.previous != head) 
		{
			n.previous.addItem(item);
			NodeInfo = new NodeInfo(n.previous, n.previous.count - 1);
		} 
		
		else if (offset == 0 && n == tail && n.previous.count == nodeSize) 
		{
			Node Node = new Node();
			
			Node.addItem(item);
			unite(tail.previous, Node);
			NodeInfo = new NodeInfo(Node, 0);

		} 
		
		else if (n.count < nodeSize) 
		{
			n.addItem(offset, item);
			NodeInfo = new NodeInfo(n, offset);
		} 
		
		else 
		{
			Node Node = new Node();
			
			unite(n, Node);
			
			for (int i = nodeSize - 1; i >= nodeSize - nodeSize / 2; i--) 
			{
				Node.addItem(0, n.data[i]);
				n.removeItem(i);
			}
			
			if (offset <= nodeSize / 2) 
			{
				n.addItem(offset, item);
				NodeInfo = new NodeInfo(n, offset);
			} 
			
			else 
			{
				Node.addItem(offset - nodeSize / 2, item);
				NodeInfo = new NodeInfo(Node, offset - nodeSize / 2);
			}
		}
		
		size++;
		return (NodeInfo);
	}

	private E remove(NodeInfo nodeInfo) 
	{
		E E = nodeInfo.node.data[nodeInfo.offset];
		
		if (nodeInfo.node.next == tail && nodeInfo.node.count == 1) 
			seperate(nodeInfo.node); 
		
		else if (nodeInfo.node.next == tail || nodeInfo.node.count > nodeSize / 2) 
			nodeInfo.node.removeItem(nodeInfo.offset);
		
		else if (nodeInfo.node.count <= nodeSize / 2) 
		{
			nodeInfo.node.removeItem(nodeInfo.offset);
			
			if (nodeInfo.node.next.count > nodeSize / 2) 
			{
				nodeInfo.node.addItem(nodeInfo.node.next.data[0]);
				nodeInfo.node.next.removeItem(0);
			} 
			
			else 
			{
				for (E e : nodeInfo.node.next.data) 
					if (e != null)
						nodeInfo.node.addItem(e);
				
				seperate(nodeInfo.node.next);
			}
		}
		
		size--;
		
		return (E);
	}

	private void unite(Node current, Node newNode) 
	{
		newNode.previous = current;
		newNode.next = current.next;
		current.next.previous = newNode;
		current.next = newNode;
	}

	private void seperate(Node current) 
	{
		current.previous.next = current.next;
		current.next.previous = current.previous;
	}

	private class genericComparator<E extends Comparable<? super E>> implements Comparator 
	{
		@Override
		public int compare(Object object1, Object object2) 
		{
			E first = (E) object1;
			E second = (E) object2;
			
			return first.compareTo(second);
		}
	}

	private class StoutListIterator implements ListIterator<E> 
	{
		private int index;

		private NodeInfo lastAction;

		private boolean canRemove;

		/**
		 * Default constructor
		 */
		public StoutListIterator() 
		{
			index = 0;
			lastAction = null;
			canRemove = false;
		}

		/**
	     * Constructor finds node at a given position.
	     * @param pos
	     */
		public StoutListIterator(int pos) 
		{
			if (pos < 0 || pos > size)
				throw new IndexOutOfBoundsException();
			
			index = pos;
			lastAction = null;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
			return (index < size);
		}

		@Override
		public E next() throws NoSuchElementException 
		{
			if (hasNext()) 
			{
				NodeInfo n = find(index++);
				
				lastAction = n;
				canRemove = true;
				
				return (n.node.data[n.offset]);
			}
			
			throw new NoSuchElementException();
		}

		@Override
		public void remove() 
		{
			if (!canRemove)
				throw new IllegalStateException();
			
			NodeInfo cursorInfo = find(index);
			
			if (lastAction.node == cursorInfo.node && lastAction.offset < cursorInfo.offset 
					|| lastAction.node != cursorInfo.node)
				
				index--;
			
			StoutList.this.remove(lastAction);
			lastAction = null;
			canRemove = false;
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}

		//Checks if iterator had previous available value
		@Override
		public E previous() throws NoSuchElementException 
		{
			if (hasPrevious()) 
			{
				NodeInfo n = find(--index);
				lastAction = n;
				canRemove = true;
				return n.node.data[n.offset];
			}
			
			throw new NoSuchElementException();
		}

		//Returns index of next open element
		@Override
		public int nextIndex() 
		{
			return index;
		}

		//Returns index of previous element
		@Override
		public int previousIndex() {
			return index - 1;
		}

		//Returns previous open element 
		@Override
		public void set(E E) 
		{
			if (E == null)
				throw new NullPointerException();
			
			if (!canRemove)
				throw new IllegalStateException();
			
			lastAction.node.data[lastAction.offset] = E;
		}

		@Override
		public void add(E e) 
		{
			if (e == null)
				throw new NullPointerException();
			
			canRemove = false;
			StoutList.this.add(index++, e);
		}
	}

	/**
	   * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
	   * @param arr   array storing elements from the list 
	   * @param comp  comparator used in sorting 
	   */
	private void insertionSort(E[] arr, Comparator<? super E> comp) 
	{
		for (int i = 1; i < arr.length; i++) 
		{
			E temp = arr[i];
			int j = i - 1;
			
			while(j > -1 && comp.compare(arr[j], temp) > 0) 
			{
				arr[j + 1] = arr[j];
				j--;
			}
			
			arr[j + 1] = temp;
		}
		
		for(int i = 0 ; i < arr.length; i++)
			this.add(arr[i]);
	}

	/**
	   * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
	   * description of bubble sort please refer to Section 6.1 in the project description. 
	   * You must use the compareTo() method from an implementation of the Comparable 
	   * interface by the class E or ? super E. 
	   * @param arr  array holding elements from the list
	   */
	private void bubbleSort(E[] arr) 
	{
		boolean swap = false;
		
		for (int i = 1; i < arr.length; i++) 
		{
			if (arr[i - 1].compareTo(arr[i]) < 0) 
			{
				E temp = arr[i - 1];
				arr[i - 1] = arr[i];
				arr[i] = temp;
				swap = true;
			}
		}
		
		if (!swap) 
			return;
		
		else 
			bubbleSort(arr);
	}
}