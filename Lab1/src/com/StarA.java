package com;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import static com.Bfs.*;

public class StarA {
    private static final Set<Bfs.Node> closed = new HashSet<>();
    private static int iterations = 0;

    public static void main(String[] args) {
        System.out.println("A* Solution:");
        int[][] initialField = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
        };
        Bfs.printField(aStar(initialField));
    }

    public static int[][] aStar(int[][] field) {
        Queue<Bfs.Node> queue = new PriorityQueue<>();
        queue.offer(new Bfs.Node(field, 0));
        return aStarRecursive(queue);
    }

    public static int[][] aStarRecursive(Queue<Bfs.Node> queue) {
        Bfs.Node node = queue.poll();
        System.out.println("--------");
        System.out.println(closed.size());
        System.out.println(iterations);
        System.out.println(queue.size());
        if (closed.contains(node)) {
            return aStarRecursive(queue);
        }
        closed.add(node);
        if (!Bfs.checkConflict(node.field)) {
            return node.field;
        }
        Bfs.expand(queue, node, true);
        iterations++;
        return aStarRecursive(queue);
    }
}
