# Thread Pool Executor

## Overview
This project implements a simple thread pool executor in Java. A thread pool executor is a core concept in concurrent programming that manages a pool of worker threads to execute tasks efficiently. Instead of creating a new thread for every task, tasks are submitted to the pool and executed by available threads, improving performance and resource management.

## What Does It Do?
- Manages a fixed number of worker threads.
- Accepts tasks (implementing `Runnable`) and assigns them to available threads.
- Reuses threads for multiple tasks, reducing the overhead of thread creation.
- Handles task execution, shutdown, and error scenarios gracefully.

## Why Use a Thread Pool?
Thread pools are commonly used in Java for efficient task execution, similar to the built-in `java.util.concurrent.ThreadPoolExecutor` class. They help:
- Limit the number of concurrent threads.
- Reuse threads to save system resources.
- Manage task queues and handle rejected tasks.

## Analogy
Think of a thread pool like a team of workers (threads) waiting for jobs (tasks). Instead of hiring a new worker for every job, you have a set team ready to take on new work as soon as they finish their current job.

## Key Classes
- `SimpleThreadPool`: Manages the pool of worker threads and task queue.
- `TaskExecutor`: Submits tasks to the pool for execution.
- `error` package: Handles error scenarios, such as pool shutdown or rejected tasks.

## Getting Started
1. Build the project with Maven:
   ```
   ./mvnw clean package
   ```
2. Run the application:
   ```
   java -jar target/thread-pool-executor-0.0.1-SNAPSHOT.jar
   ```

## References
- [Java Thread Pools (Official Documentation)](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html)
- [Java Concurrency Utilities](https://docs.oracle.com/javase/tutorial/essential/concurrency/)

---
This project is for educational purposes and demonstrates core concepts of Java concurrency in a simple, easy-to-understand way.
