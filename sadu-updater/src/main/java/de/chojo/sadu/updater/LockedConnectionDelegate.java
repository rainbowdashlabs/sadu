/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

import de.chojo.sadu.core.connection.ConnectionDelegate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.concurrent.Executor;

/**
 * A locked connection that can not be used to commit and can not be closed or rolled back.
 */
public class LockedConnectionDelegate extends ConnectionDelegate {
    public LockedConnectionDelegate(Connection connection) {
        super(connection);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        throw new IllegalStateException("This connection can not commit. It is managed externally");
    }

    @Override
    public void commit() throws SQLException {
        throw new IllegalStateException("This connection can not commit. It is managed externally");
    }

    @Override
    public void rollback() throws SQLException {
        throw new IllegalStateException("This connection can not rollback. It is managed externally");
    }

    @Override
    public void close() throws SQLException {
        throw new IllegalStateException("This connection can not be closed. It is managed externally");
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        throw new IllegalStateException("This connection can not rollback. It is managed externally");
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new IllegalStateException("This connection can commit. It is managed externally");
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        throw new IllegalStateException("This connection can not be closed. It is managed externally");
    }
}
