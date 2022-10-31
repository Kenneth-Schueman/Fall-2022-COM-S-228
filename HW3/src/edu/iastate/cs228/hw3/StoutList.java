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
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E>
{
  /**
   * Default number of elements that may be stored in each node.
   */
  private static final int DEFAULT_NODESIZE = 4;
  
  /**
   * Number of elements that can be stored in each node.
   */
  private final int nodeSize;
  
  /**
   * Dummy node for head.  It should be private but set to public here only  
   * for grading purpose.  In practice, you should always make the head of a 
   * linked list a private instance variable.  
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
  public StoutList()
  {
    this(DEFAULT_NODESIZE);
  }

  /**
   * Constructs an empty list with the given node size.
   * @param nodeSize number of elements that may be stored in each node, must be 
   *   an even number
   */
  public StoutList(int nodeSize)
  {
    if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();
    
    // dummy nodes
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.previous = head;
    this.nodeSize = nodeSize;
  }
  
  /**
   * Constructor for grading only.  Fully implemented. 
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
    return (size);
  }
  
  @Override
  public boolean add(E item)
  {
    //Checks if item is null throws NullPointerException
	  if (item == null)
	  {
		  throw new NullPointerException();
	  }
	  
	  if (contains(item))
		  return false;
	  
	  //Checks if list is empty
	  if (size == 0)
	  {
		  Node n = new Node();
		  n.addItem(item);
		  
		  head.next = n;
		  n.previous = head;
		  n.next = tail;
		  tail.previous = n;
	  }
	  
	  else
	  {
		  //Checks if last node is full, if not, adds item to last node
		  if (tail.previous.count < nodeSize)
			  tail.previous.addItem(item);
		  
		  //Checks if last node is full, creates another node and adds item if full
		  else
		  {
			  Node n = new Node();
			  n.addItem(item);
			  
			  Node temp = tail.previous;
			  temp.next = n;
			  n.previous = temp;
			  n.next = temp;
			  tail.previous = n;
		  }
	  }
	  
	  //Increments size of list
	  size++;
	  
	  return (true);
  }

  @Override
  public void add(int pos, E item)
  {
    //Checks if POS is out of bounds, throws IndexOutOFBoundsException
	  if (pos < 0 || pos > size)
		  throw new IndexOutOfBoundsException();
	  
	  //Checks fore empty list, adds new node & sets x at offset 0
	  if (head.next == tail)
		  add(item);
	  
	  NodeInfo nodeInfo = find(pos);
	  Node temp = nodeInfo.node;
	  
	  int offset = nodeInfo.offset;
	  
	  //Checks if offset == 0
	  if (offset == 0)
	  {
		  //Checks if node is not the head & has fewer then M elements, then sets X as predecessor
		  if (temp.previous.count < nodeSize && temp.previous != head)
		  {
			  temp.previous.addItem(item);
			  size++;
			  
			  return;
		  }
		  
		  //Checks if node is the tail & if predecessor = elements,
		  //then creates new node and sets x at offset 0
		  else if (temp == tail)
		  {
			  add(item);
			  size++;
			  
			  return;
		  }
	  }
	  
	  //Sets x at specific n node if space
	  if (temp.count < nodeSize)
		  temp.addItem(offset, item);
	  
	  //Split: sets n as successor
	  else
	  {
		  Node Succesor = new Node();
		  
		  int halfPoint = (nodeSize / 2);
		  int c = 0;
		  
		  while (c < halfPoint)
		  {
			  Succesor.addItem(temp.data[halfPoint]);
			  temp.removeItem(halfPoint);
			  
			  c++;
		  }
		  
		  Node Predecessor = temp.next;
		  
		  temp.next = Succesor;
		  Succesor.previous = temp;
		  Succesor.next = Predecessor;
		  Predecessor.previous = Succesor;
		  
		  //Sets x in node with offset
		  if (offset <= (nodeSize / 2))
			  temp.addItem(offset, item);
		  
		  //Sets x in node with offset - 1/2 nodeSize
		  if (offset > (nodeSize / 2))
			 Succesor.addItem((offset - (nodeSize / 2)), item);
		  
		  //Increments size of list
		  size++;
	  }
  }

  @Override
  public E remove(int pos)
  {
    if (pos < 0 || pos > size)
    		throw new IndexOutOfBoundsException();
    
    NodeInfo nodeInfo = find(pos);
    Node temp = nodeInfo.node;
    
    int offset = nodeInfo.offset;
    E nodeVal = temp.data[offset];
    
    //Checks if node containing x is tail or has one element
    if (temp.next == tail && temp.count == 1)
    {
    	Node Predecessor = temp.previous;
    	
    	Predecessor.next = temp.next;
    	temp.next.previous = Predecessor;
    	
    	temp = null;
    }
    
    //If node is tail and has more then two or elements,
    //or if n has more than nodeSize/2 elements, removes x from node
    else if (temp.next == tail || temp.count > (nodeSize / 2))
    {
    	temp.removeItem(offset);
    }
    
    //Node must have the greatest number of elements, performs merge
    else
    {
    	temp.removeItem(offset);
    	
    	Node Succesor = temp.next;
    	
    	//Mini-merge
    	if (Succesor.count > (nodeSize / 2))
    	{
    		temp.addItem(Succesor.data[0]);
    		Succesor.removeItem(0);
    	}
    	
    	//Full merge
    	else if (Succesor.count <= (nodeSize / 2))
    	{
    		for (int i = 0; i < Succesor.count; i++)
    			temp.addItem(Succesor.data[i]);
    		
    		temp.next = Succesor.next;
    		Succesor.next.previous = temp;
    		
    		Succesor = null;
    	}
    }
    
    size--;
    
    return (nodeVal);
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
	  E[] sortDataList = (E[]) new Comparable[size];
	  
	  int index = 0;
	  Node temp = head.next;
	  
	  while (temp != tail)
	  {
		  for (int i = 0; i < temp.count; i++)
		  {
			  sortDataList[index] = temp.data[i];
			  index++;
		  }
		  temp = temp.next;
	  }
	  
	  head.next = tail;
	  tail.previous = head;
	  
	  insertionSort(sortDataList, new ElementComparator());
	  
	  size = 0;
	  
	  for (int i = 0; i < sortDataList.length; i++)
		  add(sortDataList[i]);
  }
  
  /**
   * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
   * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
   *  
   * Comparable<? super E> must be implemented for calling bubbleSort(). 
   */
  public void sortReverse() 
  {
	  E[] ReverseSortDataList = (E[]) new Comparable[size];
	  
	  int index = 0;
	  Node temp = head.next;
	  
	  while (temp != tail)
	  {
		  for (int i = 0; i < temp.count; i++)
		  {
			  ReverseSortDataList[index] = temp.data[i];
			  index++;
		  }
		  temp = temp.next;
	  }
	  
	  head.next = tail;
	  tail.previous = head;
	  
	  bubbleSort(ReverseSortDataList);
	  
	  size = 0;
	  
	  for (int i = 0; i < ReverseSortDataList.length; i++)
		  add(ReverseSortDataList[i]);
  }
  
  @Override
  public Iterator<E> iterator()
  {
    return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator()
  {
    return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index)
  {
    return new StoutListIterator(index);
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
  public String toStringInternal(ListIterator<E> iter) 
  {
      int count = 0;
      int position = -1;
      
      if (iter != null) 
          position = iter.nextIndex();
   
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Node current = head.next;
      
      while (current != tail) 
      {
          sb.append('(');
          E data = current.data[0];
          
          if (data == null) 
              sb.append("-");
          
          else 
          {
              if (position == count) 
              {
                  sb.append("| ");
                  position = -1;
              }
              
              sb.append(data.toString());
              ++count;
          }

          for (int i = 1; i < nodeSize; ++i) 
          {
             sb.append(", ");
              data = current.data[i];
              
              if (data == null) 
                  sb.append("-");
              
               else 
               {
                  if (position == count) 
                  {
                      sb.append("| ");
                      position = -1;
                  }
                  
                  sb.append(data.toString());
                  ++count;

                  // iterator at end
                  if (position == size && count == size) 
                  {
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

  public boolean contains(E item)
  {
	  if (size < 1)
		  return (false);
	  
	  Node temp = head.next;
	  
	  while (temp != tail)
	  {
		  for (int i = 0; i < temp.count; i++)
		  {
			  if (temp.data[i].equals(item))
				  return (true);
			  
			  temp = temp.next;
		  }
	  }
	  
	  return (false);
  }
  
//Helper class to represent a specific point on the list
  private class NodeInfo
  {
	  public Node node;
	  public int offset;
	  
	  public NodeInfo(Node node, int offset)
	  {
		  this.node = node;
		  this.offset = offset;
	  }
  }
  
	  //Helper method to locate a specific item
	  private NodeInfo find(int pos)
	  {
		  Node temp = head.next;
		  int currentPosition = 0;
		  
		  while (temp != tail)
		  {
			  if (currentPosition + temp.count <= pos)
			  {
				  currentPosition += temp.count;
				  temp = temp.next;
				  
				  continue;
			  }
			  
			  NodeInfo nodeInfo = new NodeInfo(temp, pos - currentPosition);
			  return (nodeInfo);
		  }
		  
		  return (null);
	  }
  
  /**
   * Node type for this list.  Each node holds a maximum
   * of nodeSize elements in an array.  Empty slots
   * are null.
   */
  private class Node
  {
    /**
     * Array of actual data elements.
     */
    // Unchecked warning unavoidable.
    public E[] data = (E[]) new Comparable[nodeSize];
    
    /**
     * Link to next node.
     */
    public Node next;
    
    /**
     * Link to previous node;
     */
    public Node previous;
    
    /**
     * Index of the next available offset in this node, also 
     * equal to the number of elements in this node.
     */
    public int count;

    /**
     * Adds an item to this node at the first available offset.
     * Precondition: count < nodeSize
     * @param item element to be added
     */
    void addItem(E item)
    {
      if (count >= nodeSize)
        return;
      
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
      //useful for debugging 
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
    }

    /**
     * Deletes an element from this node at the indicated offset, 
     * shifting elements left as necessary.
     * Precondition: 0 <= offset < count
     * @param offset
     */
    void removeItem(int offset)
    {
      E item = data[offset];
      
      for (int i = offset + 1; i < nodeSize; ++i)
        data[i - 1] = data[i];

      data[count - 1] = null;
      --count;
    } 
  }
 
  private class StoutListIterator implements ListIterator<E>
  {
	final int LAST_ACTION_PREV = 0;
	final int LAST_ACTION_NEXT = 1;
	
	int currentPosition;
	int lastAction;
	
	public E[] dataList;

	  
    /**
     * Default constructor 
     */
    public StoutListIterator()
    {
    	currentPosition = 0;
    	lastAction = -1;
    	
    	setup();
    }

    /**
     * Constructor finds node at a given position.
     * @param pos
     */
    public StoutListIterator(int pos)
    {
    	currentPosition = pos;
    	lastAction = -1;
    	
    	setup();
    }
    
    //Takes StoutList and put its data into dataList
    private void setup()
    {
    	dataList = (E[]) new Comparable[size];
    	
    	int index = 0;
    	Node temp = head.next;
    	
    	while (temp != tail)
    	{
    		for (int i = 0; i < temp.count; i++)
    		{
    			dataList[index] = temp.data[i];
    			index++;
    		}
    	}
    }

    @Override
    public boolean hasNext()
    {
		if (currentPosition >= size)
			return (false);
		
		else
			return (true);
    }

    @Override
    public E next()
    {
		if (!hasNext())
			throw new NoSuchElementException();
		
		lastAction = LAST_ACTION_NEXT;
		
		return (dataList[currentPosition++]);
    }

    @Override
    public void remove()
    {
    	if (lastAction == LAST_ACTION_NEXT)
    	{
    		StoutList.this.remove(currentPosition - 1);
    		
    		setup();
    		
    		lastAction = -1;
    		currentPosition--;
    		
    		if (currentPosition < 0)
    			currentPosition = 0;
    	}
    	
    	else if (lastAction == LAST_ACTION_PREV)
    	{
    		StoutList.this.remove(currentPosition);
    		
    		setup();
    		
    		lastAction = -1;
    	}
    	
    	else
    		throw new IllegalStateException();
    }
    
 // Other methods you may want to add or override that could possibly facilitate
 		// other operations, for instance, addition, access to the previous element,
 		// etc.
 		//
 		// ...
 		//
    
    //Checks if iterator had previous available value
    @Override
    public boolean hasPrevious()
    {
    	if (currentPosition <= 0)
    		return (false);
    	
    	else
    		return (true);
    }
    
    //Returns index of next open element
    @Override
    public int nextIndex()
    {
    	return (currentPosition);
    }
    
    //Returns previous open element 
    @Override
    public E previous()
    {
    	if (!hasPrevious())
    		throw new NoSuchElementException();
    	
    	lastAction = LAST_ACTION_PREV;
    	currentPosition--;
    	
    	return (dataList[currentPosition]);
    }
    
    //Returns index of previous element
    @Override
    public int previousIndex()
    {
    	return (currentPosition - 1);
    }
    
    //Replace current element with arg0
    @Override
    public void set(E arg0)
    {
    	if (lastAction == LAST_ACTION_NEXT)
    	{
    		NodeInfo nodeInfo = find(currentPosition - 1);
    		nodeInfo.node.data[nodeInfo.offset] = arg0;
    		dataList[currentPosition - 1] = arg0;
    	}
    	
    	else if (lastAction == LAST_ACTION_PREV)
    	{
    		NodeInfo nodeInfo = find(currentPosition);
    		nodeInfo.node.data[nodeInfo.offset] = arg0;
    		dataList[currentPosition] = arg0;
    	}
    	
    	else
    		throw new IllegalStateException();
    }
    
    @Override
    public void add(E arg0)
    {
    	if (arg0 == null)
    		throw new NullPointerException();
    	
    	StoutList.this.add(currentPosition, arg0);
    	currentPosition++;
    	
    	setup();
    	
    	lastAction = -1;
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
		  E k = arr[i];
		  int j = i--;
		  
		  while (j >= 0 && comp.compare(arr[j], k) > 0)
		  {
			  arr[j + 1] = arr[j];
			  j--;
		  }
		  
		  arr[j + 1] = k;
	  }
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
	  int k = arr.length;
	  
	  for (int i = 0; i < k - i - 1; i++)
		  for (int j = 0; j < k - i - 1; j++)
			  if (arr[j].compareTo(arr[j + 1]) < 0)
			  {
				  E temp = arr[j];
				  arr[j] = arr[j + 1];
				  arr[j + 1] = temp;
			  }
  }
  
  //Comparator used by insertion sort
  class ElementComparator <E extends Comparable<E>> implements Comparator<E>
  {
	  @Override
	  public int compare(E arg0, E arg1)
	  {
		  return arg0.compareTo(arg1);
	  }
  }
 

}