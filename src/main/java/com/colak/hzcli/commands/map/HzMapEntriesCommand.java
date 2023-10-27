package com.colak.hzcli.commands.map;

import com.colak.hzcli.commands.AbstractCommand;
import com.colak.hzcli.commands.util.IteratorUtil;
import com.hazelcast.map.IMap;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jline.reader.EndOfFileException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.TableModelBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ShellComponent
public class HzMapEntriesCommand extends AbstractCommand {

    @ShellMethod(key = "m.entries", prefix = "-", value = "List all entries stored in the map")
    void showMapEntries(@ShellOption @Valid @NotNull String name,
                        @ShellOption(value = {"-p",},
                       help = "Optional pagination flag")
               boolean paginate) {
        if (paginate) {
            showMapAsPages(name);
        } else {
            showMap(name);
        }
    }

    void showMap(String name) {
        IMap<Object, Object> map = hazelcastClient.getMap(name);
        embedInTable(
                new String[]{"key", "value"},
                builder -> map.forEach(entry -> printMapEntry(builder, entry)));
    }

    void showMapAsPages(String name) {
        shellHelper.printSuccess("Press Ctrl +D to break");
        IMap<Object, Object> map = hazelcastClient.getMap(name);
        final int fetchSize = 10;
        Iterator<Map.Entry<Object, Object>> iterator = map.iterator();
        int page = 1;
        boolean continueLoop = true;
        while (continueLoop) {
            List<Map.Entry<Object, Object>> list = IteratorUtil.takeNElements(iterator, fetchSize);
            if (list.isEmpty()) {
                break;
            }
            page++;
            embedInTable(
                    new String[]{"key", "value"},
                    builder -> list.forEach(entry -> printMapEntry(builder, entry)));
            if (iterator.hasNext()) {
                try {
                    inputReader.prompt("Press any key to view the next page " + page);
                } catch (EndOfFileException exception) {
                    continueLoop = false;
                }
            }
        }
    }

    private void printMapEntry(TableModelBuilder<Object> builder, Map.Entry<Object, Object> entry) {
        builder.addRow();
        builder.addValue(entry.getKey());
        builder.addValue(entry.getValue());
    }
}
