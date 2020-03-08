/** Starter code for PERT algorithm (LP2)
* @author rbk
* @authors - 
* Ayesha Gurnani : ang170003
* Viraj Mavani : vdm180000
* Rutali Bandivadekar : rdb170002
*/

// change package to your netid
package vdm180000;

import idsa.Graph.Vertex;
import idsa.Graph;
import idsa.Graph.Edge;
import idsa.Graph.GraphAlgorithm;
import idsa.Graph.Factory;

import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;


public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
	private Vertex s;
    private Vertex t;
    private int len;
    private int criticalVerticesCount;
    
    /**
     * setter of s
     * @param Vertex s
     * @return null
     */
    public void setS(Vertex s) {
        this.s = s;
    }
    
    /**
     * getter of s
     * @param none
     * @return Vertex s
     */
    public Vertex getS() {
        return s;
    }
    
    /**
     * setter of t
     * @param Vertex t
     * @return null
     */
    public void setT(Vertex t) {
        this.t = t;
    }
    
    /**
     * getter of t
     * @param none
     * @return vertex t
     */
    public Vertex getT() {
        return t;
    }
    
    
    public static class PERTVertex implements Factory {
        private int ec;
        private int lc;
        private int slack;
        private int duration;
        private boolean isCritical; // store if the vertex is in critical path
        
        /**
         * setter of isCritical
         * @param boolean isCritical
         * @return null
         */
        public void setIsCritical(boolean isCritical) {
        	this.isCritical = isCritical;
        }
        
        /**
         * getter of isCritical
         * @param none
         * @return boolean values true or false if the vertex is in critical path
         */
        public boolean getIsCritical() {
        	return isCritical;
        }
        
        /**
         * setter of slack
         * @param int slack
         * @return null
         */
        public void setSlack(int slack) {
            this.slack = slack;
        }
        
        /**
         * getter of slack
         * @param none
         * @return int slack
         */
        public int getSlack() {
            return slack;
        }
        
        /**
         * setter of duration
         * @param int duration
         * @return null
         */
        public void setDuration(int duration) {
            this.duration = duration;
        }
        
        /**
         * getter of duration
         * @param none
         * @return int duration
         */
        public int getDuration() {
            return duration;
        }
        
        /**
         * setter of ec
         * @param int ec
         * @return null
         */
        public void setEc(int ec) {
            this.ec = ec;
        }
        
        /**
         * getter of ec
         * @param none
         * @return int ec
         */
        public int getEc() {
            return ec;
        }
        
        /**
         * setter of lc
         * @param int lc
         * @return null
         */
        public void setLc(int lc) {
            this.lc = lc;
        }
        
        /**
         * getter of lc
         * @param none
         * @return int lc
         */
        public int getLc() {
            return lc;
        }
        
        /**
         * constructor of PERTVertex -> initializes ec, slack and isCritical
         * @param Vertex u
         * @return null
         */
		public PERTVertex(Vertex u) {
			this.setEc(0);
	        this.setSlack(0);
	        this.setIsCritical(false);
		}
		
		public PERTVertex make(Vertex u) { 
			return new PERTVertex(u); 
		}
    }
    
    /**
     * constructor of PERT -> initializes s, t, len and criticalVerticesCount variables
     * @param Graph g
     * @return null
     */
    public PERT(Graph g) {
    	super(g, new PERTVertex(null));
		this.setS(g.getVertex(1)); //vertex 1 is s
		this.setT(g.getVertex(g.size())); // vertex n is t
		this.len = 0;
		this.criticalVerticesCount = 0;
    }

    public void setDuration(Vertex u, int d) {
    	get(u).setDuration(d);
    }
    
    /**
     * pert() method -> apply PERT on a graph and find the earliest completion time and count of nodes in critical path
     * @param Vertex s
     * @return true if PERT can be performed else false if the graph is not a DAG
     */
    public boolean pert() {
        List<Vertex> topList = DFS.topologicalOrder1(g);
        
        if (topList == null) 
        	return false;
        
        for (Vertex u : g)
            get(u).setEc(0);

        for (Vertex u : topList) {
            for (Edge e : g.incident(u)) {
                Vertex v = e.otherEnd(u);
                if (get(u).getEc() + get(v).getDuration() > get(v).getEc())
                    get(v).setEc(get(u).getEc() + get(v).getDuration());
            }
        }
        
        this.len = get(getT()).getEc();
        for (Vertex u : g)
            get(u).setLc(this.len);
        
        ListIterator<Vertex> reverseIterator = topList.listIterator(topList.size());
        while (reverseIterator.hasPrevious()) {
            Vertex u = reverseIterator.previous();
            for (Edge e : g.incident(u)) {
                Vertex v = e.otherEnd(u);
                if ((get(v).getLc() - get(v).getDuration()) < get(u).getLc()) 
                    get(u).setLc(get(v).getLc() - get(v).getDuration());
                
            }
            
            get(u).setSlack(get(u).getLc() - get(u).getEc());
            if(get(u).getSlack() == 0){
                get(u).setIsCritical(true);
                criticalVerticesCount += 1;
            }
        }

        return true;
    }
    
    /**
     * ec method -> returns earliest completion time of a vertex u
     * @param Vertex u
     * @return earliest completion time of a vertex u
     */
    public int ec(Vertex u) {
    	return get(u).getEc();
    }
    
    /**
     * lc method -> returns latest completion time of a vertex u
     * @param Vertex u
     * @return latest completion time of a vertex u
     */
    public int lc(Vertex u) {
    	return get(u).getLc();
    }
    
    /**
     * slack method -> returns slack completion time of a vertex u
     * @param Vertex u
     * @return slack of a vertex u
     */
    public int slack(Vertex u) {
    	return get(u).getSlack();
    }
    
    /**
     * criticalPath method 
     * @param none
     * @return length of critical path
     */
    public int criticalPath() {
        return len;
    }
    
    /**
     * critical method ->  Is vertex u on a critical path?
     * @param Vertex u
     * @return if vertex u is in critical path
     */
    public boolean critical(Vertex u) {
    	return get(u).getIsCritical();
    }
    
    /**
     * numCritical method -> returns number of critical nodes
     * @param none
     * @return number of critical nodes in graph
     */
    public int numCritical() {
    	return criticalVerticesCount;
    }

    // setDuration(u, duration[u.getIndex()]);
    /**
     * pert method -> performs pert operation on Graph g
     * @param Graph g and int[] duration
     * @return PERT object if pert can be performed on the graph else null
     */
    public static PERT pert(Graph g, int[] duration) {
    	PERT pert = new PERT(g);
        for(Vertex u: g) {
            pert.setDuration(u, duration[u.getIndex()]);
            
            /* add edges from s to all vertices and from all vertices to t
             * s -> pert.getS().getIndex()
             * t -> pert.getT().getIndex()
             */
            if(u.getName() != g.size())
                g.addEdge(u.getIndex(), pert.getT().getIndex(), 0);
            
            if(u.getName() != 1)
                g.addEdge(pert.getS().getIndex(), u.getIndex(), 0);
        }
        
        if (pert.pert()) 
        	return pert;
        
        return null;
    }
    
    public static void main(String[] args) throws Exception {
		String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new java.io.File(args[0])) : new Scanner(graph);
		
		Graph g = Graph.readDirectedGraph(in);
		
		int[] duration = new int[g.size()];
		for(int i = 0; i < g.size(); i++)
			duration[i] = in.nextInt();
		
		
		PERT p = PERT.pert(g, duration);
		
		boolean displayInfo = false; // if user wants to display the graph and node details
		if(p == null)
			System.out.println("Invalid graph: not a DAG");
		else {
			System.out.println("Length of critical path: " + p.criticalPath());
			System.out.println("Number of critical vertices: " + p.numCritical());
			System.out.println();
			if(displayInfo) { // update displayInfo variable to print the graph and each nodes ec, lc, slack and critical
				g.printGraph(false);
				System.out.println("u\tDur\tEC\tLC\tSlack\tCritical");
				for(Vertex u: g) 
					System.out.println(u + "\t" + p.get(u).getDuration() + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
			}
		}
	}
}
