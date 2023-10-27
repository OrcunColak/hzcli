package com.colak.hzcli.commands.map;

import com.colak.hzcli.commands.AbstractCommand;
import com.hazelcast.map.IMap;
import com.hazelcast.nio.serialization.HazelcastSerializationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@ShellComponent
public class HzMapKeyTypesCommand extends AbstractCommand {

    @ShellMethod(key = "m.keytypes", value = "Show the key types of the map")
    void showMapKeyTypes(@ShellOption @Valid @NotNull String name) {
        IMap<Object, Object> map = hazelcastClient.getMap(name);
        Set<Object> keySet = map.keySet();

        Set<String> types = getTypeSet(keySet);

        embedInTable(
                new String[]{"types"},
                builder -> types.forEach(typeName -> {
                    builder.addRow();
                    builder.addValue(typeName);
                }));
    }

    Set<String> getTypeSet(Set<Object> keySet) {
        Set<String> types = new HashSet<>();
        Iterator<Object> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            try {
                Object next = iterator.next();
                Class<?> type = next.getClass();
                String typeName = type.getSimpleName();
                types.add(typeName);

            } catch (HazelcastSerializationException hazelcastSerializationException) {
                String message = hazelcastSerializationException.getMessage();
                String[] strings = message.split("\\s+");
                String lastEntry = strings[strings.length - 1];
                types.add(lastEntry);
            }
        }
        return types;
    }
}
