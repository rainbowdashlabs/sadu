/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.core.databases.exceptions;

import java.io.Serial;

/**
 * Thrown when a method is called which is not supported by this type.
 */
public class NotSupportedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public NotSupportedException(String message) {
        super(message);
    }
}
