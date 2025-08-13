/*
Input: nums = [2,7,11,15], target = 9 which is the sum of 2 and 7 the list of numbers input is with no order.
Task5Output: [0,1]  should return only 2 elements that will be the sum of the target.

Function to find indices of the two numbers such that they add up to a specific target.
*/

package com.wipro.exercises;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

import static java.lang.System.*;
import static java.lang.System.out;

public class Task5 {

    // Functional interface for two sum strategies
    @FunctionalInterface
    interface TwoSumStrategy {
        int[] compute(int[] nums, int target);
    }

    public static class Result {

        // O(N²) time complexity
        // Enhanced findTwoSumNaive method with more debugging information
        public static int[] findTwoSumNaive(int[] nums, int target) {
            out.println("Naive Strategy - Starting search");
            for (int i = 0; i < nums.length; i++) {
                for (int j = i + 1; j < nums.length; j++) {
                    out.printf("Naive Strategy - Checking pair: [%d, %d] -> [%d, %d]\n", i, j, nums[i], nums[j]);
                    if (nums[i] + nums[j] == target) {
                        out.printf("Naive Strategy - Pair found: [%d, %d]\n", i, j);
                        return new int[]{i, j};
                    }
                }
            }
            throw new IllegalArgumentException("No two sum solution");
        }

        // O(N log N) time complexity
// O(N log N) time complexity
        public static int[] findTwoSumNaive2(int[] nums, int target) {
            out.println("Naive2 Strategy - Starting search");
            int[][] numsWithIndex = new int[nums.length][2];
            for (int i = 0; i < nums.length; i++) {
                numsWithIndex[i][0] = nums[i];
                numsWithIndex[i][1] = i;
            }

            Arrays.sort(numsWithIndex, Comparator.comparingInt(a -> a[0]));

            List<int[]> potentialPairs = new ArrayList<>();
            int left = 0, right = nums.length - 1;
            while (left < right) {
                int sum = numsWithIndex[left][0] + numsWithIndex[right][0];
                out.printf("Naive2 Strategy - Checking pair: [%d, %d] -> [%d, %d]\n", left, right, numsWithIndex[left][0], numsWithIndex[right][0]);
                if (sum == target) {
                    potentialPairs.add(new int[]{numsWithIndex[left][1], numsWithIndex[right][1]});
                    left++;
                    right--;
                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }

            if (potentialPairs.isEmpty()) {
                throw new IllegalArgumentException("No two sum solution");
            }

            // Determine the earliest pair based on their original indices
            return potentialPairs.stream()
                    .min(Comparator.comparingInt(pair -> Math.max(nums[pair[0]], nums[pair[1]])))
                    .orElseThrow(() -> new IllegalArgumentException("No two sum solution"));
        }

        // O(N) time complexity
        // Enhanced findTwoSum method with more debugging information
        public static int[] findTwoSum(int[] nums, int target) {
            Map<Integer, Integer> numMap = new HashMap<>();
            int minIndexSum = Integer.MAX_VALUE;
            int[] result = new int[2];

            out.println("Optimized Strategy - Starting search");
            for (int i = 0; i < nums.length; i++) {
                int complement = target - nums[i];
                out.printf("Optimized Strategy - Current number: %d, Complement: %d\n", nums[i], complement);
                if (numMap.containsKey(complement) && i + numMap.get(complement) < minIndexSum) {
                    out.printf("Optimized Strategy - Pair found: [%d, %d]\n", numMap.get(complement), i);
                    minIndexSum = i + numMap.get(complement);
                    result[0] = numMap.get(complement);
                    result[1] = i;
                }
                if (!numMap.containsKey(nums[i])) {
                    numMap.put(nums[i], i);
                }
//                out.printf("Optimized Strategy - Map updated: %s\n", numMap);
            }
            if (minIndexSum == Integer.MAX_VALUE) {
                throw new IllegalArgumentException("No two sum solution");
            }
            return result;
        }

        // Method to compare two sum strategies
        public static boolean compareStrategies(TwoSumStrategy strategy1, TwoSumStrategy strategy2, TwoSumStrategy strategy3, int[] nums, int target) {
            out.println("Comparing strategies for target: " + target);
            int[] result1 = strategy1.compute(nums, target);
            int[] result2 = strategy2.compute(nums, target);
            int[] result3 = strategy3.compute(nums, target);

            if (!Arrays.equals(result1, result2) || !Arrays.equals(result1, result3)) {
                out.printf("Discrepancy found among strategies: Strategy 1 result %s, Strategy 2 result %s, Strategy 3 result %s\n", Arrays.toString(result1), Arrays.toString(result2), Arrays.toString(result3));
                return false;
            } else {
                out.println("Strategies are consistent.");
            }

            out.println("Strategies are consistent.");
            return true;
        }

        // Stress Test including strategy comparison
        public static void runStressTest() {
//            int maxSize = 100_000;  // Size of the test array
            int maxSize = 1_000;  // Size of the test array
//            int maxSize = 100;  // Size of the test array
//            int maxSize = 50;  // Size of the test array
//            int maxSize = 10;  // Size of the test array
            int maxElement = 100;  // Maximum value for elements in the array
            int[] testArray = generateRandomArray(maxSize, maxElement);

            // Print a portion of the array for debugging
//            out.println("Array Content (first 100 elements): " + Arrays.toString(Arrays.copyOf(testArray, 100)));
            out.println("Array Content: " + Arrays.toString(testArray));

            // Using Streams to print the array
            IntStream.of(testArray).forEach(element -> out.print(element + " "));
            out.println(); // New line after printing the array

            Random rand = new Random();

            for (int i = 0; i < 10; i++) { // Run the test 10 times with different targets
                int target = rand.nextInt(maxElement * 2) + 1;  // Target can be up to twice the maxElement

                try {
                    long startTime = currentTimeMillis();
                    boolean isConsistent = compareStrategies(
                            Result::findTwoSumNaive,
                            Result::findTwoSumNaive2,
                            Result::findTwoSum,
                            testArray,
                            target
                    );
                    long endTime = currentTimeMillis();

                    out.println("Target: " + target + ", Consistency: " + isConsistent + ", Time taken: " + (endTime - startTime) + "ms");
                } catch (IllegalArgumentException e) {
                    out.println("Target: " + target + ", No solution found.");
                }
            }
        }

        protected static int[] generateRandomArray(int size, int maxElement) {
            Random rand = new Random();
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = rand.nextInt(maxElement) + 1;
            }
            return array;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));

        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = Result.findTwoSum(nums, target);
        out.println("Index: [" + result[0] + ", " + result[1] + "]");

        // Run both strategies
//        int[] resultNaive = Result.findTwoSumNaive(nums, target);
//        int[] resultOptimized = Result.findTwoSum(nums, target);

        // Print results for comparison
//        out.println("Naive Result: " + Arrays.toString(resultNaive));
//        out.println("Optimized Result: " + Arrays.toString(resultOptimized));

        // Run the stress test
        Result.runStressTest();

        bufferedWriter.write(Arrays.toString(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
