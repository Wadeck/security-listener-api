/*
 * The MIT License
 *
 * Copyright (c) 2017, CloudBees, Inc.
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.events.impl;

import org.jenkinsci.plugins.events.optional.EventWithException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * At least either the reason or the exception is non null.
 * In case you do not want to provide such information please use the {@link AuthenticationFailureEventImpl}
 */
@Immutable
public class AuthenticationFailureExceptionEventImpl
        extends AuthenticationFailureEventImpl
        implements EventWithException {

    private final String reason;
    private final Exception exception;

    public AuthenticationFailureExceptionEventImpl(@Nonnull String username,
                                                   @CheckForNull String source,
                                                   @CheckForNull String reason,
                                                   @Nonnull Exception exception) {
        super(username, source);
        this.reason = reason;
        this.exception = exception;
    }

    /**
     *
     * @param username
     * @param source
     * @param exception the {@link Exception#getMessage()} is used to populate the {@link #getReason()}.
     *                  If you want to have the reason set to null, you can use the other constructor
     *                  {@link #AuthenticationFailureExceptionEventImpl(String, String, String, Exception)}
     */
    public AuthenticationFailureExceptionEventImpl(@Nonnull String username,
                                                   @CheckForNull String source,
                                                   @Nonnull Exception exception) {
        super(username, source);
        this.reason = exception.getMessage();
        this.exception = exception;
    }

    public AuthenticationFailureExceptionEventImpl(@Nonnull String username,
                                                   @CheckForNull String source,
                                                   @Nonnull String reason) {
        super(username, source);
        this.reason = reason;
        this.exception = null;
    }

    @Override
    public @CheckForNull String getReason() {
        return reason;
    }

    @Override
    public @CheckForNull Exception getException() {
        return exception;
    }
}
