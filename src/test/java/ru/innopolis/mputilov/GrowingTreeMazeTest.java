package ru.innopolis.mputilov;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mputilov on 11.09.16.
 */
public class GrowingTreeMazeTest {
    @Test
    public void simpleTest() throws Exception {
        GrowingTreeMaze growingTreeMaze = new GrowingTreeMaze(30);
        System.out.println(growingTreeMaze.prettyPrint());
    }
}