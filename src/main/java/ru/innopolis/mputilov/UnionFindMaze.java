package ru.innopolis.mputilov;

import ru.innopolis.mputilov.union_find.QuickUnionUF;

import java.security.SecureRandom;
import java.util.*;

public class UnionFindMaze {
    private static final String VERTICAL_PATH = "| ";
    private static final String EMPTY_VERTICAL_PATH = "  ";
    private static final String HORIZONTAL_PATH = "—";
    private static final String EMPTY_HORIZONTAL_PATH = " ";
    private static final String ROOM = "◊";
    private final SecureRandom sr;
    private final QuickUnionUF quickUnion;
    private final List<Integer> alreadySeenCells = new ArrayList<>();
    private final Map<Integer, List<Integer>> paths = new HashMap<>();
    private final int size;

    public UnionFindMaze() {
        this(30);
    }

    public UnionFindMaze(int size) {
        this.size = size;
        quickUnion = new QuickUnionUF(size * size);
        sr = new SecureRandom();
        alreadySeenCells.add(0);
        for (int i = 0; i < size * size - 1; i++) {
            doIteration();
        }
    }

    private Integer getAnyAlreadySeenCells() {
        int index = sr.nextInt(alreadySeenCells.size());
        return alreadySeenCells.get(index);
    }

    private void doIteration() {
        while (true) {
            Integer randomTarget = getAnyAlreadySeenCells();
            int neighbor = getAnyNeighbor(randomTarget);

            if (!quickUnion.connected(randomTarget, neighbor)) {
                quickUnion.union(randomTarget, neighbor);

                paths.putIfAbsent(randomTarget, new ArrayList<>());
                paths.get(randomTarget).add(neighbor);

                alreadySeenCells.add(neighbor);
                break;
            }
        }
    }

    private int getAnyNeighbor(int target) {
        int neighbor;
        while (true) {
            switch (sr.nextInt(4)) {
                case 0:
                    neighbor = target - size;
                    if (neighbor < 0)
                        break;
                    return neighbor;
                case 1:
                    if ((target + 1) % size == 0) {
                        break;
                    }
                    return target + 1;
                case 2:
                    if (target % size == 0) {
                        break;
                    }
                    return target - 1;
                case 3:
                    neighbor = target + size;
                    if (neighbor > size * size - 1)
                        break;
                    return neighbor;
                default:
                    break;
            }
        }
    }

    public String prettyPrint() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            appendHorizontalRow(sb, i);
            appendVerticalPaths(sb, i);
        }
        appendHorizontalRow(sb, size - 1);
        return sb.toString();
    }

    private void appendVerticalPaths(StringBuilder sb, int row) {
        for (int column = 0; column < size; column++) {
            int current = getAbsoluteIndex(row, column);
            @SuppressWarnings("unchecked")
            List<Integer> connected = paths.getOrDefault(current, Collections.EMPTY_LIST);
            int next = getAbsoluteIndex(row + 1, column);
            @SuppressWarnings("unchecked")
            List<Integer> connected2 = paths.getOrDefault(next, Collections.EMPTY_LIST);

            sb.append(connected.contains(next) || connected2.contains(current) ? VERTICAL_PATH : EMPTY_VERTICAL_PATH);
        }
        sb.append("\n");
    }

    private void appendHorizontalRow(StringBuilder sb, int row) {
        for (int column = 0; column < size - 1; column++) {
            sb.append(ROOM);
            int current = getAbsoluteIndex(row, column);
            @SuppressWarnings("unchecked")
            List<Integer> connected = paths.getOrDefault(current, Collections.EMPTY_LIST);
            int next = getAbsoluteIndex(row, column + 1);
            @SuppressWarnings("unchecked")
            List<Integer> connected2 = paths.getOrDefault(next, Collections.EMPTY_LIST);
            sb.append(connected.contains(next) || connected2.contains(current) ? HORIZONTAL_PATH : EMPTY_HORIZONTAL_PATH);
        }
        sb.append(ROOM);
        sb.append('\n');
    }

    private int getAbsoluteIndex(int row, int column) {
        return size * row + column;
    }
}
