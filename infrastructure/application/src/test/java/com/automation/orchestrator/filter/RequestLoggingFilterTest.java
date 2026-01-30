package com.automation.orchestrator.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class RequestLoggingFilterTest {

    private static final String START_TIME_ATTR_NAME = "START_TIME";
    private static final String URI_ROOT = "/";
    private static final String URI_WITH_NUMERIC_SEGMENTS = "/api/123/users/456";
    private static final String URI_WITHOUT_LEADING_SLASH = "api/123/users";
    private static final String REMOTE_ADDR = "127.0.0.1";
    private static final String HTTP_METHOD = "GET";
    private static final String USER_ID = "user-123";

    @Mock
    private HttpServletRequest request;

    private RequestLoggingFilter filter;

    @BeforeEach
    void setUp() {
        filter = new RequestLoggingFilter();
        MDC.clear();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void should_putMdcContext_and_setStartTimeAttribute_when_beforeRequestCalled_with_authenticatedUser() {
        final String uri = "/api/test/1";

        when(request.getMethod()).thenReturn(HTTP_METHOD);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getRemoteAddr()).thenReturn(REMOTE_ADDR);

        filter.beforeRequest(request, "ignored");

        assertThat(MDC.get("requestUri")).isEqualTo(uri);
        assertThat(MDC.get("requestUriPattern")).isEqualTo("/api/test/*");
        assertThat(MDC.get("httpMethod")).isEqualTo(HTTP_METHOD);
        assertThat(MDC.get("remoteAddress")).isEqualTo(REMOTE_ADDR);

        verify(request).setAttribute(eq(START_TIME_ATTR_NAME), anyLong());
    }

    @Test
    void should_useAnonymousUser_when_beforeRequestCalled_withoutUserHeader() {
        when(request.getMethod()).thenReturn(HTTP_METHOD);
        when(request.getRequestURI()).thenReturn("/public");
        when(request.getRemoteAddr()).thenReturn(REMOTE_ADDR);

        filter.beforeRequest(request, "ignored");
    }

    @Test
    void should_clearMdcContext_when_afterRequestCalled() {
        final long startTime = System.currentTimeMillis() - 1_500L;

        when(request.getAttribute(START_TIME_ATTR_NAME)).thenReturn(startTime);
        when(request.getMethod()).thenReturn(HTTP_METHOD);
        when(request.getRequestURI()).thenReturn("/api/test/2");

        MDC.put("requestUri", "/api/test/2");
        MDC.put("httpMethod", HTTP_METHOD);
        MDC.put("userId", USER_ID);
        MDC.put("remoteAddress", REMOTE_ADDR);

        filter.afterRequest(request, "ignored");

        assertThat(MDC.get("requestUri")).isNull();
        assertThat(MDC.get("requestUriPattern")).isNull();
        assertThat(MDC.get("httpMethod")).isNull();
        assertThat(MDC.get("userId")).isNull();
        assertThat(MDC.get("remoteAddress")).isNull();
        assertThat(MDC.get("requestDurationMs")).isNull();

        verify(request).getAttribute(START_TIME_ATTR_NAME);
    }

    @Test
    void should_returnRootUri_when_extractRequestUriPatternCalled_withSingleSlash() {
        when(request.getRequestURI()).thenReturn(URI_ROOT);

        final String actual = RequestLoggingFilter.extractRequestUriPattern(request);

        assertThat(actual).isEqualTo(URI_ROOT);
    }

    @Test
    void should_returnSameUri_when_extractRequestUriPatternCalled_withoutLeadingSlash() {
        when(request.getRequestURI()).thenReturn(URI_WITHOUT_LEADING_SLASH);

        final String actual = RequestLoggingFilter.extractRequestUriPattern(request);

        assertThat(actual).isEqualTo(URI_WITHOUT_LEADING_SLASH);
    }

    @Test
    void should_maskNumericSegments_when_extractRequestUriPatternCalled_withNumericIds() {
        when(request.getRequestURI()).thenReturn(URI_WITH_NUMERIC_SEGMENTS);

        final String actual = RequestLoggingFilter.extractRequestUriPattern(request);

        assertThat(actual).isEqualTo("/api/*/users/*");
    }

    @Test
    void should_preserveNonNumericSegments_when_extractRequestUriPatternCalled() {
        when(request.getRequestURI()).thenReturn("/api/v1/users/details");

        final String actual = RequestLoggingFilter.extractRequestUriPattern(request);

        assertThat(actual).isEqualTo("/api/v1/users/details");
    }

    @Test
    void should_formatMilliseconds_when_prettyPrintMillisecondDurationCalled_withDurationBelowThreshold() throws Exception {
        final long durationMs = 999L;

        final String actual = invokePrettyPrintMillisecondDuration(durationMs);

        assertThat(actual).isEqualTo("999 ms");
    }

    @Test
    void should_formatSecondsWithTwoSignificantDigits_when_prettyPrintMillisecondDurationCalled_withDurationAboveThreshold() throws Exception {
        final long durationMs = 1_500L;

        final String actual = invokePrettyPrintMillisecondDuration(durationMs);

        assertThat(actual).isEqualTo("1.5 s");
    }

    private String invokePrettyPrintMillisecondDuration(final long durationMs) throws Exception {
        final Method method = RequestLoggingFilter.class
            .getDeclaredMethod("prettyPrintMillisecondDuration", long.class);
        method.setAccessible(true);

        return (String) method.invoke(filter, durationMs);
    }

}
