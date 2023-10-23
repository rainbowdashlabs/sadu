/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import java.util.ArrayList;

/**
 * Splits a string of SQL statements into an array of statements.
 * <p>
 *     This class is used to split a string of SQL statements into an array of statements.
 *     The delimiter is not case insensitive.
 * </p>
 */
public class StatementSplitter {

    /**
     * This is the default regex, which is regularly used to split statements.
     * <p>
     *     The default splitting regex is ";".
     * </p>
     * <p>
     *     To change this use {@link #split(String, String)}.
     * </p>
     */
    public static final String DEFAULT_SPLIT_REGEX = ";";
    /**
     * This is the default delimiter keyword, which is regularly used to change the delimiter.
     * <p>
     *     The default delimiter keyword is "DELIMITER".
     * </p>
     * <p>
     *     To change this use {@link #split(String, String, String)}.
     * </p>
     */
    public static final String DEFAULT_DELIMITER_KEYWORD = "DELIMITER";

    /**
     * Splits a string of SQL statements into an array of statements.
     * <p>
     *     This method is used to split a string of SQL statements into an array of statements.
     *     The splitting is done by the regex, which is defined in the second argument of the method.
     *     The delimiter is defined in the third argument of the method.
     * </p>
     * @param statements the statements to split
     * @param splitRegex the regex used to split the statements
     * @param delimiter the delimiter used to change the delimiter in the statements
     * @return the array of statements
     */
    public static String[] split(String statements, String splitRegex, String delimiter) {

        int index = 0;

        ArrayList<String> result = new ArrayList<>();

        while (index <= statements.length()) {

            int nextDelimiterIndex = indexOfIgnoreCase(statements.substring(index), delimiter);
            int nextSplitRegexIndex = indexOfIgnoreCase(statements.substring(index), splitRegex);

            if (nextDelimiterIndex == -1 && nextSplitRegexIndex == -1) {
                break;
            }

            if (nextSplitRegexIndex == -1) {
                splitRegex = parseNewDelimiter(statements.substring(index + nextDelimiterIndex), delimiter);
            } else if (nextDelimiterIndex != -1 &&nextDelimiterIndex < nextSplitRegexIndex) {
                splitRegex = parseNewDelimiter(statements.substring(index + nextDelimiterIndex), delimiter);
            }

            int statementLength = indexOfIgnoreCase(statements.substring(index), splitRegex);
            statementLength += splitRegex.length();

            result.add(statements.substring(index, index + statementLength));

            index += statementLength;

        }

        return result.toArray(new String[0]);

    }

    /**
     * Splits a string of SQL statements into an array of statements.
     * <p>
     *     The splitting is done by the regex, which is defined in the second argument of the method.
     *     The default delimiter keyword is "DELIMITER" ({@link #DEFAULT_DELIMITER_KEYWORD}).
     *     The delimiter is not case insensitive.
     * </p>
     * @param statements the statements to split
     * @param splitRegex the regex used to split the statements
     * @return the array of statements
     */
    public static String[] split(String statements, String splitRegex) {
        return split(statements, splitRegex, DEFAULT_DELIMITER_KEYWORD);
    }

    /**
     * Splits a string of SQL statements into an array of statements.
     * <p>
     *     The splitting is done by the regex, which default is ";" ({@link #DEFAULT_SPLIT_REGEX}).
     *     The default delimiter keyword is "DELIMITER" ({@link #DEFAULT_DELIMITER_KEYWORD}).
     *     The delimiter is not case insensitive.
     * </p>
     * @param statements the statements to split
     * @return the array of statements
     */
    public static String[] split(String statements) {
        return split(statements, DEFAULT_SPLIT_REGEX, DEFAULT_DELIMITER_KEYWORD);
    }

    private static String parseNewDelimiter(String string, String delimiter) {

        for (String currentString : string.split(" ")) {
            if (currentString.equalsIgnoreCase(delimiter)) {
                continue;
            } else if (currentString.equalsIgnoreCase("")) {
                continue;
            }
            return currentString;
        }

        return "";

    }

    private static int indexOfIgnoreCase(String string, String substring) {
        return string.toLowerCase().indexOf(substring.toLowerCase());
    }

}
