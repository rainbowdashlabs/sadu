/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases.exceptions;

/**
 * Thrown when a method is called which is not supported by this type.
 */
public class NotSupportedException extends RuntimeException {
    public NotSupportedException(String message) {
        super(message);
    }
}
