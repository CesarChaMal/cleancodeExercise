package com.wipro.exercises;

import java.util.*;

/**
 * 👉 Find all unique pairs with the given sum in an array.
 * Example:
 *   Given: int[] numbers = {8, 7, 2, 5, 3, 1}, int target = 10
 *   Expected pairs (each pair consists of two numbers):
 *     8 + 2 = 10
 *     7 + 3 = 10
 * Explanation:
 *   - 8 + 2 = 10
 *   - 7 + 3 = 10
 *   - No duplicates should be included (e.g. no reverse pair like 2 + 8 after 8 + 2)
 */
public class Task10 {
    public static void main(String[] args) {
        int[] numbers = {8, 7, 2, 5, 3, 1};
        int target = 10;

        System.out.println("1️⃣ Brute-force nested loops:");
        bruteForcePairs(numbers, target);

        System.out.println("\n2️⃣ Sort + two pointers:");
        twoPointerPairs(numbers.clone(), target);  // clone so original array stays intact

        System.out.println("\n3️⃣ HashSet single pass:");
        hashSetPairs(numbers, target);
    }

    /**
     * 1️⃣ Brute-force nested loops
     * Instructions:
     * - For each element in the array (index i)
     * - Check all elements to its right (index j > i)
     * - If numbers[i] + numbers[j] == target, print the pair
     *
     * Time complexity: O(n²)
     * Space complexity: O(1)
     */
    static void bruteForcePairs(int[] numbers, int target) {
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] + numbers[j] == target) {
                    System.out.println(numbers[i] + " + " + numbers[j] + " = " + target);
                }
            }
        }
    }

    /**
     * 2️⃣ Sort + two pointers
     * Instructions:
     * - Sort the array
     * - Use two pointers: left at start, right at end
     * - While left < right:
     *   - If sum == target, print pair, move both pointers
     *   - If sum < target, move left pointer right
     *   - If sum > target, move right pointer left
     * - Skip duplicates (if array contains duplicates)
     *
     * Time complexity: O(n log n) (due to sorting)
     * Space complexity: O(1) (if sort is in-place)
     */
    static void twoPointerPairs(int[] numbers, int target) {
        Arrays.sort(numbers);
        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                System.out.println(numbers[left] + " + " + numbers[right] + " = " + target);
                left++;
                right--;
                // Skip duplicates if present
                while (left < right && numbers[left] == numbers[left - 1]) left++;
                while (left < right && numbers[right] == numbers[right + 1]) right--;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
    }

    /**
     * 3️⃣ HashSet single pass
     * Instructions:
     * - Loop through the array
     * - For each element, calculate complement = target - element
     * - If complement is in set, print the pair
     * - Add element to set
     * - Use another set to avoid printing duplicates
     *
     * Time complexity: O(n)
     * Space complexity: O(n) (set to store seen elements)
     */
    static void hashSetPairs(int[] numbers, int target) {
        Set<Integer> seen = new HashSet<>();
        Set<String> outputPairs = new HashSet<>(); // to avoid duplicate printing

        for (int num : numbers) {
            int complement = target - num;
            if (seen.contains(complement)) {
                int smaller = Math.min(num, complement);
                int larger = Math.max(num, complement);
                String pair = smaller + "," + larger;
                if (!outputPairs.contains(pair)) {
                    System.out.println(smaller + " + " + larger + " = " + target);
                    outputPairs.add(pair);
                }
            }
            seen.add(num);
        }
    }
}
