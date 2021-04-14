/**
 *
 * @author Tarun Punhani(txp190029), Vishal Puri(vxp190034) and Biranchi Narayan Padhi (bxp200001)
 * Long Project 3: Skip Lists and RBT
 */

// Change this to netid of any member of team
package LP3;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
    static final int maxLevel = 32;
    int size;
    Entry<T> header;
    Random random = new Random();

    static class Entry<E> {
        E element;
        Entry[] next;
        Entry prev;
        int[] width;

        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev + 1];
            width = new int[lev + 1];
            // add more code if needed
        }

        public E getElement() {
            return element;
        }
    }

    // Constructor
    public SkipList() {
        size = 0;
        header = new Entry<T>(null, maxLevel);
        Arrays.fill(header.width, 1);
    }

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
    public boolean add(T x) {
        Entry[] update = new Entry[maxLevel + 1];
        int[] steps_at_level = new int[maxLevel];
        Entry<T> current = header;
        for (int i = maxLevel; i >= 0; i -= 1) {
            while (current.next[i] != null && ((T) current.next[i].getElement()).compareTo((T) x) < 0) {
                steps_at_level[i] += current.width[i];
                current = current.next[i];
            }
            update[i] = current;
        }


        current = current.next[0];
        //System.out.println(current + " " + update[0]);
        //if the element already exist in the skip list then we return False
        if (current != null && ((T) current.element).compareTo((T) x) == 0) {
            return false;
        }

        int random_level = randomLevel();
        Entry<T> newnode = new Entry<>(x, random_level);
        int steps = 0;
        Entry<T> prevnode;
        for (int level = 0; level < random_level + 1; level++) {
            prevnode = update[level];
            newnode.next[level] = prevnode.next[level];
            prevnode.next[level] = newnode;
            newnode.width[level] = prevnode.width[level] - steps;
            prevnode.width[level] = steps + 1;
            steps += steps_at_level[level];
        }

        for (int i = random_level + 1; i < maxLevel; i++) {
            update[i].width[i] += 1;
        }
        size += 1;
        return true;
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
        Entry[] update = new Entry[maxLevel + 1];
        Entry<T> current = header;
        for (int i = maxLevel; i >= 0; i -= 1) {
            while (current.next[i] != null && ((T) current.next[i].getElement()).compareTo((T) x) < 0) {
                current = current.next[i];
            }
            update[i] = current;
        }
        current = current.next[0];

        if (current != null && ((T) current.element).compareTo((T) x) == 0) {

            Entry<T> prevnode;
            for (int level = 0; level < current.next.length; level++) {
                prevnode = update[level];
                prevnode.width[level] += prevnode.next[level].width[level] - 1;
                prevnode.next[level] = prevnode.next[level].next[level];
            }

            for (int level = current.next.length; level < maxLevel; level++)
                update[level].width[level] -= 1;

            size -= 1;
            return x;
        }
        return null;
    }

    public int randomLevel() {

        int level = 0;

        //if level is zero, we keep on incrementing the level else, we stop and return the max level of a particular node.
        while (random.nextInt(2) < 0.5 && level < maxLevel) {
            level += 1;
        }
        return level;
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
        return null;
    }

    // Does list contain x?
    public boolean contains(T x) {
        Entry[] update = new Entry[maxLevel + 1];
        int[] steps_at_level = new int[maxLevel];
        Entry<T> current = header;
        for (int i = maxLevel; i >= 0; i -= 1) {
            while (current.next[i] != null && ((T) current.next[i].getElement()).compareTo((T) x) < 0) {
                steps_at_level[i] += current.width[i];
                current = current.next[i];
            }
            update[i] = current;
        }


        current = current.next[0];
        //System.out.println(current + " " + update[0]);
        //if the element already exist in the skip list then we return False
        if (current != null && ((T) current.element).compareTo((T) x) == 0) {
            return true;
        }
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
        return getLog(n);
    }

    // O(n) algorithm for get(n)
    public T getLinear(int n) {
        return null;
    }

    // Optional operation: Eligible for EC.
    // O(log n) expected time for get(n).
    public T getLog(int n) {
        Entry<T> current = header;
        if (n < size) {
            n += 1;
            for (int i = maxLevel; i >= 0; i--) {
                while (current.next[i] != null && current.width[i] <= n) {
                    n -= current.width[i];
                    current = current.next[i];
                }
            }
            return current.element;
        }
        return null;
    }

    // Is the list empty?
    public boolean isEmpty() {
        if(size()==0)
            return true;
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

    // Return the number of elements in the list
    public int size() {
        return size;
    }
}
