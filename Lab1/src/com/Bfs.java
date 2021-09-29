package com;

import java.util.*;

@SuppressWarnings("all")
public class Bfs {
    //private static int iterations = 0;
    //private static int deleted = 0;

    public static void main(String[] args) {
        System.out.println("BFS Solution:");
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
        printField(bfs(initialField));
    }

    public static int[][] bfs(int[][] field) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(field));
        return bfsRecursive(queue);
    }

    public static int[][] bfsRecursive(Queue<Node> queue) {
        for (Node node : queue) {
            /*deleted++;
            System.out.println("--------");
            System.out.println(deleted);
            System.out.println(iterations);
            System.out.println(queue.size());*/
            if (!checkConflict(node.field)) {
                /*System.out.println("--------");
                System.out.println(deleted);
                System.out.println(iterations);
                System.out.println(queue.size());*/
                return node.field;
            }
        }
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            Node node = queue.poll();
            expand(queue, node);
        }
        /*iterations++;
        System.out.println("--------");
        System.out.println(deleted);
        System.out.println(iterations);
        System.out.println(queue.size());*/
        return bfsRecursive(queue);
    }

    public static class Node implements Comparable<Node> {
        int[][] field;
        Node parent;
        int conflicts;

        public Node(int[][] field, Node parent) {
            this.field = field;
            this.parent = parent;
            this.conflicts = 0;
        }

        public Node(int[][] field) {
            this.field = field;
            this.conflicts = 0;
        }

        public Node(int[][] field, Node parent, int conflicts) {
            this.field = field;
            this.parent = parent;
            this.conflicts = conflicts;
        }

        public Node(int[][] field, int conflicts) {
            this.field = field;
            this.conflicts = conflicts;
        }

        public int compareTo(Node node) {
            return this.conflicts - node.conflicts;
        }
    }

    public static boolean checkConflict(int[][] field) {
        int size = field.length;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 1) {
                    // check horizontal and vertical direction
                    for (int k = 0; k < size; k++) {
                        if (field[i][k] == 1 && k != j || field[k][j] == 1 && k != i) {
                            return true;
                        }
                    }
                    // check diagonal direction
                    for (int k = i, p = j; k >= 0 && p >= 0; k--, p--) {

                        if (field[k][p] == 1 && k != i && p != j) {
                            return true;

                        }
                    }
                    for (int k = i, p = j; k < size && p < size; k++, p++) {
                        if (field[k][p] == 1 && k != i && p != j) {
                            return true;
                        }
                    }
                    for (int k = i, p = j; k < size && p >= 0; k++, p--) {
                        if (field[k][p] == 1 && k != i && p != j) {
                            return true;

                        }
                    }
                    for (int k = i, p = j; k >= 0 && p < size; k--, p++) {
                        if (field[k][p] == 1 && k != i && p != j) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void expand(Queue<Node> queue, Node parent) {
        expand(queue, parent, false);
    }

    public static void expand(Queue<Node> queue, Node parent, boolean prioritized) {
        int[][] parentField = deepCopy(parent.field);
        for (int i = 0; i < parentField.length; i++) {
            // find used J
            int fixedJ = 0;
            int usedI = 0;
            for (int j = 0; j < parentField.length; j++) {
                if (parentField[i][j] == 1) {
                    fixedJ = j;
                    usedI = i;
                }
            }
            // generate children
            for (int j = 0; j < parentField.length; j++) {
                int[][] newField = deepCopy(parentField);
                newField[usedI][fixedJ] = 0;
                if (j != usedI) {
                    newField[j][fixedJ] = 1;
                    if (!prioritized) {
                        queue.offer(new Node(newField, parent));
                    } else {
                        queue.offer(new Node(newField, parent, countConflicts(newField)));
                    }
                }
            }
        }
    }

    public static int countConflicts(int[][] field) {
        class Conflict {
            int x1;
            int y1;
            int x2;
            int y2;

            public Conflict(int x1, int y1, int x2, int y2) {
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Conflict conflict = (Conflict) o;
                return x1 == conflict.x1 && y1 == conflict.y1 && x2 == conflict.x2 && y2 == conflict.y2 ||
                        x1 == conflict.x2 && y1 == conflict.y2 && x2 == conflict.x1 && y2 == conflict.y1;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x1, y1, x2, y2);
            }
        }

        int size = field.length;
        List<Conflict> list = new ArrayList<>();


        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 1) {
                    // check horizontal
                    for (int k = 0; k < size; k++) {
                        if (field[i][k] == 1 && k != j) {
                            Conflict conflict = new Conflict(i, j, i, k);
                            if (!list.contains(conflict)) {
                                list.add(conflict);
                            }
                        }
                    }
                    // check vertical
                    for (int k = 0; k < size; k++) {
                        if (field[k][j] == 1 && k != i) {
                            Conflict conflict = new Conflict(i, j, k, j);
                            if (!list.contains(conflict)) {
                                list.add(conflict);
                            }
                        }
                    }
                    // check diagonal direction
                    for (int k = i, p = j; k >= 0 && p >= 0; k--, p--) {
                        if (field[k][p] == 1 && k != i && p != j) {
                            Conflict conflict = new Conflict(i, j, k, p);
                            if (!list.contains(conflict)) {
                                list.add(conflict);
                            }
                        }
                    }
                    for (int k = i, p = j; k < size && p < size; k++, p++) {
                        if (field[k][p] == 1 && k != i && p != j) {
                            Conflict conflict = new Conflict(i, j, k, p);
                            if (!list.contains(conflict)) {
                                list.add(conflict);
                            }
                        }
                    }
                    for (int k = i, p = j; k < size && p >= 0; k++, p--) {
                        if (field[k][p] == 1 && k != i && p != j) {
                            Conflict conflict = new Conflict(i, j, k, p);
                            if (!list.contains(conflict)) {
                                list.add(conflict);
                            }
                        }
                    }
                    for (int k = i, p = j; k >= 0 && p < size; k--, p++) {
                        if (field[k][p] == 1 && k != i && p != j) {
                            Conflict conflict = new Conflict(i, j, k, p);
                            if (!list.contains(conflict)) {
                                list.add(conflict);
                            }
                        }
                    }
                }
            }
        }
        return list.size();
    }

    public static int[][] deepCopy(int[][] array) {
        int[][] newArray = new int[array.length][array.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                newArray[i][j] = array[i][j];
            }
        }
        return newArray;
    }

    public static void printField(int[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}