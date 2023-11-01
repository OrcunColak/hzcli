package com.colak.hzcli.commands;

import com.colak.hzcli.shell.InputReader;
import com.colak.hzcli.shell.ShellHelper;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;

import java.util.function.Consumer;

public abstract class AbstractCommand {

    protected ShellHelper shellHelper;

    protected InputReader inputReader;

    protected HazelcastInstance hazelcastClient;

    @Autowired
    public void setShellHelper(@Lazy ShellHelper shellHelper) {
        this.shellHelper = shellHelper;
    }

    @Autowired
    public void setInputReader(@Lazy InputReader inputReader) {
        this.inputReader = inputReader;
    }


    @Autowired
    public void setHazelcastClient(HazelcastInstance hazelcastClient) {
        this.hazelcastClient = hazelcastClient;
    }

    public void embedInTable(String[] columnNames, Consumer<TableModelBuilder<Object>> fillData) {
        TableModelBuilder<Object> builder = new TableModelBuilder<>();
        builder.addRow();
        for (String columnName : columnNames) {
            builder.addValue(columnName);
        }
        fillData.accept(builder);

        TableModel tableModel = builder.build();

        TableBuilder tableBuilder = new TableBuilder(tableModel);
        tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.oldschool);
        Table table = tableBuilder.build();
        shellHelper.printInfo(table.render(100));
    }
}
