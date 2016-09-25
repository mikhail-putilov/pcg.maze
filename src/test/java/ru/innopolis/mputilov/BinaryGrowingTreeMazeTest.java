package ru.innopolis.mputilov;

import org.junit.Test;

import java.io.PrintWriter;

import static org.junit.Assert.*;

/**
 * Created by mputilov on 11.09.16.
 */
public class BinaryGrowingTreeMazeTest {
    @Test
    public void simpleTest() throws Exception {
        int i1 = 1000;
        try (PrintWriter deadEnds = new PrintWriter("dead_ends.binary.growing.tree.txt", "UTF-8")) {
            try (PrintWriter longestPath = new PrintWriter("longest_path.binary.growing.tree.txt", "UTF-8")) {
                for (int i = 0; i < i1; i++) {
                    BinaryGrowingTreeMaze maze = new BinaryGrowingTreeMaze(20);
//            System.out.println(kruskalMaze.prettyPrint());
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