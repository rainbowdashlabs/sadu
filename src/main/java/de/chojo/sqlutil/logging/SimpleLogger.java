package de.chojo.sqlutil.logging;

public abstract class SimpleLogger<T> implements LoggerAdapter {
    protected final T log;

    public SimpleLogger(T logger) {
        this.log = logger;
    }

    @Override
    public abstract void error(String message);

    @Override
    public abstract void error(String message, Throwable t);

    @Override
    public abstract void info(String message);

    @Override
    public abstract void info(String message, Throwable t);

    @Override
    public abstract void debug(String message);

    @Override
    public abstract void debug(String message, Throwable t);

    @Override
    public abstract void warn(String message);

    @Override
    public abstract void warn(String message, Throwable t);
}
