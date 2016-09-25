package ru.innopolis.mputilov;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import static ru.innopolis.mputilov.GrowingTreeMaze.*;
import static ru.innopolis.mputilov.hunt_and_kill.HuntAndKill.getLineFromTo;

/**
 * Created by mputilov on 21/09/16.
 */
public class KruskalMaze {

    private final int size;
    private final List<List<EnumSet<State>>> field;
    private int maxDepth = 0;
    private boolean[][] visited;

    public KruskalMaze() {
        this(30);
    }

    public KruskalMaze(int size) {
        this.size = size;
        visited = new boolean[this.size][this.size];
        EdgeWeightedGraph graph = new EdgeWeightedGraph(this.size * this.size);
        Random random = new Random();
        for (int i = 0; i < this.size - 1; i++) {
            for (int j = 0; j < this.size - 1; j++) {
                double w1 = random.nextDouble();
                graph.addEdge(new Edge(getAbsoluteIndex(i, j), getAbsoluteIndex(i + 1, j), w1));
                double w2 = random.nextDouble();

                graph.addEdge(new Edge(getAbsoluteIndex(i, j), getAbsoluteIndex(i, j + 1), w2));
            }
        }
        for (int i = 0; i < this.size - 1; i++) {
            double w1 = random.nextDouble();
            double w2 = random.nextDouble();
            Edge e1 = new Edge(getAbsoluteIndex(i, this.size - 1), getAbsoluteIndex(i + 1, this.size - 1), w1);
            Edge e3 = new Edge(getAbsoluteIndex(this.size - 1, i), getAbsoluteIndex(this.size - 1, i + 1), w2);
            graph.addEdge(e1);
            graph.addEdge(e3);
        }
        KruskalMST kruskalMST = new KruskalMST(graph);
        Iterable<Edge> edges = kruskalMST.edges();
        field = new ArrayList<>(this.size);
        for (int i = 0; i < this.size; i++) {
            ArrayList<EnumSet<State>> newList = new ArrayList<>(this.size);
            field.add(newList);
            for (int j = 0; j < this.size; j++) {
                newList.add(EnumSet.noneOf(State.class));
            }
        }
        for (Edge edge : edges) {
            int a = Math.min(edge.either(), edge.other(edge.either()));
            int b = Math.max(edge.either(), edge.other(edge.either()));
            int a_row = a / this.size;
            int a_col = a % this.size;
            int b_row = b / this.size;
            int b_col = b % this.size;
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
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field.get(i).get(j).size() == 1) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private int getAbsoluteIndex(int row, int column) {
        return size * row + column;
    }

    private void appendVerticalPaths(StringBuilder sb, int row, List<List<EnumSet<State>>> field) {
        for (int column = 0; column < size; column++) {
            sb.append(field.get(row).get(column).contains(State.BOTTOM) ? VERTICAL_PATH : EMPTY_VERTICAL_PATH);
        }
        sb.append("\n");
    }

    private void appendHorizontalRow(StringBuilder sb, int row, List<List<EnumSet<State>>> field) {
        for (int column = 0; column < size - 1; column++) {
            sb.append(ROOM);
            sb.append(field.get(row).get(column).contains(State.RIGHT) ? HORIZONTAL_PATH : EMPTY_HORIZONTAL_PATH);
        }
        sb.append(ROOM);
        sb.append('\n');
    }

    public String prettyPrint() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            appendHorizontalRow(sb, i, field);
            appendVerticalPaths(sb, i, field);
        }
        appendHorizontalRow(sb, size - 1, field);
        return sb.toString();
    }

    public int longestPath() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                countLongestPathFrom(i, j, 0);
                visited = new boolean[size][size];
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
        if (row < size - 1 && field.get(row).get(col).contains(State.BOTTOM)) {
            neighbors.add(new Coord(row + 1, col));
        }
        //right
        if (col < size - 1 && field.get(row).get(col).contains(State.RIGHT)) {
            neighbors.add(new Coord(row, col + 1));
        }
        return neighbors;
    }

    public String generateTikz() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            appendTikzHorizontalRow(sb, i, field);
            appendTikzVerticalPaths(sb, i, field);
        }
        appendTikzHorizontalRow(sb, size - 1, field);
        return sb.toString();
    }

    private void appendTikzVerticalPaths(StringBuilder sb, int row, List<List<EnumSet<State>>> field) {
        for (int column = 0; column < size; column++) {
            sb.append(field.get(row).get(column).contains(State.BOTTOM) ? getLineFromTo(row, column, row + 1, column) : "");
        }
    }

    private void appendTikzHorizontalRow(StringBuilder sb, int row, List<List<EnumSet<State>>> field) {
        for (int column = 0; column < size - 1; column++) {
            sb.append(field.get(row).get(column).contains(State.RIGHT) ? getLineFromTo(row, column, row, column + 1) : "");
        }
    }

    private enum State {RIGHT, BOTTOM}
}