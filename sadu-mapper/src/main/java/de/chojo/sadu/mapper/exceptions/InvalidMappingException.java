/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.exceptions;

public class InvalidMappingException extends RuntimeException {

    public InvalidMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMappingException(String message) {
        super(message);
    }
}
