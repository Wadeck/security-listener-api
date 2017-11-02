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

import org.jenkinsci.plugins.events.SecurityEvent;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public class AbstractSecurityEvent implements SecurityEvent {
    private final String username;
    private final String source;

    /**
     * @param username
     * @param source in case of null value, we replace it by {@link #SOURCE_UNKNOWN}
     */
    public AbstractSecurityEvent(@Nonnull String username,
                                 @CheckForNull String source) {
        this.username = username;
        this.source = source == null ? SOURCE_UNKNOWN : source;
    }

    @Override
    public @Nonnull String getUsername() {
        return username;
    }

    @Override
    public @Nonnull String getSource() {
        return source;
    }
}
