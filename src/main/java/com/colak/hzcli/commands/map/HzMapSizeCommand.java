package com.colak.hzcli.commands.map;

import com.colak.hzcli.commands.AbstractCommand;
import com.hazelcast.map.IMap;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class HzMapSizeCommand extends AbstractCommand {

    @ShellMethod(key = "m.size", value = "Show the size of the map")
    void showMapSize(@ShellOption @Valid @NotNull String name) {
        IMap<Object, Object> map = hazelcastClient.getMap(name);
        embedInTable(
                new String[]{"name", "size"},
                builder -> {
                    builder.addRow();
                    builder.addValue(name);
                    builder.addValue(map.size());
                });
    }
}
