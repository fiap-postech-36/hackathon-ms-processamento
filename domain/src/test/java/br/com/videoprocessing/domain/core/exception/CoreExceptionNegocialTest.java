package br.com.videoprocessing.domain.core.exception;

import br.com.videoprocessing.domain.core.domain.exception.CoreExceptionNegocial;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoreExceptionNegocialTest {

    @Test
    void testDefaultConstructor() {
        CoreExceptionNegocial exception = new CoreExceptionNegocial();
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Custom error message";
        CoreExceptionNegocial exception = new CoreExceptionNegocial(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Error with cause";
        Exception cause = new Exception("Cause exception");
        CoreExceptionNegocial exception = new CoreExceptionNegocial(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
