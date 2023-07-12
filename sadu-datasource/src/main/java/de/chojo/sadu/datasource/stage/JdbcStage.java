/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource.stage;

import javax.annotation.CheckReturnValue;
import java.util.function.Consumer;

/**
 * A stage which represents a property creator for a data source.
 */
public interface JdbcStage<T> {

    /**
     * configure the jdbc builder
     *
     * @param builder builder
     * @return builder instance
     */
    @CheckReturnValue
    JdbcStage<T> configure(Consumer<T> builder);

    /**
     * Create a configuration with these properties.
     *
     * @return a configuration stage with a configuration with applied properties.
     */
    ConfigurationStage create();
}
