

package LP5;
import java.util.Random;

public class RMQDriver {
    public static Random random = new Random();
    public static int numTrials = 100;
    public static void main(String[] args) {
	int n = 10000000;
	int choice = 5;
	int rangeSize = 0;
	if(args.length > 0) { choice = Integer.parseInt(args[0]); }
	// specify the range as % of n
	if(args.length > 1) { rangeSize = (n / 100) * Integer.parseInt(args[1]); }
	if(args.length > 2) { n = Integer.parseInt(args[2]); }
	
	if (rangeSize == 0) rangeSize = random.nextInt(n);

	System.out.println("Choice = " + choice + " n = " + n + " rangeSize = " + rangeSize);
	
	int[] arr = new int[n];
    for(int i = 0; i < n; i++) {
	    arr[i] = n - i;
	}
	// shuffle the array

//	int[] arr = new int[]{5, 6, 234, 32, 2, 535, 34646, 23432, 35, 35};

	Shuffle.shuffle(arr);
	
	RMQStructure rmqSt = null;
	switch(choice) {
//	case 0:
//		rmqSt = new RMQNoPP();
//	    break;
//	case 1:
//		rmqSt = new RMQFullTable();
//	    break;
//	case 2:
//	    rmqSt = new RMQBlock();
//	    break;
//	case 3:
//	    rmqSt = new RMQSparseTable();
//	    break;
	case 4:
	    rmqSt = new RMQHybridOne();
	    break;
	case 5:
	    rmqSt = new RMQFischerHeun();
	    break;
	default:
		System.out.println("Invalid choice");
		System.exit(1);
	}
	//pre process
	Timer timer = new Timer();
	timer.start();
	rmqSt.preProcess(arr); 
	timer.end();
	timer.scale(numTrials);

	System.out.println("Preprocessing time for Choice: " + choice + "\n" + timer);
    
	timer.start();
	int begin;
	for (int i = 0; i < numTrials; i++) {
		
		begin = random.nextInt(n - rangeSize);
		System.out.println(begin+" "+(begin+rangeSize));
		int result = rmqSt.query(arr, begin, (begin+rangeSize));
		System.out.println("Result "+result);
		verify(arr, begin, begin+rangeSize, result);
		System.out.println("minimum between " + begin + " and " + (begin + rangeSize) + "is " + result);

	}
	timer.end();
	timer.scale(numTrials);

	System.out.println("Query time for Choice: " + choice + "\n" + timer);
	}
	
	private static void verify(int[] arr, int i, int j, int result){
		int min = arr[i];
		for (int k = i; k <= j; k++)
			if (min > arr[k]) min = arr[k];
		if (min != result){
			System.out.println(" range " + i + " " + j);
			System.out.println(" result " + result + " is not min " + min);
			System.exit(1);
		}
	}



   /** Timer class for roughly calculating running time of programs
     *  @author rbk
     *  Usage:  Timer timer = new Timer();
     *          timer.start();
     *          timer.end();
     *          System.out.println(timer);  // output statistics
     */

    public static class Timer {
        long startTime, endTime, elapsedTime, memAvailable, memUsed;
        boolean ready;

        public Timer() {
            startTime = System.nanoTime();
            ready = false;
        }

        public void start() {
            startTime = System.nanoTime();
            ready = false;
        }

        public Timer end() {
            endTime = System.nanoTime();
            elapsedTime = endTime-startTime;
            memAvailable = Runtime.getRuntime().totalMemory();
            memUsed = memAvailable - Runtime.getRuntime().freeMemory();
            ready = true;
            return this;
        }

        public long duration() { if(!ready) { end(); }  return elapsedTime; }

        public long memory()   { if(!ready) { end(); }  return memUsed; }

	public void scale(int num) { elapsedTime /= num; }
	
        public String toString() {
            if(!ready) { end(); }
            return "Time: " + elapsedTime/1000 + " usec.\n" + "Memory: " + (memUsed/1048576) + " MB / " + (memAvailable/1048576) + " MB.";
        }
    }
    
    /** @author rbk : based on algorithm described in a book
     */

    /* Shuffle the elements of an array arr[from..to] randomly */
    public static class Shuffle {
	
	public static void shuffle(int[] arr) {
	    shuffle(arr, 0, arr.length-1);
	}

	public static<T> void shuffle(T[] arr) {
	    shuffle(arr, 0, arr.length-1);
	}

	public static void shuffle(int[] arr, int from, int to) {
	    int n = to - from  + 1;
	    for(int i=1; i<n; i++) {
		int j = random.nextInt(i);
		swap(arr, i+from, j+from);
	    }
	}

	public static<T> void shuffle(T[] arr, int from, int to) {
	    int n = to - from  + 1;
	    Random random = new Random();
	    for(int i=1; i<n; i++) {
		int j = random.nextInt(i);
		swap(arr, i+from, j+from);
	    }
	}

	static void swap(int[] arr, int x, int y) {
	    int tmp = arr[x];
	    arr[x] = arr[y];
	    arr[y] = tmp;
	}
	
	static<T> void swap(T[] arr, int x, int y) {
	    T tmp = arr[x];
	    arr[x] = arr[y];
	    arr[y] = tmp;
	}

	public static<T> void printArray(T[] arr, String message) {
	    printArray(arr, 0, arr.length-1, message);
	}

	public static<T> void printArray(T[] arr, int from, int to, String message) {
	    System.out.print(message);
	    for(int i=from; i<=to; i++) {
		System.out.print(" " + arr[i]);
	    }
	    System.out.println();
	}
    }
}

