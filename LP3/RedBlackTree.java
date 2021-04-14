/**
 *
 * @author Tarun Punhani(txp190029), Vishal Puri(vxp190034) and Biranchi Narayan Padhi (bxp200001)
 * Long Project 3: Skip Lists and RBT
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
        numberofBlackNode = Integer.MAX_VALUE;
        rootToLeafPath((Entry<T>)root);
        if(!check)
            return false;


        return true;
    }

    public void rootToLeafPath(Entry<T> root){
        if (root != NULLNODE){
            ancestors.add(root);
            if(root.left == NULLNODE && root.right == NULLNODE){
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
//                        System.out.println("Leaf node: "+root.element);
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

        Entry<T> currentNode = entryOfElement;
        if(currentNode.element.compareTo(x) > 0){  //case for predecessor
            //create right node
            currentNode.left = new Entry<T>(x, (Entry<T>)NULLNODE, (Entry<T>)NULLNODE);
            //call fix tree method to fix
            fixTree((Entry<T>)currentNode.left);
            if(!verifyRBT()){
                ((Entry<T>) currentNode.left).color = BLACK;
                check = true;
            }
        }else{ // case for successor
            //create left node
            currentNode.right = new Entry<T>(x, (Entry<T>)NULLNODE, (Entry<T>)NULLNODE);
            //call fix tree method to fix
            fixTree((Entry<T>)currentNode.right);
            if(!verifyRBT()){
                ((Entry<T>) currentNode.right).color = BLACK;
                check = true;
            }
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
        Entry<T> grandParentNode = stack.isEmpty() ? null : (Entry<T>) stack.peek();
        stack.push(parentNode);

        //find uncle of current entry
        Entry<T> uncleNode;
        if(grandParentNode == null){
            uncleNode = null;
        }else{
            uncleNode = grandParentNode.left == parentNode ? (Entry<T>)grandParentNode.right : (Entry<T>)grandParentNode.left;
        }


        entry.color = RED;

        while(entry != root && parentNode != null && parentNode.color != BLACK){
            //conditions for left child parent node
            if(grandParentNode != null && grandParentNode.left == parentNode){
                if(uncleNode.color == RED){  //case 1
                    uncleNode.color = BLACK;
                    parentNode.color = BLACK;
                    entry = grandParentNode;
                    stack.pop();
                    stack.pop();
                    parentNode = (Entry<T>) stack.pop();
                    if(parentNode == null){
                        root = entry;
                        break;
                    }
                    grandParentNode = (Entry<T>)stack.peek();
                    stack.push(parentNode);
                    entry.color = RED;
                    if(grandParentNode != null){
                        boolean isUncleLeftChild = grandParentNode.left == parentNode ? true : false;

                        if(isUncleLeftChild){
                            uncleNode = (Entry<T>) grandParentNode.right;
                        }else{
                            uncleNode = (Entry<T>) grandParentNode.left;
                            continue;
                        }
                    }
                    if(parentNode.color == BLACK){
                        break;
                    }

                }else if(parentNode.right == entry) {  //case 2
                    Entry<T> temp = entry;
                    entry = parentNode;
                    leftRotate(entry);

                    parentNode = temp;
                    stack.pop();
                    stack.push(parentNode);
                    updateParentPointer(parentNode, grandParentNode, true);
                    if(parentNode.color == BLACK){
                        break;
                    }

                }


                if(grandParentNode != null && parentNode.left == entry && uncleNode.color == BLACK){
                    parentNode.color = BLACK;
                    grandParentNode.color = RED;

                    stack.pop();
                    stack.pop();
                    Entry<T> grandgrandParentLeft = stack.peek()!= null ? (Entry<T>)stack.peek().left : null;
                    rightRotate(grandParentNode);
                    entry = parentNode;

                    if(grandgrandParentLeft == null){
                        //no element above parent -> new parent become new root after rotation

                        root = parentNode;
                    }else{
                        boolean isLeftParent = grandgrandParentLeft == grandParentNode ? true : false;
                        if (isLeftParent)
                            updateParentPointer(entry, (Entry<T>) stack.peek(), true);
                        else
                            updateParentPointer(entry, (Entry<T>) stack.peek(), false);
                    }
                }
                if(parentNode.color == BLACK){
                    break;
                }
            }else if(grandParentNode != null){
                if(uncleNode.color == RED){  //case 1
                    uncleNode.color = BLACK;
                    parentNode.color = BLACK;
                    entry = grandParentNode;
                    stack.pop();
                    stack.pop();
                    parentNode = (Entry<T>) stack.pop();
                    if(parentNode == null){
                        root = entry;
                        break;
                    }
                    grandParentNode = (Entry<T>)stack.peek();
                    stack.push(parentNode);
                    entry.color = RED;

                    if(grandParentNode != null){
                        boolean isUncleRightChild = grandParentNode.right == parentNode ? true : false;

                        if(isUncleRightChild){
                            uncleNode = (Entry<T>) grandParentNode.left;
                        }else{
                            uncleNode = (Entry<T>) grandParentNode.right;
                            continue;
                        }
                    }

                    if(parentNode.color == BLACK){
                        break;
                    }

                }else if(parentNode.left == entry) {  //case 2
                    Entry<T> temp = entry;
                    entry = parentNode;
                    rightRotate(entry);

                    parentNode = temp;
                    stack.pop();
                    stack.push(parentNode);
                    updateParentPointer(parentNode, grandParentNode, false);

                    if(parentNode.color == BLACK){
                        break;
                    }

                }

                if(grandParentNode != null && parentNode.right == entry && uncleNode.color == BLACK){
                    parentNode.color = BLACK;
                    grandParentNode.color = RED;

                    stack.pop();
                    stack.pop();
                    Entry<T> grandgrandParentRight = stack.peek()!= null ? (Entry<T>)stack.peek().right : null;
                    leftRotate(grandParentNode);
                    entry = parentNode;

                    if(grandgrandParentRight == null){
                        //no element above parent -> new parent become new root after rotation

                        root = parentNode;
                    }else{
                        boolean isRightParent = grandgrandParentRight == grandParentNode ? false : true;
                        if (isRightParent)
                            updateParentPointer(entry, (Entry<T>) stack.peek(), true);
                        else
                            updateParentPointer(entry, (Entry<T>) stack.peek(), false);
                    }
                }

                if(parentNode.color == BLACK){
                    break;
                }

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
        Entry<T> newRightChild = (Entry<T>)(newParent.left);

        newParent.left = entry;
        entry.right = newRightChild;

    }


    /**
     * Rotate the tree to the right
     * @param entry where the rotation is required
     */
    private void rightRotate(Entry<T> entry){
        Entry<T> newParent = (Entry<T>)entry.left;
        Entry<T> newLeftChild = (Entry<T>) (newParent.right);

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
        T temp = root.element;
        Entry<T> entryToBeRemoved = (Entry<T>)find(root, x); //need to store the entry to refer to color after remove operation
        boolean entryToBeRemovedColor = entryToBeRemoved.color;
        T removedElement = super.remove(x);
        if(removedElement == null){
            return null;
        }

        if(x == temp){
            return x;
        }

        Entry<T> current = (Entry<T>)stack.pop();


        if(entryToBeRemovedColor == BLACK)
            fixUp(current);

        if(entryToBeRemoved.color == RED && current.color == RED){
            current.color = BLACK;
        }

        if(entryToBeRemoved.isBlack() && current.isRed()){
            current.setBlack();
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
        Entry<T> parentNode = (Entry<T>)stack.peek();

        if(parentNode == null){
            root = current;
            current.color = BLACK;
            return;
        }

        //find whether current node is left child or right child
        boolean isLeftChild = parentNode.left == current;

        Entry<T> sibling = isLeftChild ? (Entry<T>)parentNode.right : (Entry<T>)parentNode.left;

        while(current != root && current.color == BLACK && parentNode.color != BLACK && sibling != NULLNODE){
            if(isLeftChild){
                if(sibling.color == RED){ //case 1
                    sibling.color = BLACK;
                    parentNode.color = RED;
                    leftRotate(parentNode);
                    stack.pop();
                    if(stack.peek() != null){
                        boolean isParentLeftChild = stack.peek().left == parentNode;
                        if(isParentLeftChild)
                            updateParentPointer(sibling, (Entry<T>)stack.peek(), true);
                        else
                            updateParentPointer(sibling, (Entry<T>)stack.peek(), false);
                    }else{
                        root = sibling;
                    }

                    //push new parent in stack for tracking
                    stack.push(parentNode);
                    stack.push(sibling);

                    sibling = (Entry<T>)parentNode.right;

                }

                if(sibling != NULLNODE && current.color == BLACK && sibling.color == BLACK && ((Entry<T>)sibling.left).color == BLACK && ((Entry<T>)sibling.right).color == BLACK){  //case 2
                    sibling.color = RED;
                    current = parentNode;
                    stack.pop();
                    parentNode = (Entry<T>)stack.peek();
                    if(parentNode == null){
                        root = current;
                        ((Entry<T>)root).color = BLACK;
                        break;
                    }else{
                        if(parentNode.left == current){
                            sibling = (Entry<T>)parentNode.right;
                        }else{
                            sibling = (Entry<T>) parentNode.left;
                            isLeftChild = false;
                            continue;
                        }
                    }

                }else if(sibling != NULLNODE && sibling.color == BLACK && ((Entry<T>)sibling.left).color == RED && ((Entry<T>)sibling.right).color == BLACK){ //case 3
                    ((Entry<T>)sibling.left).color = BLACK;
                    sibling.color = RED;
                    Entry<T> siblingsLeftChild = (Entry<T>)sibling.left; //find left child before rotation
                    rightRotate(sibling);

                    updateParentPointer(siblingsLeftChild, parentNode, false);
                    sibling = siblingsLeftChild;


                }

                if(sibling != NULLNODE && sibling.color == BLACK  && ((Entry<T>)sibling.right).color == RED){
                    //case 4

                    ((Entry<T>)sibling.right).color = BLACK;
                    sibling.color = parentNode.color;
                    parentNode.color = BLACK;
                    leftRotate(parentNode);
                    stack.pop();
                    Entry<T> grandParent = (Entry<T>) stack.peek();
                    if(grandParent == null){
                        root = sibling;
                        continue;
                    }else{
                        boolean isParentLeftChild = grandParent.left == parentNode;
                        if(isParentLeftChild){
                            updateParentPointer(sibling, grandParent, true);
                        }else{
                            updateParentPointer(sibling, grandParent, false);
                        }
                    }

                    current = (Entry<T>)root;

                }


            }else {
                if(sibling.color == RED){ //case 1
                    sibling.color = BLACK;
                    parentNode.color = RED;
                    rightRotate(parentNode);
                    stack.pop();
                    if(stack.peek() != null){
                        boolean isParentRightChild = stack.peek().right == parentNode;
                        if(isParentRightChild)
                            updateParentPointer(sibling, (Entry<T>)stack.peek(), false);
                        else
                            updateParentPointer(sibling, (Entry<T>)stack.peek(), true);
                    }else{
                        root = sibling;
                    }

                    //push new parent in stack for tracking
                    stack.push(parentNode);
                    stack.push(sibling);

                    sibling = (Entry<T>)parentNode.left;

                }

                if(sibling != NULLNODE && current.color == BLACK && sibling.color == BLACK && ((Entry<T>)sibling.left).color == BLACK && ((Entry<T>)sibling.right).color == BLACK){  //case 2
                    sibling.color = RED;
                    current = parentNode;
                    stack.pop();
                    parentNode = (Entry<T>)stack.peek();
                    if(parentNode == null){
                        root = current;
                        ((Entry<T>)root).color = BLACK;
                        break;
                    }else{
                        if(parentNode.right == current){
                            sibling = (Entry<T>)parentNode.left;
                        }else{
                            sibling = (Entry<T>) parentNode.right;
                            isLeftChild = true;
                            continue;
                        }
                    }

                }else if(sibling != NULLNODE && sibling.color == BLACK && ((Entry<T>)sibling.left).color == BLACK && ((Entry<T>)sibling.right).color == RED){ //case 3
                    ((Entry<T>)sibling.right).color = BLACK;
                    sibling.color = RED;
                    Entry<T> siblingsRightChild = (Entry<T>)sibling.right; //find left child before rotation
                    leftRotate(sibling);

                    updateParentPointer(siblingsRightChild, parentNode, true);
                    sibling = siblingsRightChild;


                }

                if(sibling != NULLNODE && sibling.color == BLACK  && ((Entry<T>)sibling.left).color == RED){
                    //case 4

                    ((Entry<T>)sibling.left).color = BLACK;
                    sibling.color = parentNode.color;
                    parentNode.color = BLACK;
                    rightRotate(parentNode);
                    stack.pop();
                    Entry<T> grandParent = (Entry<T>) stack.peek();
                    if(grandParent == null){
                        root = sibling;
                        continue;
                    }else{
                        boolean isParentRightChild = grandParent.right == parentNode;
                        if(isParentRightChild){
                            updateParentPointer(sibling, grandParent, false);
                        }else{
                            updateParentPointer(sibling, grandParent, true);
                        }
                    }

                    current = (Entry<T>)root;

                }
            }
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

        rbt.remove(20);
    }
}

