package com.colak.hzcli.config.shell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

// https://github.com/dmadunic/clidemo/blob/master/src/main/java/com/ag04/clidemo/shell/ClidemoPromptProvider.java
@Component
public class ShellPromptProviderConfig {
    @Bean
    PromptProvider promptProvider() {
        return () -> new AttributedString("HZ-CLI:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
    }

}
