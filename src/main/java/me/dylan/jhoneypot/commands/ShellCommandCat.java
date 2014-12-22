package me.dylan.jhoneypot.commands;

import me.dylan.jhoneypot.JHoneypot;

/**
 * Created with love by Dylan on 12/22/2014.
 *
 * @author Dylan Thomas Katz
 */
public class ShellCommandCat extends ShellCommand {
    public ShellCommandCat() {
        super("cat", 1);
    }

    @Override
    protected String exec(String[] args) {
        String cat = args[0];
        try {
            String result = JHoneypot.getSandbox().cat(cat);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String getHelp() {
        return "";
    }
}
