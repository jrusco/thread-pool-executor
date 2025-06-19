package com.jrusco.thread_pool_executor.error;

public class PoolShutdownException extends RuntimeException {

    public PoolShutdownException() {
        super("The thread pool executor has been shutdown");
    }

    public PoolShutdownException(String message) {
        super(message);
    }

}
