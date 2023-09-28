package com.colak.hzcli.commands;

import com.colak.hzcli.shell.ShellHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public abstract class AbstractCommand {

    protected ShellHelper shellHelper;

    @Autowired
    public void setShellHelper(@Lazy ShellHelper shellHelper) {
        this.shellHelper = shellHelper;
    }
}
