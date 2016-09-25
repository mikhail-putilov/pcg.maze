package ru.innopolis.mputilov;

import org.junit.Test;

import java.io.PrintWriter;

/**
 * Created by mputilov on 11.09.16.
 */
public class GrowingTreeMazeTest {
    @Test
    public void name1() throws Exception {
        int i1 = 1000;
        try (PrintWriter deadEnds = new PrintWriter("dead_ends.growing.tree.txt", "UTF-8")) {
            try (PrintWriter longestPath = new PrintWriter("longest_path.growing.tree.txt", "UTF-8")) {
                for (int i = 0; i < i1; i++) {
                    GrowingTreeMaze maze = new GrowingTreeMaze(20);
//            System.out.println(maze.prettyPrint());
//            System.out.println(kruskalMaze.countDeadEnds());
//            System.out.println(kruskalMaze.longestPath());
                    deadEnds.println(maze.countDeadEnds());
                    longestPath.println(maze.longestPath());
                }
            }
        }
    }

    @Test
    public void name() throws Exception {
        BinaryGrowingTreeMaze binaryGrowingTreeMaze = new BinaryGrowingTreeMaze(10);
        System.out.println(binaryGrowingTreeMaze.generateTikz());
    }
}