package com.automation.secret.filter;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

@Component
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    private static final String START_TIME_ATTR_NAME = "START_TIME";
    private static final int MAX_DURATION_MS = 1000;
    private static final double SECONDS_MULTIPLIER = 100.0;

    public RequestLoggingFilter() {
        setBeforeMessagePrefix("");
        setBeforeMessageSuffix("");
        setAfterMessagePrefix("");
        setAfterMessageSuffix("");
        setIncludeHeaders(false);
        setIncludeClientInfo(false);
        setIncludePayload(false);
        setIncludeQueryString(true);
    }

    @Override
    protected void beforeRequest(@Nonnull HttpServletRequest request, @Nonnull String message) {
        defineMdcContextBeforeRequest(request);
        log.info("[START:    {} {}]", request.getMethod(), request.getRequestURI());

        request.setAttribute(START_TIME_ATTR_NAME, System.currentTimeMillis());
    }

    @Override
    protected void afterRequest(@Nonnull HttpServletRequest request, @Nonnull String message) {
        try {
            final long startTime = (long) request.getAttribute(START_TIME_ATTR_NAME);
            final long endTime = System.currentTimeMillis();
            final String requestDuration = prettyPrintMillisecondDuration(endTime - startTime);

            defineMdcContextAfterRequest(requestDuration);

            log.info("[COMPLETE: {} {}] ({})",
                request.getMethod(), request.getRequestURI(), requestDuration
            );
        } finally {
            MDC.clear();
        }
    }

    private String prettyPrintMillisecondDuration(long durationMs) {
        if (durationMs > MAX_DURATION_MS) {
            final double seconds = (double) durationMs / MAX_DURATION_MS;
            return String.format(Locale.ROOT, "%.2g s", Math.round(seconds * SECONDS_MULTIPLIER) / SECONDS_MULTIPLIER);
        } else {
            return durationMs + " ms";
        }
    }

    private void defineMdcContextBeforeRequest(HttpServletRequest request) {
        MDC.put("requestUri", request.getRequestURI());
        MDC.put("requestUriPattern", extractRequestUriPattern(request));
        MDC.put("httpMethod", request.getMethod());
        MDC.put("remoteAddress", request.getRemoteAddr());
    }

    private void defineMdcContextAfterRequest(String requestDuration) {
        MDC.put("requestDurationMs", requestDuration);
    }

    static String extractRequestUriPattern(@Nonnull HttpServletRequest request) {
        final String uri = request.getRequestURI();
        if (uri.length() == 1) {
            return uri;
        }

        if (!uri.startsWith("/")) {
            return uri;
        }

        StringBuilder output = new StringBuilder();

        String[] segments = splitByWholeSeparatorPreserveAllTokens(uri, "/");

        for (int i = 1; i < segments.length; i++) {
            String segment = segments[i];
            if (isNumeric(segment)) {
                output.append("/*");
            } else {
                output.append("/").append(segment);
            }
        }

        return output.toString();
    }
}
