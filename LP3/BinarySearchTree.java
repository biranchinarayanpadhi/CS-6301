/**
 *
 * @author Tarun Punhani(txp190029), Vishal Puri(vxp190034) and Biranchi Narayan Padhi (bxp200001)
 * Long Project 3: Skip Lists and RBT
 */

package LP3;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {

    Stack<Entry<T>> stack; //stack for tracking parent node
    int index = 0; //for tracking the array index during recursion

    Entry<T> NULLNODE = new Entry<T>(null, null, null);;  //single NULL node for every leaf in RBT

    static class Entry<T> {
        T element;
        Entry<T> left, right;

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    Entry<T> root;
    int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }


    /**
     * TO DO: Is x contained in tree?
     */
    public boolean contains(T x) {

        Entry<T> elementEntry = find(root, x);
        return elementEntry.element != null && elementEntry.element.compareTo(x)==0;
    }

    /**
     * TO DO: Is there an element that is equal to x in the tree?
     * Element in tree that is equal to x is returned, null otherwise.
     */
    public T get(T x) {
        if (contains(x))
            return stack.peek().element;
        return null;
    }


    /**
     * find the element in tree from root
     */
    public Entry<T> find(Entry<T> t, T x) {
        stack = new Stack<>();
        stack.push(null);
        if (t == null || t == NULLNODE)
            return null;
        if (t.element.compareTo(x) == 0)
            return t;
        while (true) {
            if (x.compareTo(t.element) < 0) {
                if (t.left == null || t.left == NULLNODE)
                    break;
                stack.push(t);
                t = t.left;
            } else if (t.element.compareTo(x) == 0)
                break;
            else {
                if (t.right == null || t.right == NULLNODE)
                    break;
                stack.push(t);
                t = t.right;
            }
        }
        return t;
    }


    /**
     * TO DO: Add x to tree.
     * If tree contains a node with same key, replace element by x.
     * Returns true if x is a new element added to tree.
     */
    public boolean add(T x) {
        if (size == 0) {
            root = new Entry<>(x, null, null);
            size++;
            return true;
        }
        Entry<T> elementEntry = find(root, x);
        if (elementEntry != null  && elementEntry != NULLNODE && elementEntry.element == x) {
            return false;
        }
//        Entry<T> parent = stack.peek();
        if (x.compareTo(elementEntry.element) < 0) {
            elementEntry.left = new Entry<>(x, null, null);
        } else {
            elementEntry.right = new Entry<>(x, null, null);
        }
        size++;
        return true;

    }

    /**
     * TO DO: Remove x from tree.
     * Return x if found, otherwise return null
     */
    public T remove(T x) {
        if (size == 0)
            return null;

        Entry<T> elementEntry = find(root, x);
        if (elementEntry.element.compareTo(x) != 0) {
            return null;
        }

        if (elementEntry.left == null || elementEntry.right == null || elementEntry.left == NULLNODE || elementEntry.right == NULLNODE) {
            splice(elementEntry);
            size--;
        } else {
            stack.push(elementEntry);
            Entry<T> minRight = find(elementEntry.right, x);
            if(stack.peek() == null || stack.peek() == NULLNODE){
                stack.push(elementEntry);
            }
            elementEntry.element = minRight.element;
            splice(minRight);
            Entry<T> topOfStack = stack.pop();
            find(root, elementEntry.element);
            stack.push(elementEntry);
            stack.push(topOfStack);
            size--;
            return x;
        }

        return x;

    }


    /**
     * Splice the tree from the element
     */
    private void splice(Entry<T> t) {
        Entry<T> parent = stack.peek();
        Entry<T> child = ((t.left == null || t.left == NULLNODE) ? t.right : t.left);
        if (parent == null || parent == NULLNODE) {
            root = child;
        } else if (parent.left == t)
            parent.left = child;
        else
            parent.right = child;


        stack.push(child);

    }


    /**
     * @return minimum element in the tree.
     */
    public T min() {
        Entry<T> t = root;
        while (t != null && t != NULLNODE) {
            t = t.left;
        }
        return t.element;
    }


    /**
     * @return maximum element in the tree.
     */
    public T max() {
        Entry<T> t = root;
        while (t != null && t != NULLNODE) {
            t = t.right;
        }
        return t.element;
    }

    // TODO: Create an array with the elements using in-order traversal of tree
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        /* write code to place elements in array here */
        Entry<T> elementEntry = root;
        index = 0;
        helper(elementEntry, arr);
        return arr;
    }


    /**
     * Helper method to run recursion for inorder traversal
     */
    private void helper(Entry<T> t, Comparable[] arr) {
        if (t == null)
            return;
        helper(t.left, arr);
        arr[index++] = t.element;
        helper(t.right, arr);
    }


// Start of Optional problem 2

    /**
     * Optional problem 2: Iterate elements in sorted order of keys
     * Solve this problem without creating an array using in-order traversal (toArray()).
     */
    public Iterator<T> iterator() {
        return null;
    }

    // Optional problem 2.  Find largest key that is no bigger than x.  Return null if there is no such key.
    public T floor(T x) {
        return null;
    }

    // Optional problem 2.  Find smallest key that is no smaller than x.  Return null if there is no such key.
    public T ceiling(T x) {
        return null;
    }

    // Optional problem 2.  Find predecessor of x.  If x is not in the tree, return floor(x).  Return null if there is no such key.
    public T predecessor(T x) {
        return null;
    }

    // Optional problem 2.  Find successor of x.  If x is not in the tree, return ceiling(x).  Return null if there is no such key.
    public T successor(T x) {
        return null;
    }

// End of Optional problem 2

    public static void main(String[] args) {
        BinarySearchTree<Integer> t = new BinarySearchTree<>();
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int x = in.nextInt();
            if (x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if (x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for (int i = 0; i < t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }
        }
    }


    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }

}
/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0

Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10 

*/
