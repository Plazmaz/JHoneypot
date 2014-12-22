package me.dylan.jhoneypot.commands;

import lombok.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with love by Dylan on 12/20/2014.
 *
 * @author Dylan Thomas Katz
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandFlag {
    @NonNull
    public String flagName();
}
