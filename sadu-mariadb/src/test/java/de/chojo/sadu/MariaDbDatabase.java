/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class MariaDbDatabase {
        public static GenericContainer<?> createContainer(String user/*ignored*/, String pw) {
        GenericContainer<?> self = new GenericContainer<>(DockerImageName.parse("mariadb:latest"))
                .withExposedPorts(3306)
                .withEnv("MARIADB_ROOT_PASSWORD", pw)
                .waitingFor(Wait.forLogMessage(".*mariadbd: ready for connections\\..*", 2));
        self.start();
        return self;
    }
}
