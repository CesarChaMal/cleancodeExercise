package com.wipro.exercises;

import com.wipro.exercises.Task5.Result;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Task5Test {

    @Test
    public void testWithDuplicates() {
        int[] nums = {3, 3, 4, 5};
        int target = 6;
        compareResults(nums, target);
    }

    @Test
    public void testWithNegatives() {
        int[] nums = {-1, -2, 3, 4};
        int target = 2;
        compareResults(nums, target);
    }

    @Test
    public void testNoValidPair() {
        int[] nums = {1, 2, 3, 4};
        int target = 10;
        compareResults(nums, target);
    }

    @Test
    public void testLargeArray() {
        int[] nums = Task5.Result.generateRandomArray(10000, 100); // Assuming this method is public
        int target = 150;
        compareResults(nums, target);
    }

    private void compareResults(int[] nums, int target) {
        int[] resultNaive = null, resultOptimized = null;
        boolean thrownNaive = false, thrownOptimized = false;

        try {
            resultNaive = Result.findTwoSumNaive(nums, target);
        } catch (IllegalArgumentException e) {
            thrownNaive = true;
        }

        try {
            resultOptimized = Result.findTwoSum(nums, target);
        } catch (IllegalArgumentException e) {
            thrownOptimized = true;
        }

        // Check if both methods threw exceptions for no solution scenario
        if (thrownNaive && thrownOptimized) {
            return;
        }

        // Check if both methods returned valid pairs
        if (resultNaive != null && resultOptimized != null) {
            assertTrue("Naive strategy did not return a valid pair.", isValidPair(nums, resultNaive, target));
            assertTrue("Optimized strategy did not return a valid pair.", isValidPair(nums, resultOptimized, target));
        } else {
            fail("Mismatch in exception throwing between strategies.");
        }
    }

    private boolean isValidPair(int[] nums, int[] indices, int target) {
        return nums[indices[0]] + nums[indices[1]] == target;
    }

}
