package de.chojo.sadu.databases.exceptions;

/**
 * Thrown when a method is called which is not implemented by this type.
 */
public class NotImplementedException extends RuntimeException{
    public NotImplementedException(String message) {
        super(message);
    }
}
