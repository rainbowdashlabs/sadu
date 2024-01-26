/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class PostgresDatabase {
    public static GenericContainer<?> createContainer(String user, String pw) {
        GenericContainer<?> self = new GenericContainer<>(DockerImageName.parse("postgres:latest"))
                .withExposedPorts(5432)
                .withEnv("POSTGRES_USER", user)
                .withEnv("POSTGRES_PASSWORD", pw)
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 2));
        self.start();
        return self;
    }
}
