/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.exceptions;

import java.io.Serial;

public class MappingAlreadyRegisteredException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public MappingAlreadyRegisteredException(String message) {
        super(message);
    }
}
