package com.colak.hzcli.config.shell;

import com.colak.hzcli.shell.InputReader;
import com.colak.hzcli.shell.ShellHelper;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Parser;
import org.jline.terminal.Terminal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.boot.CompleterAutoConfiguration;

@Configuration
public class ShellConfig {

    @Bean
    public ShellHelper shellHelper(@Lazy Terminal terminal) {
        return new ShellHelper(terminal);
    }

    @Bean
    public InputReader inputReader(
            @Lazy Terminal terminal,
            @Lazy Parser parser,
            CompleterAutoConfiguration.CompleterAdapter completer,
            @Lazy History history,
            ShellHelper shellHelper
    ) {
        LineReaderBuilder lineReaderBuilder = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .history(history);

        LineReader lineReader = lineReaderBuilder.build();
        lineReader.unsetOpt(LineReader.Option.INSERT_TAB);
        return new InputReader(lineReader, shellHelper);
    }

}
