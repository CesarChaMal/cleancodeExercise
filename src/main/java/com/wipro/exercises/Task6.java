/*
Given an array of positive integers nums and a positive integer target, return the minimal length of a
subarray whose sum is greater than or equal to target.
If there is no such subarray, return 0 instead.

Example 1:

Input: target = 7, nums = [2,3,1,2,4,3]
Output: 2
Explanation: The subarray [4,3] has the minimal length under the problem constraint.

Example 2:

Input: target = 4, nums = [1,4,4]
Output: 1

Example 3:

Input: target = 11, nums = [1,1,1,1,1,1,1,1]
Output: 0

class Solution {

    public int minSubArrayLen(int target, int[] nums) {
    }
}

Example 1:
Input: target = 7, nums = [2,3,1,2,4,3]
Output: 2

Explanation:
Start with minLength = Integer.MAX_VALUE, start = 0, and sum = 0.
Iterate through nums:
sum = 2 (first element), not >= 7, continue.
sum = 5 (2 + 3), not >= 7, continue.
sum = 6 (2 + 3 + 1), not >= 7, continue.
sum = 8 (2 + 3 + 1 + 2), now >= 7.
Update minLength to 4 (current subarray length).
Subtract nums[start] (2) from sum, making it 6. Increment start.
sum = 10 (3 + 1 + 2 + 4), still >= 7.
Update minLength to 3.
Subtract nums[start] (3) from sum, making it 7. Increment start.
sum = 7 (1 + 2 + 4), exactly 7.
Update minLength to 3.
Subtract nums[start] (1) from sum, making it 6. Increment start.
sum = 9 (2 + 4 + 3), now >= 7.
Update minLength to 3.
Subtract nums[start] (2) from sum, making it 7. Increment start.
sum = 7 (4 + 3), exactly 7.
Update minLength to 2.
Subtract nums[start] (4) from sum, making it 3. Increment start.
Result: The smallest subarray length that meets or exceeds 7 is 2 (subarray [4,3]).

Example 2:
Input: target = 4, nums = [1,4,4]
Output: 1

Explanation:
sum = 1 initially, not >= 4.
sum = 5 (1 + 4), now >= 4.
Update minLength to 2.
Subtract nums[start] (1) from sum, making it 4. Increment start.
sum = 4, exactly 4.
Update minLength to 1.
Result: The smallest subarray length is 1 (subarray [4]).

Example 3:
Input: target = 11, nums = [1,1,1,1,1,1,1,1]
Output: 0

Explanation:
In this case, the sum of all elements in the array is 8, which is less than 11.
The minLength is never updated because no subarray sum reaches 11.
Result: Since no such subarray exists, the function returns 0.


Of course! Let's focus on the specific part of the example where minLength is updated to 4, and then we subtract nums[start] from sum and increment start.

Example 1:
Input: target = 7, nums = [2,3,1,2,4,3]

We're looking at the part of the algorithm where the subarray's sum has first exceeded the target:

Start of Iteration: At this point, our subarray includes the first four elements [2, 3, 1, 2].
Current State:
sum = 8 (2 + 3 + 1 + 2), which is greater than the target of 7.
start = 0 (index of the first element of the current subarray).
end = 3 (index of the last element of the current subarray, which is the second '2').
Process:
Update minLength:

We calculate the length of the current subarray: end - start + 1.
Here, it's 3 - 0 + 1 = 4.
So, minLength is updated to 4. This is the length of the subarray [2, 3, 1, 2].
Reduce the sum:

Since our sum (8) is greater than or equal to the target (7), we try to shorten the subarray to find a smaller subarray that still meets the target.
We remove the first element of the subarray from the sum: sum -= nums[start]. Here, sum = 8 - 2 = 6.
We then increment start to 1. Now, start points to the second element of the original array, which is 3.
Continuing the Iteration:

Now, our subarray starts from the second element and ends at the fourth: [3, 1, 2].
The sum of this new subarray is 6, which is less than the target.
The iteration continues with adding the next element (which is 4) to the sum.
By performing these steps, we continuously adjust our subarray's start and end positions to find the minimal length subarray whose sum is greater than or equal to the target. This method efficiently finds the smallest possible subarray meeting the criteria without checking every possible subarray.
*/

package com.wipro.exercises;

import java.io.*;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.System.in;
import static java.lang.System.out;

public class Task6 {

    // Functional interface for subarray length strategies
    @FunctionalInterface
    interface SubArrayLengthStrategy {
        int compute(int target, int[] nums);
    }

    public static class Result {

        // O(N²) time complexity
        public static int minSubArrayLenNaive(int target, int[] nums) {
            int minLength = Integer.MAX_VALUE;
            for (int start = 0; start < nums.length; start++) {
                int sum = 0;
                for (int end = start; end < nums.length; end++) {
                    sum += nums[end];
                    if (sum >= target) {
                        minLength = Math.min(minLength, end - start + 1);
                        break;
                    }
                }
            }
            return minLength == Integer.MAX_VALUE ? 0 : minLength;
        }

        // O(N) time complexity
        public static int minSubArrayLen(int target, int[] nums) {
            int minLength = Integer.MAX_VALUE;
            int start = 0;
            int sum = 0;

            for (int end = 0; end < nums.length; end++) {
                sum += nums[end];

                while (sum >= target) {
                    minLength = Math.min(minLength, end - start + 1);
                    sum -= nums[start];
                    start++;
                }
            }

            return minLength == Integer.MAX_VALUE ? 0 : minLength;
        }

        // Method to compare subarray length strategies
        public static boolean compareStrategies(SubArrayLengthStrategy strategy1, SubArrayLengthStrategy strategy2, int target, int[] nums) {
            int result1 = strategy1.compute(target, nums);
            int result2 = strategy2.compute(target, nums);

            if (result1 != result2) {
                out.printf("Discrepancy found between strategies: %d != %d%n", result1, result2);
                return false;
            }
            return true;
        }

        // Stress Test within the Result class
        public static void runStressTest() {
//            int maxSize = 100_000;  // Size of the test array
            int maxSize = 100;  // Size of the test array
            int maxElement = 100;   // Maximum value for elements in the array
            int[] testArray = generateRandomArray(maxSize, maxElement);

            // Print a portion of the array for debugging
//            out.println("Array Content (first 100 elements): " + Arrays.toString(Arrays.copyOf(testArray, 100)));
//            out.println("Array Content: " + Arrays.toString(testArray));

            // Using Streams to print the array
            IntStream.of(testArray).forEach(element -> out.print(element + " "));
            out.println(); // New line after printing the array

            Random rand = new Random();

            // Generate random targets for the stress test
            int[] targets = new int[10]; // Number of different targets to test
            for (int i = 0; i < targets.length; i++) {
                targets[i] = rand.nextInt(maxElement * 2) + 1; // Random target up to twice the maxElement
            }

            // Run stress test for each target
            for (int target : targets) {
                long startTime = System.currentTimeMillis();
                boolean isConsistent = compareStrategies(Result::minSubArrayLen, Result::minSubArrayLenNaive, target, testArray);
                long endTime = System.currentTimeMillis();

                if (isConsistent) {
                    out.println("Target: " + target + " - Strategies are consistent, Time taken: " + (endTime - startTime) + "ms");
                } else {
                    out.println("Target: " + target + " - Strategies are not consistent, Time taken: " + (endTime - startTime) + "ms");
                }
            }
        }

        private static int[] generateRandomArray(int size, int maxElement) {
            Random rand = new Random();
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = rand.nextInt(maxElement) + 1;  // Ensure non-zero elements
            }
            return array;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));

        int[] nums = {2, 3, 1, 2, 4, 3};
        int result = Result.minSubArrayLen(7, nums);
        out.println("Result: " + result);

        // Run the stress test
        Result.runStressTest();

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
