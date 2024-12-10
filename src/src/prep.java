package src;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collections;

class Prep {
    public static void main(String... args) {
        // int[] arr1 = new int[] {1,2,3,4,4,4,4,5,5,10,11,12,13};
        // int[] arr2 = new int[] {4,6,7,8};
        // List<Integer> intersection = getIntersection(arr1, arr2);
        // System.out.println("The intersection is: " + intersection.toString());
        // List<Integer> union = getUnion(arr1, arr2);
        // System.out.println("The union is: " + union.toString());
        // Set<Integer> a = new HashSet<>(), b = new HashSet<>(), c = new HashSet<>(), d = new HashSet<>();
        // a.add(1);
        // a.add(2);
        // a.add(3);
        // b.add(3);
        // b.add(4);
        // c.add(4);
        // c.add(5);
        // d.add(5);
        // List<Set<Integer>> input = new ArrayList<>(Arrays.asList(a,b,c,d));
        // System.out.println("Before:");
        // for (Set<Integer> s : input) 
        //     System.out.println(s.toString());
        // reduceSet(input);
        // System.out.println("After:");
        // for (Set<Integer> s : input) 
        //     System.out.println(s.toString());

        int[] arr = new int[] { 1, 1, 1, 1, 1, 1, 2, 2, 2,
            2, 3, 5, 5, 7, 7, 8, 8, 9,
            9, 10, 11, 12 };
        System.out.println(uniqueElements(arr).toString());
    }


    /**
     * Given 2 sorted arrays, find the unique elements across them.
     * This basically means find the intersection of 2 sorted arrays.
     * Assume there can be duplicates.
     * 
     * Brute force: iterate through one list and try to find the matching
     * elements in the other. This has a time complexity of O(n^2). However
     * is there a way to optimize? Yes, this can be optimized to O(n)
     * by using a set. Add all elements from l1 to a set and when iterating
     * through l2 then only add to the answer if the element is in the set.
     * This makes time/space O(n) but does not utilize the sorted property.
     * To use the sorted property, maintain 2 pointers at each array and 
     * increase when one is less than the other. Else, increment both.
     * Time: O(n) and Space: O(1)
     */
    public static List<Integer> getIntersection(int[] l1, int[] l2) {
        if (l1.length == 0 && l2.length == 0)
            return new ArrayList<>();
        
        List<Integer> res = new ArrayList<>();
        int p1 = 0, p2 = 0;
        while (p1 < l1.length && p2 < l2.length) {
            if (l1[p1] < l2[p2]) {
                p1++;
            }
            else if (l1[p1] > l2[p2]) {
                p2++;
            }
            else {
                res.add(l1[p1]);
                // handle duplicates
                while (p1 < l1.length-1 && l1[p1] == l1[p1+1])
                    p1++;
                while (p2 < l2.length-1 && l2[p2] == l2[p2+1])
                    p2++;
                p1++;
                p2++;
            }
        }
        return res;
    }

    /**
     * What about union? Same approach with 2 pointers except add the 
     * smaller element and then handle duplicates. If the values are the
     * same then add only one and then handle duplicates for both lists.
     * Time: O(n) and Space: O(1)
     */
    public static List<Integer> getUnion(int[] l1, int[] l2) {
        List<Integer> res = new ArrayList<>();
        if (l1.length == 0 && l2.length == 0)
            return res;
        
        int p1 = 0, p2 = 0;
        while (p1 < l1.length && p2 < l2.length) {
            if (l1[p1] < l2[p2]) {
                res.add(l1[p1]);
                // handle duplicates
                while (p1 < l1.length-1 && l1[p1] == l1[p1+1])
                    p1++;
                p1++;
            }
            else if (l2[p2] < l1[p1]) {
                res.add(l2[p2]);
                while (p2 < l2.length-1 && l2[p2] == l2[p2+1])
                    p2++;
                p2++;
            }
            else {
                res.add(l1[p1]);
                while (p1 < l1.length-1 && l1[p1] == l1[p1+1])
                    p1++;

                while (p2 < l2.length-1 && l2[p2] == l2[p2+1])
                    p2++;

                p1++; 
                p2++;
            }
        }

        while (p1 < l1.length) {
            if (p1 > 0 && l1[p1-1]==l1[p1]) {
                p1++;
                continue;
            }
            res.add(l1[p1]);
            p1++;
        }

        while (p2 < l2.length) {
            if (p2 > 0 && l2[p2-1]==l2[p2]) {
                p2++;
                continue;
            }
            res.add(l2[p2]);
            p2++;
        }

        return res;
    }

    /**
     * Given a list of sets, write a reduce function to remove duplicates and make
     * sure that no set is empty
     * A: {1,2,3}
     * B: {3,4}
     * C: {4,5}
     * D: {5}
     * 
     * output:
     * A: {1,2}
     * B: {3}
     * C: {4}
     * D: {5}
     * Time: O(n^2 * k)
     */
    
    public static List<Set<Integer>> reduceSet(List<Set<Integer>> sets) {
        // assume valid input
        // sort by smallest to greatest
        Collections.sort(sets, (a,b)-> Integer.compare(a.size(), b.size()));

        for (int i=0; i<sets.size(); i++) {
            for (int j=0; j<sets.size(); j++) {
                if (i==j) continue;
                Set<Integer> a = sets.get(i), b = sets.get(j);
                for (int num : a) {
                    if (b.contains(num)) {
                        if (a.size() > b.size()) {
                            a.remove(num);
                        }
                        else {
                            if (a.size() == 1 && b.size() == 1) return null;
                            if (b.size() == 1) return null;
                            b.remove(num);
                        }
                    }
                }
            }
        }
        return sets;
    }

    /**
     * Given a sorted array with duplicates, return all the unique elements.
     * 
     * This can be solved by using binary search. Take the first number, then find
     * its last occurrence (upperbound) using binary search. Place the pointer to 
     * the next element and repeat.
     * 
     * Time: O(k * logn) where k is the number of unique elements
     */
    public static List<Integer> uniqueElements(int[] nums) {
        int n = nums.length;
        List<Integer> res = new ArrayList<>();
        int left = 0;
        while (left < n) {
            res.add(nums[left]);
            // gets the first occurrence of the next unique num
            left = nextIdx(nums, left, nums[left]);
        }
        return res;
    }

    private static int nextIdx(int[] nums, int l, int target) {
        int res = -1;
        int r = nums.length-1;
        while (l <= r) {
            int mid = l + ((r-l)/2);
            if (nums[mid] == target) {
                res = mid;
                l = mid+1;
            }
            else if (nums[mid] > target) {
                r = mid-1;
            }
            else {
                l = mid+1;
            }
        }
        return res+1;
    }
}

/**
 *     public static void main(String[] args) {
        // Define the input sets
        Map<String, Set<Integer>> inputSets = new LinkedHashMap<>();
        inputSets.put("A", new HashSet<>(Arrays.asList(1, 2, 3)));
        inputSets.put("B", new HashSet<>(Arrays.asList(3, 4)));
        inputSets.put("C", new HashSet<>(Arrays.asList(4, 5)));
        inputSets.put("D", new HashSet<>(Arrays.asList(5)));

        // Process the sets to remove duplicates and ensure no set is empty
        Map<String, Set<Integer>> reducedSets = reduceSets(inputSets);

        // Print the output
        for (Map.Entry<String, Set<Integer>> entry : reducedSets.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    // GPT attempt 1
    // public static void reduceSets(List<Set<Integer>> sets) {
    //     // Map from element to list of set indices containing the element
    //     Map<Integer, List<Integer>> elementToSetIndices = new HashMap<>();

    //     // Build the map
    //     for (int i = 0; i < sets.size(); i++) {
    //         for (Integer element : sets.get(i)) {
    //             elementToSetIndices
    //                 .computeIfAbsent(element, k -> new ArrayList<>())
    //                 .add(i);
    //         }
    //     }

    //     // Process elements that appear in multiple sets
    //     for (Map.Entry<Integer, List<Integer>> entry : elementToSetIndices.entrySet()) {
    //         List<Integer> setIndices = entry.getValue();
    //         if (setIndices.size() <= 1) {
    //             continue; // Element is unique or only in one set
    //         }

    //         int element = entry.getKey();

    //         // Find sets with the smallest sizes
    //         int minSize = Integer.MAX_VALUE;
    //         List<Integer> minSizeIndices = new ArrayList<>();
    //         for (int idx : setIndices) {
    //             int size = sets.get(idx).size();
    //             if (size < minSize) {
    //                 minSize = size;
    //                 minSizeIndices.clear();
    //                 minSizeIndices.add(idx);
    //             } else if (size == minSize) {
    //                 minSizeIndices.add(idx);
    //             }
    //         }

    //         // Choose one set to keep the element
    //         int setToKeep = minSizeIndices.get(0);

    //         // Remove element from other sets if possible
    //         for (int idx : setIndices) {
    //             if (idx != setToKeep) {
    //                 Set<Integer> currentSet = sets.get(idx);
    //                 if (currentSet.size() > 1) {
    //                     currentSet.remove(element);
    //                 }
    //             }
    //         }
    //     }
    // }

    // GPT Attempt 2
    // public static void reduceSets(List<Set<Integer>> sets) {
    //     // Track elements that have already been assigned to a set
    //     Set<Integer> assignedElements = new HashSet<>();

    //     // Iterate through each set and remove duplicates
    //     for (Set<Integer> set : sets) {
    //         Iterator<Integer> iterator = set.iterator();
    //         while (iterator.hasNext()) {
    //             int element = iterator.next();
    //             // Remove element if it has already been assigned
    //             if (assignedElements.contains(element)) {
    //                 iterator.remove();
    //             } else {
    //                 assignedElements.add(element);
    //             }
    //         }
    //     }
    // }

    // GPT Attempt 3
    public static Map<String, Set<Integer>> reduceSets(Map<String, Set<Integer>> inputSets) {
        // Build the adjacency list: set -> elements
        Map<String, Set<Integer>> adj = new LinkedHashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : inputSets.entrySet()) {
            adj.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }

        // Initialize the matching: element -> set
        Map<Integer, String> matching = new HashMap<>();

        // Perform maximum matching
        for (String set : adj.keySet()) {
            Set<String> visited = new HashSet<>();
            bpm(set, visited, matching, adj);
        }

        // Collect assigned elements per set
        Map<String, Set<Integer>> assignedElements = new LinkedHashMap<>();
        for (String set : adj.keySet()) {
            assignedElements.put(set, new HashSet<>());
        }

        for (Map.Entry<Integer, String> entry : matching.entrySet()) {
            Integer element = entry.getKey();
            String set = entry.getValue();
            assignedElements.get(set).add(element);
        }

        // Assign remaining elements to sets without duplications
        Set<Integer> usedElements = new HashSet<>(matching.keySet());
        for (String set : adj.keySet()) {
            Set<Integer> elements = adj.get(set);
            Set<Integer> assigned = assignedElements.get(set);
            for (Integer element : elements) {
                if (!usedElements.contains(element)) {
                    assigned.add(element);
                    usedElements.add(element);
                }
            }
        }

        return assignedElements;
    }

    // Bipartite matching algorithm (DFS based)
    private static boolean bpm(String set, Set<String> visited, Map<Integer, String> matching,
                               Map<String, Set<Integer>> adj) {
        if (visited.contains(set)) {
            return false;
        }
        visited.add(set);

        for (Integer element : adj.get(set)) {
            String assignedSet = matching.get(element);
            if (assignedSet == null || bpm(assignedSet, visited, matching, adj)) {
                matching.put(element, set);
                return true;
            }
        }
        return false;
    }

 */