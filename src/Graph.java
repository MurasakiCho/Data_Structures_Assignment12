public interface Graph<V> {
    //return the number of vertices in the graph
    public int getSize();

    //return the vertices in the graph
    public java.util.List<V> getVertices();

    //return the object for the specified vertex index
    public V getVertex(int index);

    //return the index for the specified vertex object
    public int getIndex(V v);

    //return the neighbors of vertex with the specified index
    public java.util.List<Integer> getNeighbors(int index);

    //return the degree for a specified vertex
    public int getDegree(int v);

    //print the edges
    public void printEdges();

    //clear the graph
    public void clear();

    //add a vertex to the graph
    public boolean addVertex(V vertex);

    //add an edge to the graph
    public boolean addEdge(int u, int v);

    //obtain a depth-first search tree starting from v
    public AbstractGraph<V>.Tree dfs(int v);

    //obtain a breadth-first searcht ree starting from v
    public AbstractGraph<V>.Tree bfs(int v);
}
