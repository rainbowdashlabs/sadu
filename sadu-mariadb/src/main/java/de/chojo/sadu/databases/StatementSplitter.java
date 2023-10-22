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
 *     The splitting is done by a regex. The default regex is {@link StatementSplitter#DEFAULT_SPLIT_REGEX}.
 *     The regex can be changed by calling {@link #setStandardSplitRegex(String)}.
 *     The default delimiter is {@link StatementSplitter#DEFAULT_DELIMITER_KEYWORD}.
 *     The delimiter can be changed by calling {@link #setStandardDelimiter(String)}.
 *     The delimiter is not case insensitive.
 * </p>
 */
public class StatementSplitter {

    public static final String DEFAULT_SPLIT_REGEX = ";";
    public static final String DEFAULT_DELIMITER_KEYWORD = "DELIMITER";

    private final String statements;

    private String splitRegex = DEFAULT_SPLIT_REGEX;
    private String delimiter = DEFAULT_DELIMITER_KEYWORD;

    /**
     * Creates a new StatementSplitter.
     * <p>
     *     Use {@link #split()} to split the statements.
     * </p>
     * <p>
     *     The default regex is {@link StatementSplitter#DEFAULT_SPLIT_REGEX}.
     *     The default delimiter is {@link StatementSplitter#DEFAULT_DELIMITER_KEYWORD}.
     *     You can specify a custom standard regex by calling {@link #setStandardSplitRegex(String)}.
     *     You can specify a custom standard delimiter by calling {@link #setStandardDelimiter(String)}.
     * </p>
     * @param statements the statements to split
     */
    public StatementSplitter(String statements) {
        this.statements = statements.replace("\n", " ");
    }

    /**
     * Returns the standard regex.
     * <p>
     *     The default regex is {@link StatementSplitter#DEFAULT_SPLIT_REGEX}.
     *     You can specify a custom standard regex by calling {@link #setStandardSplitRegex(String)}.
     * </p>
     * @return the standard regex
     */
    public String getStandardSplitRegex() {
        return splitRegex;
    }

    /**
     * Sets the standard regex.
     * <p>
     *     The default regex is {@link StatementSplitter#DEFAULT_SPLIT_REGEX}.
     * </p>
     * <p>
     *     The regex is used to split the statements.
     * </p>
     * @param splitRegex the standard regex to be used
     */
    public void setStandardSplitRegex(String splitRegex) {
        this.splitRegex = splitRegex;
    }

    /**
     * Returns the standard delimiter.
     * <p>
     *     The default delimiter is {@link StatementSplitter#DEFAULT_DELIMITER_KEYWORD}.
     *     You can specify a custom standard delimiter by calling {@link #setStandardDelimiter(String)}.
     * </p>
     * @return the standard delimiter
     */
    public String getStandardDelimiter() {
        return delimiter;
    }

    /**
     * Sets the standard delimiter.
     * <p>
     *     The default delimiter is {@link StatementSplitter#DEFAULT_DELIMITER_KEYWORD}.
     * </p>
     * <p>
     *     The delimiter is used to indicate a new regex.
     * </p>
     * @param delimiter the standard delimiter to be used
     */
    public void setStandardDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Splits the statements.
     * <p>
     *     The splitting is done by a regex. The default regex is {@link StatementSplitter#DEFAULT_SPLIT_REGEX}.
     *     The regex can be changed by calling {@link #setStandardSplitRegex(String)}.
     * </p>
     * @return the statements as an array
     */
    public String[] split() {

        int index = 0;

        ArrayList<String> result = new ArrayList<>();

        while (index <= this.statements.length()) {

            int nextDelimiterIndex = indexOfIgnoreCase(this.statements.substring(index), this.delimiter);
            int nextSplitRegexIndex = indexOfIgnoreCase(this.statements.substring(index), this.splitRegex);

            if (nextDelimiterIndex == -1 && nextSplitRegexIndex == -1) {
                break;
            }

            if (nextSplitRegexIndex == -1) {
                this.splitRegex = parseNewDelimiter(this.statements.substring(index + nextDelimiterIndex));
            } else if (nextDelimiterIndex != -1 &&nextDelimiterIndex < nextSplitRegexIndex) {
                this.splitRegex = parseNewDelimiter(this.statements.substring(index + nextDelimiterIndex));
            }

            int statementLength = indexOfIgnoreCase(this.statements.substring(index), this.splitRegex);
            statementLength += this.splitRegex.length();

            result.add(this.statements.substring(index, index + statementLength));

            index += statementLength;

        }

        return result.toArray(new String[0]);

    }

    private String parseNewDelimiter(String string) {

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

    private int indexOfIgnoreCase(String string, String substring) {
        return string.toLowerCase().indexOf(substring.toLowerCase());
    }

}
