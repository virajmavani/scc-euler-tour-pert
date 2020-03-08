/**
 * 
 *
 * @author Ayesha Gurnani, Rutali  Viraj
 * Usage: Computes the number of strongly connected components in a graph
 */

package vdm180000;

import vdm18000.Graph;
import vdm18000.Graph.Vertex;
import vdm18000.Graph.Edge;
import vdm18000.Graph.GraphAlgorithm;
import vdm18000.Graph.Factory;

import java.io.File;
import java.util.*;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {

    // topNum is the count of all the vertices in the graph
    private int topNum;
    // list of vertices in topological order
    private List<Vertex> finishList;

    // flag to determine if graph contains a cycle or a back edge
    private boolean isCycle;

    //global counter for component number
    private int cno;

    public boolean isCycle() {
        return isCycle;
    }

    public void setCycle(boolean cycle) {
        isCycle = cycle;
    }


    public static class DFSVertex implements Factory {
        // component number of a vertex
        private int cno;

        // assigns a numerical value to each vertex in the graph to refer it's topological order
        private int top;
        // assigns a color to each vertex in the graph
        /*
        0: white vertex (undiscovered vertex)
        1: gray vertex (discovered vertex whose adjacent vertices are still undiscovered)
        2: black vertex (discovered vertex whose adjacent vertices are discovered)
         */
        private int color;

        public Vertex getParent() {
            return parent;
        }

        public void setParent(Vertex parent) {
            this.parent = parent;
        }

        // stores the parent of the vertex while doing depth first traversal
        private Vertex parent;

        // flag to check if vertex is visited or not
        private boolean seen;

        // stores the number of incomming edges for a vertex
        private int inDegreeCount;


        public DFSVertex(Vertex u) {
            seen = false;
            color = 0;// initially a vertex is coloured as white
            setParent(null);
            top = 0;
            cno = 0;
            inDegreeCount = 0;
        }

        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }
    }

    public DFS(Graph g) {
        super(g, new DFSVertex(null));
        topNum = g.size();// initializes topNum with count of vertices in a graph
        finishList = new LinkedList<>();
        setCycle(false);// flag to detect a cycle in the graph, initialized with false
        cno = 0;// initially component number is set to 0

    }

    // computes depth first order of a graph
    public static DFS depthFirstSearch(Graph g) {
        DFS d = new DFS(g);
        d.dfs();
        return d;
    }

    // helper method to compute depth first order of a graph
    public void dfs() {
        topNum = g.size();
        for (Vertex u : g) {
            get(u).color = 0;
            get(u).setParent(null);
            get(u).top = 0;
        }
        for (Vertex u : g) {
            if (!get(u).seen) {
                dfsVisit(u);
            }
        }
    }

    // Recursively visits vertex in depth first order
    /*
    Whenever a back edge is detected this function sets isCycle flag to true.
    This flag (isCycle) decides whether a topological order is possible or not.
     */
    private void dfsVisit(Vertex u) {
        get(u).color = 1;// vertex is coloured as gray
        get(u).seen = true;
        get(u).cno = cno;
        for (Edge e : g.incident(u)) {
            Vertex v = e.otherEnd(u);

            if (!get(v).seen) { // white vertex
                get(v).setParent(u);
                dfsVisit(v);
            } else if (get(u).top >= get(v).top) {
                setCycle(true);// back edge detected
            }
        }
        get(u).top = topNum--;
        finishList.add(0, u);
        get(u).color = 2;
    }


    // computes finishList on a reversed graph by going through nodes  in decreasing finish time in first dfs traversal
    private void dfs(Iterable<Vertex> iterable) {

        topNum = g.size();

        for (Vertex u : iterable) {
            get(u).color = 0;
            get(u).seen = false;
            get(u).setParent(null);
        }
        finishList = new LinkedList<>();
        cno = 0;
        for (Vertex u : iterable) {
            if (!get(u).seen) {
                cno++;
                dfsVisit(u);
            }
        }
    }


    /*
    Based on isCycle flag, this function returns either null or list of vertices in topological order.
     */
    public List<Vertex> topologicalOrder1() {
        return isCycle() ? null : this.finishList;
    }


    // Find the number of connected components of the graph g by running dfs.
    // Enter the component number of each vertex u in u.cno.
    // Note that the graph g is available as a class field via GraphAlgorithm.
    public int connectedComponents() {
        return cno;
    }

    // After running the connected components algorithm, the component no of each vertex can be queried.
    public int cno(Vertex u) {
        return get(u).cno;
    }

    // Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder1(Graph g) {
        DFS d = depthFirstSearch(g);
        return d.topologicalOrder1();
    }

    // Find the number of strongly connected components of the graph g.
    public static DFS stronglyConnectedComponents(Graph g) {
        DFS d = new DFS(g);
        d.dfs(g);
        List<Vertex> vertexList = d.finishList;
        g.reverseGraph();
        d.dfs(vertexList);
        g.reverseGraph();
        return d;
    }


    

    public static void main(String[] args) throws Exception {


        String string = "5 9   1 2 1   2 1 1   1 3 1   1 4 1   1 5 1   2 4 1   3 4 1   4 5 1   3 5 1";//dag

        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);


        List<Vertex> vertexList = topologicalOrder1(g);
        System.out.println("Topological Order:");
        if (vertexList == null) System.out.println("null");
        else {
            for (Vertex vertex : vertexList) {
                System.out.print(vertex.getName() + " ");
            }
        }
        System.out.println("\n");

        DFS dfs = stronglyConnectedComponents(g);
        System.out.println("Vertex  ComponentNo:\n");
        for (Vertex vertex : dfs.finishList) {
            System.out.println(vertex.getName() + "   :    " + dfs.cno(vertex));
        }
        System.out.println("\nNumber of strongly connected components in the graph: " + dfs.cno);


    }
}
