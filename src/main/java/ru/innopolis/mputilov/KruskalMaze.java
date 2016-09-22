package ru.innopolis.mputilov;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import static ru.innopolis.mputilov.GrowingTreeMaze.*;

/**
 * Created by mputilov on 21/09/16.
 */
public class KruskalMaze {

    private final int SIZE;
    private final List<List<EnumSet<State>>> field;
    private int maxDepth = 0;
    private boolean[][] visited;

    public KruskalMaze() {
        this(30);
    }

    public KruskalMaze(int size) {
        SIZE = size;
        visited = new boolean[SIZE][SIZE];
        EdgeWeightedGraph graph = new EdgeWeightedGraph(SIZE * SIZE);
        Random random = new Random();
        for (int i = 0; i < SIZE - 1; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                double w1 = random.nextDouble();
                graph.addEdge(new Edge(getAbsoluteIndex(i, j), getAbsoluteIndex(i + 1, j), w1));
                double w2 = random.nextDouble();

                graph.addEdge(new Edge(getAbsoluteIndex(i, j), getAbsoluteIndex(i, j + 1), w2));
            }
        }
        for (int i = 0; i < SIZE - 1; i++) {
            double w1 = random.nextDouble();
            double w2 = random.nextDouble();
            Edge e1 = new Edge(getAbsoluteIndex(i, SIZE - 1), getAbsoluteIndex(i + 1, SIZE - 1), w1);
            Edge e3 = new Edge(getAbsoluteIndex(SIZE - 1, i), getAbsoluteIndex(SIZE - 1, i + 1), w2);
            graph.addEdge(e1);
            graph.addEdge(e3);
        }
        KruskalMST kruskalMST = new KruskalMST(graph);
        Iterable<Edge> edges = kruskalMST.edges();
        field = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            ArrayList<EnumSet<State>> newList = new ArrayList<>(SIZE);
            field.add(newList);
            for (int j = 0; j < SIZE; j++) {
                newList.add(EnumSet.noneOf(State.class));
            }
        }
        for (Edge edge : edges) {
            int a = Math.min(edge.either(), edge.other(edge.either()));
            int b = Math.max(edge.either(), edge.other(edge.either()));
            int a_row = a / SIZE;
            int a_col = a % SIZE;
            int b_row = b / SIZE;
            int b_col = b % SIZE;
            if (a_row == b_row) {
                int same_row = a_row = b_row;
                if (a_col + 1 == b_col) {
                    field.get(same_row).get(a_col).add(State.RIGHT);
                } else if (b_col + 1 == a_col) {
                    field.get(same_row).get(b_col).add(State.RIGHT);
                } else {
                    throw new RuntimeException();
                }
            } else if (a_col == b_col) {
                int same_col = a_col = b_col;
                if (a_row + 1 == b_row) {
                    field.get(a_row).get(same_col).add(State.BOTTOM);
                } else if (b_row + 1 == a_row) {
                    field.get(b_row).get(same_col).add(State.BOTTOM);
                } else {
                    throw new RuntimeException();
                }
            }
        }
    }

    public int countDeadEnds() {
        int counter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (field.get(i).get(j).size() == 1) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private int getAbsoluteIndex(int row, int column) {
        return SIZE * row + column;
    }

    private void appendVerticalPaths(StringBuilder sb, int row, List<List<EnumSet<State>>> field) {
        for (int column = 0; column < SIZE; column++) {
            sb.append(field.get(row).get(column).contains(State.BOTTOM) ? VERTICAL_PATH : EMPTY_VERTICAL_PATH);
        }
        sb.append("\n");
    }

    private void appendHorizontalRow(StringBuilder sb, int row, List<List<EnumSet<State>>> field) {
        for (int column = 0; column < SIZE - 1; column++) {
            sb.append(ROOM);
            sb.append(field.get(row).get(column).contains(State.RIGHT) ? HORIZONTAL_PATH : EMPTY_HORIZONTAL_PATH);
        }
        sb.append(ROOM);
        sb.append('\n');
    }

    public String prettyPrint() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE - 1; i++) {
            appendHorizontalRow(sb, i, field);
            appendVerticalPaths(sb, i, field);
        }
        appendHorizontalRow(sb, SIZE - 1, field);
        return sb.toString();
    }

    public int longestPath() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                countLongestPathFrom(i, j, 0);
            }
        }
        return maxDepth;
    }

    private void countLongestPathFrom(int i, int j, int depth) {
        visited[i][j] = true;
        if (depth > maxDepth) {
            maxDepth = depth;
        }
        List<Coord> neighbors = getNeighbors(i, j);
        for (Coord neighbor : neighbors) {
            if (!visited[neighbor.row][neighbor.col]) {
                countLongestPathFrom(neighbor.row, neighbor.col, depth + 1);
            }
        }
    }

    private List<Coord> getNeighbors(int row, int col) {
        List<Coord> neighbors = new ArrayList<>();
        //top
        if (row != 0 && field.get(row - 1).get(col).contains(State.BOTTOM)) {
            neighbors.add(new Coord(row - 1, col));
        }
        //left
        if (col != 0 && field.get(row).get(col - 1).contains(State.RIGHT)) {
            neighbors.add(new Coord(row, col - 1));
        }
        //bottom
        if (row < SIZE - 1 && field.get(row).get(col).contains(State.BOTTOM)) {
            neighbors.add(new Coord(row + 1, col));
        }
        //right
        if (col < SIZE - 1 && field.get(row).get(col).contains(State.RIGHT)) {
            neighbors.add(new Coord(row, col + 1));
        }
        return neighbors;
    }

    private enum State {RIGHT, BOTTOM}

    private class Coord {
        int row;
        int col;

        Coord(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}