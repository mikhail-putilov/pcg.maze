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

    public static final int SIZE = 10;
    private final List<List<EnumSet<State>>> field;
    private final Iterable<Edge> edges;
    private Random random = new Random();

    public KruskalMaze() {
        EdgeWeightedGraph graph = new EdgeWeightedGraph(SIZE * SIZE);
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
        edges = kruskalMST.edges();
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


    enum State {RIGHT, BOTTOM}
}