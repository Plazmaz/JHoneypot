package me.dylan.jhoneypot.commands;

import lombok.Getter;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with love by Dylan on 12/20/2014.
 *
 * @author Dylan Thomas Katz
 */
public class CommandRegistration {
    private static @Getter List<ShellCommand> commands = new ArrayList<>();
    private static Reflections reflections = new Reflections("me.dylan.jhoneynet.commands");

    public static void registerAll() throws IllegalAccessException, InstantiationException {
        for(Class<? extends ShellCommand> clasz : reflections.getSubTypesOf(ShellCommand.class)) {
            commands.add(clasz.newInstance());
        }
    }

    public static ShellCommand getCommandMapping(String cmd) {
        for(ShellCommand command : commands) {
            if(command.getName().equals(cmd)) {
                return command;

            }

        }

        return null;
    }

}
