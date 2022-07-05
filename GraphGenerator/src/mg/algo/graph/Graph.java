package mg.algo.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Graph {
    private int verticesCount;
    private int edgesCount;
    private int[][] links;
    private int[][] adjacentMartix;

    public Graph(int verticesCount, int edgesCount, int[][] links) {
        this.verticesCount = verticesCount;
        this.edgesCount = edgesCount;
        this.links = links;
        initAdjacentMatrix();
    }

    private void initAdjacentMatrix() {
        adjacentMartix = new int[verticesCount][verticesCount];
        Arrays.stream(links).forEach(couple -> {
            adjacentMartix[couple[0]][couple[1]] = 1;
            adjacentMartix[couple[1]][couple[0]] = 1;
        });
    }

    public static Graph[] parseFile(String filepath) throws FileNotFoundException {
        ArrayList<Graph> graphs = new ArrayList<>();
        File file = new File(filepath);
        Scanner render = new Scanner(file);
        while(render.hasNextLine()) {
            int verticesCount = parseVerticesCount(render.nextLine());
            int edgesCount = parseEdgesCount(render.nextLine());
            int[][] links = parseLinks(render.nextLine(),edgesCount);
            graphs.add(new Graph(verticesCount,edgesCount,links));
            if (render.hasNextLine()) render.nextLine();
        }
        return graphs.toArray(Graph[]::new);
    }

    private static int[][] parseLinks(String line,int edgesCount) {
        int[][] links = new int[edgesCount][];
        String[] elements = line.split("\\(");
        for(int i = 0; i < links.length; i++) {
            links[i] = new int[2];
            String values = elements[i+1].substring(0,elements[i+1].length() - 1);
            links[i][0] = Integer.parseInt(values.split(",")[0]);
            links[i][1] = Integer.parseInt(values.split(",")[1]);
        }
        return links;
    }

    private static int parseEdgesCount(String line) {
        return Integer.parseInt(line.split(" : ")[1]);
    }

    private static int parseVerticesCount(String line) {
        return Integer.parseInt(line.split(" : ")[1]);
    }

    public boolean isHamiltonian() {
        int[] path = new int[verticesCount];
        int[] included = new int[verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            path[i] = -1;
        }

        /* Let us put vertex 0 as the first vertex in the path.
           If there is a Hamiltonian Cycle, then the path can be
           started from any point of the cycle as the graph is
           undirected */
        path[0] = 0;

        return hamCycleUtil(path,included, 1);
    }

    private boolean isSafe(int v,int[] path,int[] included,int pos)
    {
        /* Check if this vertex is an adjacent vertex of
           the previously added vertex. */
        if (adjacentMartix[path[pos - 1]][v] == 0)
            return false;

        /* Check if the vertex has already been included.
           This step can be optimized by creating an array
           of size V */
        if (included[v] == 1)
            return false;

        return true;
    }

    /* A recursive utility function to solve hamiltonian
       cycle problem */
    private boolean hamCycleUtil(int[] path,int[] included,int pos)
    {
        /* base case: If all vertices are included in
           Hamiltonian Cycle */
        if (pos == verticesCount)
        {
            // And if there is an edge from the last included
            // vertex to the first vertex
            if (adjacentMartix[path[pos - 1]][path[0]] == 1) {
                return true;
            } else {
                return false;
            }
        }

        // Try different vertices as a next candidate in
        // Hamiltonian Cycle. We don't try for 0 as we
        // included 0 as starting point in hamCycle()
        for (int v = 1; v < verticesCount; v++)
        {
            /* Check if this vertex can be added to Hamiltonian
               Cycle */
            if (isSafe(v,path,included,pos))
            {
                path[pos] = v;
                included[v] = 1;

                /* recur to construct rest of the path */
                if (hamCycleUtil(path,included,pos + 1)) {
                    return true;
                }

                /* If adding vertex v doesn't lead to a solution,
                   then remove it */
                path[pos] = -1;
                included[v] = 0;
            }
        }

        /* If no vertex can be added to Hamiltonian Cycle
           constructed so far, then return false */
        return false;
    }


}
