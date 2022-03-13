/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.datasource.stage;

import de.chojo.sqlutil.datasource.DbConfig;

/**
 * A stage which represents a property creator for a data source.
 */
public interface PropertyStage {
    /**
     * Set a property of this stage
     *
     * @param key   key
     * @param value value
     * @return PropertyStage with value set.
     */
    PropertyStage withProperty(String key, String value);

    /**
     * Set the address.
     *
     * @param address address
     * @return PropertyStage with value set.
     */
    PropertyStage withAddress(String address);

    /**
     * Set the port.
     *
     * @param portNumber port
     * @return PropertyStage with value set.
     */
    PropertyStage withPort(String portNumber);

    /**
     * Set the port.
     *
     * @param portNumber port
     * @return PropertyStage with value set.
     */
    PropertyStage withPort(int portNumber);

    /**
     * Set the user
     *
     * @param user user
     * @return PropertyStage with value set.
     */
    PropertyStage withUser(String user);

    /**
     * Set the password
     *
     * @param password password
     * @return PropertyStage with value set.
     */
    PropertyStage withPassword(String password);

    /**
     * Set the database name.
     *
     * @param database database name
     * @return PropertyStage with value set.
     */
    PropertyStage forDatabase(String database);

    /**
     * Applies a config onto the properties. Null values will be ignored.
     *
     * @param config config
     * @return PropertyStage with value set.
     */
    PropertyStage withSettings(DbConfig config);

    /**
     * Create a configuration with these properties.
     *
     * @return a configuration stage with a configuration with applied properties.
     */
    ConfigurationStage create();
}
