package mg.algo.graph;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.math3.util.Combinations;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Generator {
    static final int MIN_VERTICES = 50;
    static final int MAX_VERTICES = 150;
    static final int MIN_EDGES = 0;

    public static void generateGraphsAndSave(int amount,String filename) {
        File file = initializeFile(filename);
        for (int i = 0; i < amount; i++) {
            int verticesCount = getRandomVerticesCount();
            int edgesCount = getRandomEdgesCount(verticesCount);
            Combinations edgesOptions = getRandomVerticesCombinations(verticesCount);
            int[][] links = generateRandomLinks(edgesOptions, edgesCount);
            try {
                saveIntoFile(file, verticesCount, links);
                System.out.println(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static File initializeFile(String filename) {
        File file = null;
        try {
            file = new File(filename);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static int getRandomVerticesCount() {
        return RandomUtils.nextInt(MIN_VERTICES,MAX_VERTICES+1);
    }

    public static int getRandomEdgesCount(int verticesCount) {
        return RandomUtils.nextInt(MIN_EDGES,(int) CombinatoricsUtils.binomialCoefficient(verticesCount,2)+1);
    }

    public static Combinations getRandomVerticesCombinations(int verticesCount) {
        return new Combinations(verticesCount, 2);
    }

    public static int[][] generateRandomLinks(Combinations edges, int edgesCount) {
        int[][] links = new int[edgesCount][];
        boolean[] mask = new boolean[(int) CombinatoricsUtils.binomialCoefficient(edges.getN(),edges.getK())];
        ArrayList<int[]> options = new ArrayList<>();
        edges.forEach(options::add);
        Collections.shuffle(options);
        for(int i = 0; i < edgesCount; i++) {
            links[i] = options.get(i);
        }
        return links;
    }

    public static void saveIntoFile(File file, int verticesCount, int[][] links) throws IOException {
        int edgesCount = links.length;
        FileWriter writer = new FileWriter(file,true);
        String globalInfos = String.format("sommets : %d\naretes : %d\n",verticesCount,edgesCount);
        writer.append(globalInfos);
        for(int[] link : links) {
            writer.append(String.format("(%d,%d)",link[0],link[1]));
        }
        writer.append("\n\n");
        writer.close();
    }
}
