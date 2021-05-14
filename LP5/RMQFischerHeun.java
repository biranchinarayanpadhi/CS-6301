/**
 *
 * @author Tarun Punhani(txp190029), Vishal Puri(vxp190034) and Biranchi Narayan Padhi (bxp200001)
 * Long Project 3: Skip Lists and RBT
 */

package LP5;


import java.util.Stack;

public class RMQFischerHeun implements RMQStructure {


    private int[] originalArray;
    private int[][] sparseTable;
    private int sizeOfArray;
    private int sizeOfBlock;
    private int blocks;
    private RMQStructureFromCartesian[] rmqStructureFromCartesian;
    private int[] cartesianNumbers;
    private int[] blockMinima;


    public int getSizeOfArray() {
        return sizeOfArray;
    }

    public void setSizeOfArray(int sizeOfArray) {
        this.sizeOfArray = sizeOfArray;
        this.sizeOfBlock = (int) (Math.log(this.sizeOfArray) / (4 * Math.log(2)));
        blocks = (int) Math.ceil((double) (this.sizeOfArray) / this.sizeOfBlock);

    }

    public int getNumberOfBlocks() {
        return this.blocks;
    }

    public int getSizeOfBlock() {
        return this.sizeOfBlock;
    }

    public int[] getOriginalArray() {
        return originalArray;
    }

    public void setOriginalArray(int[] originalArray) {
        this.originalArray = originalArray;
        this.sizeOfArray = this.originalArray.length;
    }

    public int[][] getSparseTable() {
        return sparseTable;
    }

    public void setSparseTable(int[][] sparseTable) {
        this.sparseTable = sparseTable;
    }

    public int[] getCartesianNumbers() {
        return cartesianNumbers;
    }

    public void setCartesianNumbers(int[] cartesianNumbers) {
        this.cartesianNumbers = cartesianNumbers;
    }

    public int[] getBlockMinima() {
        return blockMinima;
    }

    public void setBlockMinima(int[] blockMinima) {
        this.blockMinima = blockMinima;
    }


    @Override
    public void preProcess(int[] arr) {
        //initialize the original array
        setOriginalArray(arr);

        //no preprocessing required when empty array is passed or size of block is 0
        if (getSizeOfArray() == 0 || getSizeOfBlock() < 1) return;

        setBlockMinima(new int[getNumberOfBlocks()]);

        //create sparse table
        int j = 0;
        for (int i = 0; i < arr.length; i += getSizeOfBlock()) {
            getBlockMinima()[j++] = new RMQHybridOne().minBlock(arr, i, Math.min(arr.length - 1, i + getSizeOfBlock() - 1));
        }
        sparseTable = RMQHybridOne.buildRMQStructure(getOriginalArray(), getBlockMinima(), 2);

        //build the cartesian number references
        buildCartesianNumberReferences();
    }

    @Override
    public int query(int[] arr, int i, int j) {
        // complete in linear pass if size of block is less than 1
        if (getSizeOfBlock() < 1) return getOriginalArray()[linearPass(i, j)];
        int minimumOfBottom = calculateMinimumUsingCartesian(i, j);


        // Calculate minimum of the top level sparse tree
        int topStart = (i / getSizeOfBlock()) + 1;
        int topEnd = (j / getSizeOfBlock()) - 1;
        if (topEnd < topStart) return minimumOfBottom;
        int topMin = calculateMinimumFromSparseTable(topStart, topEnd);


        return getOriginalArray()[findMinimumIndex(minimumOfBottom, topMin)];
    }


    /**
     * Helper Functions in cartesian number calculation
     *
     * @param number
     * @return
     */
    private int add0ToRight(int number) {
        return 2 * number;
    }

    private int add1ToRight(int number) {
        return 2 * number + 1;
    }

    /**
     * Calculate the cartesian number between two indices
     *
     * @param i start index
     * @param j end index
     * @return cartesian number
     */
    private int CartesianNumber(int i, int j) {
        int cNumber = 0;
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(i);
        cNumber = add1ToRight(cNumber);
        for (int k = i + 1; k <= j; k++) {
            while (!stack.isEmpty() && getOriginalArray()[k] < getOriginalArray()[stack.peek()]) {
                stack.pop();
                cNumber = add0ToRight(cNumber);
            }
            stack.push(k);
            cNumber = add1ToRight(cNumber);
        }
        while (!stack.isEmpty()) {
            stack.pop();
            cNumber = add0ToRight(cNumber);
        }
        return cNumber;
    }


    /**
     * Create cartesian number references
     */
    private void buildCartesianNumberReferences() {
        setCartesianNumbers(new int[getNumberOfBlocks()]);
        rmqStructureFromCartesian = new RMQStructureFromCartesian[(int) Math.pow(4, getSizeOfBlock())];
        for (int blk = 0; blk < getNumberOfBlocks(); blk++) {
            int i = blk * getSizeOfBlock();
            int j = Math.min(getSizeOfArray() - 1, (blk + 1) * getSizeOfBlock() - 1);
            int c = CartesianNumber(i, j);
            getCartesianNumbers()[blk] = c;
            if (rmqStructureFromCartesian[c] == null) {
                rmqStructureFromCartesian[c] = new RMQStructureFromCartesian(i, j);
            }
        }
    }

    /**
     * Get the minium index between two
     *
     * @param index1 the first index
     * @param index2 the second
     * @return the index of minimum element
     */
    private int findMinimumIndex(int index1, int index2) {
        return getOriginalArray()[index1] <= getOriginalArray()[index2] ? index1 : index2;
    }


    /**
     * Calculate min of within two blocks
     *
     * @param i the index of the first block
     * @param j the index of the second block
     * @return the minimum index
     */
    private int calculateMinimumUsingCartesian(int i, int j) {
        int startBlock = i / getSizeOfBlock();
        int endBlock = j / getSizeOfBlock();
        // First block (i)
        int end;
        if (startBlock == endBlock) {
            end = j;
        } else {
            end = (startBlock + 1) * getSizeOfBlock() - 1;
        }
        int firstMin = rmqStructureFromCartesian[getCartesianNumbers()[startBlock]].RMQ(i % getSizeOfBlock(), end % getSizeOfBlock()) + startBlock * getSizeOfBlock();
        int start;
        if (startBlock == endBlock) {
            start = i;
        } else {
            start = endBlock * getSizeOfBlock();
        }
        int secondMin = rmqStructureFromCartesian[getCartesianNumbers()[endBlock]].RMQ(start % getSizeOfBlock(), j % getSizeOfBlock()) + endBlock * getSizeOfBlock();
        return findMinimumIndex(firstMin, secondMin);
    }

    /**
     * Finds minimum of top array of blocks.
     *
     * @param start index of start block
     * @param end   index of end block
     * @return the minimum index
     */
    private int calculateMinimumFromSparseTable(int start, int end) {
        int i = (int) RMQHybridOne.customLog(2, end - start);
        int powerOfTwo = (int) Math.pow(2, i);
        int min = findMinimumIndex(sparseTable[start][i], sparseTable[end - powerOfTwo + 1][i]);
        return min;
    }


    /**
     * Linear pass
     *
     * @param i start index
     * @param j end index
     * @return minimum index
     */
    private int linearPass(int i, int j) {
        int minIndex = i;
        for (int k = i + 1; k <= j; k++) {
            minIndex = findMinimumIndex(minIndex, k);
        }
        return minIndex;
    }


    /**
     * Build Cartesian structure
     */
    private class RMQStructureFromCartesian {
        private int[][] fullProcessedRMQTable;

        /**
         * Constructor. Builds a precomputed RMQ structure from array between i and j.
         *
         * @param i start index
         * @param j end index
         */
        public RMQStructureFromCartesian(int i, int j) {
            int numElems = j - i + 1;
            fullProcessedRMQTable = new int[numElems][numElems];
            // Build graph by diagonals, starting with main diagonal
            // Initialize main diagonal
            for (int k = 0; k < numElems; k++) {
                fullProcessedRMQTable[k][k] = k;
            }
            // Dynamically build rest of table
            for (int k = 0; k < numElems; k++) {
                for (int l = k + 1; l < numElems; l++) {
                    if (getOriginalArray()[fullProcessedRMQTable[k][l - 1] + i] <= getOriginalArray()[i + l]) {
                        fullProcessedRMQTable[k][l] = fullProcessedRMQTable[k][l - 1];
                    } else {
                        fullProcessedRMQTable[k][l] = l;
                    }
                }
            }
        }

        /**
         * Query function
         *
         * @param k start index
         * @param l end index
         * @return the minimum index
         */
        public int RMQ(int k, int l) {
            return fullProcessedRMQTable[k][l];
        }

    }

    ;
}
