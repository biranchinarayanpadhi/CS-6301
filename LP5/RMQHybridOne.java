package LP5;

public class RMQHybridOne implements RMQStructure {

    private int sparseTable[][];
    int blockLength = 0;

    public int[][] getSparseTable() {
        return sparseTable;
    }

    public void setSparseTable(int[][] rmqBlockMinimum) {
        this.sparseTable = rmqBlockMinimum;
    }

    public int getBlockLength() {
        return blockLength;
    }

    public void setBlockLength(int blockLength) {
        this.blockLength = blockLength;
    }

    /**
     * @param arr is an input array for which preprocessing is applied
     */
    @Override
    public void preProcess(int[] arr) {
        int b = (int) Math.ceil(customLog(2, arr.length));
        int blocks = (int) Math.ceil((double) (arr.length) / b);
        setBlockLength(b);
        int min_array[] = new int[blocks];

        // build a min array containing minimum of each block using query function
        int j = 0;
        for (int i = 0; i < arr.length; i += b) {
            min_array[j++] = minBlock(arr, i, Math.min(arr.length - 1, i + b - 1));
        }

        // Sparse Table RMQ Sturucture for min_array
        setSparseTable(buildRMQStructure(arr, min_array, 2));
    }

    @Override
    public int query(int[] arr, int i, int j) {
        return (applyQueryAndReturnMin(arr, i, j));
    }


    /**
     * Helper method for query
     *
     * @param arr is the original array
     * @param i   start index
     * @param j   end index
     * @return minimum value between two indices
     */
    private int applyQueryAndReturnMin(int[] arr, int i, int j) {
        int bottomMinElementIndex = getMinimumOfBottomTable(arr, i, j);
        int topi = (int) (i / getBlockLength()) + 1;
        int topj = (int) (j / getBlockLength()) - 1;
        if (topj < topi) return arr[bottomMinElementIndex];
        int topMinElementIndex = getTopTableMinimum(arr, topi, topj);

        return Math.min(arr[bottomMinElementIndex], arr[topMinElementIndex]);
    }

    /**
     * Find the minium index in a block
     *
     * @param arr is the original array
     * @param i   start index of block
     * @param j   end index of block
     * @return minimum index within a block
     */
    public int minBlock(int[] arr, int i, int j) {
        int min = arr[i];
        int index = i;
        int k;
        for (k = i; k <= j; k++) {
            if (min > arr[k]) {
                min = arr[k];
                index = k;
            }
        }
        return index;
    }


    /**
     * Find minimum of bottom table
     * @param arr is the original array
     * @param i start index
     * @param j end index
     * @return minimum element of two indices
     */
    public int getMinimumOfBottomTable(int[] arr, int i, int j) {
        int bottomTableMinimumIndex = i;
        int firstBlock = (i / getBlockLength()) * getBlockLength() + getBlockLength();
        for (int k = i; k < Math.min(firstBlock, j + 1); k++) {
            bottomTableMinimumIndex = getMinimumIndex(arr, bottomTableMinimumIndex, k);
        }
        int lastBlock = (j / getBlockLength()) * getBlockLength();
        for (int k = Math.max(lastBlock, i); k <= j; k++) {
            bottomTableMinimumIndex = getMinimumIndex(arr, bottomTableMinimumIndex, k);
        }

        return bottomTableMinimumIndex;
    }


    /**
     * Get the top table (from sparse) minimum
     * @param arr is the original array
     * @param firstBlockIndex start index
     * @param lastBlockIndex end index
     * @return minium value in top table
     */
    public int getTopTableMinimum(int[] arr, int firstBlockIndex, int lastBlockIndex) {
        int k = (int) customLog(2, (lastBlockIndex - firstBlockIndex + 1));
        int topMin = getMinimumIndex(arr, getSparseTable()[firstBlockIndex][k], getSparseTable()[lastBlockIndex - (1 << k) + 1][k]);
        return topMin;
    }

    private int getMinimumIndex(int[] arr, int start, int end) {
        return arr[start] <= arr[end] ? start : end;
    }


    public static double customLog(double base, double logNumber) {
        return Math.log(logNumber) / Math.log(base);
    }


    /**
     * Builds the RMQ structure
     * @param arr is the original array
     * @param min_arr is the block
     * @param type is 1 or 2 for full preprocessing and partial preprocessing respectively
     * @return sparse table after preprocessing
     */
    public static int[][] buildRMQStructure(int[] arr, int min_arr[], int type) {
        // if type =1, full preprocessing else build psrase table
        if (type == 1) {

            int n = arr.length;
            int[][] rmq_struct = new int[n][n];

            // filling diagonal elements
            for (int i = 0; i < arr.length; i += 1) {
                rmq_struct[i][i] = i;
            }

            // DP Solution in order to build the Table
            int k = 1;
            while (k < n) {
                int i = 0;
                int j = k;

                while (j < n) {

                    if (arr[rmq_struct[i][j - 1]] < arr[rmq_struct[i + 1][j]]) {
                        rmq_struct[i][j] = rmq_struct[i][j - 1];
                    } else {

                        rmq_struct[i][j] = rmq_struct[i + 1][j];
                    }
                    //rmq_struct[i][j] = Math.min(rmq_struct[i][j - 1], rmq_struct[i + 1][j]);
                    j += 1;
                    i += 1;
                }
                k += 1;
            }

            return rmq_struct;

        } else {

            // building jagged array
            int n = min_arr.length;
            int k = (int) (Math.log(n) / Math.log(2));
            int[][] rmq_struct = new int[n][k + 1];

            // assigning all the columns with values of the arrays
            for (int i = 0; i < n; i++) {
                rmq_struct[i][0] = min_arr[i];
            }

            // we will run this loop upto K everytime where K = log2(N)
            for (int j = 1; j < k + 1; j++) {

                // Compute minimum value for all intervals with size 2^j
                int p = (1 << j) - 1;
                for (int i = 0; (i + p) < n; i++) {

                    if (arr[rmq_struct[i][j - 1]] < arr[rmq_struct[i + (1 << j - 1)][j - 1]]) {
                        rmq_struct[i][j] = rmq_struct[i][j - 1];
                    } else {
                        rmq_struct[i][j] = rmq_struct[i + (1 << j - 1)][j - 1];
                    }
                    // rmq_struct[i][j] = Math.min(rmq_struct[i][j-1],rmq_struct[i+(1<<j-1)][j-1]);
                }
            }
            return rmq_struct;
        }

    }
}
