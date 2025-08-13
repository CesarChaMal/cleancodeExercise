/*
    Input: nums unorder = [1,2,3, 1,2,3], return order [1, 1, 2, 2, 3 ,3 ]
 */

package com.wipro.exercises;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static java.lang.System.out;

public class Task4 {

    public static class Result {

        public static int[] sort(int[] nums, String sortType) {
            SortStrategy strategy = null;
            SortContextFunctional contextFunctional = null;
            switch (sortType) {
                case "basic":
                    strategy = new BasicSortStrategy();
                    break;
                case "bubble":
                    strategy = new BubbleSortStrategy();
                    break;
                case "merge":
                    strategy = new MergeSortStrategy();
                    break;
                case "quick":
                    strategy = new QuickSortStrategy();
                    break;
                case "basic_functional":
                    contextFunctional = new SortContextFunctional(new BasicSortStrategyFunctional().sort());
                    break;
                case "bubble_functional":
                    contextFunctional = new SortContextFunctional(new BubbleSortStrategyFunctional().sort());
                    break;
                case "merge_functional":
                    contextFunctional = new SortContextFunctional(new MergeSortStrategyFunctional().sort());
                    break;
                case "quick_functional":
                    contextFunctional = new SortContextFunctional(new QuickSortStrategyFunctional().sort());
                default:
                    strategy = new BasicSortStrategy();
            }

            if (strategy != null) {
                SortContext context = new SortContext(strategy);
                context.sortArray(nums);
            } else if (contextFunctional != null) {
                contextFunctional.sortArray(nums);
            }
            return nums;
        }

        private static boolean compare(int[] originalArray, String sortType1, String sortType2) {
            // Clone the original array to avoid modifying it
            int[] arrayForSortType1 = originalArray.clone();
            int[] arrayForSortType2 = originalArray.clone();

            // Sort using the first strategy
            sort(arrayForSortType1, sortType1);

            // Sort using the second strategy
            sort(arrayForSortType2, sortType2);

            // Check if the results are the same
            if (!Arrays.equals(arrayForSortType1, arrayForSortType2)) {
                out.printf("Discrepancy found between %s and %s%n", sortType1, sortType2);
                out.println("Array with " + sortType1 + ": " + Arrays.toString(arrayForSortType1));
                out.println("Array with " + sortType2 + ": " + Arrays.toString(arrayForSortType2));
                return false;
            }
            return true;
        }

        private static void compareAllStrategies(int[] randomArray, String[] strategies) {
            boolean allComparisonsOk = true;

            for (int i = 0; i < strategies.length; i++) {
                for (int j = i + 1; j < strategies.length; j++) {
                    if (!compare(randomArray, strategies[i], strategies[j])) {
                        out.printf("Comparison failed between %s and %s%n", strategies[i], strategies[j]);
                        allComparisonsOk = false;
                        break; // Exit the inner loop
                    }
                }
                if (!allComparisonsOk) break; // Exit the outer loop
            }

            if (allComparisonsOk) {
                out.println("All comparisons OK for this iteration");
            }
        }

        public static void stressTest() {
            int maxIterations = 100_000;
            int maxArraySize = 100; // Maximum size of the array
            int maxValue = 1_000; // Maximum value of an element in the array
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            String[] strategies = {"sort", "bubble", "merge", "quick", "basic_functional", "bubble_functional", "merge_functional", "quick_functional"};

            for (int iteration = 0; iteration < maxIterations; iteration++) {
                int[] randomArray = getRandomArray(maxArraySize, maxValue);
                int finalIteration = iteration;

                executor.submit(() -> {
                    out.println("Iteration " + finalIteration + " with array size = " + randomArray.length);
                    compareAllStrategies(randomArray, strategies);
                });
            }

            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        private static int[] getRandomArray(int size, int maxValue) {
            Random random = new Random();
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(maxValue);
            }
            return array;
        }
    }

    public static void main(String[] args) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));

        int[] originalNums = {1, 2, 3, 1, 2, 3};

        // Clone and sort using Basic Java Sort
        int[] numsForSort = originalNums.clone();
        Result.sort(numsForSort, "basic");
        out.println("Basic Java Sorted: " + Arrays.toString(numsForSort));

        // Clone and sort using Bubble Sort
        numsForSort = originalNums.clone();
        Result.sort(numsForSort, "bubble");
        out.println("Bubble Sorted: " + Arrays.toString(numsForSort));

        // Clone and sort using Merge Sort
        numsForSort = originalNums.clone();
        Result.sort(numsForSort, "merge");
        out.println("Merge Sorted: " + Arrays.toString(numsForSort));

        // Clone and sort using Quick Sort
        numsForSort = originalNums.clone();
        Result.sort(numsForSort, "quick");
        out.println("Quick Sorted: " + Arrays.toString(numsForSort));

        // Clone and sort using Basic Java Functional Sort
        numsForSort = originalNums.clone();
        Result.sort(numsForSort, "sort_functional");
        out.println("Basic Java Functional Sorted: " + Arrays.toString(numsForSort));

        // Clone and sort using Bubble Functional Sort
        numsForSort = originalNums.clone();
        Result.sort(numsForSort, "bubble_functional");
        out.println("Bubble Functional Sorted: " + Arrays.toString(numsForSort));

        // Clone and sort using Merge Functional Sort
        numsForSort = originalNums.clone();
        Result.sort(numsForSort, "merge_functional");
        out.println("Merge Functional Sorted: " + Arrays.toString(numsForSort));

        // Clone and sort using Quick Functional Sort
        numsForSort = originalNums.clone();
        Result.sort(numsForSort, "quick_functional");
        out.println("Quick Functional Sorted: " + Arrays.toString(numsForSort));

        Result.stressTest();

        bufferedWriter.write(Arrays.toString(originalNums));
//        bufferedWriter.write(String.valueOf(nums));
        bufferedWriter.newLine();
//        bufferedReader.close();
        bufferedWriter.close();
    }
}

interface SortStrategy {
    int[] sort(int[] array);
}

@FunctionalInterface
interface SortStrategyFunctional {
    Function<int[], int[]> sort();
}

class BasicSortStrategy implements SortStrategy {
    @Override
    public int[] sort(int[] array) {
        Arrays.sort(array);
        return array;
    }
}

class BasicSortStrategyFunctional implements SortStrategyFunctional {
    @Override
    public Function<int[], int[]> sort() {
        return (array) -> {
            Arrays.sort(array);
            return array;
        };
    }
}

class BubbleSortStrategy implements SortStrategy {
    @Override
    public int[] sort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (array[j] > array[j + 1]) {
                    // swap arr[j+1] and arr[j]
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
        return array;
    }
}

class BubbleSortStrategyFunctional implements SortStrategyFunctional {
    @Override
    public Function<int[], int[]> sort() {
        return (array) -> {
            int n = array.length;
            for (int i = 0; i < n - 1; i++)
                for (int j = 0; j < n - i - 1; j++)
                    if (array[j] > array[j + 1]) {
                        int temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                    }
            return array;
        };
    }
}

class MergeSortStrategy implements SortStrategy {
    @Override
    public int[] sort(int[] array) {
        if (array == null) {
            return null;
        }
        if (array.length <= 1) {
            return array;
        }
        return mergeSort(array, 0, array.length - 1);
    }

    private static int[] mergeSort(int[] array, int left, int right) {
        if (left < right) {
            // Find the middle point
            int mid = left + (right - left) / 2;

            // Sort first and second halves
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            // Merge the sorted halves
            merge(array, left, mid, right);
        }
        return array;
    }

    private static void merge(int[] array, int left, int mid, int right) {
        // Find sizes of two subarrays to be merged
        int n1 = mid - left + 1;
        int n2 = right - mid;

        /* Create temp arrays */
        int[] L = new int[n1];
        int[] R = new int[n2];

        /*Copy data to temp arrays*/
        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
        }
    }
}

class MergeSortStrategyFunctional implements SortStrategyFunctional {
    @Override
    public Function<int[], int[]> sort() {
        return (int[] array) -> {
            if (array == null) {
                return null;
            }
            if (array.length <= 1) {
                return array;
            }
            mergeSort(array, 0, array.length - 1);
            return array;
        };
    }

    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
        }
    }
}

class QuickSortStrategy implements SortStrategy {
    @Override
    public int[] sort(int[] array) {
        quickSort(array, 0, array.length - 1);  // Sort the original array in place
        return array;
    }

    private static void quickSort(int[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private static int partition(int[] arr, int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        int swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }
}

class QuickSortStrategyFunctional implements SortStrategyFunctional {
    @Override
    public Function<int[], int[]> sort() {
        return (array) -> {
            quickSort(array, 0, array.length - 1);
            return array;
        };
    }

    private static void quickSort(int[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private static int partition(int[] arr, int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        int swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }
}

class SortContext {
    private SortStrategy strategy;

    public SortContext(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void sortArray(int[] array) {
        strategy.sort(array);
    }
}

class SortContextFunctional {
    private Function<int[], int[]> strategy;

    public SortContextFunctional(Function<int[], int[]> strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Function<int[], int[]> strategy) {
        this.strategy = strategy;
    }

    public void sortArray(int[] array) {
        strategy.apply(array);
    }
}
