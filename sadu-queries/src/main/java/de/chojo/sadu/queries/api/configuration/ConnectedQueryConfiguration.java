/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.configuration;

import java.sql.Connection;

public interface ConnectedQueryConfiguration extends ActiveQueryConfiguration, AutoCloseable {
    Connection connection();
}
