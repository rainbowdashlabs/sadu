/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

public class QueryReplacement {
    private final String target;
    private final String replacement;

    /**
     * Creates a new replacement
     *
     * @param target      regular expression for the target
     * @param replacement replacement of the target
     */
    public QueryReplacement(String target, String replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    public String apply(String source) {
        return source.replaceAll(target, replacement);
    }
}
