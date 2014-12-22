package me.dylan.jhoneypot.commands;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created with love by Dylan on 12/20/2014.
 *
 * @author Dylan Thomas Katz
 */
public abstract class ShellCommand {
    private @Getter String name = "";
    public int minArgs;

    public ShellCommand(String name, int minArgs) {
        this.name = name;
        this.minArgs = minArgs;
    }

    protected abstract String exec(String[] args);

    public String execute(String[] args) {
        if(args.length >= minArgs) {
            for(Method method : this.getClass().getMethods()) {
                if(method.getAnnotation(CommandFlag.class) != null) {
                    CommandFlag flag = method.getAnnotation(CommandFlag.class);
                    if(Arrays.asList(args).contains("-" + flag.flagName())) {
                        try {
                            return (String) method.invoke(this);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }

            return exec(args);
        }
        return null;
    }

    public abstract String getHelp();

    public String getVersion() {
        return name + " (GNU coreutils) 8.22\n" +
                "Copyright (C) 2013 Free Software Foundation, Inc.\n" +
                "License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>.\n" +
                "This is free software: you are free to change and redistribute it.\n" +
                "There is NO WARRANTY, to the extent permitted by law.\n" +
                "\n" +
                "Written by David MacKenzie.\n";
    }

}
