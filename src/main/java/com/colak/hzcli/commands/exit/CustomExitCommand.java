package com.colak.hzcli.commands.exit;

import com.colak.hzcli.commands.AbstractCommand;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Quit;

@ShellComponent
public class CustomExitCommand extends AbstractCommand implements Quit.Command {

    // From https://self-learning-java-tutorial.blogspot.com/2021/02/spring-shell-override-built-in-commands.html
    @ShellMethod(value = "Exit the shell.", key = {"quit", "exit", "terminate"})
    public void quit() {
        // We need to shut down HZ Client
        hazelcastClient.shutdown();

        System.out.println("Exiting the Application");
        System.out.println("Good Bye!!!!!!!!");
        System.exit(0);
    }

}