package ru.innopolis.mputilov.hunt_and_kill;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mputilov on 13.09.16.
 */
public class HuntAndKill {
    private static final String VERTICAL_PATH = "| ";
    private static final String EMPTY_VERTICAL_PATH = "  ";
    private static final String HORIZONTAL_PATH = "—";
    private static final String EMPTY_HORIZONTAL_PATH = " ";
    private static final String ROOM = "◊";
    private EnumSet<Type>[][] field;
    private boolean[][] visited;
    private SecureRandom sr = new SecureRandom();
    @SuppressWarnings("FieldCanBeLocal")
    private int size = 30;

    public HuntAndKill() {
        this(30);
    }

    public HuntAndKill(int size) {
        this.size = size;
        //noinspection unchecked
        field = new EnumSet[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = EnumSet.noneOf(Type.class);
            }
        }
        visited = new boolean[size][size];

        int i = sr.nextInt(size);
        int j = sr.nextInt(size);
        visited[i][j] = true;
        Coord nextCoord = new Coord(i, j);
        while (true) {
            walk(nextCoord);
            nextCoord = hunt();
            if (nextCoord == null) {
                return;
            }
        }
    }

    public int countDeadEnds() {
        int counter = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j].size() == 1) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private void walk(Coord start) {
        Coord currentCoord = start;
        List<Coord> unvisitedNeighbors = getAllUnvisitedNeighbors(start);
        while (!unvisitedNeighbors.isEmpty()) {
            Coord randomNextCoord = getRandomFromList(unvisitedNeighbors);

            walkRoutine(currentCoord, randomNextCoord);

            currentCoord = randomNextCoord;
            unvisitedNeighbors = getAllUnvisitedNeighbors(randomNextCoord);
        }
    }

    private Coord hunt() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (visited[i][j]) {
                    Coord currentCoord = new Coord(i, j);
                    List<Coord> allUnvisitedNeighbors = getAllUnvisitedNeighbors(currentCoord);
                    if (!allUnvisitedNeighbors.isEmpty()) {
                        Coord randomNextCoord = getRandomFromList(allUnvisitedNeighbors);
                        visited[randomNextCoord.i][randomNextCoord.j] = true;
                        walkRoutine(currentCoord, randomNextCoord);
                        return randomNextCoord;
                    }
                }
            }
        }
        return null;
    }

    private void walkRoutine(Coord currentCoord, Coord randomNextCoord) {
        visited[randomNextCoord.i][randomNextCoord.j] = true;

        linkTwoCoords(currentCoord, randomNextCoord);
    }

    private void linkTwoCoords(Coord currentCoord, Coord randomNextCoord) {
        EnumSet<Type> currentType = field[currentCoord.i][currentCoord.j];
        Type directionForCurrentCoord = getDirectionForCurrentCoord(currentCoord, randomNextCoord);
        currentType.add(directionForCurrentCoord);
        EnumSet<Type> randomNextCoordType = field[randomNextCoord.i][randomNextCoord.j];
        randomNextCoordType.add(directionForCurrentCoord.getOpposite());
    }

    private Type getDirectionForCurrentCoord(Coord currentCoord, Coord randomNextCoord) {
        if (currentCoord.i == randomNextCoord.i) {
            if (currentCoord.j < randomNextCoord.j) {
                return Type.RIGHT_CONNECTED;
            } else if (currentCoord.j > randomNextCoord.j) {
                return Type.LEFT_CONNECTED;
            } else {
                throw new RuntimeException("");
            }
        } else if (currentCoord.j == randomNextCoord.j) {
            if (currentCoord.i < randomNextCoord.i) {
                return Type.BOTTOM_CONNECTED;
            } else if (currentCoord.i > randomNextCoord.i) {
                return Type.TOP_CONNECTED;
            } else {
                throw new RuntimeException("");
            }
        }
        throw new RuntimeException("");
    }

    private <T> T getRandomFromList(List<T> list) {
        return list.get(sr.nextInt(list.size()));
    }

    private List<Coord> getAllUnvisitedNeighbors(Coord start) {
        List<Coord> unvisited = new ArrayList<>();
        if (start.i != 0 && !visited[start.i - 1][start.j]) { //top
            unvisited.add(new Coord(start.i - 1, start.j));
        }
        if (start.i < size - 1 && !visited[start.i + 1][start.j]) { //BOTTOM
            unvisited.add(new Coord(start.i + 1, start.j));
        }
        if (start.j != 0 && !visited[start.i][start.j - 1]) { //left
            unvisited.add(new Coord(start.i, start.j - 1));
        }
        if (start.j < size - 1 && !visited[start.i][start.j + 1]) { //RIGHT
            unvisited.add(new Coord(start.i, start.j + 1));
        }
        return unvisited;
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
            EnumSet<Type> types = field[row][column];
            sb.append(types.contains(Type.BOTTOM_CONNECTED) ? VERTICAL_PATH : EMPTY_VERTICAL_PATH);
        }
        sb.append("\n");
    }

    private void appendHorizontalRow(StringBuilder sb, int row) {
        for (int column = 0; column < size - 1; column++) {
            sb.append(ROOM);
            sb.append(field[row][column].contains(Type.RIGHT_CONNECTED) ? HORIZONTAL_PATH : EMPTY_HORIZONTAL_PATH);
        }
        sb.append(ROOM);
        sb.append('\n');
    }

    private enum Type {
        LEFT_CONNECTED, RIGHT_CONNECTED, BOTTOM_CONNECTED, TOP_CONNECTED;

        Type getOpposite() {
            switch (this) {
                case LEFT_CONNECTED:
                    return RIGHT_CONNECTED;
                case RIGHT_CONNECTED:
                    return LEFT_CONNECTED;
                case BOTTOM_CONNECTED:
                    return TOP_CONNECTED;
                case TOP_CONNECTED:
                    return BOTTOM_CONNECTED;
                default:
                    throw new RuntimeException("");
            }
        }
    }

    private static class Coord {
        int i;
        int j;

        Coord(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
}
