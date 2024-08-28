/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.annotation;

import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.mapper.wrapper.Row;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a constructor or method as a {@link RowMapping} provider.
 * <p>
 * A Method needs to return a {@link RowMapping} which maps to the annotated class.
 * <p>
 * A Constructor needs to accept only a {@link Row}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface MappingProvider {
    String[] value();
}
