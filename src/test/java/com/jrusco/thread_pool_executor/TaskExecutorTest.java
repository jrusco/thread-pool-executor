package com.jrusco.thread_pool_executor;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskExecutorTest {

    static class SimpleTaskExecutor implements TaskExecutor {
        private final ExecutorService executor = Executors.newSingleThreadExecutor();
        private volatile boolean isShutdown = false;

        @Override
        public void execute(Runnable task) {
            if (isShutdown) {
                throw new RuntimeException("Executor is shutdown");
            }
            executor.execute(task);
        }

        @Override
        public <T> Future<T> execute(Callable<T> task) {
            if (isShutdown) {
                throw new RuntimeException("Executor is shutdown");
            }
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
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted while awaiting termination", ex);
            }
            return Collections.emptyList();
        }

        @Override
        public void close() {
            shutdown();
        }

        @Override
        public boolean isShutdown() {
            return isShutdown;
        }
    }

    @Test
    void test_givenExecuteRunnable_whenRunnableSubmitted_thenRunnableIsExecuted() throws InterruptedException {
        // Arrange
        try (SimpleTaskExecutor exec = new SimpleTaskExecutor()) {
            final CountDownLatch latch = new CountDownLatch(1);

            // Act
            exec.execute(latch::countDown);

            // Assert
            assertTrue(latch.await(1, TimeUnit.SECONDS), "Runnable should decrement latch within timeout");
        }
    }

    @Test
    void test_givenExecuteCallable_whenCallableSubmitted_thenReturnsExpectedResult() throws Exception {
        // Arrange
        try (SimpleTaskExecutor exec = new SimpleTaskExecutor()) {
            // Act
            Future<Integer> future = exec.execute(() -> 42);

            // Assert
            assertEquals(42, future.get(), "Callable should return 42");
        }
    }

    @Test
    void test_givenShutdown_whenCalled_thenExecutorIsShutdownAndReturnsEmptyList() {
        // Arrange
        try (SimpleTaskExecutor exec = new SimpleTaskExecutor()) {
            // Act
            List<Runnable> tasks = exec.shutdown();

            // Assert
            assertTrue(tasks.isEmpty(), "Shutdown should return empty list");
            assertTrue(exec.isShutdown(), "Executor should be marked as shutdown");
        }
    }

    @Test
    void test_givenShutdownAndAwaitTermination_whenCalled_thenExecutorIsShutdownAndReturnsEmptyList() {
        // Arrange
        try (SimpleTaskExecutor exec = new SimpleTaskExecutor()) {
            // Act
            List<Runnable> tasks = exec.shutdownAndAwaitTermination();

            // Assert
            assertTrue(tasks.isEmpty(), "ShutdownAndAwaitTermination should return empty list");
            assertTrue(exec.isShutdown(), "Executor should be marked as shutdown");
        }
    }

    @Test
    void test_givenExecuteAfterShutdown_whenSubmittingTask_thenThrowsException() {
        // Arrange
        try (SimpleTaskExecutor exec = new SimpleTaskExecutor()) {
            exec.shutdown();

            // Act & Assert
            assertThrows(RuntimeException.class, () -> exec.execute(() -> {
            }), "Should throw when executing Runnable after shutdown");
            assertThrows(RuntimeException.class, () -> exec.execute(() -> 1),
                    "Should throw when executing Callable after shutdown");
        }
    }

    @Test
    void test_givenTaskThrowsException_whenExecuted_thenExecutorContinuesOperating() throws Exception {
        // Arrange
        try (SimpleTaskExecutor exec = new SimpleTaskExecutor()) {
            // Act
            exec.execute(() -> {
                throw new RuntimeException("fail");
            });

            // Submit another task to ensure executor is still operational
            Future<Integer> future = exec.execute(() -> 123);

            // Assert
            assertEquals(123, future.get(), "Executor should continue operating after a task throws");
        }
    }
}
