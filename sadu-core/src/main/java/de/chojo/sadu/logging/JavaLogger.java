/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaLogger extends SimpleLogger<Logger> {
    public JavaLogger(Logger logger) {
        super(logger);
    }

    @Override
    public void error(String message) {
        log.severe(message);
    }

    @Override
    public void error(String message, Throwable t) {
        log.log(Level.SEVERE, message, t);
    }

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void info(String message, Throwable t) {
        log.log(Level.INFO, message, t);
    }

    @Override
    public void debug(String message) {
        log.config(message);
    }

    @Override
    public void debug(String message, Throwable t) {
        log.log(Level.CONFIG, message, t);
    }

    @Override
    public void warn(String message) {
        log.warning(message);
    }

    @Override
    public void warn(String message, Throwable t) {
        log.log(Level.WARNING, message, t);
    }
}
