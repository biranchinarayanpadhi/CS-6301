/* Starter code for PERT algorithm (LP2)
 * @author rbk
 */

// change package to your netid
package LP2;



import LP2.Graph.*;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
    static LinkedList<Vertex> topList;
    static int criticalVerticesCount;

    public static class PERTVertex implements Factory {
        private int es, ec, ls, lc, slack, vertexDuration;
        private boolean isCritical;

        public PERTVertex(Vertex u) {
            es = 0;
            ec = 0;
            ls = 0;
            lc = 0;
            slack = 0;
            vertexDuration = 0;
            isCritical = false;
            criticalVerticesCount = 0;
        }

        public PERTVertex make(Vertex u) {
            return new PERTVertex(u);
        }
    }

    public PERT(Graph g) {
        super(g, new PERTVertex(null));
        initializeStartAndFinishNodes(g);
        PERT.topList = (LinkedList<Vertex>) DFS.topologicalOrder1(g);
    }

    public void initializeStartAndFinishNodes(Graph g){
        Vertex s = g.getVertex(1);
        Vertex t = g.getVertex(g.n);
        int edgeName = g.n;
        g.addEdge(s, g.getVertex(2), 1, 1);

        for(int i=1;i<g.n;i+=1){
            if(!(g.incident(g.getVertex(i))).iterator().hasNext()){
                g.addEdge(g.getVertex(i), t, 1, ++edgeName);
            }
        }

    }

    public void setDuration(Vertex u, int d) {
        get(u).vertexDuration = d;
    }

    /**
     * @return whether time is calculated or it's not possible.
     */
    public boolean pert() {
    	if(PERT.topList != null){
//            int[] duration = new int[PERT.topList.size()];
//            int counter = 0;
            for(Vertex u: PERT.topList){
//                duration[counter++] = get(u).vertexDuration;
                get(u).es = 0;
                get(u).ec = 0;
            }

            PERTVertex u;
            PERTVertex v;
            for(Vertex vertex: PERT.topList){
                u = get(vertex);
                u.ec = u.es + u.vertexDuration;
                for(Graph.Edge e: g.incident(vertex)){
                    Vertex otherEnd = e.otherEnd(vertex);
                    v = get(otherEnd);
                    if(v.es < u.ec){
                        v.es = u.ec;
                    }
                }
            }

            int maxFinishTime = get(g.getVertex(g.n)).ec;

            //setting t's latest completion and latest start to it's early completion
            get(g.getVertex(g.n)).lc = get(g.getVertex(g.n)).ls = maxFinishTime;

            //setting all nodes latest completion to the maximum time
            for(Vertex vertex: topList){
                get(vertex).lc = maxFinishTime;
                get(vertex).ls = get(vertex).lc - get(vertex).vertexDuration;
            }


            //getting the reverse list
            Iterator iterator = PERT.topList.descendingIterator();
            iterator.next();

            while(iterator.hasNext()){
                Vertex uVertex = (Vertex) iterator.next();
                PERTVertex pu = get(uVertex);
                pu.ls = pu.lc - pu.vertexDuration;


                for(Graph.Edge e: g.incident(uVertex)){
                    Vertex otherEnd = e.otherEnd(uVertex);
                    PERTVertex pv = get(otherEnd);

                    if(pu.lc > pv.ls){
                        pu.lc = pv.ls;
                        pu.ls = pu.lc - pu.vertexDuration;
                    }
                }


            }

            for(Vertex vertex: topList){
                PERTVertex pu = get(vertex);
                pu.slack = pu.lc - pu.ec;

                if(pu.slack == 0){
                    pu.isCritical = true;
                    PERT.criticalVerticesCount++;
                }
            }

            get(g.getVertex(1)).isCritical = false;
            get(g.getVertex(1)).lc = 0;
            get(g.getVertex(g.n)).isCritical = false;
            PERT.criticalVerticesCount -= 2;

        }else{
    	    return false;
        }

    	return true;
    }

    /**
     * @param u for vertex
     * @return earliest completion time of u
     */
    public int ec(Vertex u) {
        return get(u).ec;
    }

    /**
     * @param u for vertex
     * @return latest completion time of u
     */
    public int lc(Vertex u) {
        return get(u).lc;
    }

    /**
     * @param u for vertex
     * @return Slack of u
     */
    public int slack(Vertex u) {
        return get(u).slack;
    }

    /**
     * Minimum duration to complete the project
     * @return length of critical path
     */
    public int criticalPath() {
        PERTVertex lastVertex = get(g.getVertex(g.n));
        return lastVertex.lc;
    }


    /**
     * @param u for vertex
     * @return Is vertex u on a critical path?
     */
    public boolean critical(Vertex u) {
        return get(u).isCritical;
    }


    /**
     * @return Number of critical nodes in graph
     */
    public int numCritical() {
        return PERT.criticalVerticesCount;
    }

    // setDuration(u, duration[u.getIndex()]);
    public static PERT pert(Graph g, int[] duration) {

    	return null;
    }

    public static void main(String[] args) throws Exception {
        String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
//        String graph = "9 8   2 3 1   2 4 1   3 5 1   4 6 1   4 7 1   5 7 1   5 8 1   6 8 1   0 3 3 2 3 7 4 6 0";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        PERT p = new PERT(g);
        for (Vertex u : g) {
            p.setDuration(u, in.nextInt());
        }
        // Run PERT algorithm.  Returns null if g is not a DAG
        if (!p.pert()) {
            System.out.println("Invalid graph: not a DAG");
        } else {
            System.out.println("Number of critical vertices: " + p.numCritical());
            System.out.println("u\tEC\tLC\tSlack\tCritical");
            for (Vertex u : g) {
                System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
            }
        }
    }
}
