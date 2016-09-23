package ru.innopolis.mputilov.hunt_and_kill;

import org.junit.Test;

/**
 * Created by mputilov on 13.09.16.
 */
public class HuntAndKillTest {
    @Test
    public void simpleTest() throws Exception {
        HuntAndKill huntAndKill = new HuntAndKill(10);
        System.out.println(huntAndKill.prettyPrint());
        System.out.println(huntAndKill.countDeadEnds());
        System.out.println(huntAndKill.longestPath());
        System.out.println(huntAndKill.generateTikz());
        // number dead ends, longest pathes, shortest paths, longest dead end
    }
}