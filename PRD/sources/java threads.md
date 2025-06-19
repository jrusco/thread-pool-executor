# Java Threads - All You Need to Know

---

1. **Thread t = new Thread(); → Thread Object**
   - This is called a Thread.
   - Use to run code in parallel to the main program (e.g., processing file while UI is responsive).

2. **t.start() → Starts a Thread**
   - This starts the new thread and runs run() method.
   - Use to begin concurrent execution (e.g., sending email while continuing signup process).

3. **implements Runnable → Runnable Interface**
   - This is a task that can be run by a thread.
   - Use when you want to define logic separately from thread creation (e.g., reusable task classes).

4. **implements Callable<T> → Callable Interface**
   - Like Runnable but returns a value.
   - Use when you need a result from thread (e.g., background calculation with result).

5. **Thread.sleep(ms) → Pause Thread**
   - Makes current thread sleep.
   - Use to simulate delay or wait (e.g., retry after 1 second).

6. **t.join() → Wait for Thread**
   - This blocks current thread until it finishes.
   - Use when main thread must wait for child to complete (e.g., combining partial results).

7. **synchronized → Locking Mechanism**
   - Prevents race conditions on shared data.
   - Use when multiple threads access/modify shared objects (e.g., adding to shared list).

8. **volatile → Visibility Keyword**
   - Ensures all threads see latest value.
   - Use for flags, not full object sync (e.g., stop = true to safely stop a thread).

9. **ThreadLocal<T> → Per-Thread Variable**
   - Each thread gets its own copy.
   - Use for user context, date formatters, etc. (e.g., storing request ID per thread).

10. **ExecutorService → Thread Pool Manager**
   `ExecutorService pool = Executors.newFixedThreadPool(5);`
   - Manages thread reuse.
   - Use instead of manual thread creation (e.g., running 10 tasks in a pool of 3 threads).

11. **Future<T> → Result of Async Task**
   - Returned by submit(Callable).
   - Use to get result of async operation (e.g., future.get() blocks until result is ready).

12. **CompletableFuture → Modern Async Tool**
   - Used for non-blocking, chainable async operations.
   - Use for composing async workflows (e.g., fetch → process → save).

13. **Thread.setDaemon(true) → Daemon Thread**
   - Runs in background, doesn’t block JVM shutdown.
   - Use for background logging, cleanup tasks (e.g., heartbeat pings).

14. **ReentrantLock → Explicit Locking**
   - More flexible than synchronized.
   - Use when fine-grained control needed (e.g., tryLock, fairness).

15. **wait() / notify() → Thread Communication**
   - Used for coordination between threads.
   - Use in producer-consumer problems (e.g., waiting for queue to have data).
