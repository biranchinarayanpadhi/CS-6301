/* Starter code for LP3 */

// Change this to netid of any member of team
package LP3;

import java.util.Iterator;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
    static final int maxLevel = 32;

    static class Entry<E> {
	E element;
	Entry[] next;
	Entry prev;

	public Entry(E x, int lev) {
	    element = x;
	    next = new Entry[lev];
	    // add more code if needed
	}

	public E getElement() {
	    return element;
	}
    }

    // Constructor
    public SkipList() {
    }

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
    public boolean add(T x) {
	return true;
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
	return null;
    }

    // Does list contain x?
    public boolean contains(T x) {
	return false;
    }

    // Return first element of list
    public T first() {
	return null;
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
	return null;
    }

    // Return element at index n of list.  First element is at index 0.
    public T get(int n) {
	return null;
    }

    // O(n) algorithm for get(n)
    public T getLinear(int n) {
	return null;
    }

    // Optional operation: Eligible for EC.
    // O(log n) expected time for get(n).
    public T getLog(int n) {
        return null;
    }

    // Is the list empty?
    public boolean isEmpty() {
	return false;
    }

    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {
	return null;
    }

    // Return last element of list
    public T last() {
	return null;
    }

 
    // Not a standard operation in skip lists. 
    public void rebuild() {
	
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
	return null;
    }

    // Return the number of elements in the list
    public int size() {
	return 0;
    }
}
