/**
 * 
 *
 * @author Ayesha Gurnani, Rutali  Viraj
 * Usage: Computes the number of strongly connected components in a graph
 */

package vdm180000;

import idsa.Graph;
import idsa.Graph.Vertex;
import idsa.Graph.Edge;
import idsa.Graph.GraphAlgorithm;
import idsa.Graph.Factory;

import java.io.File;
import java.util.*;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
   
    private int cno;
    private List<Vertex> outList;
    private int topOrd;
    private boolean isCycle;
    
    public boolean isCycle() {
        return isCycle;
    }

    public void setCycle(boolean cycle) {
        isCycle = cycle;
    }


    public static class DFSVertex implements Factory {
        private int cno;
        
        /** assigns a color to each vertex in the graph
         * 0: white vertex for vertex which is not dicovered yet.
         * 1: gray vertex for vertex which is discovered but edges are yet to be discovered.
         * 2: black vertex for vertex which is dicovered as well as edges are also discovered.
         */
        private int color;
        private Vertex parent;
        private boolean seen;
        private int inDegreeCount;
        private int top;
        
        public DFSVertex(Vertex u) {
            top = 0;
            cno = 0;
            inDegreeCount = 0;
            seen = false;
            color = 0;
            setParent(null);
            
        }

        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }

        public Vertex getParent() {
            return parent;
        }

        public void setParent(Vertex parent) {
            this.parent = parent;
        }


    }

    /**
     * 
     * @param g
     * This method initializes topOrd with the size of the vertices in the graph, initializes cycle as false, and cno as 0.
     */
    public DFS(Graph g) {
        super(g, new DFSVertex(null));
        topOrd = g.size();
        outList = new LinkedList<>();
        setCycle(false);
        cno = 0;

    }

    /**
     * 
     * @param g
     * @return depth first search of graph
     */
    public static DFS depthFirstSearch(Graph g) {
        DFS d = new DFS(g);
        d.dfs();
        return d;
    }

    /**
     * DFS helper
     */
    public void dfs() {
        topOrd = g.size();
        for (Vertex u : g) {
            get(u).color = 0;
            get(u).setParent(null);
            get(u).top = 0;
        }
        for (Vertex u : g) {
            if (!get(u).seen) {
                visitNode(u);
            }
        }
    }

    
    /**
     * 
     * Function to visit nodes in Depth First order and detect cycle while at it.
     * Also populates the topological ordering list
     * @param u
     */
    private void visitNode(Vertex u) {
        get(u).color = 1;
        get(u).seen = true;
        get(u).cno = cno;
        for (Edge e : g.incident(u)) {
            Vertex v = e.otherEnd(u);

            if (!get(v).seen) { 
                get(v).setParent(u);
                visitNode(v);
            } else if (get(u).top >= get(v).top) {
                setCycle(true);
            }
        }
        get(u).top = topOrd--;
        outList.add(0, u);
        get(u).color = 2;
    }

    /**
     * dfs helper
     * @param iterable
     */    
    private void dfs(Iterable<Vertex> iterable) {

        topOrd = g.size();

        for (Vertex u : iterable) {
            get(u).color = 0;
            get(u).seen = false;
            get(u).setParent(null);
        }
        outList = new LinkedList<>();
        cno = 0;
        for (Vertex u : iterable) {
            if (!get(u).seen) {
                cno++;
                visitNode(u);
            }
        }
    }


    /*
    Based on isCycle flag, this function returns either null or list of vertices in topological order.
     */
    public List<Vertex> topologicalOrder1() {
        return isCycle() ? null : this.outList;
    }

    /**
     * Method to find the number of connected components of the graph.
     * @return
     */
    public int connectedComponents() {
        return cno;
    }

    /**
     * 
     * @param u
     * @return
     */
    public int cno(Vertex u) {
        return get(u).cno;
    }

    /**
     * 
     * @param g
     * @return
     */
    public static List<Vertex> topologicalOrder1(Graph g) {
        DFS d = depthFirstSearch(g);
        return d.topologicalOrder1();
    }

    /**
     * Method to find strongly connected component of the graph.
     * @param g
     * @return dfs
     */
    public static DFS stronglyConnectedComponents(Graph g) {
        DFS d = new DFS(g);
        d.dfs(g);
        List<Vertex> vertexList = d.outList;
        g.reverseGraph();
        d.dfs(vertexList);
        g.reverseGraph();
        return d;
    }


    

    public static void main(String[] args) throws Exception {


        String string = "5 8   2 1 1   1 3 1   1 4 1   1 5 1   2 4 1   3 4 1   4 5 1   3 5 1";

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
        System.out.println("\nTotal strong components in the graph are: " + dfs.cno);
        System.out.println("=============================");
        System.out.println("Vertex  Component:\n");
        for (Vertex vertex : dfs.outList) {
            System.out.println("-----------------------");
            System.out.println(vertex.getName() + "   :    " + dfs.cno(vertex));
        }
        


    }
}
