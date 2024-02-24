package de.chojo.sadu.core.configuration;

/**
 * This interface represents the configuration for a database connection.
 * It provides methods to retrieve the address, port, user, password, and database name.
 */
public interface DatabaseConfig {
    /**
     * Returns the address of the database server.
     *
     * @return the address of the database server
     */
    String host();

    /**
     * Returns the port for the database connection.
     *
     * @return the port for the database connection
     */
    String port();

    /**
     * Retrieves the username associated with the database connection.
     *
     * @return The username as a string.
     */
    String user();

    /**
     * Returns the password for the database user.
     *
     * @return the password for the database user
     */
    String password();

    /**
     * Returns the database for the database connection.
     *
     * @return The name of the database.
     */
    String database();

}
