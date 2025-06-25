package com.jrusco.thread_pool_executor.error;

import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFailureAndContinuePolicy implements RejectedTaskExecutionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogFailureAndContinuePolicy.class);

    @Override
    public void handleRejection(FutureTask<?> rejectedTask, String reason) {
        LOGGER.info("msg=[Task rejected by handler], task=[{}], reason=[{}]",
                rejectedTask, reason);
    }

}
