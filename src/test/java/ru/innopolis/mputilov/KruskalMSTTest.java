package ru.innopolis.mputilov;

import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import static ru.innopolis.mputilov.GrowingTreeMaze.*;

/**
 * Created by mputilov on 21/09/16.
 */
public class KruskalMSTTest {

    public static final int SIZE = 10;
    private Random random = new Random();

    @Test
    public void name() throws Exception {
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
        Iterable<Edge> edges = kruskalMST.edges();
        List<List<EnumSet<state>>> field = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            ArrayList<EnumSet<state>> newList = new ArrayList<>(SIZE);
            field.add(newList);
            for (int j = 0; j < SIZE; j++) {
                newList.add(EnumSet.noneOf(state.class));
            }
        }
        for (Edge edge : edges) {
            int a = Math.min(edge.either(), edge.other(edge.either()));
            int b = Math.max(edge.either(), edge.other(edge.either()));
            int a_row = a / SIZE;
            int a_col = a % SIZE;
            int b_row = b / SIZE;
            int b_col = b % SIZE;
//            System.out.format("a:(%d,%d)\n", a_row, a_col);
//            System.out.format("b:(%d,%d)\n", b_row, b_col);
            if (a_row == b_row) {
                int same_row = a_row = b_row;
                if (a_col + 1 == b_col) {
                    field.get(same_row).get(a_col).add(state.right);
//                    System.out.println("right");
                } else if (b_col + 1 == a_col) {
                    field.get(same_row).get(b_col).add(state.right);
//                    System.out.println("right");
                } else {
                    throw new RuntimeException();
                }
            } else if (a_col == b_col) {
                int same_col = a_col = b_col;
                if (a_row + 1 == b_row) {
                    field.get(a_row).get(same_col).add(state.bottom);
//                    System.out.println("bottom");
                } else if (b_row + 1 == a_row) {
                    field.get(b_row).get(same_col).add(state.bottom);
//                    System.out.println("bottom");
                } else {
                    throw new RuntimeException();
                }
            }
            System.out.println();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE - 1; i++) {
            appendHorizontalRow(sb, i, field);
            appendVerticalPaths(sb, i, field);
        }
        appendHorizontalRow(sb, SIZE - 1, field);
        System.out.println(sb.toString());

        int counter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (field.get(i).get(j).size() == 1) {
                    counter++;
                }
            }
        }
        System.out.println(counter);
    }

    private int getAbsoluteIndex(int row, int column) {
        return SIZE * row + column;
    }

    private void appendVerticalPaths(StringBuilder sb, int row, List<List<EnumSet<state>>> field) {
        for (int column = 0; column < SIZE; column++) {
            sb.append(field.get(row).get(column).contains(state.bottom) ? VERTICAL_PATH : EMPTY_VERTICAL_PATH);
        }
        sb.append("\n");
    }

    private void appendHorizontalRow(StringBuilder sb, int row, List<List<EnumSet<state>>> field) {
        for (int column = 0; column < SIZE - 1; column++) {
            sb.append(ROOM);
            sb.append(field.get(row).get(column).contains(state.right) ? HORIZONTAL_PATH : EMPTY_HORIZONTAL_PATH);
        }
        sb.append(ROOM);
        sb.append('\n');
    }


    enum state {right, bottom}
}