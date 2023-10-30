package com.colak.hzcli.config.shell;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.CommandNotFound;
import org.springframework.shell.command.CommandExceptionResolver;
import org.springframework.shell.command.CommandHandlingResult;

@Configuration
public class CLIExceptionResolverConfig {

    // https://dev.to/noelopez/spring-shell-build-cli-apps-2l1o
    // resolve exceptions and can return messages to be displayed in the console.
    @Bean
    CommandExceptionResolver customExceptionResolver() {
        return ex -> {
            if (ex instanceof CommandNotFound e) {
                return CommandHandlingResult.of("CommandNotFound. Please retry ");
            }
            return CommandHandlingResult.of(ex.getMessage() + '\n', 1);
        };
    }
}
