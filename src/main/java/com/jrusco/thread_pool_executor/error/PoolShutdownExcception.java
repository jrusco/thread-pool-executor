package com.jrusco.thread_pool_executor.error;

public class PoolShutdownExcception extends Exception{

    public PoolShutdownExcception() {
        super("The thread pool executor has been shutdown");
    }

    public PoolShutdownExcception(String message) {
        super(message);
    }

}
