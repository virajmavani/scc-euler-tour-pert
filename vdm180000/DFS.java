/** Starter code for SP5
 *  @author rbk
 */

// change to your netid
package vdm180000;

import vdm180000.Graph.Vertex;
import vdm180000.Graph.Edge;
import vdm180000.Graph.GraphAlgorithm;
import vdm180000.Graph.Factory;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
    private static List<Vertex> tplist = new LinkedList<>();
    private static int cno;

    public static class DFSVertex implements Factory {
        int cno;
        int color = 0;
        Vertex parent;

        public DFSVertex(Vertex u) {
            color = 0;
            parent = null;
            cno = 0;
        }

        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }
    }

    public DFS(Graph g) {
        super(g, new DFSVertex(null));
        cno = 0;
    }

    public DFS depthFirstSearch(Graph g) {
        DFS d = new DFS(g);

        for (Vertex v : g) {
            if (d.get(v).color == 0) {
                try {
                    d.visitAndDetectCycle(g, v);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return null;
                }
            }
        }
        return d;
    }

    /**
     * Function to visit nodes in Depth First order and detect cycle while at it.
     * Also populates the topological ordering list
     */
    private void visitAndDetectCycle(Graph g, Vertex u) throws Exception {
        get(u).color = 1;

        for (Edge e : g.incident(u)) {
            Vertex v = e.otherEnd(u);
            if (get(v).color == 0) {
                get(v).parent = u;
                visitAndDetectCycle(g, v);
            } else if (get(v).color == 1) {
                throw new Exception("Cycle Detected!");
            } else if (get(v).color == 2) {
                continue;
            }
        }

        tplist.add(0, u);
        get(u).color = 2;
	}

    // Member function to find topological order
    public List<Vertex> topologicalOrder1() {
        DFS d = depthFirstSearch(this.g);

        if (d == null) return null;

        return tplist;
    }

	// Find the number of connected compo
           // Enter the component number of each vertex u in u.cno.
    // Note that the graph g is available as a class field via GraphAlgorithm.
    public int connectedComponents() {
	return 0;
    }

    // After running the connected components algorithm, the component no of each vertex can be queried.
    public int cno(Vertex u) {
	return get(u).cno;
    }
    
    // Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder1(Graph g) {
	DFS d = new DFS(g);
	return d.topologicalOrder1();
    }

    // Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder2(Graph g) {
	return null;
    }

    public static void main(String[] args) throws Exception {
        String string = "7 8   1 2 2   1 3 3   2 4 5   3 4 4   3 6 6   4 5 1   1 5 7   6 7 1";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
        
        // Read graph from input
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);
        
        DFS d = new DFS(g);
        List<Vertex> order = d.topologicalOrder1();
        if (order == null) {
            System.out.println("Cycle Detected!");
        } else {
            for ( Vertex v : order ) {
                System.out.print(v.getName() + " ");
            }
        }
        System.out.println();
    }
}