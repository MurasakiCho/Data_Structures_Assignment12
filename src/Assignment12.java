/*
Name: Briana O'Neal
Class: CS 3305/W01
Term: Fall 2022
Instructor: Sharon Perry
Assignment: 12-Part-1-Prims
*/
public class Assignment12 {
    public static void main(String[] args) {
        String[] vertices = {"A", "B", "C", "D", "E", "F", "G", "H"};

        Integer[][] edges = {
                {null, null, 4, null, null, 7, null, null},
                {null, null, null, null, 9, null, null, 3},
                {4, null, null, 3, null, 2, 9, null},
                {null, null, 3, null, 3, null, 7, null},
                {null, 9, null, 3, null, null, 2, 7},
                {7, null, 2, null, null, null, 8, null},
                {null, null, 9, 7, 2, 8, null, 3},
                {null, 3, null, null, 7, null, 3, null  }
        };

        System.out.println(edges.length);
        WeightedGraph<String> graph1 = new WeightedGraph<>(vertices, edges);
        WeightedGraph<String>.MST tree1 = graph1.getMinimumSpanningTree(0);
        System.out.println("Total weight is " + tree1.getTotalWeight());
        tree1.printTree();
        graph1.printWeightedEdges();
    }
}
