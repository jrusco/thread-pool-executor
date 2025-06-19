package com.jrusco.thread_pool_executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    private boolean isShutdown = false;
    private LinkedBlockingQueue<FutureTask<?>> tasks;
    private List<Thread> workerThreads;

    public SimpleThreadPool(int poolSize) {

        if (poolSize <= 0){
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
        if (isShutdown) {
            LOGGER.warn("Cannot queue task, thread pool is shutting down");
            throw new PoolShutdownException();
        }
        
        tasks.add(new FutureTask<>(task, null));
        LOGGER.debug("task added to the queue successfully");
    }

    @Override
    public <T> Future<T> execute(Callable<T> task) throws PoolShutdownException {
        if (isShutdown) {
            LOGGER.warn("Cannot queue task, thread pool is shutting down");
            throw new PoolShutdownException();
        }

        FutureTask<T> futureTask = new FutureTask<>(task);
        tasks.add(futureTask);
        LOGGER.debug("task added to the queue successfully");
        return futureTask;
    }
    
    @Override
    public List<Runnable> shutdown() {
        LOGGER.info("Shutdown requested.");
        this.isShutdown = true;
        for (Thread worker : workerThreads) {
            worker.interrupt();
        }
        LOGGER.info("Shut down completed. Remaining tasks:{}", tasks.size());
        return new ArrayList<>(tasks);
    }

    @Override
    public List<Runnable> shutdownAndAwaitTermination() {
        LOGGER.info("Shutdown and await termination requested.");
        this.isShutdown = true;

        for (Thread worker : workerThreads) {
            worker.interrupt();
        }

        for (Thread worker : workerThreads) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warn("Thread interrupted while waiting for worker to terminate: {}", worker.getName());
            }
        }

        return new ArrayList<>(tasks);
    }

    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }
    
    private class Worker implements Runnable {
        private static final int WORKER_TIMEOUT_MS = 100;

        @Override
        public void run() {
            while (!isShutdown || !tasks.isEmpty()) {
                try {
                    FutureTask<?> task = tasks.poll(WORKER_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        try {
                            task.run();
                        } catch (Exception e) {
                            LOGGER.error("Task execution failed", e);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.info("Worker thread interrupted, shutting down.");
                    break;
                }
            }
            LOGGER.info("Worker thread exiting.");
        }
}
}
