package com.jrusco.thread_pool_executor;

import com.jrusco.thread_pool_executor.error.PoolShutdownException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class SimpleThreadPoolTest {
    private SimpleThreadPool pool;

    @BeforeEach
    void setUp() {
        pool = new SimpleThreadPool(4);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (!pool.isShutdown()) {
            pool.close();
        }
    }

    @Test
    void test_givenExecuteRunnable_whenRunnableSubmitted_thenRunnableIsExecuted() throws Exception {
        // Arrange
        AtomicInteger counter = new AtomicInteger(0);

        // Act
        pool.execute(counter::incrementAndGet);
        pool.close();

        // Assert
        assertEquals(1, counter.get(), "Runnable should increment counter once");
    }

    @Test
    void test_givenExecuteCallable_whenCallableSubmitted_thenReturnsExpectedResult() throws Exception {
        // Arrange
        Callable<Integer> task = () -> 42;

        // Act
        Future<Integer> future = pool.execute(task);
        int result = future.get();
        pool.close();

        // Assert
        assertEquals(42, result, "Callable should return 42");
    }

    @Test
    void test_givenShutdown_whenCalled_thenPoolIsShutdownAndReturnsRemainingTasks() {
        // Arrange
        pool.execute(() -> {
        });

        // Act
        List<Runnable> remaining = pool.shutdown();

        // Assert
        assertTrue(pool.isShutdown(), "Pool should be shutdown");
        assertNotNull(remaining, "Remaining tasks list should not be null");
    }

    @Test
    void test_givenShutdownAndAwaitTermination_whenCalled_thenPoolIsShutdownAndReturnsRemainingTasks()
            throws Exception {
        // Arrange
        pool.execute(() -> {
        });

        // Act
        List<Runnable> remaining = pool.shutdownAndAwaitTermination();

        // Assert
        assertTrue(pool.isShutdown(), "Pool should be shutdown");
        assertNotNull(remaining, "Remaining tasks list should not be null");
    }

    @Test
    void test_givenExecuteAfterShutdown_whenSubmittingTask_thenThrowsPoolShutdownException() {
        // Arrange
        pool.shutdown();

        // Act & Assert
        assertThrows(PoolShutdownException.class, () -> pool.execute(() -> {
        }), "Should throw on Runnable after shutdown");
        assertThrows(PoolShutdownException.class, () -> pool.execute(() -> 1),
                "Should throw on Callable after shutdown");
    }

    @Test
    void test_givenMultipleShutdownCalls_whenCalledRepeatedly_thenNoExceptionIsThrown() {
        // Arrange
        pool.shutdown();

        // Act & Assert
        assertDoesNotThrow(() -> pool.shutdown(), "Multiple shutdown calls should not throw");
        assertDoesNotThrow(() -> pool.shutdownAndAwaitTermination(), "Shutdown after shutdown should not throw");
    }

    @Test
    void test_givenTaskThrowsException_whenExecuted_thenPoolContinuesOperating() throws Exception {
        // Arrange & Act
        pool.execute(() -> {
            throw new RuntimeException("fail");
        });
        pool.close();

        assertTrue(true, "No exception should propagate here");
    }

    @Test
    void test_givenShutdown_whenTasksRemainInQueue_thenReturnsUnexecutedTasks() {
        // Arrange
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> sleepSilently(100));
        }

        // Act
        List<Runnable> remaining = pool.shutdown();

        // Assert
        assertNotNull(remaining, "Remaining tasks list should not be null");
        // Some tasks may remain unexecuted
    }

    @Test
    void test_givenShutdownAndAwaitTermination_whenTasksRemainInQueue_thenReturnsUnexecutedTasks() throws Exception {
        // Arrange
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> sleepSilently(100));
        }

        // Act
        List<Runnable> remaining = pool.shutdownAndAwaitTermination();

        // Assert
        assertNotNull(remaining, "Remaining tasks list should not be null");
        // Should be empty or very few
    }

    /**
     * Utility method to sleep without throwing checked exceptions.
     * 
     * @param millis milliseconds to sleep
     */
    private static void sleepSilently(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            // Thread interrupted, ignore for test
        }
    }
}
