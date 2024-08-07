/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.exception;

import java.util.List;

public interface ExceptionHolder {
    List<Exception> exceptions();

    void logException(Exception e);
}
