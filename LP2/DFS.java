/** Starter code for SP5
 *  @author Darrin Willey(dmw150030) , Biranchi Padhi(BXP200001)
 */

// change to your netid
package LP2;

import  LP2.Graph.*;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.Set;


public class DFS extends GraphAlgorithm<DFS.DFSVertex> {

	int UNVISITED = 0;
	int IN_PROGRESS = 1;
	int FINISHED = 2;
	List<Set<Vertex>> result;
	

	public static class DFSVertex implements Factory {
		int cno;

		public DFSVertex(Vertex u) {
		}

		public DFSVertex make(Vertex u) {
			return new DFSVertex(u);
		}
	}

	public DFS(Graph g) {
		super(g, new DFSVertex(null));
	}

	public static DFS depthFirstSearch(Graph g) {
		return null;
	}

	public static DFS StronglyConnectedGraph(Graph g){

		DFS d = new DFS(g);
		List<Vertex> topo_order = d.topologicalOrder1();
		g.reverseGraph();

		List<Vertex> visited = new ArrayList<Vertex>();
		List<Set<Vertex>> result =new ArrayList<Set<Vertex>>();

		while(topo_order.size()>0){
			Vertex top_vertex = topo_order.remove(0);
			if (visited.contains(top_vertex)){
				continue;
			}
			Set<Vertex> component = new HashSet<Vertex>();
			d.dfs(top_vertex,visited,component);
			result.add(component);

		}
		d.result=result;
		return d;
	}

	public void dfs(Vertex top_vertex,List<Vertex> visited,Set<Vertex> component){

		visited.add(top_vertex);
		AdjList adj = g.adj(top_vertex.getIndex());
		for (Edge e : adj.outEdges) {
			if(visited.contains(e.from)){
				continue;
			}
			dfs(e.from,visited,component);
		}
		component.add(top_vertex);

	}

	// Member function to find topological order
	public List<Vertex> topologicalOrder1() {

		int[] status = new int[g.n];
		Stack<Vertex> stack = new Stack<Vertex>();
		List<Vertex> result = new ArrayList<Vertex>();

		// topological Sort starts from here
		for (int id = 0; id < g.n; id += 1) {

			if (status[id] == UNVISITED) {
				topologicalSort(id, status, stack);
			}
		}
		while (!stack.empty()) {
			result.add(stack.pop());
		}
		
		return result;
	}

	//returns false if Cycle Detected , returns True if successfull.
	public void topologicalSort(int id, int[] status, Stack<Vertex> stack) {

		status[id] = IN_PROGRESS;
		AdjList adj = g.adj(id);
		for (Edge e : adj.outEdges) {

			if(status[e.to.getIndex()] == UNVISITED){
				topologicalSort(e.to.getIndex(), status, stack);
			}
		}

		stack.add(g.getVertex(id + 1));
		status[id] = FINISHED;
	}

	// Find the number of connected components of the graph g by running dfs.
	// Enter the component number of each vertex u in u.cno.
	// Note that the graph g is available as a class field via GraphAlgorithm.
	public int connectedComponents() {
		return 0;
	}

	// After running the connected components algorithm, the component no of each
	// vertex can be queried.
	public int cno(Vertex u) {
		return get(u).cno;
	}

	// Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
	public static List<Vertex> topologicalOrder1(Graph g) {
		DFS d = new DFS(g);
		return d.topologicalOrder1();
	}

	// Find topological oder of a DAG using the second algorithm. Returns null if g
	// is not a DAG.
	public static List<Vertex> topologicalOrder2(Graph g) {
		return null;
	}

	public static void main(String[] args) throws Exception {
		//String string = "7 7   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 6 7   6 1 1";
		//String string = "6 6    6 1 1  1 6 6  5 1 1   6 3 1   5 2 1   4 2 1   3 4 1 0";
		//String string="4 4  1 2 2  2 3 3  3 4 1  4 1 1";
		//String string="4 3   1 2 2   2 3 2   3 4 1  4 1 0";
		//String string = "5 5  2 1 1  1 3 3  3 2 2  1 4 4  4 5 5";
		String string = "5 6  1 2 1  2 3 3  3 5 2  3 4 2   4 1 1  5 3 4";
		
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
		// Read graph from input
		Graph g = Graph.readGraph(in, true);
		g.printGraph(false);

		DFS df = StronglyConnectedGraph(g);
		System.out.println(df.result);
	}
}