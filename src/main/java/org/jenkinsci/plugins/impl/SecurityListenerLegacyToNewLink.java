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
package org.jenkinsci.plugins.impl;

import hudson.Extension;
import jenkins.security.SecurityListener;
import org.acegisecurity.userdetails.UserDetails;
import org.jenkinsci.plugins.SecurityListener2;
import org.jenkinsci.plugins.events.*;
import org.jenkinsci.plugins.events.impl.*;

import javax.annotation.Nonnull;

/**
 * Make the bridge between the SecurityListener 1 and 2
 * by passing the legacy events to the new listeners
 */
@Extension
public class SecurityListenerLegacyToNewLink extends SecurityListener {

    /**
     * Avoid transformation done in the SecurityListener to keep the "sl2:" prefix
     * added by {@link SecurityListenerNewToLegacyLink}
     *
     * @param source
     * @return source not changed
     */
    @Override
    protected String transformSource(String source) {
        return source;
    }

    /*
     * Implementation details: first lines of each methods ensure we do not trigger infinite loop
     */

    @Override
    protected void authenticated(@Nonnull UserDetails userDetails, @Nonnull String source) {
        if (source.startsWith(SOURCE_NEW_SL)) return;

        AuthenticationEvent event = new AuthenticationWithUserDetailsEventImpl(userDetails, source) {
            @Override
            public boolean isFromLegacy() {
                return true;
            }
        };

        SecurityListener2.fireAuthenticated(event);
    }

    @Override
    protected void failedToAuthenticate(@Nonnull String username, @Nonnull String source) {
        if (source.startsWith(SOURCE_NEW_SL)) return;

        AuthenticationFailureEvent event = new AuthenticationFailureEventImpl(username, source) {
            @Override
            public boolean isFromLegacy() {
                return true;
            }
        };
        SecurityListener2.fireFailedToAuthenticate(event);
    }

    @Override
    protected void loggedIn(@Nonnull String username, @Nonnull String source) {
        if (source.startsWith(SOURCE_NEW_SL)) return;

        LoginEvent event = new LoginEventImpl(username, source) {
            @Override
            public boolean isFromLegacy() {
                return true;
            }
        };
        SecurityListener2.fireLoggedIn(event);
    }

    @Override
    protected void failedToLogIn(@Nonnull String username, @Nonnull String source) {
        if (source.startsWith(SOURCE_NEW_SL)) return;

        LoginFailureEvent event = new LoginFailureEventImpl(username, source) {
            @Override
            public boolean isFromLegacy() {
                return true;
            }
        };
        SecurityListener2.fireFailedToLogIn(event);
    }

    @Override
    protected void loggedOut(@Nonnull String username, @Nonnull String source) {
        if (source.startsWith(SOURCE_NEW_SL)) return;

        LogoutEvent event = new LogoutEventImpl(username, source) {
            @Override
            public boolean isFromLegacy() {
                return true;
            }
        };
        SecurityListener2.fireLoggedOut(event);
    }
}
