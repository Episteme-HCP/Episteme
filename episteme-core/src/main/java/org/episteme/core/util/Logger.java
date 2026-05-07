/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.core.util;

import java.util.function.Supplier;
import org.slf4j.LoggerFactory;

/**
 * Logging facade for Episteme.
 * <p>
 * Provides a simple, performance-optimized logging API built on SLF4J.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Logger {

    private final org.slf4j.Logger delegate;

    private Logger(org.slf4j.Logger delegate) {
        this.delegate = delegate;
    }

    /**
     * Gets a logger for the specified class.
     *
     * @param clazz the class
     * @return the logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return new Logger(LoggerFactory.getLogger(clazz));
    }

    /**
     * Gets a logger with the specified name.
     *
     * @param name the logger name
     * @return the logger
     */
    public static Logger getLogger(String name) {
        return new Logger(LoggerFactory.getLogger(name));
    }

    // TRACE level

    public void trace(String message) {
        delegate.trace(message);
    }

    public void trace(Supplier<String> messageSupplier) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(messageSupplier.get());
        }
    }

    // DEBUG level

    public void debug(String message) {
        delegate.debug(message);
    }

    public void debug(Supplier<String> messageSupplier) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(messageSupplier.get());
        }
    }

    // INFO level

    public void info(String message) {
        delegate.info(message);
    }

    public void info(Supplier<String> messageSupplier) {
        if (delegate.isInfoEnabled()) {
            delegate.info(messageSupplier.get());
        }
    }

    // WARN level

    public void warn(String message) {
        delegate.warn(message);
    }

    public void warn(String message, Throwable throwable) {
        delegate.warn(message, throwable);
    }

    public void warn(Supplier<String> messageSupplier) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(messageSupplier.get());
        }
    }

    // ERROR level

    public void error(String message) {
        delegate.error(message);
    }

    public void error(String message, Throwable throwable) {
        delegate.error(message, throwable);
    }

    public void error(Supplier<String> messageSupplier) {
        if (delegate.isErrorEnabled()) {
            delegate.error(messageSupplier.get());
        }
    }

    /**
     * Check if trace is enabled.
     * @return true if enabled
     */
    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    /**
     * Check if debug is enabled.
     * @return true if enabled
     */
    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    /**
     * Check if info is enabled.
     * @return true if enabled
     */
    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    /**
     * Check if warn is enabled.
     * @return true if enabled
     */
    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    /**
     * Check if error is enabled.
     * @return true if enabled
     */
    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    /**
     * Gets the underlying SLF4J Logger for advanced use cases.
     *
     * @return the delegate logger
     */
    public org.slf4j.Logger getDelegate() {
        return delegate;
    }
}
