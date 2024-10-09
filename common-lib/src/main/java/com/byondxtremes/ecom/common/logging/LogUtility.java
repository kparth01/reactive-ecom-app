package com.byondxtremes.ecom.common.logging;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Utility class that provide all functionality required for logging purposes.
 * All logging should be done through this utility class
 */
@Component
public class LogUtility {

    private Logger logger;

    public LogUtility() {
        logger = LoggerFactory.getLogger(LogUtility.class);
    }

    public LogUtility(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Write an INFO level log, use this only if event-specific logging is not possible.
     *
     * @param message input message to display
     */
    public void info(String message) {
        message = sanitizeMessage(message);
        logger.info(message);
    }

    /**
     * Write an ERROR level log, use this only if event-specific logging is not possible.
     *
     * @param message input message to display
     */
    public void error(String message) {
        UUID uuid = UUID.randomUUID();
        message = sanitizeMessage(message);
        logger.error(uuid.toString().concat(" ").concat(message));
    }

    /**
     * Write an ERROR level log, use this only if event-specific logging is not possible.
     *
     * @param message input message to display
     * @param throwable input throwable to display stacktrace
     */
    public void error(String message, Throwable throwable) {
        UUID uuid = UUID.randomUUID();
        message = sanitizeMessage(message);
        logger.error(uuid.toString().concat(" ").concat(message), throwable);
    }

    public String sanitizeMessage(String input) {
        return input.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
            .replaceAll("(?i)<script.*?/>", "")
            .replaceAll("(?i)<script.*?>", "")
            .replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "")
            .replaceAll("(?i)<.*?javascript:.*?/>", "")
            .replaceAll("(?i)<.*?javascript:.*?>", "")
            .replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "")
            .replaceAll("(?i)<.*?\\s+on.*?/>", "")
            .replaceAll("(?i)<.*?\\s+on.*?>", "");
    }

}

