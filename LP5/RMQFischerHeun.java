package LP5;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class RMQFischerHeun implements RMQStructure {

    private int[] top;
    private int[] elements;
    private int[][] sparseTable;
    private int n;  // size of array
    private int b;  // size of blocks
    private int blocks;
    private RMQStructure[] cartesianRMQs;
    private int[] cartesians;

    @Override
    public void preProcess(int[] arr) {
        n = arr.length;
        if (n == 0) return;
        // Copy elems to permanent storage for MinIndex function in rmq
        elements = arr;

       // System.arraycopy(arr, 0, elements, 0, n);
        b = (int)(Math.log(n) / (4*Math.log(2)));
        // If b = 0, just linear pass through it
        if (b < 1) return;
        blocks = (int) Math.ceil((double)(n)/b);
        // initialize arrays and fill in bottom one
//        InitializeTop();

        top = new int[blocks];

        // build a min array containing minimum of each block using query function
        int j = 0;
        for (int i = 0; i < arr.length; i += b) {
            top[j++] = new RMQHybridOne().minBlock(arr, i, Math.min(arr.length-1, i+b-1));
        }

        sparseTable = RMQHybridOne.buildRMQStructure(elements, top,2 );
        InitializeCartesians();
    }

    @Override
    public int query(int[] arr, int i, int j) {
        return rmq(i, j);
    }

    /**
     * Structure holding precomputed rmqs. Arrays structed the same way (with the same
     * cartesian number) have the same structure, thus can save on time and space by
     * storing RMQ structures.
     * @author jirvine
     *
     */
    private class RMQStructure {
        private int[][] rmqs;

        /**
         * Constructor. Builds a precomputed RMQ structure from array between i and j.
         * @param i start index
         * @param j end index
         */
        public RMQStructure(int i, int j) {
            int numElems = j - i + 1;
            rmqs = new int[numElems][numElems];
            // Build graph by diagonals, starting with main diagonal
            // Initialize main diagonal
            for (int k = 0; k < numElems; k++) {
                rmqs[k][k] = k;
            }
            // Dynamically build rest of table
            for (int k = 0; k < numElems; k++) {
                for (int l = k+1; l < numElems; l++) {
                    if (elements[rmqs[k][l-1] + i] <= elements[i+l]) {
                        rmqs[k][l] = rmqs[k][l-1];
                    } else {
                        rmqs[k][l] = l;
                    }
                }
            }
        }

        /**
         * Query between indices
         * @param k start index
         * @param l end index
         * @return the minimum index
         */
        public int RMQ(int k, int l) {
            return rmqs[k][l];
        }
    };

    /**
     * Helper functions for cartesian number calculation.
     * @param x
     * @return
     */
    private int add0right(int x) {
        return 2*x;
    }

    private int add1right(int x) {
        return 2*x + 1;
    }

    /**
     * Finds cartesian number for array between indices.
     * @param i start index
     * @param j end index
     * @return cartesian number
     */
    private int CartesianNumber(int i, int j) {
        int cartesian = 0;
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(i);
        cartesian = add1right(cartesian);
        // Calculate the cartesian number using stack algorithm. Each is pushed
        // on stack once and popped off once, thus linear time.
        for (int k = i+1; k <= j; k++) {
            while (!stack.isEmpty() && elements[k] < elements[stack.peek()]) {
                stack.pop();
                cartesian = add0right(cartesian);
            }
            stack.push(k);
            cartesian = add1right(cartesian);
        }
        while (!stack.isEmpty()) {
            stack.pop();
            cartesian = add0right(cartesian);
        }
        return cartesian;
    }

    /**
     * Initialize cartesian numbers for each block and all possible rmq structures.
     */
    private void InitializeCartesians() {
        cartesians = new int[blocks];
        cartesianRMQs = new RMQStructure[(int)Math.pow(4,b)];
        for (int block = 0; block < blocks; block++) {
            int i = block*b;
            int j = Math.min(n-1, (block+1)*b - 1);
            int c = CartesianNumber(i, j);
            cartesians[block] = c;
            if (cartesianRMQs[c] == null) {
                cartesianRMQs[c] = new RMQStructure(i, j);
            }
        }
    }

    /**
     * Helper function for getting which index has the minimum value in
     * the array
     * @param index1 the first index in question
     * @param index2 the second
     * @return the index that represents the min
     */
    private int MinIndex(int index1, int index2) {
        return elements[index1] <= elements[index2] ? index1 : index2;
    }

    /**
     * Initializes top blocks to hold mins of each block. Linear time.
     */
    private void InitializeTop() {
        top = new int[blocks];
        for (int block = 0; block < blocks; block++) {
            int start = block*b;
            int minIndex = start;
            for (int i = start; i < Math.min(start + b, n); i++) {
                minIndex = MinIndex(minIndex, i);
            }
            top[block] = minIndex;
        }
    }






    /**
     * Builds a sparse table dynamically in linear time.
     */
    private void BuildSparseTable() {

    }

    /**
     * Creates a new FischerHeunRMQ structure to answer queries about the
     * array given by elems.
     *
     * @elems The array over which RMQ should be computed.
     */



    /**
     * Finds min of within two blocks on bottom layer.
     * @param i the index of the first block
     * @param j the index of the second block
     * @return the minimum index
     */
    private int BottomMin(int i, int j) {
        int iBlock = (int)(i/b);
        int jBlock = (int)(j/b);
        // First block (i)
        int end;
        if (iBlock == jBlock) {
            end = j;
        } else {
            end = (iBlock + 1)*b - 1;
        }
        int firstMin = cartesianRMQs[cartesians[iBlock]].RMQ(i%b, end%b) + iBlock*b;
        // Second block (j)
        int start;
        if (iBlock == jBlock) {
            start = i;
        } else {
            start = jBlock*b;
        }
        int secondMin = cartesianRMQs[cartesians[jBlock]].RMQ(start%b, j%b) + jBlock*b;
        return MinIndex(firstMin, secondMin);
    }

    /**
     * Finds minimum of top array of blocks.
     * @param topi index of start block
     * @param topj index of end block
     * @return the minimum index
     */
    private int TopMin(int topi, int topj) {
        int k = (int)RMQHybridOne.customLog(2, topj-topi);
        int twotok = (int)Math.pow(2,k);
        int topMin = MinIndex(sparseTable[topi][k], sparseTable[topj-twotok+1][k]);
        return topMin;
    }

    /**
     * Simple linear-time rmq query.
     * @param i start index
     * @param j end index
     * @return minimum index
     */
    private int linearPass(int i, int j) {
        int minIndex = i;
        for (int k = i+1; k <= j; k++) {
            minIndex = MinIndex(minIndex, k);
        }
        return minIndex;
    }

    /**
     * Evaluates RMQ(i, j) over the array stored by the constructor, returning
     * the index of the minimum value in that range.
     */

    public int rmq(int i, int j) {
        if (b < 1) return elements[linearPass(i, j)];
        // Find min of bottom layer indices
        int bottomMin = BottomMin(i, j);
        // Find min over top layer sparse tree
        int topi = (int)(i/b) + 1;
        int topj = (int)(j/b) - 1;
        if (topj < topi) return bottomMin;
        int topMin = TopMin(topi, topj);
        return elements[MinIndex(bottomMin, topMin)];
    }
}
