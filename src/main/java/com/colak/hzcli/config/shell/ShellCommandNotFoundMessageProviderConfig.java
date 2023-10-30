package com.colak.hzcli.config.shell;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.result.CommandNotFoundMessageProvider;

@Configuration
public class ShellCommandNotFoundMessageProviderConfig {

    // https://dev.to/noelopez/spring-shell-availability-and-customization-18j
    // Internally CommandNotFoundResultHandler is using CommandNotFoundMessageProvider which is a functional interface taking
    // a ProviderContext and returning a text message.
    // It is possible to define it as a bean and can be written as a lambda function.
    // Return a new message
    @Bean
    CommandNotFoundMessageProvider provider() {
        var message = """
                The command '%s' you entered was not found.\s
                Use help to view the list of available commands\s
                """;
        return ctx -> String.format(message, ctx.text());
    }
}
