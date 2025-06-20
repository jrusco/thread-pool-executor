package com.jrusco.thread_pool_executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jrusco.thread_pool_executor.error.PoolShutdownException;

public class SimpleThreadPool implements TaskExecutor {

    private static final Integer TASK_QUEUE_SIZE_DEFAULT = 100;
    private static final Integer POOL_SIZE_DEFAULT = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleThreadPool.class);

    private volatile boolean shutdown = false;
    private LinkedBlockingQueue<FutureTask<?>> tasks;
    private List<Thread> workerThreads;

    public SimpleThreadPool(int poolSize) {

        if (poolSize <= 0) {
            poolSize = POOL_SIZE_DEFAULT;
        }

        workerThreads = new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            Thread workerThread = new Thread(new Worker(), "thread-pool-worker-" + i);
            workerThreads.add(workerThread);
            workerThread.start();
        }

        this.tasks = new LinkedBlockingQueue<FutureTask<?>>(TASK_QUEUE_SIZE_DEFAULT);
    }

    @Override
    public void execute(Runnable task) throws PoolShutdownException {
        if (isShutdown()) {
            LOGGER.info("SimpleThreadPool - msg=[Cannot queue task, thread pool is shutting down]");
            throw new PoolShutdownException();
        }
        tasks.add(new FutureTask<>(task, null));
        LOGGER.debug("SimpleThreadPool - msg=[Runnable task added to the queue successfully]");
    }

    @Override
    public <T> Future<T> execute(Callable<T> task) throws PoolShutdownException {
        if (isShutdown()) {
            LOGGER.info("SimpleThreadPool - msg=[Cannot queue task, thread pool is shutting down]");
            throw new PoolShutdownException();
        }
        FutureTask<T> futureTask = new FutureTask<>(task);
        tasks.add(futureTask);
        LOGGER.debug("SimpleThreadPool - msg=[Callable task added to the queue successfully]");
        return futureTask;
    }

    @Override
    public List<Runnable> shutdown() {
        LOGGER.info("SimpleThreadPool - msg=[Shutdown requested]");
        this.shutdown = true;
        for (Thread worker : workerThreads) {
            worker.interrupt();
        }
        LOGGER.info("SimpleThreadPool - msg=[Shutdown completed], remainingTasksCount=[{}]", tasks.size());
        return new ArrayList<>(tasks);
    }

    @Override
    public List<Runnable> shutdownAndAwaitTermination() {
        LOGGER.info("SimpleThreadPool - msg=[Shutdown and await termination requested]");
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
                        "SimpleThreadPool - msg=[Thread interrupted while waiting for worker to terminate], workerName=[{}]",
                        worker.getName());
            }
        }

        LOGGER.info("SimpleThreadPool - msg=[Shutdown and await termination completed], remainingTasksCount=[{}]",
                tasks.size());
        return new ArrayList<>(tasks);
    }

    @Override
    public void close() throws Exception {
        LOGGER.info("SimpleThreadPool - msg=[close() called, initiating shutdown and awaiting termination]");
        shutdownAndAwaitTermination();
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    private class Worker implements Runnable {
        private static final int WORKER_TIMEOUT_MS = 100;

        @Override
        public void run() {
            while (!shutdown || !tasks.isEmpty()) {
                try {
                    FutureTask<?> task = tasks.poll(WORKER_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        try {
                            task.run();
                            LOGGER.debug("SimpleThreadPool - msg=[Task executed successfully], workerName=[{}]",
                                    Thread.currentThread().getName());
                        } catch (Exception e) {
                            LOGGER.error("SimpleThreadPool - msg=[Task execution failed], workerName=[{}]",
                                    Thread.currentThread().getName(), e);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.info("SimpleThreadPool - msg=[Worker thread interrupted, shutting down], workerName=[{}]",
                            Thread.currentThread().getName());
                    break;
                }
            }
            LOGGER.info("SimpleThreadPool - msg=[Worker thread exiting], workerName=[{}]",
                    Thread.currentThread().getName());
        }
    }
}
