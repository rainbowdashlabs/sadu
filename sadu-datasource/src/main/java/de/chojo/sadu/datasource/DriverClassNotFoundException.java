/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
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
