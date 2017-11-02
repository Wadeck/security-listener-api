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
package org.jenkinsci.plugins;

import hudson.ExtensionList;
import hudson.ExtensionPoint;
import org.jenkinsci.plugins.events.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SecurityListener2 implements ExtensionPoint {
    private static final Logger LOGGER = Logger.getLogger(SecurityListener2.class.getName());

    /**
     * Fired when a user was successfully authenticated using (any) credentials.
     *
     * @param event contains username of the authenticated user and source of that event.
     * @return true means we can continue, false that the listener does not want other to have that event
     * TODO add implementation links
     */
    protected boolean authenticated(@Nonnull AuthenticationEvent event) {
        return true;
    }

    /**
     * Fired when a user tried to authenticate but failed.
     * In case the authentication method uses multiple layers to validate the credentials,
     * we do fire this event only when even the last layer failed to authenticate.
     *
     * @param event contains username of the user and source of that event.
     * @return true means we can continue, false that the listener does not want other to have that event
     * @see #authenticated
     * TODO add implementation links
     */
    protected boolean failedToAuthenticate(@Nonnull AuthenticationFailureEvent event) {
        return true;
    }

    /**
     * Fired when a user has logged in. Compared to authenticated, there is a notion of storage / cache.
     * Would be called after {@link #authenticated}.
     * It should be called after the {@link org.acegisecurity.context.SecurityContextHolder#getContext()}'s authentication is set.
     *
     * @param event contains the username of the user, the granted authorities and the source of the event
     * @return true means we can continue, false that the listener does not want other to have that event
     * TODO add implementation links
     */
    protected boolean loggedIn(@Nonnull LoginEvent event) {
        return true;
    }

    /**
     * Fired when a user has failed to log in.
     * Would be called after {@link #failedToAuthenticate}.
     *
     * @param event contains the username and the source of the event
     * @return true means we can continue, false that the listener does not want other to have that event
     * TODO add implementation links
     */
    protected boolean failedToLogIn(@Nonnull LoginFailureEvent event) {
        return true;
    }

    /**
     * Fired when a user logs out.
     *
     * @param event contains the username and the source of the event
     * @return true means we can continue, false that the listener does not want other to have that event
     * TODO add implementation links
     */
    protected boolean loggedOut(@Nonnull LogoutEvent event) {
        return true;
    }

    /**
     * @see #authenticated(AuthenticationEvent)
     */
    public static void fireAuthenticated(@Nonnull AuthenticationEvent event) {
        LOGGER.log(Level.FINE, "authenticated: {0} from {1}", new Object[]{event.getUsername(), event.getSource()});
        for (SecurityListener2 l : all()) {
            l.authenticated(event);
        }
    }

    /**
     * @see #failedToAuthenticate(AuthenticationFailureEvent)
     */
    public static void fireFailedToAuthenticate(@Nonnull AuthenticationFailureEvent event) {
        LOGGER.log(Level.FINE, "failed to authenticate: {0} from {1}", new Object[]{event.getUsername(), event.getSource()});
        for (SecurityListener2 l : all()) {
            l.failedToAuthenticate(event);
        }
    }

    /**
     * @see #loggedIn(LoginEvent)
     */
    public static void fireLoggedIn(@Nonnull LoginEvent event) {
        LOGGER.log(Level.FINE, "logged in: {0} from {1}", new Object[]{event.getUsername(), event.getSource()});
        for (SecurityListener2 l : all()) {
            l.loggedIn(event);
        }
    }

    /**
     * @see #failedToLogIn(LoginFailureEvent)
     */
    public static void fireFailedToLogIn(@Nonnull LoginFailureEvent event) {
        LOGGER.log(Level.FINE, "failed to log in: {0} from {1}", new Object[]{event.getUsername(), event.getSource()});
        for (SecurityListener2 l : all()) {
            l.failedToLogIn(event);
        }
    }

    /**
     * @see #loggedOut(LogoutEvent)
     */
    public static void fireLoggedOut(@Nonnull LogoutEvent event) {
        LOGGER.log(Level.FINE, "logged out: {0} from {1}", new Object[]{event.getUsername(), event.getSource()});
        for (SecurityListener2 l : all()) {
            l.loggedOut(event);
        }
    }

    private static List<SecurityListener2> all() {
        return ExtensionList.lookup(SecurityListener2.class);
    }
}
