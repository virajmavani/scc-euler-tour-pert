/** Starter code for LP2
 *  @author Ayesha Gurnani (ang170003)
 *  @author Rutali Bandivadekar 
 *  @author Viraj Mavani (vdm180000)
 */

// change to your netid
package vdm180000;

import vdm180000.Graph.Vertex;
import vdm180000.Graph.Edge;
import vdm180000.Graph.GraphAlgorithm;
import vdm180000.Graph.Factory;
import vdm180000.Graph.Timer;

import java.io.FileInputStream;
import java.util.*;
import java.io.File;


public class Euler extends GraphAlgorithm<Euler.EulerVertex> {
    LinkedList<Vertex> topoList = new LinkedList<>(); // list contains vertices in topological order
    HashMap<Vertex, LinkedList<Edge>> edge_counter = new HashMap<>();
    static int VERBOSE = 1;
    Vertex start;
    LinkedList<Vertex> tour;

    // You need this if you want to store something at each node
    static class EulerVertex implements Factory {
        boolean seen;

        EulerVertex(Vertex u) {
            this.seen = false;
        }
        public EulerVertex make(Vertex u) {  return new EulerVertex(u); }

    }

    // To do: function to find an Euler tour
    public Euler(Graph g, Vertex start) {
        super(g, new EulerVertex(null));
        this.start = start;
        tour = new LinkedList<>();
    }

    /* To do: test if the graph is Eulerian.
     * If the graph is not Eulerian, it prints the message:
     * "Graph is not Eulerian" and one reason why, such as
     * "inDegree = 5, outDegree = 3 at Vertex 37" or
     * "Graph is not strongly connected"
     */

    public boolean isEulerian() {

        boolean strongly_connected = true;
        boolean iod = true;
        if(!stronglyConnectedComponents()){
            strongly_connected = false;
            System.out.println("Graph is not strongly connected");
        }

        for(Vertex u: g){
            if(g.adj(u).outEdges.size() != g.adj(u).inEdges.size()){
                iod = false;
                String output_msg = "in-degree = " + g.getVertex(u).inDegree() + " and out-degree = " + g.getVertex(u).outDegree() + " at Vertex = " + u.getIndex();
                System.out.println(output_msg);
            }
        }

        return (strongly_connected && iod);

    }

    /**
     * Function to check if Graph is strongly connected
     * @return
     */
    public boolean stronglyConnectedComponents(){
        visitDFS(g,start);
        for(Vertex u: g){
            if(get(u).seen == false){
                return false;
            }
        }

        g.reverseGraph();
        for(Vertex u: g){
            get(u).seen = false;
        }

        visitDFS(g, start);
        for(Vertex u: g){
            if(get(u).seen == false){
                return false;
            }
        }
        return true;
    }


    /**
     * DFS Traversal of Graph
     * @param g - Graph to be traversed
     * @param u - Vertex to start traversal from
     */
    public void visitDFS(Graph g, Vertex u){
        if(!get(u).seen){
            get(u).seen = true;
            for(Edge e: g.incident(u)) {
                Vertex v = e.otherEnd(u);
                visitDFS(g,v);
            }
        }
    }


    public List<Vertex> findEulerTour(boolean isEuler) {
	if(!isEuler) {
	    return new LinkedList<Vertex>();
	}
	for(Vertex u: g){
	    LinkedList<Edge> list = new LinkedList<>();
	    for(Edge e: g.outEdges(u))
	        list.add(e);
	    edge_counter.put(u, list);
    }

	topoList.addFirst(start);
	Vertex curr = start;
	while(!topoList.isEmpty()){
	    if(edge_counter.get(curr).size() > 0){
	        topoList.addFirst(curr);
	        Edge ed = edge_counter.get(curr).getLast();
	        Vertex next = ed.otherEnd(curr);
	        edge_counter.get(curr).removeLast();
	        curr = next;
        }
	    else{
	        tour.addFirst(curr);
	        curr = topoList.removeFirst();
        }
    }
       // Graph is Eulerian...find the tour and return tour
	return tour;
    }

    public static void main(String[] args) throws Exception {
        Scanner in;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            String input = "9 13 1 2 1 2 3 1 3 1 1 3 4 1 4 5 1 5 6 1 6 3 1 4 7 1 7 8 1 8 4 1 5 7 1 7 9 1 9 5 1";

//            String input = "5 6   2 1 1   1 3 1   3 2 1   1 4 1   4 5 1   5 1 1";
//
//            String input ="7 10   1 3 1   1 7 1   2 3 1   3 1 1   3 4 1   4 5 1   5 3 1   5 6 1   6 1 1   7 5 1";
            in = new Scanner(input);
        }
        int start = 1;
        if(args.length > 1) {
            start = Integer.parseInt(args[1]);
        }
        if(args.length > 2) {
            VERBOSE = Integer.parseInt(args[2]);
        }
        Graph g = Graph.readDirectedGraph(in);
        Vertex startVertex = g.getVertex(start);

        g.printGraph(false);

        Timer timer = new Timer();


        Euler euler = new Euler(g, startVertex);
        euler.setVerbose(VERBOSE);
        Boolean isEuler = euler.isEulerian();
        System.out.println("Is Eulerian? "+isEuler);

	    List<Vertex> tour = euler.findEulerTour(isEuler);


        timer.end();
        if(VERBOSE > 0) {
            if(tour.size() > 0){
                System.out.println("Output:");
                // print the tour as sequence of vertices (e.g., 3,4,6,5,2,5,1,3)
                for(Vertex i: tour)
                    System.out.print((i.getIndex()+1)+" ");
            }
            else
                System.out.println("Not a Eulerian Graph");

	    System.out.println();
        }
        System.out.println(timer);


    }

    public void setVerbose(int ver) {
        VERBOSE = ver;
    }
}
