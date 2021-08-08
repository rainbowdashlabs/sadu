package de.chojo.sqlutil.updater.logging;

public interface LoggerAdapter {
    void error(String message);

    void error(String message, Throwable t);

    void info(String message);

    void info(String message, Throwable t);

    void debug(String message);

    void debug(String message, Throwable t);

    void warn(String message);

    void warn(String message, Throwable t);
}
