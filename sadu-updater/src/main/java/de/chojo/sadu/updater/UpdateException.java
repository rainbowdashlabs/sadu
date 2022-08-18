/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

/**
 * Represents a exception thrown while the database got updated.
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
