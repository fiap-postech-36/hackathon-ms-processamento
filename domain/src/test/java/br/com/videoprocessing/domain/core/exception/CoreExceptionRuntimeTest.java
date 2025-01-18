package br.com.videoprocessing.domain.core.exception;

import br.com.videoprocessing.domain.core.domain.exception.CoreExceptionRuntime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoreExceptionRuntimeTest {

    @Test
    void testDefaultConstructor() {
        CoreExceptionRuntime exception = new CoreExceptionRuntime();
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Custom runtime error message";
        CoreExceptionRuntime exception = new CoreExceptionRuntime(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Runtime error with cause";
        Exception cause = new Exception("Cause of runtime exception");
        CoreExceptionRuntime exception = new CoreExceptionRuntime(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}

