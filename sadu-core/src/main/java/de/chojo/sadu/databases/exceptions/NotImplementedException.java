/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases.exceptions;

/**
 * Thrown when a method is called which is not implemented by this type.
 */
public class NotImplementedException extends RuntimeException{
    public NotImplementedException(String message) {
        super(message);
    }
}
