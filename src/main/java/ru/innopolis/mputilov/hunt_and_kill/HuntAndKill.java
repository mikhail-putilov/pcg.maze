package ru.innopolis.mputilov.hunt_and_kill;

import ru.innopolis.mputilov.Coord;

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
    @SuppressWarnings("FieldCanBeLocal")
    private final int size;
    private EnumSet<Type>[][] field;
    private boolean[][] visited;
    private SecureRandom sr = new SecureRandom();
    private int maxDepth = 0;
    private boolean[][] visited2;

    public HuntAndKill() {
        this(30);
    }

    public HuntAndKill(int size) {
        this.size = size;
        visited2 = new boolean[size][size];
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
                        visited[randomNextCoord.row][randomNextCoord.col] = true;
                        walkRoutine(currentCoord, randomNextCoord);
                        return randomNextCoord;
                    }
                }
            }
        }
        return null;
    }

    private void walkRoutine(Coord currentCoord, Coord randomNextCoord) {
        visited[randomNextCoord.row][randomNextCoord.col] = true;

        linkTwoCoords(currentCoord, randomNextCoord);
    }

    private void linkTwoCoords(Coord currentCoord, Coord randomNextCoord) {
        EnumSet<Type> currentType = field[currentCoord.row][currentCoord.col];
        Type directionForCurrentCoord = getDirectionForCurrentCoord(currentCoord, randomNextCoord);
        currentType.add(directionForCurrentCoord);
        EnumSet<Type> randomNextCoordType = field[randomNextCoord.row][randomNextCoord.col];
        randomNextCoordType.add(directionForCurrentCoord.getOpposite());
    }

    private Type getDirectionForCurrentCoord(Coord currentCoord, Coord randomNextCoord) {
        if (currentCoord.row == randomNextCoord.row) {
            if (currentCoord.col < randomNextCoord.col) {
                return Type.RIGHT_CONNECTED;
            } else if (currentCoord.col > randomNextCoord.col) {
                return Type.LEFT_CONNECTED;
            } else {
                throw new RuntimeException("");
            }
        } else if (currentCoord.col == randomNextCoord.col) {
            if (currentCoord.row < randomNextCoord.row) {
                return Type.BOTTOM_CONNECTED;
            } else if (currentCoord.row > randomNextCoord.row) {
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
        if (start.row != 0 && !visited[start.row - 1][start.col]) { //top
            unvisited.add(new Coord(start.row - 1, start.col));
        }
        if (start.row < size - 1 && !visited[start.row + 1][start.col]) { //BOTTOM
            unvisited.add(new Coord(start.row + 1, start.col));
        }
        if (start.col != 0 && !visited[start.row][start.col - 1]) { //left
            unvisited.add(new Coord(start.row, start.col - 1));
        }
        if (start.col < size - 1 && !visited[start.row][start.col + 1]) { //RIGHT
            unvisited.add(new Coord(start.row, start.col + 1));
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

    public int longestPath() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                countLongestPathFrom(i, j, 0);
                visited2 = new boolean[size][size];
            }
        }
        return maxDepth;
    }

    private void countLongestPathFrom(int i, int j, int depth) {
        visited2[i][j] = true;
        if (depth > maxDepth) {
            maxDepth = depth;
        }
        List<Coord> neighbors = getNeighbors(i, j);
        for (Coord neighbor : neighbors) {
            if (!visited2[neighbor.row][neighbor.col]) {
                countLongestPathFrom(neighbor.row, neighbor.col, depth + 1);
            }
        }
    }

    private List<Coord> getNeighbors(int row, int col) {
        List<Coord> neighbors = new ArrayList<>();
        //top
        if (row != 0 && (field[row - 1][col].contains(Type.BOTTOM_CONNECTED) || field[row][col].contains(Type.TOP_CONNECTED))) {
            neighbors.add(new Coord(row - 1, col));
        }
        //left
        if (col != 0 && (field[row][col - 1].contains(Type.RIGHT_CONNECTED) || field[row][col].contains(Type.LEFT_CONNECTED))) {
            neighbors.add(new Coord(row, col - 1));
        }
        //bottom
        if (row < size - 1 && (field[row][col].contains(Type.BOTTOM_CONNECTED) || field[row + 1][col].contains(Type.TOP_CONNECTED))) {
            neighbors.add(new Coord(row + 1, col));
        }
        //right
        if (col < size - 1 && (field[row][col].contains(Type.RIGHT_CONNECTED) || field[row][col + 1].contains(Type.LEFT_CONNECTED))) {
            neighbors.add(new Coord(row, col + 1));
        }
        return neighbors;
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
}
