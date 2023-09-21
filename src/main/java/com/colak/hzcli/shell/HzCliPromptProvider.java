package com.colak.hzcli.shell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

// https://github.com/dmadunic/clidemo/blob/master/src/main/java/com/ag04/clidemo/shell/ClidemoPromptProvider.java
@Component
public class HzCliPromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("HZ-CLI:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
    }
}
