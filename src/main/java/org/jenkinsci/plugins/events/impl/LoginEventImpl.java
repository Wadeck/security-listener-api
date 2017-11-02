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

import org.jenkinsci.plugins.events.LoginEvent;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.*;

@Immutable
public class LoginEventImpl extends AbstractSecurityEvent implements LoginEvent {
    private final Set<String> authorities;

    public LoginEventImpl(@Nonnull String username,
                          @CheckForNull String source,
                          @Nonnull Set<String> authorities) {
        super(username, source);
        assert authorities.stream().allMatch(Objects::nonNull);

        this.authorities = Collections.unmodifiableSet(authorities);
    }

    public LoginEventImpl(@Nonnull String username,
                          @CheckForNull String source,
                          @Nonnull Collection<String> authorities) {
        this(username, source, new HashSet<>(authorities));
    }

    public LoginEventImpl(@Nonnull String username,
                          @CheckForNull String source,
                          String... authorities) {
        this(username, source, Arrays.asList(authorities));
    }

    @Override
    public @Nonnull Set<String> getAuthorities() {
        return authorities;
    }
}
