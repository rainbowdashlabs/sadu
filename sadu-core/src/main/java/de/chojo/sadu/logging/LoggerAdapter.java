/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.logging;

public interface LoggerAdapter {
    static LoggerAdapter wrap(java.util.logging.Logger logger) {
        return new JavaLogger(logger);
    }

    static LoggerAdapter wrap(org.slf4j.Logger logger) {
        return new Slf4jLogger(logger);
    }

    void error(String message);

    void error(String message, Throwable t);

    void info(String message);

    void info(String message, Throwable t);

    void debug(String message);

    void debug(String message, Throwable t);

    void warn(String message);

    void warn(String message, Throwable t);
}
