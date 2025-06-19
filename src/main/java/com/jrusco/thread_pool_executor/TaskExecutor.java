package com.jrusco.thread_pool_executor;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public interface TaskExecutor extends Executor, AutoCloseable{
    
    void execute(Runnable task);

    <T> Future<T> execute(Callable<T> task);

    Set<Runnable> shutdown();

    Set<Runnable> shutdownAndAwaitTermination();
}
