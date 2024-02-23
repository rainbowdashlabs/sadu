/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.core.jdbc;

import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * A base builder to create a remote database jdbc url.
 *
 * @param <T> type of database
 */
public abstract class RemoteJdbcConfig<T extends RemoteJdbcConfig<?>> extends JdbcConfig<T> {
    private static final Pattern IPV4 = Pattern.compile("^(?:\\d{1,3}\\.){3}\\d{1,3}$");
    private static final Pattern IPV6 = Pattern.compile(
            "(([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}|([\\da-fA-F]{1,4}:){1,7}:|([\\da-fA-F]{1,4}:){1,6}:[\\da-fA-F]{1,4}|([\\da-fA-F]{1,4}:){1,5}(:[\\da-fA-F]{1,4}){1,2}|([\\da-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([\\da-fA-F]{1,4}:){1,3}(:[\\da-fA-F]{1,4}){1,4}|([\\da-fA-F]{1,4}:){1,2}(:[\\da-fA-F]{1,4}){1,5}|[\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,6})|:((:[\\da-fA-F]{1,4}){1,7}|:)|fe80:(:[\\da-fA-F]{0,4}){0,4}%[\\da-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?\\d)?\\d)\\.){3}(25[0-5]|(2[0-4]|1?\\d)?\\d)|([\\da-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?\\d)?\\d)\\.){3}(25[0-5]|(2[0-4]|1?\\d)?\\d))");
    private String host = "localhost";
    private String port;
    private String database;

    public RemoteJdbcConfig() {
    }

    /**
     * Sets the host explicit to local host.
     * <p>
     * This is usually the default value defined by jdbc drivers.
     *
     * @return builder instance
     */
    public T localhost() {
        return host("localhost");
    }

    /**
     * Set the host name of the connection
     *
     * @param host host
     * @return builder instance
     */
    public T host(String host) {
        this.host = host;
        return self();
    }

    /**
     * Sets the host to an ipv4 address.
     * This address must be a valid ipv4 address
     *
     * @param ipv4 ipv4 address
     * @return builder instance
     * @throws IllegalArgumentException when an invalid ipv4 was passed
     */
    public T ipv4(String ipv4) {
        if (!IPV4.matcher(ipv4).matches()) {
            throw new IllegalArgumentException("The ip " + ipv4 + " is not a valid ipv4 address");
        }
        return host(ipv4);
    }

    /**
     * Sets the host to an ipv6 address.
     * <p>
     * This address must be a valid ipv6 address.
     * <p>
     * The method will ensure that the ipv6 address is surrounded by [...], which is required by jdbc.
     *
     * @param ipv6 ipv6 address
     * @return builder instance
     * @throws IllegalArgumentException when an invalid ipv6 was passed
     */
    public T ipv6(String ipv6) {
        ipv6 = ipv6.replaceAll("[\\[\\]]", "");
        if (!IPV6.matcher(ipv6).matches()) {
            throw new IllegalArgumentException("The ip " + ipv6 + " is not a valid ipv6 address");
        }
        return host(String.format("[%s]", ipv6));
    }

    /**
     * Set the port.
     *
     * @param port port
     * @return builder instance
     * @throws IllegalArgumentException when the port is not a number
     * @throws IllegalArgumentException when the port is smaller than 1 or larger than 65535
     */
    public T port(String port) {
        try {
            var val = Integer.parseInt(port);
            if (val < 1 || val > 65535) {
                throw new IllegalArgumentException("Port \"" + port + "\" is out of range. Valid range is 1-65535");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Port \"" + port + "\" is not a number", e);
        }
        this.port = port;
        return self();
    }

    /**
     * Set the port.
     *
     * @param port port
     * @return builder instance
     * @throws IllegalArgumentException when the port is smaller than 1 or larger than 65535
     */
    public T port(int port) {
        return port(String.valueOf(port));
    }

    /**
     * Sets the database for the connection.
     * <p>
     * Most drivers default to the username if not entered.
     *
     * @param database database
     * @return builder instance
     */
    public T database(String database) {
        if (database.isBlank()) {
            throw new IllegalArgumentException("Database name is empty");
        }
        this.database = database;
        return self();
    }

    @Override
    protected String baseUrl() {
        var jdbc = new StringBuilder("jdbc:")
                .append(driver())
                .append(":");

        if (port != null && host == null) {
            throw new IllegalStateException("A port can not be defined without defining a host.");
        }

        if (host != null) {
            jdbc.append("//").append(host);

            // Port depends on existence of host
            if (port != null) {
                jdbc.append(":").append(port);
            }
            jdbc.append("/");
        }

        if (database != null) {
            jdbc.append(database);
        } else if (host == null) {
            jdbc.append("/");
        }

        return jdbc.toString();
    }

    /**
     * Sets the login data
     *
     * @param user     username
     * @param password user password
     * @return builder instance
     */
    public T login(String user, String password) {
        password(password).user(user);
        return self();
    }

    /**
     * Sets the password for the connection.
     *
     * @param password password
     * @return builder instance
     */
    public T password(String password) {
        return addParameter("password", password);
    }

    /**
     * Sets the user for the connection.
     *
     * @param user user
     * @return builder instance
     */
    public T user(String user) {
        return addParameter("user", user);
    }

    /**
     * Returns user credentials if the dataSourceCreator should set the credentials instead of jdbc url
     *
     * @return holding the credentials when they should be applied
     */
    @ApiStatus.Internal
    public Credentials userCredentials() {
        return Credentials.EMPTY;
    }

    public static class Credentials {
        public static final Credentials EMPTY = new Credentials(null, null);
        private final JdbProperty<?> user;
        private final JdbProperty<?> password;

        public Credentials(JdbProperty<?> user, JdbProperty<?> password) {
            this.user = user;
            this.password = password;
        }

        public Optional<JdbProperty<?>> user() {
            return Optional.ofNullable(user);
        }

        public Optional<JdbProperty<?>> password() {
            return Optional.ofNullable(password);
        }
    }
}
