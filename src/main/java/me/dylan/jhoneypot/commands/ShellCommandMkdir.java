package me.dylan.jhoneypot.commands;

import me.dylan.jhoneypot.JHoneypot;

import java.io.IOException;

/**
 * Created with love by Dylan on 12/22/2014.
 *
 * @author Dylan Thomas Katz
 */
public class ShellCommandMkdir extends ShellCommand {
    public ShellCommandMkdir() {
        super("mkdir", 1);
    }

    @Override
    protected String exec(String[] args) {
        try {
            JHoneypot.getSandbox().mkDir(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
