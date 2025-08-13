package com.wipro.exercises;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Given a sorted int array, return squares sorted in non-decreasing order.
 * Demos: loop+sort, streams, IntStream, two-pointer O(n), and List variants.
 */
public class Task11 {

    // ---------- Arrays returning versions ----------
    // 1) Simple loop to square, then Arrays.sort => O(n log n)
    static int[] squaredSorted_loopSort(int[] nums) {
        int[] out = new int[nums.length];
        for (int i = 0; i < nums.length; i++) out[i] = nums[i] * nums[i];
        Arrays.sort(out);
        return out;
    }

    // 2) Streams pipeline => O(n log n)
    static int[] squaredSorted_stream(int[] nums) {
        return Arrays.stream(nums)
                .map(x -> x * x)
                .sorted()
                .toArray();
    }

    // 3) Explicit IntStream.of (same as Arrays.stream for int[]) => O(n log n)
    static int[] squaredSorted_intStream(int[] nums) {
        return IntStream.of(nums)
                .map(x -> x * x)
                .sorted()
                .toArray();
    }

    // 4) Two-pointer optimal O(n) using the fact input is sorted
    static int[] squaredSorted_twoPointers(int[] nums) {
        int n = nums.length;
        int[] out = new int[n];
        int left = 0, right = n - 1, pos = n - 1;

        while (left <= right) {
            int l = nums[left] * nums[left];
            int r = nums[right] * nums[right];
            if (l > r) {
                out[pos--] = l;
                left++;
            } else {
                out[pos--] = r;
                right--;
            }
        }
        return out;
    }

    // ---------- List<Integer> returning versions ----------
    // 5a) Build List via loop (no Stream.boxed used; autoboxing occurs per add)
    static List<Integer> squaredSorted_toList_loop(int[] nums) {
        int[] squaredSorted = squaredSorted_loopSort(nums); // reuse any above; could use twoPointers too
        List<Integer> list = new ArrayList<>(squaredSorted.length);
        for (int x : squaredSorted) list.add(x); // autoboxing int -> Integer
        return list;

    }

    // 5b) Build List via streams
    static List<Integer> squaredSorted_toList_stream(int[] nums) {
        return Arrays.stream(nums)
                .map(x -> x * x)
                .sorted()
                .boxed()
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        int[] nums = {-4, -1, 0, 3, 10};

        System.out.println("Input: " + Arrays.toString(nums));

        // 1) Loop + sort (O(n log n))
        System.out.println("loop+sort      : " + Arrays.toString(squaredSorted_loopSort(nums)));

        // 2) Streams (O(n log n))
        System.out.println("streams        : " + Arrays.toString(squaredSorted_stream(nums)));

        // 3) IntStream (O(n log n))
        System.out.println("intStream      : " + Arrays.toString(squaredSorted_intStream(nums)));

        // 4) Two-pointer (O(n))
        System.out.println("twoPointers O(n): " + Arrays.toString(squaredSorted_twoPointers(nums)));

        // 5) List<Integer> versions
        System.out.println("toList (loop)  : " + squaredSorted_toList_loop(nums));
        System.out.println("toList (stream): " + squaredSorted_toList_stream(nums));
    }
}
