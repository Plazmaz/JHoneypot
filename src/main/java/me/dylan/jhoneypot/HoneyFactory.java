package me.dylan.jhoneypot;

import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import me.dylan.jhoneypot.commands.CommandRegistration;
import me.dylan.jhoneypot.commands.ShellCommand;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with love by Dylan on 12/20/2014.
 *
 * @author Dylan Thomas Katz
 */
public class HoneyFactory implements Command, Runnable {
    private static final Logger log = LoggerFactory.getLogger(HoneyFactory.class);

    public static final boolean IS_MAC_OSX = System.getProperty("os.name").startsWith("Mac OS X");
    private static final String SHELL_PROMPT = "[admin@mainz %s]$ ";
    private static final String THREAD_NAME = "HoneyFactory";
    public static String location = "~";
    private InputStream in;
    private OutputStream out;
    private OutputStream err;
    private ExitCallback callback;
    private Environment environment;
    private Thread thread;
    private ConsoleReader reader;
    private PrintWriter writer;

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public OutputStream getErr() {
        return err;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setInputStream(InputStream in) {
        this.in = in;
    }

    public void setOutputStream(OutputStream out) {
        this.out = out;
    }

    public void setErrorStream(OutputStream err) {
        this.err = err;
    }

    public void setExitCallback(ExitCallback callback) {
        this.callback = callback;
    }

    public void start(Environment env) throws IOException {
        environment = env;
        thread = new Thread(this, THREAD_NAME);
        thread.start();
    }

    public void destroy() {
        if (reader != null)
            reader.shutdown();
        thread.interrupt();
    }

    @Override
    public void run() {
        try {

            reader = new ConsoleReader(in, new FilterOutputStream(out) {
                @Override
                public void write(final int i) throws IOException {
                    super.write(i);

                    // workaround for MacOSX!! reset line after CR..
                    if (IS_MAC_OSX && i == ConsoleReader.CR.toCharArray()[0]) {
                        super.write(ConsoleReader.RESET_LINE);
                    }
                }
            });
            reader.setPrompt(String.format(SHELL_PROMPT, location));
            List<String> autocompletes = new ArrayList<>();
            for(ShellCommand command : CommandRegistration.getCommands()) {
                autocompletes.add(command.getName());

            }

            reader.addCompleter(new StringsCompleter(autocompletes.toArray(new String[0])));
            writer = new PrintWriter(reader.getOutput());

            // output welcome banner on ssh session startup
            writer.println("****************************************************");
            writer.println("*        Welcome to Application Shell.             *");
            writer.println("****************************************************");
            writer.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                handleUserInput(line.trim());
            }

        } catch (InterruptedIOException e) {
            // Ignore
        } catch (Exception e) {
            log.error("Error executing InAppShell...", e);
        } finally {
            callback.onExit(0);
        }
    }

    private void handleUserInput(String line) throws InterruptedIOException {
        String response;
        String[] segments = line.split(" ");
        String[] args;
        boolean helpOnly = false;
        boolean versionOnly = false;
        if(line.contains(" ")) {
            args = line.substring(line.indexOf(" ") + 1).split(" ");
            if(args[0].equals("--help")) {
                helpOnly = true;
            } else if(args[0].equals("--version")) {
                versionOnly = true;
            }
        } else {
            args = new String[] {};
        }

        if(CommandRegistration.getCommandMapping(segments[0]) == null) {
            response = "bash: " + segments[0] + ": command not found";
        } else {
            ShellCommand command = CommandRegistration.getCommandMapping(segments[0]);
            if(versionOnly) {
                response = command.getVersion();
            } else if(helpOnly) {
                response = command.getHelp();
            } else {
                response = command.execute(args);
            }
        }
        location = JHoneypot.getSandbox().getLocation();
        if(location.contains("\\")) {
            location = location.substring(location.lastIndexOf("\\"));
            location = location.replace("\\", "/");

        }
        reader.setPrompt(String.format(SHELL_PROMPT, location));

        if(!response.isEmpty()) {
            writer.println(response);
        }
        writer.flush();

    }
}
