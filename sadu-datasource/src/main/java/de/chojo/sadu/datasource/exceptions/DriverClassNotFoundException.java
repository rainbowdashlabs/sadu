/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource.exceptions;

import java.io.Serial;

/**
 * Thrown when a driver class is not found.
 */

public class DriverClassNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1;

    public DriverClassNotFoundException(String message, ClassNotFoundException e) {
        super(message, e);
    }
}
