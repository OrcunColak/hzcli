package com.colak.hzcli.commands;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jline.reader.EndOfFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ShellComponent
public class HzObjectsCommand extends AbstractCommand {

    private HazelcastInstance hazelcastClient;

    @Autowired
    public void setHazelcastClient(HazelcastInstance hazelcastClient) {
        this.hazelcastClient = hazelcastClient;
    }

    @ShellMethod("List Hazelcast distributed objects")
    void objects() {
        embedInTable(
                new String[]{"type", "name"},
                builder -> hazelcastClient.getDistributedObjects().forEach(distributedObject -> {
                    builder.addRow();
                    String type = getDistributedObjectType(distributedObject);
                    builder.addValue(type);
                    builder.addValue(distributedObject.getName());
                }));
    }

    private static String getDistributedObjectType(DistributedObject distributedObject) {
        String type = "";
        if (distributedObject instanceof IMap) {
            type = "IMap";
        }
        return type;
    }

    @ShellMethod(key = "showm", prefix = "-", value = "List all entries stored in the map")
    void showm(@ShellOption @Valid @NotNull String name,
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
                builder -> map.forEach(entry -> {
                    builder.addRow();
                    builder.addValue(entry.getKey());
                    builder.addValue(entry.getValue());
                }));
    }

    void showMapAsPages(String name) {
        shellHelper.printSuccess("Press Ctrl +D to break");
        IMap<Object, Object> map = hazelcastClient.getMap(name);
        final int fetchSize = 10;
        Iterator<Map.Entry<Object, Object>> iterator = map.iterator();
        int page = 1;
        while (true) {
            List<Map.Entry<Object, Object>> list = takeNElements(iterator, fetchSize);
            if (list.isEmpty()) {
                break;
            }
            page++;
            embedInTable(
                    new String[]{"key", "value"},
                    builder -> list.forEach(entry -> {
                        builder.addRow();
                        builder.addValue(entry.getKey());
                        builder.addValue(entry.getValue());
                    }));
            if (iterator.hasNext()) {
                try {
                    inputReader.prompt("Press any key to view the page " + page);
                } catch (EndOfFileException exception) {
                    break;
                }
            }
        }
    }

    public <T> List<T> takeNElements(Iterator<T> iterator, int n) {
        List<T> elements = new ArrayList<>();

        for (int index = 0; index < n && iterator.hasNext(); index++) {
            elements.add(iterator.next());
        }
        return elements;
    }
}
