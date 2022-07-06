package mg.algo.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
        for(int i = 0; i < verticesCount; i++) {
            adjacentMartix[i][i] = 1;
        }
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
        Arrays.sort(links,new java.util.Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                if(a[0] != b[0]) {
                    return Integer.compare(a[0],b[0]);
                } else {
                    return Integer.compare(a[1],b[1]);
                }
            }
        });
        return links;
    }

    private static int parseEdgesCount(String line) {
        return Integer.parseInt(line.split(" : ")[1]);
    }

    private static int parseVerticesCount(String line) {
        return Integer.parseInt(line.split(" : ")[1]);
    }

    public boolean Ore() {
        for(int i = 0; i < verticesCount; i++) {
            for(int j = 0; j < verticesCount; j++) {
                if(adjacentMartix[i][j] == 0) {
                    if(degrees(i)+degrees(j) < verticesCount) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean Posa() {
        int[] allDegrees = new int[verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            allDegrees[i] = degrees(i);
        }
        int max = (verticesCount - 1) / 2;

        for(int k = 1; k <= max; k++) {
            int count = 0;
            for(int degrees : allDegrees) {
                if(degrees < k) {
                    count++;
                }
            }
            if(count >= k) {
                return false;
            }
        }

        float maxF = ((float)verticesCount - 1) / 2;
        int count = 0;
        for(int degrees : allDegrees) {
            if(degrees < maxF) {
                count++;
            }
        }
        if(count > maxF) {
            return false;
        }

        return true;
    }

    public int degrees(int index) {
        int result = -1;
        for (int element : adjacentMartix[index]) {
            if(element == 1) {
                result++;
            }
        }
        return result;
    }

    public boolean isHamiltonian() {
        if(Ore() || Posa()) {
            return true;
        }
        if(!enoughEdges()) {
            return false;
        }
        return HAM();
//        int[] path = new int[verticesCount];
//        int[] included = new int[verticesCount];
//        for (int i = 0; i < verticesCount; i++) {
//            path[i] = -1;
//        }
//
//        /* Let us put vertex 0 as the first vertex in the path.
//           If there is a Hamiltonian Cycle, then the path can be
//           started from any point of the cycle as the graph is
//           undirected */
//        path[0] = 0;
//
//        return hamCycleUtil(path,included, 1);
    }

    private boolean enoughEdges() {
        for(int[] row: adjacentMartix) {
            int counter = -1;
            for(int element:row) {
                if(element == 1) {
                    counter++;
                }
            }
            if(counter <= 1) {
                return false;
            }
        }
        return true;
    }

    public boolean HAM() {
        ArrayList<Integer> P = new ArrayList<>();
        int k = 0;
        P.add(links[0][0]);
        P.add(links[0][1]);
        // L1 begin
        ArrayList<Integer> deltaQ = new ArrayList<>();
        deltaQ.add(0);
        float d = 2*(float)edgesCount/verticesCount;
        ArrayList<ArrayList<Integer>> Q = new ArrayList<>();
        int s = 0;
        int t = 0;
        while(deltaQ.get(s) <Math.ceil((Math.log(verticesCount)/(Math.log(d) - Math.log(Math.log(d))))) + 2) {
            boolean skip = false;
            int deltaQs = deltaQ.get(s);
            Q.add((ArrayList<Integer>)P.clone());
            ArrayList<Integer> Qs = Q.get(s);
            for (int i = 0; i < Qs.size(); i += Qs.size() - 1) {
                ArrayList<int[]> p = externalEdges(Qs, i);
                for (int j = 0; j < p.size(); j++) {
                    if (!Qs.contains(p.get(j)[1])) {
                        i = i == 0 ? 0 : Qs.size();
                        P.add(i,p.get(j)[1]);
                        k++;
                        skip = true;
                        if(deltaQ.size() == s+1) deltaQ.add(deltaQs);
                        break;
                    } else if (p.get(j)[1] == Qs.get((i == 0 ? Qs.size() - 1 : 0))) {
                        ArrayList<Integer> C = Qs;
                        int i_temp = i == 0 ? 0 : Qs.size();
                        C.add(i_temp,Qs.get((i == 0 ? Qs.size() - 1 : 0)));
                        if (hamiltonian(C)) {
                            return true;
                        } else {
                            Integer v_Qs = null;
                            int low_neighbor = -1;

                            for (Integer vertex : Qs) {
                                for (int n_index = 0; n_index < adjacentMartix[vertex].length; n_index++) {
                                    if (!C.contains(n_index) && adjacentMartix[vertex][n_index] == 1) {
                                        v_Qs = vertex;
                                        low_neighbor = n_index;
                                        break;
                                    }
                                }
                                if(low_neighbor != -1) break;
                            }
                            int index_1 = Qs.indexOf(v_Qs) == 0 ? Qs.size() - 2 : Qs.indexOf(v_Qs) - 1;
                            int index_2 = Qs.indexOf(v_Qs) == Qs.size() - 1 ? 1 : Qs.indexOf(v_Qs) + 1;
                            int to_replace = Qs.get(index_1) < Qs.get(index_2) ? index_1 :index_2;
                            C.remove(to_replace);
                            C.add(to_replace,low_neighbor);
                            P = C;
                            k++;
                            skip = true;
                            if(deltaQ.size() == s+1) deltaQ.add(deltaQs);
                            break;
                        }
                    } else {
                        t++;
                        Q.add(PosaRotation(Qs,Qs.indexOf(p.get(j)[1]),i));
                        deltaQ.add(deltaQs+1);
                    }
                }
                if(skip) break;
            }
            s++;
        }
        return false;
    }

    private ArrayList<Integer> PosaRotation(ArrayList<Integer> path,int i,int k) {
        int index = k == 0 ? i-1 : i+1;
        ArrayList<Integer> sub1;
        ArrayList<Integer> sub2;
        if(index < i) {
            sub1 = new ArrayList<>(path.subList(0,index+1));
            sub2 = new ArrayList<>(path.subList(index+1,path.size()));
            Collections.reverse(sub1);
            sub1.addAll(sub2);
            return sub1;
        } else {
            sub1 = new ArrayList<>(path.subList(index,path.size()));
            sub2 = new ArrayList<>(path.subList(0,index));
            Collections.reverse(sub1);
            sub2.addAll(sub1);
            return sub2;
        }
    }

    private ArrayList<int[]> externalEdges(ArrayList<Integer> path,int index) {
        int exclude = index == 0 ? path.get(index+1) : path.get(index-1);
        ArrayList<int[]> result = new ArrayList<>();
        for(int[] edge : links) {
            if((edge[0] == path.get(index) && edge[1] != exclude) || (edge[1] == path.get(index) && edge[0] != exclude)) {
                if(edge[1] == path.get(index)) {
                    int t = edge[0];
                    edge[0] = edge[1];
                    edge[1] = t;
                }
                result.add(edge);
            }
        }
        return result;
    }

    private boolean hamiltonian(ArrayList<Integer> C) {
        if(C.size() == verticesCount + 1 && (C.get(0).equals(C.get(C.size()-1)))) {
            return true;
        }
        return false;
    }

//    private boolean isSafe(int v,int[] path,int[] included,int pos)
//    {
//        /* Check if this vertex is an adjacent vertex of
//           the previously added vertex. */
//        if (adjacentMartix[path[pos - 1]][v] == 0)
//            return false;
//
//        /* Check if the vertex has already been included.
//           This step can be optimized by creating an array
//           of size V */
//        if (included[v] == 1)
//            return false;
//
//        return true;
//    }

    /* A recursive utility function to solve hamiltonian
       cycle problem */
//    private boolean hamCycleUtil(int[] path,int[] included,int pos)
//    {
//        /* base case: If all vertices are included in
//           Hamiltonian Cycle */
//        if (pos == verticesCount)
//        {
//            // And if there is an edge from the last included
//            // vertex to the first vertex
//            if (adjacentMartix[path[pos - 1]][path[0]] == 1) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//
//        // Try different vertices as a next candidate in
//        // Hamiltonian Cycle. We don't try for 0 as we
//        // included 0 as starting point in hamCycle()
//        for (int v = 1; v < verticesCount; v++)
//        {
//            /* Check if this vertex can be added to Hamiltonian
//               Cycle */
//            if (isSafe(v,path,included,pos))
//            {
//                path[pos] = v;
//                included[v] = 1;
//
//                /* recur to construct rest of the path */
//                if (hamCycleUtil(path,included,pos + 1)) {
//                    return true;
//                }
//
//                /* If adding vertex v doesn't lead to a solution,
//                   then remove it */
//                path[pos] = -1;
//                included[v] = 0;
//            }
//        }
//
//        /* If no vertex can be added to Hamiltonian Cycle
//           constructed so far, then return false */
//        return false;
//    }


}
