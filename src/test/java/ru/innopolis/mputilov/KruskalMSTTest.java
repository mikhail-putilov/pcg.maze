package ru.innopolis.mputilov;

import org.junit.Test;

/**
 * Created by mputilov on 21/09/16.
 */
public class KruskalMSTTest {

    @Test
    public void name() throws Exception {
        KruskalMaze kruskalMaze = new KruskalMaze(3);
        System.out.println(kruskalMaze.prettyPrint());
        System.out.println(kruskalMaze.countDeadEnds());
        System.out.println(kruskalMaze.longestPath());
    }

}