package com.jrusco.thread_pool_executor.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoolShutdownExcceptionTest {
    @Test
    void testDefaultMessage() {
        PoolShutdownExcception ex = new PoolShutdownExcception();
        assertEquals("The thread pool executor has been shutdown", ex.getMessage());
    }

    @Test
    void testCustomMessage() {
        PoolShutdownExcception ex = new PoolShutdownExcception("Custom message");
        assertEquals("Custom message", ex.getMessage());
    }
}
