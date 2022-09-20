/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.exceptions;

public class MappingAlreadyRegisteredException extends RuntimeException {
    public MappingAlreadyRegisteredException(String message) {
        super(message);
    }
}
