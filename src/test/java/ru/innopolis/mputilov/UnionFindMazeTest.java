package ru.innopolis.mputilov;

import org.junit.Test;

/**
 * Created by mputilov on 10.09.16.
 */
public class UnionFindMazeTest {
    @Test
    public void simpleTest() throws Exception {
        UnionFindMaze unionFindMaze1 = new UnionFindMaze();
        System.out.println(unionFindMaze1.prettyPrint());
    }
}