# FibonacciHeap

## Description
This repository contains a university course project that focuses on implementing a Fibonacci Heap.

## Usage
1. Import the FibonacciHeap class into your Java project.
2. Create an instance of the Fibonacci Heap:
    ```java
    FibonacciHeap heap = new FibonacciHeap();
    ```
3. Use the available methods to interact with the heap. For example:
    ```java
    heap.insert(10);
    heap.insert(20);
    System.out.println(heap.findMin()); // Outputs the minimum element
    ```

## Output
The implementation does not include direct interaction or terminal output but provides return values from the methods for verification. Example outputs:
- `findMin()`:
    ```java
    System.out.println(heap.findMin());
    // Output: HeapNode with the minimum key
    ```
- `countersRep()`:
  ```java
  int[] ranks = heap.countersRep();
  System.out.println(Arrays.toString(ranks));
  // Output: Array of ranks of trees in the heap
  ```
- `size()`:
    ```java
    System.out.println(heap.size());
    // Output: Number of elements in the heap
    ```
