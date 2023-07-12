/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

/**
 * Represents an exception thrown while the database got updated.
 */
public class UpdateException extends RuntimeException {
    /**
     * A new update exception
     *
     * @param message message
     */
    public UpdateException(String message) {
        super(message);
    }

    /**
     * /**
     * A new update exception
     *
     * @param message message
     * @param cause   cause
     */
    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
