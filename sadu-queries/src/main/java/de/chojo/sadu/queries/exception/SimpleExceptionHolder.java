/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.exception;

import de.chojo.sadu.queries.api.exception.ExceptionHolder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SimpleExceptionHolder implements ExceptionHolder {
    private final List<Exception> exceptions = new LinkedList<>();

    public SimpleExceptionHolder() {
    }

    @Override
    public List<Exception> exceptions() {
        return Collections.unmodifiableList(exceptions);
    }

    @Override
    public void logException(Exception e) {
        exceptions.add(e);
    }
}
