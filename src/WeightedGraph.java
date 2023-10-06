import java.util.*;

public class WeightedGraph<V> extends AbstractGraph<V>{
    //construct empty graph
    public WeightedGraph(){
    }

    //construct WeightedGraph from vertices and edges in arrays
    public WeightedGraph(V[] vertices, int[][] edges){
        createWeightedGraph(List.of(vertices), edges);
    }

    //constructor for adjacency matrix
    public WeightedGraph(V[] vertices, Integer[][] edges){
        createWeightedGraph(List.of(vertices), edges);
    }

    //construct WeightGraph from vertices and edges in list
    public WeightedGraph(int[][]edges, int numberOfVertices){
        List<V> vertices = new ArrayList<>();
        for(int i = 0; i < numberOfVertices; i++)
            vertices.add((V)(Integer.valueOf(i)));

        createWeightedGraph(vertices,edges);
    }

    //construct a WeightedGraph for vertices 0, 1, 2 and edge list
    public WeightedGraph(List<V> vertices, List<WeightedEdge> edges){
        createWeightedGraph(vertices, edges);
    }

    //construct weighted graph from vertices 0, 1 and edge array
    public WeightedGraph(List<WeightedEdge> edges, int numberOfVertices){
        List<V> vertices = new ArrayList<>();
        for(int i = 0; i < numberOfVertices; i++)
            vertices.add((V)(Integer.valueOf(i)));

        createWeightedGraph(vertices, edges);
    }

    //create adjacency lists from edge arrays
    private void createWeightedGraph(List<V> vertices, int[][] edges){
        this.vertices = vertices;

        for(int i = 0; i < vertices.size(); i++){
            neighbors.add(new ArrayList<>()); //create list for vertices
        }

        for(int i = 0; i < edges.length; i++){
            neighbors.get(edges[i][0]).add(new WeightedEdge(edges[i][0], edges[i][1], edges[i][2]));
        }
    }

    //create adjacency lists from adjacency matrix
    private void createWeightedGraph(List<V> vertices, Integer[][] edges){
        this.vertices = vertices;

        for(int i = 0; i < vertices.size(); i++){
            neighbors.add(new ArrayList<>()); //create list for vertices
        }

        for(int i = 0; i < edges.length; i++){
           for(int j = 0; j < edges.length; j++){
               if(!((edges[i][j]) == null)){
                   neighbors.get(i).add(new WeightedEdge(i, j, edges[i][j]));
               }
           }
        }


    }

    //create adjacency lists from edge lists
    private void createWeightedGraph(List<V> vertices, List<WeightedEdge> edges){
        this.vertices = vertices;

        for(int i = 0; i < vertices.size(); i++){
            neighbors.add(new ArrayList<Edge>()); //create list for vertices

        }

        for(WeightedEdge edge:  edges){
            neighbors.get(edge.u).add(edge); //add an edge to the list
        }
    }

    //return weight on the edge (u, v)
    public double getWeight(int u, int v) throws Exception{
        for(Edge edge: neighbors.get(u)){
            if(edge.v == v){
                return ((WeightedEdge)edge).weight;
            }
        }
        throw new Exception("Edge does not exist");
    }

    //display edges with weights
    public void printWeightedEdges(){
        for(int i = 0; i < getSize(); i++){
            System.out.print(getVertex(i) + " (" + i + "): ");
            for(Edge edge: neighbors.get(i)){
                System.out.print("(" + edge.u + ", " + edge.v + ", " + ((WeightedEdge)edge).weight + ") ");
            }
            System.out.println();
        }
    }

    //add edges to the weighted graph
    public boolean addEdge(int u, int v, double weight){
        return addEdge(new WeightedEdge(u, v, weight));
    }

    //get a minimum spanning tree rooted at vertex 0
    public MST getMinimumSpanningTree(int startingVertex){
        //cost[v] stores the cost by adding v to the tree
        double[] cost = new double[getSize()];
        for(int i = 0; i < cost.length; i++){
            cost[i] = Double.POSITIVE_INFINITY; //initial cost
        }
        cost[startingVertex] = 0; // cost of source is 0

        int[] parent = new int [getSize()]; //parent of a vertex
        parent[startingVertex] = -1; //startingVertex is the root
        double totalWeight = 0; // total weight of the tree so far

        List<Integer> T = new ArrayList<>();

        //expand T
        while (T.size() < getSize()){
            //find the smallest cost v in V - T
            int u = -1; //vertex to be determined
            double currentMinCost = Double.POSITIVE_INFINITY;
            for(int i = 0; i < getSize(); i++){
                if(!T.contains(i) && cost[i] < currentMinCost){
                    currentMinCost = cost[i];
                    u = i;
                }
            }

            T.add(u); //add a new vertex to T
            totalWeight += cost[u]; //add cost[u] to the tree

            //adjust cost[v] for v that is adjacent to u and v in V -T
            for(Edge e: neighbors.get(u)){
                if(!T.contains(e.v) && cost[e.v] > ((WeightedEdge)e).weight){
                    cost[e.v] = ((WeightedEdge)e).weight;
                    parent[e.v] = u;
                }
            }
        }// end of while

        return new MST(startingVertex, parent, T, totalWeight);
    }

    //MST is an inner class in WeightedGraph
    public class MST extends Tree{
        private double totalWeight; //total weight of all edges in the tree

        public MST (int root, int[] parent, List<Integer> searchOrder, double totalWeight){
            super(root, parent, searchOrder);
            this.totalWeight = totalWeight;
        }

        public double getTotalWeight(){
            return totalWeight;
        }
    }

    //find single source the shortest paths
    public ShortestPathTree getShortestPath(int sourceVertex){
        //cost[v] stores the cost of the path from v to the source
        double[] cost = new double[getSize()];
        for(int i = 0; i < cost.length; i++){
            cost[i] = Double.POSITIVE_INFINITY; //initial cost set to infinity
        }
        cost[sourceVertex] = 0; //cost of source is 0

        //parent[v] stores previous vertex of v in the path
        int[] parent = new int[getSize()];
        parent[sourceVertex] = -1; //parent of source is set to -1

        //T stores the vertices whose path found so far
        List<Integer> T = new ArrayList<>();

        //expand T
        while(T.size() < getSize()){
            //find the smallest cost v in V - T
            int u = -1; //vertex to be determined
            double currentMinCost = Double.POSITIVE_INFINITY;
            for(int i = 0; i < getSize(); i++){
                if(!T.contains(i) && cost[i] < currentMinCost){
                    currentMinCost = cost[i];
                    u = i;
                }
            }

            T.add(u); //add a new vertex to T

            //adjust cost[v] for v that is adjacent to u and v in V - T
            for(Edge e: neighbors.get(u)){
                if(!T.contains(e.v) && cost[e.v] > cost[u] + ((WeightedEdge)e).weight){
                    cost[e.v] = cost[u] = ((WeightedEdge)e).weight;
                    parent[e.v] = u;
                }
            }
        }//end of while

        //create ShortestPathTree
        return new ShortestPathTree(sourceVertex, parent, T, cost);
    }

    //ShortestPathTree is an inner class in WeightedGraph
    public class ShortestPathTree extends Tree{
        private double[] cost; //cost[v] is the cost from v to source

        //construct a path
        public ShortestPathTree(int source, int[] parent, List<Integer> searchOrder, double[] cost){
            super(source, parent, searchOrder);
            this.cost = cost;
        }

        //return the cost for a path from the root to vertex v
        public double getCost(int v){
            return cost[v];
        }

        //prints paths from all vertices to the source
        public void printAllPaths(){
            System.out.println("All shortest paths from " + vertices.get(getRoot()) + " are: ");
            for(int i = 0; i < cost.length; i++){
                printPath(i); //print a path from i to the source
                System.out.println("(cost: " + cost[i] + ")"); //path cost
            }
        }
    }
}
