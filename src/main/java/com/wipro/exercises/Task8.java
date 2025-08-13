
/*
Find the number

Given an unsorted array of n elements, find if the element k is present in the array or not.

Complete the findNumber function in the editor below. It has 2 parameters:

1. An array of integers, arr, denoting the elements in the array.
2. An integer, k, denoting the element to be searched in the array.

The function must return a string "YES" or "NO" denoting if the element is present in the array or not.

Input Format

The first line contains an integer n, denoting the number of elements in the array arr.

Each line i of the n subsequent lines (where 0 ≤ i < n) contains an integer describing arr[i].

The next line contains an integer, k, the element that needs to be searched.

Constraints

1 ≤ n ≤ 10^5
1 ≤ arr[i] ≤ 10^9

Output Format

The function must return a string "YES" or "NO" denoting if the element is present in the array or not. This is printed to stdout by locked stub code in the editor.

Sample Input 0

5
1
2
3
4
5
1

Sample Output 0

YES

Explanation 0

Given the array = [1, 2, 3, 4, 5], we want to find the element 1 if it is present or not. We can find the element 1 at index = 0. Therefore we print "YES".

Sample Input 1

3
2
3
1
5

Sample Output 1

NO

Explanation 1

Given the array [2, 3, 1] and k = 5. There is no element 5 in the array and therefore we print "NO".
*/

package com.wipro.exercises;

import java.io.*;
        import java.util.*;
        import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Task8 {

    public static class Result {

        /*
         * Complete the 'findNumber' function below.
         *
         * The function is expected to return a STRING.
         * The function accepts following parameters:
         *  1. INTEGER_ARRAY arr
         *  2. INTEGER k
         */

        public static String findNumber(List<Integer> arr, int k) {
            // Validate the size of the array
            if (arr.size() < 1 || arr.size() > Math.pow(10, 5)) {
                throw new IllegalArgumentException("Array size must be between 1 and 10^5");
            }

            // Validate elements of the array
            for (int num : arr) {
                if (num < 1 || num > Math.pow(10, 9)) {
                    throw new IllegalArgumentException("Array element must be between 1 and 10^9");
                }
            }

            // Search for the element k
/*
            for (int num : arr) {
                if (num == k) {
                    return "YES";
                }
            }
            return "NO";
*/
            // Search for the element k using a stream
            return arr.stream().anyMatch(num -> num == k) ? "YES" : "NO";
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));

        int arrCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> arr = IntStream.range(0, arrCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
            .map(String::trim)
            .map(Integer::parseInt)
            .collect(toList());

        int k = Integer.parseInt(bufferedReader.readLine().trim());

        String result = Result.findNumber(arr, k);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
