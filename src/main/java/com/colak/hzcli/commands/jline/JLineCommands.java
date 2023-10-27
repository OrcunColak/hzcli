package com.colak.hzcli.commands.jline;

import com.colak.hzcli.commands.AbstractCommand;
import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;

@ShellComponent
public class JLineCommands extends AbstractCommand {

    private LineReader lineReader;

    @Autowired
    public void setLineReader(@Lazy LineReader lineReader) {
        this.lineReader = lineReader;
    }


    // https://jamesmcnee.com/blog/posts/2019/december/15/spring-shell-application/
    @ShellMethod(key = "clearHistory", value = "Clear all search history")
    public void clearHistory() throws IOException {
        lineReader.getHistory().purge();

        shellHelper.printInfo("Command history has now been purged!");
    }
}
