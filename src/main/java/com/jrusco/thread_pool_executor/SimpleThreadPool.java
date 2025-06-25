package com.jrusco.thread_pool_executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jrusco.thread_pool_executor.error.LogFailureAndContinuePolicy;
import com.jrusco.thread_pool_executor.error.PoolShutdownException;
import com.jrusco.thread_pool_executor.error.RejectedTaskExecutionManager;

public class SimpleThreadPool implements TaskExecutor {

    private static final RejectedTaskExecutionManager REJECTION_POLICY_DEFAULT = new LogFailureAndContinuePolicy();
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleThreadPool.class);
    private static final int TASK_QUEUE_SIZE_DEFAULT = 100;
    private static final int THREAD_POOL_SIZE_DEFAULT = 10;
    private static final int THREAD_DEFAULT_TTL_MS = 100;

    private LinkedBlockingQueue<FutureTask<?>> tasks;
    private List<Thread> workerThreads;
    private RejectedTaskExecutionManager rejectionPolicy;
    private volatile boolean shutdown = false;
    private volatile int threadLifeSpanMs = THREAD_DEFAULT_TTL_MS;

    public SimpleThreadPool(int startingPoolSize, int maxPoolSize, RejectedTaskExecutionManager rejectionPolicy) {

        // initialize the thread pool
        if (startingPoolSize <= 0) {
            startingPoolSize = THREAD_POOL_SIZE_DEFAULT;
        }
        if (maxPoolSize <= 0) {
            maxPoolSize = THREAD_POOL_SIZE_DEFAULT;
        }
        workerThreads = new ArrayList<>(maxPoolSize);
        for (int i = 0; i < startingPoolSize; i++) {
            Thread workerThread = new Thread(new Worker(), "thread-pool-worker-" + i);
            workerThreads.add(workerThread);
            workerThread.start();
        }

        // initialize the task queue
        this.tasks = new LinkedBlockingQueue<>(TASK_QUEUE_SIZE_DEFAULT);

        // set the rejection policy
        if (Objects.isNull(rejectionPolicy)) {
            this.rejectionPolicy = REJECTION_POLICY_DEFAULT;
            LOGGER.debug("msg=[No rejection policy specified: Setting default policy], rejectionPolicy=[{}]",
                    this.rejectionPolicy);
        }
        this.rejectionPolicy = rejectionPolicy;
    }

    @Override
    public void execute(Runnable task) throws PoolShutdownException {
        if (isShutdown()) {
            LOGGER.info("msg=[Cannot queue task, thread pool is shutting down]");
            throw new PoolShutdownException();
        }

        FutureTask<?> futureTask = new FutureTask<>(task, null);
        if (!tasks.offer(futureTask)) {
            rejectionPolicy.handleRejection(futureTask, "Task queue is full, task rejected");
        }
    }

    @Override
    public <T> Future<T> execute(Callable<T> task) throws PoolShutdownException {
        if (isShutdown()) {
            LOGGER.info("msg=[Cannot queue task, thread pool is shutting down]");
            throw new PoolShutdownException();
        }
        FutureTask<T> futureTask = new FutureTask<>(task);
        tasks.add(futureTask);
        LOGGER.debug("msg=[Callable task added to the queue successfully]");
        return futureTask;
    }

    @Override
    public List<Runnable> shutdown() {
        LOGGER.info("msg=[Shutdown requested]");
        this.shutdown = true;
        for (Thread worker : workerThreads) {
            worker.interrupt();
        }
        LOGGER.info("msg=[Shutdown completed], remainingTasksCount=[{}]", tasks.size());
        return new ArrayList<>(tasks);
    }

    @Override
    public List<Runnable> shutdownAndAwaitTermination() {
        LOGGER.info("msg=[Shutdown and await termination requested]");
        this.shutdown = true;

        for (Thread worker : workerThreads) {
            worker.interrupt();
        }

        for (Thread worker : workerThreads) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warn(
                        "msg=[Thread interrupted while waiting for worker to terminate], workerName=[{}]",
                        worker.getName());
            }
        }

        LOGGER.info("msg=[Shutdown and await termination completed], remainingTasksCount=[{}]",
                tasks.size());
        return new ArrayList<>(tasks);
    }

    @Override
    public void close() throws Exception {
        LOGGER.info("msg=[close() called, initiating shutdown and awaiting termination]");
        shutdownAndAwaitTermination();
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    public int getThreadLifeSpanMs() {
        return this.threadLifeSpanMs;
    }

    public void setThreadLifeSpanMs(int newLifeSpanMs) {
        if (newLifeSpanMs > 0) {
            this.threadLifeSpanMs = newLifeSpanMs;
            LOGGER.debug("msg=[New value for thread life span is set up], threadLifeSpanMs=[{}]",
                    this.threadLifeSpanMs);
        }
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            while (!shutdown || !tasks.isEmpty()) {
                try {
                    FutureTask<?> task = tasks.poll(threadLifeSpanMs, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        executeTask(task);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.info("msg=[Worker thread interrupted, shutting down], workerName=[{}]",
                            Thread.currentThread().getName());
                    break;
                }
            }
            LOGGER.info("msg=[Worker thread exiting], workerName=[{}]",
                    Thread.currentThread().getName());
        }

        private void executeTask(FutureTask<?> task) {
            try {
                task.run();
                LOGGER.debug("msg=[Task executed successfully], workerName=[{}]",
                        Thread.currentThread().getName());
            } catch (Exception e) {
                LOGGER.error("msg=[Task execution failed], workerName=[{}]",
                        Thread.currentThread().getName(), e);
            }
        }
    }
}
