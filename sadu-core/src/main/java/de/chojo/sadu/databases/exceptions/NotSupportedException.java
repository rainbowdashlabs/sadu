package de.chojo.sadu.databases.exceptions;

/**
 * Thrown when a method is called which is not supported by this type.
 */
public class NotSupportedException extends RuntimeException{
    public NotSupportedException(String message) {
        super(message);
    }
}
