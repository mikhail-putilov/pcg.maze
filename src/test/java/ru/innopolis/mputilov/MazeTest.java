package ru.innopolis.mputilov;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mputilov on 10.09.16.
 */
public class MazeTest {
    @Test
    public void simpleTest() throws Exception {
        Maze maze1 = new Maze();
        System.out.println(maze1.prettyPrint());
    }
}