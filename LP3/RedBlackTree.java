/** Starter code for Red-Black Tree
 */
package LP3;

import java.util.Comparator;

public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Entry<T> NULLNODE;  //single NULL node for every leaf

    static class Entry<T> extends BinarySearchTree.Entry<T> {
        boolean color;
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            color = RED;
        }

        boolean isRed() {
	    return color == RED;
        }

        boolean isBlack() {
	    return color == BLACK;
        }

        void setRed(){
            color = RED;
        }

        void setBlack(){
            color = BLACK;
        }
    }

    RedBlackTree() {
	    super();
	    NULLNODE = new Entry<>(null, null, null);
	    NULLNODE.setBlack();
	    root = NULLNODE;
    }


    /**
     * Overridden method of BinarySearchTree add operation
     * @param x for value of new element
     */
    @Override
    public boolean add(T x){
        //case where no element exist in the tree
        if(root == NULLNODE){
            root = new Entry<>(x, NULLNODE, NULLNODE);
            size = 1;
            ((Entry<T>)root).setBlack();
            return true;
        }

        //find the element entry of successor/predecessor to get the add location
        Entry<T> entryOfElement = (Entry<T>)find(root, x);

        //check if element already exists in the tree than no need to add again
        if(entryOfElement != NULLNODE){
            return false;
        }

        //get the current node
        Entry<T> currentNode = (Entry<T>)stack.peek();

        if(currentNode.element.compareTo(x) > 0){  //case for predecessor
            //create right node
            currentNode.left = new Entry<>(x, NULLNODE, NULLNODE);
            //call fix tree method to fix
            fixTree((Entry<T>)currentNode.left);
        }else{ // case for successor
            //create left node
            currentNode.right = new Entry<>(x, NULLNODE, NULLNODE);
            //call fix tree method to fix
            fixTree((Entry<T>)currentNode.right);
        }

        size++;
        return true;
    }



    /**
     * Fix the tree with RBT rules
     * @param entry where the fix is required
     */
    private void fixTree(Entry<T> entry){

        //find the parent and grandparent of the entry
        Entry<T> parentNode = stack.isEmpty() ? null : (Entry<T>)stack.pop();
        Entry<T> grandParentNode = stack.isEmpty() ? null : (Entry<T>) stack.pop();

        //no fix is required if parentNode is BLACK
        if(parentNode != null && parentNode.color == BLACK){
            return;
        }

        //find uncle of current entry
        Entry<T> uncleNode;
        if(grandParentNode == null){
            uncleNode = null;
        }else{
            uncleNode = grandParentNode.left == parentNode ? (Entry<T>)grandParentNode.right : (Entry<T>)grandParentNode.left;
        }



        while(entry != root && parentNode.color != BLACK){
            //conditions for left child parent node
            if(grandParentNode != null && grandParentNode.left == parentNode){
                if(uncleNode == null || uncleNode.color == RED){  //case 1
                    parentNode.color = uncleNode.color = BLACK;
                    entry = grandParentNode;
                    entry.color = RED;
                }else if(parentNode.right == entry){  //case 2
                    Entry<T> temp = entry;
                    entry = parentNode;
                    leftRotate(entry);

                    parentNode = temp;
                    updateParentPointer(parentNode, grandParentNode, true);

                    //case 3
                    parentNode.color = BLACK;
                    grandParentNode.color = RED;
                    rightRotate(grandParentNode);
                    entry = parentNode;
                    updateParentPointer(entry, (Entry<T>)stack.peek(), false);
                }
            }else if (grandParentNode != null && grandParentNode.right == parentNode){
                if(uncleNode == null || uncleNode.color == RED){  //case 1
                    parentNode.color = uncleNode.color = BLACK;
                    entry = grandParentNode;
                    entry.color = RED;
                }else if(parentNode.right == entry){  //case 2
                    Entry<T> temp = entry;
                    entry = parentNode;
                    rightRotate(entry);

                    parentNode = temp;
                    updateParentPointer(parentNode, grandParentNode, false);

                    //case 3
                    parentNode.color = BLACK;
                    grandParentNode.color = RED;
                    leftRotate(grandParentNode);
                    entry = parentNode;
                    updateParentPointer(entry, (Entry<T>)stack.peek(), true);
                }
            }


            //change the values of parent, uncle and grandparent nodes
            parentNode = stack.isEmpty() ? null : (Entry<T>)stack.pop();
            grandParentNode = stack.isEmpty() ? null : (Entry<T>)stack.pop();

            if(grandParentNode == null){
                uncleNode = null;
            }else{
                uncleNode = grandParentNode.left == parentNode ? (Entry<T>)grandParentNode.right : (Entry<T>)grandParentNode.left;
            }
        }

        //set the color of root to BLACK
        ((Entry<T>)root).color = BLACK;

    }

    /**
     * Rotate the tree to the left
     * @param entry where the rotation is required
     */
    private void leftRotate(Entry<T> entry){
        Entry<T> newParent = (Entry<T>)entry.right;
        Entry<T> newRightChild = (Entry<T>)newParent.left;

        newParent.left = entry;
        entry.right = newRightChild;

    }


    /**
     * Rotate the tree to the right
     * @param entry where the rotation is required
     */
    private void rightRotate(Entry<T> entry){
        Entry<T> newParent = (Entry<T>)entry.left;
        Entry<T> newLeftChild = (Entry<T>)newParent.right;

        newParent.right = entry;
        entry.left = newLeftChild;
    }


    /**
     * Update parent pointer after rotation
     * @param newParentNode for which pointer needs to be updated
     * @param oldGrandParentNode for which newParent become the child
     * @param direction for left or right child
     */
    private void updateParentPointer(Entry<T> newParentNode, Entry<T> oldGrandParentNode, boolean direction){ //true for left
        if(oldGrandParentNode == null){
            return;
        }
        if(direction){
            oldGrandParentNode.left = newParentNode;
        }else{
            oldGrandParentNode.right = newParentNode;
        }
    }

    public static void main(String[] args){
        RedBlackTree rbt = new RedBlackTree();
        rbt.add(39);
        rbt.add(35);
        rbt.add(70);
        rbt.add(20);
        rbt.add(38);
        rbt.add(30);
        rbt.add(50);
        rbt.add(75);
        rbt.add(40);
        rbt.add(60);

        rbt.add(65);
        rbt.add(55);
        rbt.add(53);
    }
}

