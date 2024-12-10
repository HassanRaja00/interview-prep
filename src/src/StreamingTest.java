package src;
// https://stackify.com/streams-guide-java-8/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StreamingTest { // Java 8+
    // note that streams are not data structures but tools for operations 
    // like map-reduce transformations on collections

    Employee[] arrEmployees = {
        new Employee(1, "Hassan", 1000000.0), 
        new Employee(2, "Eren", 1500),
        new Employee(3, "Ayanokouji", 9000000.0)
    };

    // the multiple ways to create streams
    @Test
    public void testStreamCreate() {
        Stream<Employee> s1, s2, s3;
        s1 = Stream.of(arrEmployees);

        s2 = List.of(arrEmployees).stream();

        Stream.Builder<Employee> empBuilder = Stream.builder();
        empBuilder.accept(arrEmployees[0]);
        empBuilder.accept(arrEmployees[1]);
        empBuilder.accept(arrEmployees[2]);
        s3 = empBuilder.build();

        assertNotNull(s1);
        assertNotNull(s2);
        assertNotNull(s3);
    }

    // forEach(). This is a terminal operation, stream pipeline is 
    // considered consumed and can no longer be used
    @Test
    public void testForeach() {
        List.of(arrEmployees).stream()
        .forEach(e -> e.salaryIncrement(10));

        assertEquals(1000010d, arrEmployees[0].salary);
        assertEquals(1510d, arrEmployees[1].salary);
        assertEquals(9000010d, arrEmployees[2].salary);
    }

    // map() produces a new stream after applying a function to each element in 
    // the original stream. This takes a stream of employees and makes them into strings
    // NOTE we create a new stream bc the for-each before closed the previous stream
    @Test
    public void testMap() {
        List<String> employees = List.of(arrEmployees).stream()
        .map(e -> e.toString())
        .collect(Collectors.toList());  // collect() gets stuff out of the stream once we are done with all processing

        assertEquals(3, employees.size());
    }

    // filter() produces a new stream that contains the elements of the original stream
    // that meet a condition (predicate)
    @Test
    public void testFilter() {
        List<Employee> goodEmployees = List.of(arrEmployees).stream()
        .filter(e -> e.id % 2 == 1)
        .collect(Collectors.toList());

        assertEquals(2, goodEmployees.size());
    }

    // findFirst() gets the first entry in the stream or be empty (Optional)
    @Test
    public void testFindFirst() {
        Employee broke = Arrays.asList(arrEmployees).stream()
        .filter(e -> e.salary < 100)
        .findFirst()
        .orElse(null);


        Employee rich = Arrays.asList(arrEmployees).stream()
        .filter(e -> e.salary > 9e5)
        .findFirst()
        .orElse(null);

        assertNull(broke);
        assertNotNull(rich);
    }

    // toArray() converts the stream into an array
    @Test
    public void testConvertStreamToArray() {
        Employee[] employees = List.of(arrEmployees).stream().toArray(Employee[]::new);
        
        assertEquals(3, employees.length);
    }

    // flatMap() flattens data structures to hold complex objects
    // NOTE: this is a good way to combine lists it looks like
    // simplifies Stream<List<String>> to Stream<String>
    @Test
    public void testFlatMap() {
        List<List<String>> namesNested = Arrays.asList( 
        Arrays.asList("Hassan", "Raja"), 
        Arrays.asList("Eren", "Yaeger"), 
        Arrays.asList("Toyotaki", "Ananokouji"));

        List<String> namesFlatList = namesNested.stream()
        .flatMap(e -> e.stream())
        .collect(Collectors.toList());

        assertEquals(6, namesFlatList.size());
    }

    // peek() does the same thing as forEach() except this is an intermediate 
    // operation, it returns a new stream instead of ending the stream.
    // so we must use .collect() to save the results to a data-structure
    @Test
    public void testPeek() {
        List<Employee> employees = List.of(arrEmployees).stream()
        .peek(e -> e.salaryIncrement(9000)) 
        .peek(System.out::println)
        // .peek(e -> System.out.println(e))  NOTE this is an alternative to the above
        .collect(Collectors.toList());

        // System.out.println(employees.toString());

        assertEquals(3, employees.size());
    }

    // mapToLong(), mapToInt(), mapToDouble() are specialized to handle primitives efficiently
    @Test
    public void testPrimitiveStream() {
        List<String> numsAsStrings = List.of("12", "13");
        List<Integer> nums = new ArrayList<>();
        numsAsStrings.stream()
        .mapToInt(Integer::valueOf)
        .forEach(e -> nums.add(e));

        assertEquals(2, nums.size());        
    }

    // flatMapToInt/Long/Double() are used when each element of a stream should be
    // mapped to a stream of primitive values and into a single stream
    // NOTE: this is also a good way to combine different streams/lists
    @Test
    public void testFlatMapToInt() {
        Stream<String> strings = Stream.of("1,2,3", "4,5");
        List<Integer> nums = new ArrayList<>();
        strings.flatMapToInt(s -> Stream.of(s.split(","))
        .mapToInt(Integer::valueOf))
        .forEach(e -> nums.add(e));

        assertEquals(5, nums.size());
    }

    // can use .count() to get the size of results
    @Test
    public void testCount() {
        int richEmployees = (int)List.of(arrEmployees).stream()
        .filter(e -> e.salary > 1e6)
        .count();

        assertEquals(1, richEmployees);
    }

    // can use *short-circuiting* operations to allow computations are infinite
    // streams to be completed in finite time
    @Test
    public void testShortCircuit() {
        Stream<Integer> infiniteStream = Stream.iterate(2, i -> i*2);  // iterate allows you to create an infinite stream
        List<Integer> collect = infiniteStream
        .skip(1)  // skip the first 1 elements
        .limit(5)  // limit the list to only 5 elements
        .collect(Collectors.toList());

        assertEquals(5, collect.size());
        assertEquals(List.of(4,8,16,32,64), collect);
    }

    /**
     * IMPORTANT!!!!
     * All intermediate operations are lazy!!!
     * that means they are not executed until a terminal operation
     * is called (where a result is needed) and this allows for
     * significant optimizations
     */

    // sorted() sorts the stream elements
    @Test
    public void testSorted() {
        List<Employee> sorted = List.of(arrEmployees).stream()
        .sorted((a,b)->a.name.compareTo(b.name))
        .collect(Collectors.toList());

        assertEquals("Ayanokouji", sorted.get(0).name);
        assertEquals("Eren", sorted.get(1).name);
        assertEquals("Hassan", sorted.get(2).name);
    }

    // min/max return the min/max based on element property
    @Test
    public void testMinMax() {
        Employee richest = List.of(arrEmployees).stream()
        .max((a,b)->Double.compare(a.salary, b.salary))
        .orElseThrow(NoSuchElementException::new); // this is necessary if it is empty???

        Employee brokest = List.of(arrEmployees).stream()
        .min((a,b)->Double.compare(a.salary, b.salary))
        .orElseThrow(NoSuchElementException::new);
        
        assertEquals("Ayanokouji", richest.name);
        assertEquals("Eren", brokest.name);
    }

    // distinct() returns all distinct elements
    @Test
    public void testDistinct() {
        List<Integer> intList = Arrays.asList(2, 5, 3, 2, 4, 3);
        List<Integer> withoutDups = intList.stream()
        .distinct().collect(Collectors.toList());

        assertEquals(List.of(2,5,3,4), withoutDups);
    }

    // allMatch is a good way to check properties of a list
    // anyMatch is good to find if a list should be looked at
    // noneMatch is the opposite of allMatch
    @Test
    public void testMatch() {
        List<Integer> intList = Arrays.asList(2, 5, 3, 2, 4, 3);
        boolean allEven = intList.stream().allMatch(e -> e%2 == 0);
        boolean oneEven = intList.stream().anyMatch(e -> e%2 == 0);
        boolean noneMultipleOfThree = intList.stream().noneMatch(e -> e%3 == 0);

        assertFalse(allEven);
        assertTrue(oneEven);
        assertFalse(noneMultipleOfThree);
    }

    // reduce() or fold operation takes a sequence of input elements and combines them 
    // into a single summary result. for ex: all sales or employee cost
    @Test
    public void testReduce() {
        Double employeeCosts = List.of(arrEmployees).stream()
        .map(e -> e.salary)
        // .reduce(0d, Double::sum);  // this is a method reference
        .reduce(0d, (a,b)->Double.sum(a, b));

        assertEquals(1.00015E7, employeeCosts);
    }

    // Collectors.joining() is another way to collect elements from a stream
    @Test
    public void testJoining() {
        String empNames = List.of(arrEmployees).stream()
        .map(e -> e.name)
        .collect(Collectors.joining(", "))
        .toString();
        
        assertEquals("Hassan, Eren, Ayanokouji", empNames);
    }

    // Collectors.toCollection() extracts stream elements into any 
    // Collection type
    @Test
    public void testToCollection() {
        Set<String> empSet = List.of(arrEmployees).stream()
        .map(e -> e.name)
        .collect(Collectors.toCollection(HashSet::new));
        
        assertEquals(3, empSet.size());
    }

    // partitioningBy() splits the stream into 2 based off a criteria
    @Test
    public void testPartitionBy() {
        List<Integer> intList = Arrays.asList(2, 5, 3, 2, 4, 3);
        Map<Boolean,List<Integer>> isEven = intList.stream()
        .collect(Collectors.partitioningBy(e -> e%2 == 0));

        assertEquals(List.of(2,2,4), isEven.get(true));
        assertEquals(List.of(5,3,3), isEven.get(false));
    }

    // groupingBy() paritions to more than 2 groups
    @Test
    public void testGroupingBy() {
        Map<Character,List<Employee>> groupByAlphabet = List.of(arrEmployees).stream()
        .collect(Collectors.groupingBy(e -> Character.valueOf(e.name.charAt(0))));
        
        assertEquals(3, groupByAlphabet.get('A').get(0).id);
        assertEquals(2, groupByAlphabet.get('E').get(0).id);
        assertEquals(1, groupByAlphabet.get('H').get(0).id);
    }

    // mapping allows to adapt the collector to a different type
    @Test
    public void testMapping() {
        Map<Character,List<Integer>> idGroupedByAlphabet = List.of(arrEmployees).stream()
        .collect(Collectors.groupingBy(e -> Character.valueOf(e.name.charAt(0)), Collectors.mapping(e -> e.id , Collectors.toList())));

        assertEquals(List.of(3), idGroupedByAlphabet.get('A'));
        assertEquals(List.of(2), idGroupedByAlphabet.get('E'));
        assertEquals(List.of(1), idGroupedByAlphabet.get('H'));
    }

    // NOTE: you can add .parallel() to execute foreach/reduce/etc on multiple elements
    // in parallel, but make sure the code is thread-safe, order does not matter, and
    // if the optimization is ultimately necessary for performance

    // generate() can be used whenever new elements need to be generated
    @Test
    public void testGenerate() {
        List<Double> nums = Stream.generate(Math::random)
        .limit(5)
        .collect(Collectors.toList());

        assertEquals(5, nums.size());
    }

    // iterate() takes 2 parameters: an initial val (seed) and a function that generates 
    // next element using the prev vals. NOTE: this is stateful, not good for parallel
    @Test
    public void testIterate() {
        Stream<Integer> evenNumStream = Stream.iterate(2, i->i+2);
        List<Integer> collect = evenNumStream.limit(6)
        .collect(Collectors.toList());

        assertEquals(List.of(2,4,6,8,10,12), collect);
    }

    // BELOW IS JAVA 9

    // takeWhile takes from an infinite stream while the condition holds
    // stops immediately once predicate (condition) is false (filter would continue)
    @Test
    public void testTakeWhile() {
        List<Integer> nums = Stream.iterate(1, i -> i+1)
        .takeWhile(n -> n <=10)
        .map(i -> i*i)
        .collect(Collectors.toList());

        assertEquals(List.of(1,4,9,16,25,36,49,64,81,100), nums);
    }

    // dropWhile skips elements where the condition is true only until the first time the condition is false
    @Test
    public void testDropWhile() {
        List<Integer> nums = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
        .dropWhile(x -> x <= 5)
        .collect(Collectors.toList());

        // NOTE: it only drops until the condition is false, then gets everything
        assertEquals(List.of(6,7,8,9,0,9,8,7,6,5,4,3,2,1,0), nums);
    }

}
class Employee {
    int id;
    String name;
    double salary;
    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public void salaryIncrement(int inc) {
        this.salary += inc;
    }

    @Override
    public String toString() {
        return "{ Id: " + this.id + ", name: " + this.name + ", salary: " + this.salary + "}";
    }
}