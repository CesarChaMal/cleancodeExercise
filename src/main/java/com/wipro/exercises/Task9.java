/*
Odd Numbers

Given two integers, l and r, print all the odd numbers between l and r (l and r inclusive).

Complete the oddNumbers function in the editor below. It has 2 parameters:

1. An integer, l, denoting the left part of the range.
2. An integer, r, denoting the right part of the range.

The function must return an array of integers denoting the odd numbers between l and r.

Input Format

Locked stub code in the editor reads the following input from stdin and passes it to the function:

The first line contains an integer, l, denoting the left part of the range.

The second line contains an integer, r, denoting the right part of the range.

Constraints

1 ≤ l ≤ r ≤ 10^5

Output Format

The function must return an array of integers denoting the odd numbers between l and r.
This is printed to stdout by locked stub code in the editor.

Sample Input 0

2
5

Sample Output 0

3
5

Explanation 0

The value of l is 2 and value of r is 5. The odd numbers between [2, 5] are 3 and 5.

Sample Input 1

3
9

Sample Output 1

3
5
7
9

Explanation 1

The value of l is 3 and value of r is 9. The odd numbers between [3, 9] are 3, 5, 7 and 9.
*/

package com.wipro.exercises;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class Task9 {

    public static class Result {

        /*
         * Complete the 'oddNumbers' function below.
         *
         * The function is expected to return an INTEGER_ARRAY.
         * The function accepts following parameters:
         *  1. INTEGER l
         *  2. INTEGER r
         */

        public static List<Integer> oddNumbers(int l, int r) {
            // Validate the constraints
            if (l < 1 || l > Math.pow(10, 5) || r < 1 || r > Math.pow(10, 5) || l > r) {
                throw new IllegalArgumentException("Invalid input. Ensure 1 ≤ l ≤ r ≤ 10^5.");
            }

/*
            List<Integer> oddNumbers = new ArrayList<>();
            // Adjust the start if l is even
            if (l % 2 == 0) {
                l++;
            }
            for (int i = l; i <= r; i += 2) {
                oddNumbers.add(i);
            }
            return oddNumbers;
*/
            return IntStream.rangeClosed(l, r)  // Create a stream from l to r inclusive
                    .filter(i -> i % 2 != 0)  // Filter to keep only odd numbers
                    .boxed()  // Box the ints to Integers
                    .collect(Collectors.toList());  // Collect into a List
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));

        int l = Integer.parseInt(bufferedReader.readLine().trim());

        int r = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> result = Result.oddNumbers(l, r);

        bufferedWriter.write(
            result.stream()
                .map(Object::toString)
                .collect(joining("\n"))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
