package de.chojo.sadu.datasource;

/**
 * Thrown when a driver class is not found.
 */

public class DriverClassNotFoundException extends RuntimeException {

    public DriverClassNotFoundException(String message) {
        super(message);
    }
}
