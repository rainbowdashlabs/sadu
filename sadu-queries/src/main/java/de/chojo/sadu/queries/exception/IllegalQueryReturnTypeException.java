/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.exception;

public class IllegalQueryReturnTypeException extends RuntimeQueryException{
    public IllegalQueryReturnTypeException(String message) {
        super(message);
    }
}
