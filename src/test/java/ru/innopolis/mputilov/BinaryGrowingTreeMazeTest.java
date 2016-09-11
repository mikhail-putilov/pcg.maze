package ru.innopolis.mputilov;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mputilov on 11.09.16.
 */
public class BinaryGrowingTreeMazeTest {
    @Test
    public void simpleTest() throws Exception {
        BinaryGrowingTreeMaze binaryGrowingTreeMaze = new BinaryGrowingTreeMaze();
        System.out.println(binaryGrowingTreeMaze.prettyPrint());
    }
}