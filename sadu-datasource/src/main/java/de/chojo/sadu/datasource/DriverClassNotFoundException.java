/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource;

/**
 * Thrown when a driver class is not found.
 */

public class DriverClassNotFoundException extends RuntimeException {

    public DriverClassNotFoundException(String message, ClassNotFoundException e) {
        super(message, e);
    }
}
