package mg.algo;

import mg.algo.graph.Graph;

public class Main {

    public static void main(String[] args) {
        try {
            Graph[] graphs = Graph.parseFile("trace.txt");
            for(Graph g : graphs) {
                System.out.println(g.isHamiltonian());
            }
//            int links[][] = {
//                    {0,1},{0,3},{1,2},{1,3},{1,4},{2,4},{3,0}
//            };
//            int e = 6;
//            int v = 5;
//            Graph g = new Graph(v,e,links);
//            System.out.println(g.isHamiltonian());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
