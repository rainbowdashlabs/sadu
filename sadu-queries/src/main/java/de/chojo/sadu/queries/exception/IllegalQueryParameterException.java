/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.exception;

public class IllegalQueryParameterException extends RuntimeQueryException{
    public IllegalQueryParameterException(String message) {
        super(message);
    }
}
