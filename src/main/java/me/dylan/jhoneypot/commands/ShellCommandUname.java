package me.dylan.jhoneypot.commands;

import me.dylan.jhoneypot.JHoneypot;

import java.util.Date;

/**
 * Created with love by Dylan on 12/20/2014.
 *
 * @author Dylan Thomas Katz
 */
public class ShellCommandUname extends ShellCommand {
    public ShellCommandUname() {
        super("uname", 0);
    }

    @Override
    public String exec(String[] args) {
        return "Linux";
    }

    @CommandFlag(flagName="a")
    public String showAll() {
        return "Linux " + JHoneypot.getConfig().getString("user") + " 2.6.24-1-686 #1 SMP " + new Date().toString()  + " x86_64 x86_64 x86_64 GNU/Linux";
    }

    @Override
    public String getHelp() {
        return "Usage: uname [OPTION]...\n" +
                "Print certain system information.  With no OPTION, same as -s.\n" +
                "\n" +
                "  -a, --all                print all information, in the following order,\n" +
                "      --help     display this help and exit\n" +
                "      --version  output version information and exit\n" +
                "\n" +
                "GNU coreutils online help: <http://www.gnu.org/software/coreutils/>\n" +
                "For complete documentation, run: info coreutils 'uname invocation'\n";
    }
}
