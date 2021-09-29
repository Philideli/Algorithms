package com;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GeneratingTests {

    static Random random = new Random();

    public static void main(String[] args) {
        Set<int[][]> set = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            set.add(generateField());
        }
        int count = 1;
        for (int[][] field : set) {
            System.out.println(count);
            printField(field);
            System.out.println();
            count++;
        }
    }

    public static int[][] generateField() {

        int[][] field = new int[8][8];
        for (int i = 0; i < 8; i++) {
            int j = random.nextInt(8);
            field[j][i] = 1;
        }
        return field;
    }

    public static void printField(int[][] field) {
        StringBuilder sb = new StringBuilder("{");
        sb.append(System.lineSeparator());
        for (int[] ints : field) {
            sb.append("{");
            for (int j = 0; j < field.length; j++) {
                sb.append(ints[j]).append(", ");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            sb.append("},").append(System.lineSeparator());
        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append(System.lineSeparator());
        sb.append("}");
        sb.append(System.lineSeparator());
        System.out.println(sb);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt",true))){
            writer.append(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}