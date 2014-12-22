package me.dylan.jhoneypot.commands;

import me.dylan.jhoneypot.JHoneypot;

/**
 * Created with love by Dylan on 12/20/2014.
 *
 * @author Dylan Thomas Katz
 */
public class ShellCommandCd extends ShellCommand {
    public ShellCommandCd() {
        super("cd", 1);
    }

    @Override
    public String exec(String[] args) {
        if(args.length >= minArgs) {
            String location = args[0];
            if(location.startsWith("..") || location.startsWith("../")) {
                String origLoc = JHoneypot.getSandbox().getLocation();
                String path;

                if(!JHoneypot.getSandbox().cdUp()) {
                    return "bash: cd: " + location + ": No such file or directory";
                }
                if(!location.equals("..") && !location.equals("../")) {
                    if (location.contains("../")) {
                        path = location.substring(location.indexOf("../") + 2);
                    } else {
                        path = location.substring(location.indexOf("..") + 1);
                    }
                    if(JHoneypot.getSandbox().cdTo(path)) {
                        return "";
                    }
                    JHoneypot.getSandbox().cdTo(origLoc);
                    return "bash: cd: " + location + ": No such file or directory";
                }
                return "";
            } else {

                if (JHoneypot.getSandbox().cdTo(location)) {
                    return "";
                } else {
                    return "bash: cd: " + location + ": No such file or directory";
                }
            }
        }
        return getHelp();
    }


    @Override
    public String getHelp() {
        return "cd: usage: cd [-L|[-P [-e]]] [dir]\n";
    }

}
