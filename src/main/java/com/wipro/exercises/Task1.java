/*
For two positive integers, lo and hi and a limit k, find two integers, a and b, satisfying the following criteria. Return the value of a⊕ b. The ⊕ symbol denotes the bitwise XOR operator.

        lo ≤ a < b ≤ hi

        The value of  a ⊕ b is maximal for a ⊕ b ≤ k

        Example
        lo = 3
        hi = 5
        k = 6


        a   b   a ⊕ b
        3   4   7
        3   5   6
        4   5   1


        The maximal useable XORed value is  because it is the maximal value that is less than or equal to the limit k = 6.

        Complete the function maxXor. The function must return an integer denoting the maximum possible value of a ⊕ b for all a ⊕ b ≤ k.


        hint: the java syntax for bitwise XOR operator is ^

        maxXor has the following parameter(s):

        int lo: an integer
        int hi: an integer
        k: an integer

        Constraints

        1 ≤ lo < hi ≤ 10^4
        1 ≤ k ≤ 10^4

 */

package com.wipro.exercises;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Task1 {

    public static class Result {

        /*
         * Complete the 'maxXor' function below.
         *
         * The function is expected to return an INTEGER.
         * The function accepts following parameters:
         *  1. INTEGER lo
         *  2. INTEGER hi
         *  3. INTEGER k
         */

        public static int maxXor(int lo, int hi, int k) {
            int max = 0;
            for (int a = lo; a <= hi; a++) {
                for (int b = a + 1; b <= hi; b++) {
                    int xor = a ^ b;
                    if (xor <= k && xor > max) {
                        max = xor;
                    }
                }
            }
            return max;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));

        int lo = Integer.parseInt(bufferedReader.readLine().trim());

        int hi = Integer.parseInt(bufferedReader.readLine().trim());

        int k = Integer.parseInt(bufferedReader.readLine().trim());

        int result = Result.maxXor(lo, hi, k);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
