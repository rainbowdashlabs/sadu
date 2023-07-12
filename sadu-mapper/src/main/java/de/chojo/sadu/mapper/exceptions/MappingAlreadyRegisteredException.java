/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.exceptions;

public class MappingAlreadyRegisteredException extends RuntimeException {
    public MappingAlreadyRegisteredException(String message) {
        super(message);
    }
}
