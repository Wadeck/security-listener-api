package org.jenkinsci.plugins.events.optional;

import org.acegisecurity.userdetails.UserDetails;

import javax.annotation.Nonnull;

public interface UserDetailsProvider {
    @Nonnull UserDetails getUserDetails();
}
