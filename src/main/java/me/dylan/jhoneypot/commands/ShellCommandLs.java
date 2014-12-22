package me.dylan.jhoneypot.commands;

import me.dylan.jhoneypot.JHoneypot;

/**
 * Created with love by Dylan on 12/20/2014.
 *
 * @author Dylan Thomas Katz
 */
public class ShellCommandLs extends ShellCommand {
    public ShellCommandLs() {
        super("ls", 0);
    }

    @Override
    public String exec(String[] args) {
        String loc = JHoneypot.getSandbox().getLocation();
        if(args.length == 1) {
            JHoneypot.getSandbox().cdTo(args[0]);
        }
        String lsResult = JHoneypot.getSandbox().ls();
        JHoneypot.getSandbox().cdTo(loc);
        return lsResult;
    }

    @Override
    public String getHelp() {
        return "";
    }
}
