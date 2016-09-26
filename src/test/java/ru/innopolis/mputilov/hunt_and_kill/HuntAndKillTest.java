package ru.innopolis.mputilov.hunt_and_kill;

import org.junit.Test;
import ru.innopolis.mputilov.BinaryGrowingTreeMaze;
import ru.innopolis.mputilov.GrowingTreeMaze;
import ru.innopolis.mputilov.KruskalMaze;

import java.io.PrintWriter;

/**
 * Created by mputilov on 13.09.16.
 */
public class HuntAndKillTest {
    @Test
    public void simpleTest() throws Exception {
        int i1 = 1000;
        try (PrintWriter deadEnds = new PrintWriter("dead_ends.hunt.and.kill.txt", "UTF-8")) {
            try (PrintWriter longestPath = new PrintWriter("longest_path.hunt.and.kill.txt", "UTF-8")) {
                for (int i = 0; i < i1; i++) {
                    HuntAndKill maze = new HuntAndKill(20);
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
    public void presentation() throws Exception {
        KruskalMaze maze = new KruskalMaze(50);
        System.out.println(maze.generateTikz());

    }
}