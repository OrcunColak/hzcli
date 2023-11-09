package com.colak.hzcli.commands.distributedobjects;

import com.colak.hzcli.commands.AbstractCommand;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.map.IMap;
import com.hazelcast.ringbuffer.Ringbuffer;
import com.hazelcast.topic.ITopic;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class HzObjectCommands extends AbstractCommand {

    @ShellMethod(key = "objects", value = "List Hazelcast distributed objects")
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
        } else if (distributedObject instanceof ITopic<?>) {
            type = "ITopic";
        } else if (distributedObject instanceof Ringbuffer<?>) {
            type = "Ringbuffer";
        }
        return type;
    }

}
