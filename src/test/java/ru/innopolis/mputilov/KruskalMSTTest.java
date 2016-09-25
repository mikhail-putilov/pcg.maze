package ru.innopolis.mputilov;

import org.junit.Test;

import java.io.PrintWriter;

/**
 * Created by mputilov on 21/09/16.
 */
public class KruskalMSTTest {

    @Test
    public void name1() throws Exception {
        int i1 = 1000;
        try (PrintWriter deadEnds = new PrintWriter("dead_ends.kruskals.txt", "UTF-8")) {
            try (PrintWriter longestPath = new PrintWriter("longest_path.kruskals.txt", "UTF-8")) {
                for (int i = 0; i < i1; i++) {
                    KruskalMaze kruskalMaze = new KruskalMaze(20);
//            System.out.println(kruskalMaze.prettyPrint());
//            System.out.println(kruskalMaze.countDeadEnds());
//            System.out.println(kruskalMaze.longestPath());
                    deadEnds.println(kruskalMaze.countDeadEnds());
                    longestPath.println(kruskalMaze.longestPath());
                }
            }
        }
    }

    @Test
    public void name() throws Exception {
        KruskalMaze kruskalMaze = new KruskalMaze(10);
        System.out.println(kruskalMaze.generateTikz());
    }
}