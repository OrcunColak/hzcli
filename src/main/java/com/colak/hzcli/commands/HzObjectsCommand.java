package com.colak.hzcli.commands;

import com.colak.hzcli.shell.ShellHelper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModelBuilder;

import java.util.function.Consumer;

@ShellComponent
public class HzObjectsCommand {

    @Autowired
    HazelcastInstance hazelcastClient;

    @Autowired
    ShellHelper shellHelper;

    @ShellMethod("List Hazelcast distributed objects")
    void objects() {
        embedInTable(
                new String[] {"name"},
                buider -> hazelcastClient.getDistributedObjects().forEach(distributedObject -> {
                    buider.addRow();
                    buider.addValue(distributedObject.getName());
                }));
    }

    @ShellMethod("List all entries stored in the map")
    void showm(@ShellOption String name) {
        IMap<Object, Object> map = hazelcastClient.getMap(name);
        embedInTable(
                new String[] {"key", "value"},
                buider -> map.forEach(entry -> {
                    buider.addRow();
                    buider.addValue(entry.getKey());
                    buider.addValue(entry.getValue());
                }));
    }

    private void embedInTable(String[] names, Consumer<TableModelBuilder<Object>> fillData) {
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
