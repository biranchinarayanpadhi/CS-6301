
package idsa;
//import vxp190034.Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

//Driver program for skip list implementation.

public class SkipListDriver {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        Long returnValue = null;
        RedBlackTree<Long> skipList = new RedBlackTree<>();
        // Initialize the timer
        //Timer timer = new Timer();

        while (!((operation = sc.next()).equals("End"))) {
            //System.out.println(operation);
            switch (operation) {

                case "Add": {

                    operand = sc.nextLong();
//                    if(operand == 62){
//                        System.out.println("Break");
//                    }
//                    System.out.println("Add "+operand+" "+result);
                    //System.out.println(operand);
                    boolean temp = skipList.add(operand);
//                    System.out.println("RBT RESULT: "+skipList.verifyRBT());
                    if (temp) {
                        //System.out.println(temp+" ");
                        result = (result + 1) % modValue;
//                        System.out.println(result);
//                        if(result == 148){
//                            System.out.println("break");
//                        }
                    }
                    break;
                }
//                case "Ceiling": {
//                    operand = sc.nextLong();
//                    System.out.println(operand);
//
//                    returnValue = skipList.ceiling(operand);
//                    if (returnValue != null) {
//                        result = (result + returnValue) % modValue;
//                    }
//                    break;
//                }
//                case "First": {
//                    returnValue = skipList.first();
//                    if (returnValue != null) {
//                        result = (result + returnValue) % modValue;
//                    }
//                    break;
//                }
//                case "Get": {
//                    int intOperand = sc.nextInt();
//
//                    returnValue = skipList.get(intOperand);
//                    if (returnValue != null) {
//                        System.out.println(returnValue + " ");
//                        result = (result + returnValue) % modValue;
//                    }
//                    break;
//                }
//                case "Last": {
//                    returnValue = skipList.last();
//                    if (returnValue != null) {
//                        result = (result + returnValue) % modValue;
//                    }
//                    break;
//                }
//                case "Floor": {
//                    operand = sc.nextLong();
//                    returnValue = skipList.floor(operand);
//                    if (returnValue != null) {
//                        result = (result + returnValue) % modValue;
//                    }
//                    break;
//                }
                case "Remove": {

                    operand = sc.nextLong();
//                    System.out.println("Remove "+operand+" "+result);
//                    if(operand == 41 && result==28){
//                        System.out.println("Break");
//                    }
                    Long temp = skipList.remove(operand);
//                    System.out.println("RBT RESULT: "+skipList.verifyRBT());
                    if (temp!=null) {
                        result = (result + 1) % modValue;
                        //System.out.println(temp);
                        //System.out.println(result);

                    }
                    break;
                }
                case "Contains": {

                    operand = sc.nextLong();
//                    System.out.println("Contains "+operand+" "+result);
//                    if(operand == 138){
//                        System.out.println("Hello");
//                    }
                    boolean temp = skipList.contains(operand);
//                    System.out.println("RBT RESULT: "+skipList.verifyRBT());
                    //System.out.println(temp);
                    if (temp) {
                        //System.out.println(temp);
                        result = (result + 1) % modValue;
                        //System.out.println(result);
                    }
                    break;
                }
//                case "IsEmpty": {
//                    boolean temp = skipList.isEmpty();
//                    if (skipList.isEmpty()){
//                        System.out.println("SkipList is empty");
//                    } else
//                        System.out.println("Skiplist has size: "+ skipList.size());
//                    break;
//                }

            }
        }

        // End Time
//        timer.end();
        System.out.println(result);
//        System.out.println(timer);
    }
}
