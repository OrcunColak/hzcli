package com.colak.hzcli.commands;

import com.colak.hzcli.shell.InputReader;
import com.colak.hzcli.shell.ShellHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModelBuilder;

import java.util.function.Consumer;

public abstract class AbstractCommand {

    protected ShellHelper shellHelper;

    protected InputReader inputReader;

    @Autowired
    public void setShellHelper(@Lazy ShellHelper shellHelper) {
        this.shellHelper = shellHelper;
    }

    @Autowired
    public void setInputReader(@Lazy InputReader inputReader) {
        this.inputReader = inputReader;
    }

    protected void embedInTable(String[] names, Consumer<TableModelBuilder<Object>> fillData) {
        TableModelBuilder<Object> builder = new TableModelBuilder<>();
        builder.addRow();
        for (String name : names) {
            builder.addValue(name);
        }
        fillData.accept(builder);

        TableBuilder tableBuilder = new TableBuilder(builder.build());
        tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.oldschool);
        shellHelper.printInfo(tableBuilder.build().render(100));
    }
}
