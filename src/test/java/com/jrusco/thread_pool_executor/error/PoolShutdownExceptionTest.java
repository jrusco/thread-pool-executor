package com.jrusco.thread_pool_executor.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoolShutdownExceptionTest {
    @Test
    void testDefaultMessage() {
        PoolShutdownException ex = new PoolShutdownException();
        assertEquals("The thread pool executor has been shutdown", ex.getMessage());
    }

    @Test
    void testCustomMessage() {
        PoolShutdownException ex = new PoolShutdownException("Custom message");
        assertEquals("Custom message", ex.getMessage());
    }
}
