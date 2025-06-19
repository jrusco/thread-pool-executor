# Fundamental Multithreading & Parallelism Concepts

---

1. **Thread** - The smallest unit of execution within a process; shares memory with other threads.

2. **Process** - A self-contained program with its own memory space and threads.

3. **Concurrency** - Multiple tasks are _in progress_ at the same time (not necessarily simultaneously).

4. **Parallelism** - Multiple tasks are executed _at the same time_, on multiple cores/CPUs.

5. **Context Switching** - The CPU switches between threads/processes - adds overhead.

6. **Thread Pool** - A fixed number of threads reused to handle many tasks efficiently.

7. **Race Condition** - A bug where multiple threads access shared data in an unsafe order.

8. **Mutex (Mutual Exclusion)** - A lock to ensure only one thread accesses a resource at a time.

9. **Deadlock** - Two or more threads waiting forever for each other to release resources.

10. **Livelock** - Threads keep changing state in response to each other, but no progress is made.

11. **Starvation** - A thread never gets CPU time because others monopolize the resource.

12. **Semaphore** - A signaling mechanism to control access to a resource with multiple slots.

13. **Monitor** - A combination of a mutex and condition variable for managing thread coordination.

14. **Atomic Operation** - An operation that completes in a single step with no interruption.

15. **Volatile Keyword (Java/C++)** - Ensures visibility of changes to variables across threads.

16. **Memory Barrier** - Prevents certain types of CPU optimizations that reorder instructions.

17. **False Sharing** - Performance bug when threads use different variables in the same CPU cache line.

18. **Thread-Safe Code** - Code that functions correctly when accessed by multiple threads.

19. **Reentrant Function** - Can be safely interrupted and called again ("re-entered") before previous execution finishes.

20. **Thread-local Storage** - Each thread gets its own copy of a variable.

21. **Futures & Promises** - Represent a value that may be available in the future (used in async execution).

22. **Async/Await** - Syntactic sugar for managing asynchronous non-blocking operations.

23. **Fork-Join Model** - Tasks split into subtasks (fork), then results are combined (join).

24. **Producer-Consumer Pattern** - A classic multithreaded model where producers generate data and consumers process it.

25. **Work Stealing** - Threads "steal" tasks from others when idle to balance the workload.
