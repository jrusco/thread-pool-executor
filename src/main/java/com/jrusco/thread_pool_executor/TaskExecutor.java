package com.jrusco.thread_pool_executor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import com.jrusco.thread_pool_executor.error.PoolShutdownException;

public interface TaskExecutor extends Executor, AutoCloseable{
    
    void execute(Runnable task) throws PoolShutdownException;

    <T> Future<T> execute(Callable<T> task) throws PoolShutdownException;

    List<Runnable> shutdown();

    List<Runnable> shutdownAndAwaitTermination();

    boolean isShutdown();
}
