/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson.exception;

public class UnknownTypeException extends RuntimeException{
    public UnknownTypeException(String name) {
        super("Unknown type " + name + " found. Please Register a mapper for this type.");
    }
}
