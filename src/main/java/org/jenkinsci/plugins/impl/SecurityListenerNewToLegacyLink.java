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
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.jenkinsci.plugins.SecurityListener2;
import org.jenkinsci.plugins.events.*;
import org.jenkinsci.plugins.events.optional.UserDetailsProvider;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Make the bridge between the SecurityListener 2 and 1
 * by passing the new event to the legacy listeners
 */
@Extension
public class SecurityListenerNewToLegacyLink extends SecurityListener2 {

    private static final Logger LOGGER = Logger.getLogger(SecurityListenerNewToLegacyLink.class.getName());

    /*
     * Implementation details: first lines of each methods ensure we do not trigger infinite loop
     */

    @Override
    protected boolean authenticated(@Nonnull AuthenticationEvent event) {
        if(event.isFromLegacy()) return true;

        UserDetails userDetails;
        if (event instanceof UserDetailsProvider) {
            userDetails = ((UserDetailsProvider) event).getUserDetails();
        } else {
            userDetails = new LegacyUserDetails(event);
        }

        bypassRestrictedToCall("fireAuthenticated", userDetails, SecurityListener.SOURCE_NEW_PREFIX + event.getSource());

        return true;
    }

    @Override
    protected boolean failedToAuthenticate(@Nonnull AuthenticationFailureEvent event) {
        if(event.isFromLegacy()) return true;

        bypassRestrictedToCall("fireFailedToAuthenticate", event.getUsername(), SecurityListener.SOURCE_NEW_PREFIX + event.getSource());

        return true;
    }

    @Override
    protected boolean loggedIn(@Nonnull LoginEvent event) {
        if(event.isFromLegacy()) return true;

        bypassRestrictedToCall("fireLoggedIn", event.getUsername(), SecurityListener.SOURCE_NEW_PREFIX + event.getSource());

        return true;
    }

    @Override
    protected boolean failedToLogIn(@Nonnull LoginFailureEvent event) {
        if(event.isFromLegacy()) return true;

        bypassRestrictedToCall("fireFailedToLogIn", event.getUsername(), SecurityListener.SOURCE_NEW_PREFIX + event.getSource());

        return true;
    }

    @Override
    protected boolean loggedOut(@Nonnull LogoutEvent event) {
        if(event.isFromLegacy()) return true;

        bypassRestrictedToCall("fireLoggedOut", event.getUsername(), SecurityListener.SOURCE_NEW_PREFIX + event.getSource());

        return true;
    }

    /**
     * Reflection helper to bypass @Restricted methods in SecurityListener
     *
     * @param methodName the method to find in the legacy SecurityListener
     * @param arguments  the arguments to pass to the method
     * @return true if the method exists, false otherwise
     */
    private void bypassRestrictedToCall(String methodName, Object... arguments) {
        Class<?>[] argumentTypes = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            argumentTypes[i] = arguments[i].getClass();
        }

        Method m;
        try {
            m = SecurityListener.class.getDeclaredMethod(methodName, argumentTypes);
        } catch (NoSuchMethodException e) {
            LOGGER.log(Level.WARNING, "Method " + methodName +" not found in SecurityListener", e);
            return;
        }

        // static invocation, no need an instance
        try {
            m.invoke(null, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.log(Level.WARNING, "Problem during invocation of " + methodName +" in SecurityListener", e);
        }
    }

    /**
     * Create a fake userDetails by using the information provided
     */
    @Restricted(NoExternalUse.class)
    public static class LegacyUserDetails extends User {
        private AuthenticationEvent originalEvent;
        private String originalSource;

        public LegacyUserDetails(@Nonnull AuthenticationEvent event) {
            super(
                    event.getUsername(), "",
                    true, true, true, true,
                    new GrantedAuthority[0]
            );

            this.originalEvent = event;
            this.originalSource = event.getSource();
        }

        public @Nonnull AuthenticationEvent getOriginalEvent() {
            return originalEvent;
        }

        public @Nonnull String getOriginalSource() {
            return originalSource;
        }
    }
}
