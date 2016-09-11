package ru.innopolis.mputilov;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mputilov on 11.09.16.
 */
public class BinaryGrowingTreeMaze extends GrowingTreeMaze {
    @Override
    protected void doIteration() {
        while (!activeSet.isEmpty()) {
            Integer randomTarget = getAnyCellFromActiveSet();
            List<Integer> unvisitedNeighbors = getUnvisitedNeighbors(randomTarget);
            if (unvisitedNeighbors.isEmpty()) {
                removeFromActiveSet(randomTarget);
                continue;
            }
            Integer neighbor = unvisitedNeighbors.get(unvisitedNeighbors.size() - 1);
            if (unvisitedNeighbors.size() > 1) {
                Integer neighbor2 = unvisitedNeighbors.get(unvisitedNeighbors.size() - 2);
                activeSet.add(neighbor2);
                alreadySeenCells[neighbor2] = true;
                paths.putIfAbsent(randomTarget, new ArrayList<>());
                paths.get(randomTarget).add(neighbor2);
            }
            activeSet.add(neighbor);
            alreadySeenCells[neighbor] = true;
            paths.putIfAbsent(randomTarget, new ArrayList<>());
            paths.get(randomTarget).add(neighbor);
        }
    }
}
