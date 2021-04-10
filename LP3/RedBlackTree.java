/** Starter code for Red-Black Tree
 */
package LP3;

import java.util.ArrayList;
import java.util.List;

public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private boolean check = true;
    private List<Entry<T>> ancestors;
    private int numberofBlackNode=Integer.MAX_VALUE;
//    private Entry<T> NULLNODE;  //single NULL node for every leaf

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
        ((Entry<T>)NULLNODE).setBlack();
        root = NULLNODE;
    }


    /**
     * Verify following properties of RBT:
     * 1. Root Property: The root is black
     * 2. Red Property: The parent of a red node is black
     * 3. Depth Property: The number of black nodes on each path from the root to a leaf node is the same
     * 4. Leaf Nodes: All leaf nodes are black nil nodes
     */
    public boolean verifyRBT(){

        //case 1
        if(((Entry<T>)root).color != BLACK){
            return false;
        }

        //case 2
        treeTraversal((Entry<T>)root, null);
        if(!check)
            return false;

        //case 3 & 4
        ancestors = new ArrayList<>();
        rootToLeafPath((Entry<T>)root);
        if(!check)
            return false;


        return true;
    }

    public void rootToLeafPath(Entry<T> root){
        if (root != NULLNODE){
            ancestors.add(root);
            if(root.left == NULLNODE && root.right == NULLNODE){
                if(root.color == RED){
                    check=false;
                }
                int count=0;
                for(int i=0;i<ancestors.size();i+=1){
                    if(ancestors.get(i).color == BLACK){
                        count+=1;
                    }
                }
                if(numberofBlackNode == Integer.MAX_VALUE){
                    numberofBlackNode=count;
                }else{
                    if(numberofBlackNode != count){
                        check=false;
                    }
                }
            }
            rootToLeafPath((Entry<T>) root.left);
            rootToLeafPath((Entry<T>) root.right);
            ancestors.remove(ancestors.size()-1);
        }
    }


    public void treeTraversal(Entry<T> root,Entry<T> parent){
        if(root!=NULLNODE){
            if(root.color == RED && parent.color == RED){
                check =false;
            }
            treeTraversal((Entry<T>) root.left,root);
            treeTraversal((Entry<T>) root.right,root);

        }
    }


    /**
     * Overridden method of BinarySearchTree add operation
     * @param x for value of new element
     */
    @Override
    public boolean add(T x){
        //case where no element exist in the tree
        if(root == NULLNODE){
            root = new Entry<T>(x, (Entry<T>)NULLNODE, (Entry<T>)NULLNODE);
            size = 1;
            ((Entry<T>)root).setBlack();
            return true;
        }

        //find the element entry of successor/predecessor to get the add location
        Entry<T> entryOfElement = (Entry<T>)find(root, x);

        //check if element already exists in the tree than no need to add again
        if(entryOfElement != null && entryOfElement.element.compareTo(x) == 0){
            return false;
        }

        stack.push(entryOfElement);
        //get the current node
//        Entry<T> currentNode = (Entry<T>)stack.peek();

        Entry<T> currentNode = entryOfElement;
        if(currentNode.element.compareTo(x) > 0){  //case for predecessor
            //create right node
            currentNode.left = new Entry<T>(x, (Entry<T>)NULLNODE, (Entry<T>)NULLNODE);
            //call fix tree method to fix
            fixTree((Entry<T>)currentNode.left);
        }else{ // case for successor
            //create left node
            currentNode.right = new Entry<T>(x, (Entry<T>)NULLNODE, (Entry<T>)NULLNODE);
            //call fix tree method to fix
            fixTree((Entry<T>)currentNode.right);
        }

        size++;

//        super.add(x);
//
//        Entry<T> elementEntry = (Entry<T>)find(root, x);
//
//        //check if element already exists in the tree than no need to add again
//        if(elementEntry == null){
//            return false;
//        }
//
//        //define NULL nodes to left and right for leaf nodes
//        if(elementEntry.left == null && elementEntry.right == null){
//            elementEntry.left = NULLNODE;
//            elementEntry.right = NULLNODE;
//
//        }
//        elementEntry.color = RED;
//        fixTree(elementEntry);

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



    /**
     * Overridden method of BinarySearchTree remove operation
     * @return
     */
    @Override
    public T remove(T x){
        Entry<T> entryToBeRemoved = (Entry<T>)find(root, x); //need to store the entry to refer to color after remove operation
        T removedElement = super.remove(x);
        if(removedElement == null){
            return null;
        }

        Entry<T> current = (Entry<T>)stack.pop();

        if(entryToBeRemoved.color == BLACK){
            if(current.color == BLACK)
                fixUp(current);
            else
                current.color = BLACK;
        }
        return x;
    }


    /**
     * To fix the tree after deletion
     * @param current element at which slice operation is done
     */
    private void fixUp(Entry<T> current){
        //return the element if only root is available in the tree
        if(current == root){
            current.color = BLACK;
            return;
        }
        Entry<T> parentNode = (Entry<T>)stack.pop();
        //find whether current node is left child or right child
        boolean isLeftChild = parentNode.left == current;

        Entry<T> sibling = isLeftChild ? (Entry<T>)parentNode.right : (Entry<T>)parentNode.left;

        if(sibling == null){
            current.color = RED;
            return;
        }
        while(current != root && current.color == BLACK){
            if(isLeftChild){
                if(sibling.color == RED){ //case 1
                    sibling.color = BLACK;
                    parentNode.color = RED;
                    leftRotate(parentNode);
                    updateParentPointer(sibling, (Entry<T>) stack.peek(), true);
                    sibling = (Entry<T>)parentNode.right;
                }

                if(current.color == ((Entry<T>)sibling.left).color == ((Entry<T>)sibling.right).color == BLACK){  //case 2
                    sibling.color = RED;
                    current = parentNode;
                }else if(((Entry<T>)sibling.right).color == BLACK){ //case 3
                    ((Entry<T>)sibling.left).color = BLACK;
                    sibling.color = RED;
                    Entry<T> siblingsLeftChild = (Entry<T>)sibling.left; //find left child before rotation
                    rightRotate(sibling);
                    updateParentPointer(siblingsLeftChild, parentNode, false);
                    sibling = siblingsLeftChild;

                    //case 4

                    ((Entry<T>)sibling.right).color = BLACK;
                    sibling.color = parentNode.color;
                    parentNode.color = BLACK;
                    Entry<T> siblingsRightChild = (Entry<T>)sibling.right;
                    leftRotate(parentNode);

                    updateParentPointer(siblingsRightChild, parentNode, true);
                    if(root == parentNode)
                        root = sibling;
                }
                current = (Entry<T>)root;

            }else{

                if(sibling.color == RED){ //case 1
                    sibling.color = BLACK;
                    parentNode.color = RED;
                    rightRotate(parentNode);
                    updateParentPointer(sibling, (Entry<T>) stack.peek(), false);
                    sibling = (Entry<T>)parentNode.left;
                }

                if(current.color == ((Entry<T>)sibling.left).color == ((Entry<T>)sibling.right).color == BLACK){  //case 2
                    sibling.color = RED;
                    current = parentNode;
                }else if(((Entry<T>)sibling.left).color == BLACK){ //case 3
                    ((Entry<T>)sibling.right).color = BLACK;
                    sibling.color = RED;
                    Entry<T> siblingsRightChild = (Entry<T>)sibling.right;  //right child before rotation
                    leftRotate(sibling);
                    updateParentPointer(siblingsRightChild, parentNode, true);
                    sibling = (Entry<T>)sibling.right;

                    //case 4

                    ((Entry<T>)sibling.left).color = BLACK;
                    sibling.color = parentNode.color;
                    parentNode.color = BLACK;
                    Entry<T> siblingsLeftChild = (Entry<T>)sibling.left; //left child before rotation
                    rightRotate(parentNode);
                    updateParentPointer(siblingsLeftChild, parentNode, false);
                    if(root == parentNode)
                        root = sibling;

                }
                current = (Entry<T>)root;
            }
        }
    }


    public static void main(String[] args){
        RedBlackTree rbt = new RedBlackTree();
//        rbt.add(39);
//        rbt.add(35);
//        rbt.add(70);
//        rbt.add(20);
//        rbt.add(38);
//        rbt.add(30);
//        rbt.add(50);
//        rbt.add(75);
//        rbt.add(40);
//        rbt.add(60);
//
//        rbt.add(65);
//        rbt.add(55);
//        rbt.add(53);


        rbt.add(30);

        rbt.add(20);
        rbt.add(40);
        rbt.add(35);
        rbt.add(50);


//        rbt.add(12);
//        rbt.add(5);
//        rbt.add(15);
//        rbt.add(3);
//        rbt.add(10);
//        rbt.add(13);
//        rbt.add(17);
//        rbt.add(4);
//        rbt.add(7);
//        rbt.add(11);
//        rbt.add(14);
//        rbt.add(6);
//        rbt.add(8);


        rbt.remove(20);
    }
}

