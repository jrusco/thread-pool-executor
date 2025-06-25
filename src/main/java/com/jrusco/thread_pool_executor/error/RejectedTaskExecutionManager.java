package com.jrusco.thread_pool_executor.error;

import java.util.concurrent.FutureTask;


public interface RejectedTaskExecutionManager {

  void handleRejection(FutureTask<?> rejectedTask, String reason);

} 
