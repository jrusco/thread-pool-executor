package com.jrusco.thread_pool_executor;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskExecutorTest {
    static class SimpleTaskExecutor implements TaskExecutor {
        private final ExecutorService executor = Executors.newSingleThreadExecutor();
        private boolean isShutdown = false;

        @Override
        public void execute(Runnable task) {
            if (isShutdown)
                throw new RuntimeException("Executor is shutdown");
            executor.execute(task);
        }

        @Override
        public <T> Future<T> execute(Callable<T> task) {
            if (isShutdown)
                throw new RuntimeException("Executor is shutdown");
            return executor.submit(task);
        }

        @Override
        public List<Runnable> shutdown() {
            isShutdown = true;
            executor.shutdown();
            return Collections.emptyList();
        }

        @Override
        public List<Runnable> shutdownAndAwaitTermination() {
            isShutdown = true;
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
                // In tests, interruption is not expected. If it happens, fail the test.
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted while awaiting termination", ignored);
            }
            return Collections.emptyList();
        }

        @Override
        public void close() {
            shutdown();
        }

        @Override
        public boolean isShutdown() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'isShutdown'");
        }
    }

    @Test
    void testExecuteRunnable() throws InterruptedException {
        try (SimpleTaskExecutor exec = new SimpleTaskExecutor()) {
            final CountDownLatch latch = new CountDownLatch(1);
            exec.execute(latch::countDown);
            assertTrue(latch.await(1, TimeUnit.SECONDS));
        }
    }

    @Test
    void testExecuteCallable() throws Exception {
        try (SimpleTaskExecutor exec = new SimpleTaskExecutor()) {
            Future<Integer> future = exec.execute(() -> 42);
            assertEquals(42, future.get());
        }
    }

    @Test
    void testShutdown() {
        try (SimpleTaskExecutor exec = new SimpleTaskExecutor()) {
            List<Runnable> tasks = exec.shutdown();
            assertTrue(tasks.isEmpty());
        }
    }

    @Test
    void testShutdownAndAwaitTermination() {
        try (SimpleTaskExecutor exec = new SimpleTaskExecutor()) {
            List<Runnable> tasks = exec.shutdownAndAwaitTermination();
            assertTrue(tasks.isEmpty());
        }
    }
}
