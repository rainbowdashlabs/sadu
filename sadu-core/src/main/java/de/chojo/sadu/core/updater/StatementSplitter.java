/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.core.updater;

import org.intellij.lang.annotations.RegExp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Splits a string of SQL statements into an array of statements.
 * <p>
 * This class is used to split a string of SQL statements into an array of statements.
 * The delimiter is not case sensitive.
 * </p>
 */
public final class StatementSplitter {
    /**
     * The default marker, which is regularly used to split statements.
     * <p>
     * The default splitting regex is ";".
     * </p>
     * <p>
     * To change this use {@link #split(String, String)}.
     * </p>
     */
    public static final String DEFAULT_SPLIT_MARKER = ";";
    /**
     * This is the default delimiter keyword, which is regularly used to change the delimiter.
     * <p>
     * The default delimiter keyword is "DELIMITER".
     * </p>
     * <p>
     * To change this use {@link #split(String, String, String)}.
     * </p>
     */
    public static final String DEFAULT_DELIMITER_KEYWORD = "DELIMITER";
    private static final Pattern CLEANER = Pattern.compile("^([ \n\r]+)");
    @RegExp
    private static final String DELIMITER_FORMAT = "^%s (?<delimiter>[^ \n\r]+)";
    private StatementSplitter() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Splits a string of SQL statements into an array of statements.
     * <p>
     * The splitting is done by the regex, which default is ({@link #DEFAULT_SPLIT_MARKER}).
     * The default delimiter keyword is ({@link #DEFAULT_DELIMITER_KEYWORD}).
     * The delimiter is not case sensitive.
     * </p>
     *
     * @param sql         the statements to split
     * @param splitMarker the marker used to split the statements
     * @param delimiter   the delimiter keyword to change the delimiter in the statements
     * @return the array of statements
     */
    public static String[] split(String sql, String splitMarker, String delimiter) {
        // Pattern to extract new delimiter
        var delimiterPattern = Pattern.compile(DELIMITER_FORMAT.formatted(delimiter), Pattern.CASE_INSENSITIVE);
        // Pattern to split statements
        var splitter = Pattern.compile(splitMarker);
        // The remaining statements that are not yet parsed
        var remaining = sql;
        List<String> statements = new ArrayList<>();
        while (!remaining.isBlank()) {

            remaining = CLEANER.matcher(remaining).replaceAll("");
            if (remaining.isBlank()) break;
            Matcher delimiterCheck = delimiterPattern.matcher(remaining);
            // Check if next statement is internal delimiter
            if (delimiterCheck.find()) {
                // Extract delimiter
                splitter = Pattern.compile(Pattern.quote(delimiterCheck.group("delimiter")));
                // Remove the delimiter statement
                remaining = delimiterCheck.reset().replaceAll("");
                continue;
            }

            var split = splitter.split(remaining, 2);
            // Replace the remaining statements
            remaining = split.length == 2 ? split[1] : "";

            // Store current frame with correct sql delimiter
            if (!split[0].isBlank()) statements.add(split[0] + ";");
        }

        return statements.toArray(new String[0]);
    }

    /**
     * Splits a string of SQL statements into an array of statements.
     * <p>
     * The splitting is done by the marker
     * The default delimiter keyword is {@link #DEFAULT_DELIMITER_KEYWORD}.
     * The delimiter is not case sensitive.
     * </p>
     *
     * @param sql         the statements to split
     * @param splitMarker the marker used to split the statements
     * @return the array of statements
     */
    public static String[] split(String sql, String splitMarker) {
        return split(sql, splitMarker, DEFAULT_DELIMITER_KEYWORD);
    }

    /**
     * Splits a string of SQL statements into an array of statements.
     * <p>
     * The splitting is done by the regex, which default is ({@link #DEFAULT_SPLIT_MARKER}).
     * The default delimiter keyword is ({@link #DEFAULT_DELIMITER_KEYWORD}).
     * The delimiter is not case sensitive.
     * </p>
     *
     * @param sql the statements to split
     * @return the array of statements
     */
    public static String[] split(String sql) {
        return split(sql, DEFAULT_SPLIT_MARKER, DEFAULT_DELIMITER_KEYWORD);
    }
}
