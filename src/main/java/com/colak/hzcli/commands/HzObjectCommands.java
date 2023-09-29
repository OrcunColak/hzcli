package com.colak.hzcli.commands;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.map.IMap;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class HzObjectCommands extends AbstractCommand {

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

}
