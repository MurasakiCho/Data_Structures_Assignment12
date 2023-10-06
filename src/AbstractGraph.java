import java.util.*;

public abstract class AbstractGraph<V> implements Graph<V> {
    protected List<V> vertices = new ArrayList<>(); //store vertices
    protected List<List<Edge>> neighbors = new ArrayList<>(); //adjacency lists

    //empty graph constructor
    protected AbstractGraph(){
    }

    //construct graph with vertices and edges stored in arrays
    protected AbstractGraph(V[] vertices, int[][] edges){
        for(int i = 0; i < vertices.length; i++)
            addVertex(vertices[i]);

        createAdjacencyLists(edges, vertices.length);
    }

    //construct graph with vertices and edges stored in List
    protected AbstractGraph(List<V> vertices, List<Edge> edges){
        for(int i = 0; i <vertices.size(); i++)
            addVertex(vertices.get(i));

        createAdjacencyLists(edges, vertices.size());
    }

    //construct graph with for integer vertices 0, 1, 2 and edge list
    protected AbstractGraph(List<Edge> edges, int numberOfVertices){
        for(int i = 0; i < numberOfVertices; i++)
            addVertex((V)(Integer.valueOf(i))); //vertices is {0, 1, ...}

        createAdjacencyLists(edges, numberOfVertices);
    }

    //construct a graph from integer vertices 0, 1, and edge array
    protected AbstractGraph(int[][] edges, int numberOfVertices){
        for(int i = 0; i < numberOfVertices; i++)
            addVertex((V)(Integer.valueOf(i))); //vertices is {0, 1, ...}

        createAdjacencyLists(edges, numberOfVertices);
    }

    //create adjacency lists for each vertex
    private void createAdjacencyLists(int[][] edges, int numberOfVertices){
        for(int i = 0; i < edges.length; i++){
            addEdge(edges[i][0], edges[i][1]);
        }
    }

    //create adjacency lists for each vertex
    private void createAdjacencyLists(List<Edge> edges, int numberOfVertices){
        for(Edge edge: edges){
            addEdge(edge.u, edge.v);
        }
    }

    //return the number of vertices in the graph
    @Override
    public int getSize(){
        return vertices.size();
    }

    //return the vertices in the graph
    @Override
    public List<V> getVertices(){
        return vertices;
    }

    //return the object for the specified vertex
    @Override
    public V getVertex(int index){
        return vertices.get(index);
    }

    //return the index for the specified vertex object
    @Override
    public int getIndex(V v){
        return vertices.indexOf(v);
    }

    //return the neighbors of the specified vertex
    @Override
    public List<Integer> getNeighbors(int index){
        List<Integer> result = new ArrayList<>();
        for(Edge e: neighbors.get(index))
            result.add(e.v);

        return result;
    }

    //return the degree for a specified vertex
    @Override
    public int getDegree(int v){
        return neighbors.get(v).size();
    }

    //print the edges
    @Override
    public void printEdges(){
        for(int u = 0; u < neighbors.size(); u++){
            System.out.print(getVertex(u) + " (" + u + "): ");
            for(Edge e: neighbors.get(u)){
                System.out.print("(" + getVertex(e.u) + ", " + getVertex(e.v) + ") ");
            }
            System.out.println();
        }
    }

    //clear the graph
    @Override
    public void clear(){
        vertices.clear();
        neighbors.clear();
    }

    //add a vertex to the graph
    @Override
    public boolean addVertex(V vertex){
        if(!vertices.contains(vertex)){
            vertices.add(vertex);
            neighbors.add(new ArrayList<Edge>());
            return true;
        }
        else{
            return false;
        }
    }

    //add an edge to the graph
    protected boolean addEdge(Edge e){
        if(e.u < 0 || e.u > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.u);

        if(e.v < 0 || e.v > getSize() -1)
            throw new IllegalArgumentException("No such index: " + e.v);

        if(!neighbors.get(e.u).contains(e)) {
            neighbors.get(e.u).add(e);

            return true;
        }
        else{
            return false;
        }
    }

    //add an edge to the graph
    @Override
    public boolean addEdge(int u, int v){
        return addEdge(new Edge(u, v));
    }

    //inner class Edge
    public static class Edge{
        public int u; //starting vertex of the edge
        public int v; //ending vertex of the edge

        //construct an edge for (u, v)
        public Edge(int u, int v){
            this.u = u;
            this.v = v;
        }

        public boolean equals(Object o){
            return u == ((Edge)o).u && v == ((Edge)o).v;
        }
    }

    //obtain a DFS tree starting from vertex v using a non-recursive method
    @Override
    public Tree dfs(int v){
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[vertices.size()];
        for(int i = 0; i < parent.length; i++)
            parent[i] = -1; //initialize parent[i] to -1

        //mark visited vertices
        boolean[] isVisited = new boolean[vertices.size()];

        //recursively search
        dfs(v, parent, searchOrder, isVisited);

        //return a search tree
        return new Tree(v, parent, searchOrder);
    }

    //recursive method for DFS search
    private void dfs(int u, int[] parent, List<Integer> searchOrder, boolean[] isVisited){
        //store the visited vertex
        searchOrder.add(u);
        isVisited[u] = true; //vertex v visited

        for(Edge e: neighbors.get(u)){
            if(!isVisited[e.v]){
                parent[e.v] = u; //parent of vertex e.v is u
                dfs(e.v, parent, searchOrder, isVisited); //recursive search
            }
        }
    }

    //starting bfs search from vertex v
    @Override
    public Tree bfs(int v){
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[vertices.size()];
        for(int i = 0; i < parent.length; i++)
            parent[i] = -1; //initialize parent[i] to -1

        java.util.LinkedList<Integer> queue = new java.util.LinkedList<>(); //list used as a queue
        boolean[] isVisited = new boolean[vertices.size()];
        queue.offer(v); //enqueue v
        isVisited[v] = true; // mark it as visited

        while(!queue.isEmpty()){
            int u = queue.poll(); //dequeue to u
            searchOrder.add(u); //u search
            for(Edge e: neighbors.get(u)){
                if(!isVisited[e.v]){
                    queue.offer(e.v); //enqueue w
                    parent[e.v]  = u; //the parent of w is u
                    isVisited[e.v] = true; //mark it visited
                }
            }
        }
        return new Tree(v, parent, searchOrder);
    }

    //inner class Tree
    public class Tree{
        private int root; // root of the tree
        private int[] parent; //parent of each vertex
        private List<Integer> searchOrder; //store the search order

        //construct a tree with a root, parent and searchOrder
        public Tree(int root, int[] parent, List<Integer> searchOrder){
            this.root = root;
            this.parent = parent;
            this.searchOrder = searchOrder;
        }

        //return the root of the tree
        public int getRoot(){
            return root;
        }

        //return the parent of vertex v
        public int getParent(int v){
            return parent[v];
        }

        //return an array representing search order
        public List<Integer> getSearchOrder(){
            return searchOrder;
        }

        //return number of vertices found
        public int getNumberOfVerticesFound(){
            return searchOrder.size();
        }

        //return path of vertices from a vertex to the root
        public List<V> getPath(int index){
            ArrayList<V> path = new ArrayList<>();

            do{
                path.add(vertices.get(index));
                index = parent[index];
            }
            while(index != -1);

            return path;
        }

        //print a path from the root to vertex v
        public void printPath(int index){
            List<V> path = getPath(index);
            System.out.print("A path from " + vertices.get(root) + " to " + vertices.get(index) + ": ");
            for(int i = path.size() - 1; i >= 0; i--)
                System.out.print(path.get(i) + " ");
        }

        //print the whole tree
        public void printTree(){
            System.out.println("Root is: " + vertices.get(root));
            System.out.print("Edges: ");
            for(int i = 0; i < parent.length; i++){
                if(parent[i] != -1){
                    //display an edge
                    System.out.print("(" + vertices.get(parent[i]) + ", " + vertices.get(i) + ") ");
                }
            }
            System.out.println();
        }
    }
}
