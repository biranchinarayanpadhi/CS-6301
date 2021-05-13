
package LP5;

import java.lang.Math;
import java.util.Stack;

public class RMQNoPP implements RMQStructure {

	boolean DEBUG = false;

	public void preProcess(int[] arr) {
		int b = (int) math.ceil(0.5 * customLog(4, arr.length));
		int min_array[] = new int[arr.length / b];

		// build a min array containing minimum of each block using query function
		int j = 0;
		for (int i = 0; i < arr.length; i += b) {
			min_array[j++] = query(arr, i, i + b);
		}

		// Sparse Table RMQ Sturucture for min_array
		int min_rmq[][] = buildRMQStructure(min_array, 2);

		

	}

	public int query(int[] arr, int i, int j) {
		int min = arr[i];
		for (int k = i; k <= j; k++)
			if (min > arr[k])
				min = arr[k];
		if (DEBUG)
			System.out.println("min = " + min + " range " + i + " " + j);
		return min;
	}

	public static double customLog(double base, double logNumber) {
		return Math.log(logNumber) / Math.log(base);
	}

	public static String buildCartesianNumber(int[] arr){

		Stack<Integer> stack = new Stack<Integer>();
		StringBuilder cartesian_number = new StringBuilder();
		for(int i=0;i<arr.length;i+=1){

			//if stack is empty add the element to stack and add 1 to cartesian_number
			if(stack.isEmpty()){

				stack.add(arr[i]);
				cartesian_number.append(1);
			}
			else{

				while (!stack.isEmpty() && stack.peek() > arr[i]){
					cartesian_number.append(0);
					stack.pop();
				}

				stack.add(arr[i]);
				cartesian_number.append(1);


			}

		}

		while (!stack.isEmpty()){
			stack.pop();
			cartesian_number.append(0);
		}
		return cartesian_number.toString();
	}

	public static int[][] buildRMQStructure(int arr[], int type) {

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

					if(arr[rmq_struct[i][j - 1]]<arr[rmq_struct[i + 1][j]]){
						rmq_struct[i][j] = rmq_struct[i][j - 1];
					}
					else{

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
			int n = arr.length;
			int[][] rmq_struct = new int[n][];
			int j = n;

			for (int i = 0; i < n; i++) {
				rmq_struct[i] = new int[j--];
			}

			// assigning all the columns with values of the arrays
			for (int i = 0; i < n; i++) {
				rmq_struct[i][0] = i;
			}

			// we will run this loop upto K everytime where K = log2(N)
			for (j = 1; (1 << j) <= n; j++) {

				// Compute minimum value for all intervals with size 2^j
				int k = (1 << j) - 1;
				for (int i = 0; (i + k) < n; i++) {

					if (arr[rmq_struct[i][j - 1]] < arr[rmq_struct[i + (1 << j - 1)][j - 1]]) {
						rmq_struct[i][j] = rmq_struct[i][j - 1];
					}
					else{
						rmq_struct[i][j] = rmq_struct[i + (1 << j - 1)][j - 1];
					}
					// rmq_struct[i][j] = Math.min(rmq_struct[i][j-1],rmq_struct[i+(1<<j-1)][j-1]);
				}
			}
			return rmq_struct;
		}

	}

}
