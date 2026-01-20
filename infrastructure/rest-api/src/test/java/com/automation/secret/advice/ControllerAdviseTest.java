package com.automation.secret.advice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.automation.secret.exception.NoTestUserAvailableException;
import org.junit.jupiter.api.Test;

class ControllerAdviseTest {
    private final ControllerAdvise advise = new ControllerAdvise();

    @Test
    void shouldHandleNoTestUserAvailableException() {
        final NoTestUserAvailableException exception = mock(NoTestUserAvailableException.class);

        assertDoesNotThrow(() -> advise.handleNotFoundExceptions(exception));
    }

    @Test
    void shouldHandleIllegalStateException() {
        final IllegalStateException exception = mock(IllegalStateException.class);
        when(exception.getMessage()).thenReturn("Test illegal state");

        assertDoesNotThrow(() -> advise.handleIllegalStateException(exception));
    }

    @Test
    void shouldHandleException() {
        final Exception exception = mock(Exception.class);
        when(exception.getMessage()).thenReturn("Test exception");

        assertDoesNotThrow(() -> advise.handleException(exception));
    }
}
