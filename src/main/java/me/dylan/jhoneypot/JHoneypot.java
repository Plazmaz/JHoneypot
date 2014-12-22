package me.dylan.jhoneypot;

import lombok.Getter;
import me.dylan.jhoneypot.commands.CommandRegistration;
import me.dylan.jhoneypot.fs.FileSystemSandbox;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.Factory;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created with love by Dylan on 12/20/2014.
 *
 * @author Dylan Thomas Katz
 */
public class JHoneypot {
    private static @Getter Config config;
    private static @Getter FileSystemSandbox sandbox = new FileSystemSandbox("fs");
    public JHoneypot() {

        try {
            config = new Config("config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            CommandRegistration.registerAll();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(22);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("keyfile"));
        sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(new SftpSubsystem.Factory()));
        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            @Override
            public boolean authenticate(String username, String password, ServerSession session) {
                boolean checkUser =  config.getString("user").isEmpty();
                boolean checkPassword = config.getString("password").isEmpty();
                boolean proceed = true;
                if(checkUser) {
                    proceed = username.equals(config.getString("user"));
                }
                if(checkPassword) {
                    proceed = proceed && password.equals(config.getString("password"));
                }

                return  proceed;
            }

        });

        sshd.setShellFactory(new Factory<Command>() {
            @Override
            public Command create() {
                return new HoneyFactory();
            }
        });
        try {
            sshd.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
       new JHoneypot();
    }
}
