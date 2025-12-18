package org.jscience.util;

import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * Logging facade for JScience.
 * <p>
 * Provides a simple, performance-optimized logging API built on
 * java.util.logging.
 * Users can bridge to SLF4J/Logback if needed.
 * </p>
 * <p>
 * <b>Usage:</b>
 * 
 * <pre>{@code
 * private static final Logger LOG = Logger.getLogger(MyClass.class);
 * 
 * LOG.info("Operation completed");
 * LOG.debug(() -> "Expensive computation: " + expensiveCalc());
 * LOG.error("Failed to process", exception);
 * }</pre>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Logger {

    private final java.util.logging.Logger delegate;

    private Logger(java.util.logging.Logger delegate) {
        this.delegate = delegate;
    }

    /**
     * Gets a logger for the specified class.
     *
     * @param clazz the class
     * @return the logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return new Logger(java.util.logging.Logger.getLogger(clazz.getName()));
    }

    /**
     * Gets a logger with the specified name.
     *
     * @param name the logger name
     * @return the logger
     */
    public static Logger getLogger(String name) {
        return new Logger(java.util.logging.Logger.getLogger(name));
    }

    // TRACE level (using FINEST)

    public void trace(String message) {
        delegate.finest(message);
    }

    public void trace(Supplier<String> messageSupplier) {
        if (delegate.isLoggable(Level.FINEST)) {
            delegate.finest(messageSupplier.get());
        }
    }

    // DEBUG level (using FINE)

    public void debug(String message) {
        delegate.fine(message);
    }

    public void debug(Supplier<String> messageSupplier) {
        if (delegate.isLoggable(Level.FINE)) {
            delegate.fine(messageSupplier.get());
        }
    }

    // INFO level

    public void info(String message) {
        delegate.info(message);
    }

    public void info(Supplier<String> messageSupplier) {
        if (delegate.isLoggable(Level.INFO)) {
            delegate.info(messageSupplier.get());
        }
    }

    // WARN level (using WARNING)

    public void warn(String message) {
        delegate.warning(message);
    }

    public void warn(String message, Throwable throwable) {
        delegate.log(Level.WARNING, message, throwable);
    }

    public void warn(Supplier<String> messageSupplier) {
        if (delegate.isLoggable(Level.WARNING)) {
            delegate.warning(messageSupplier.get());
        }
    }

    // ERROR level (using SEVERE)

    public void error(String message) {
        delegate.severe(message);
    }

    public void error(String message, Throwable throwable) {
        delegate.log(Level.SEVERE, message, throwable);
    }

    public void error(Supplier<String> messageSupplier) {
        if (delegate.isLoggable(Level.SEVERE)) {
            delegate.severe(messageSupplier.get());
        }
    }

    // Level checks

    public boolean isTraceEnabled() {
        return delegate.isLoggable(Level.FINEST);
    }

    public boolean isDebugEnabled() {
        return delegate.isLoggable(Level.FINE);
    }

    public boolean isInfoEnabled() {
        return delegate.isLoggable(Level.INFO);
    }

    public boolean isWarnEnabled() {
        return delegate.isLoggable(Level.WARNING);
    }

    public boolean isErrorEnabled() {
        return delegate.isLoggable(Level.SEVERE);
    }

    /**
     * Gets the underlying java.util.logging.Logger for advanced use cases.
     *
     * @return the delegate logger
     */
    public java.util.logging.Logger getDelegate() {
        return delegate;
    }
}
