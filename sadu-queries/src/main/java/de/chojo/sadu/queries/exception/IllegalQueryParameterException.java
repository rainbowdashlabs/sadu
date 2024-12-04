package de.chojo.sadu.queries.exception;

public class IllegalQueryParameterException extends RuntimeQueryException{
    public IllegalQueryParameterException(String message) {
        super(message);
    }
}
