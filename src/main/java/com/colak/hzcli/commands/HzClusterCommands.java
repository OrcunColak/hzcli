package com.colak.hzcli.commands;

import com.hazelcast.cluster.ClusterState;
import com.hazelcast.version.MemberVersion;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Map;

@ShellComponent
public class HzClusterCommands extends AbstractCommand {

    @ShellMethod(key = "members", value = "List Hazelcast cluster members")
    void members() {
        embedInTable(
                new String[]{"member", "attributes", "version"},
                builder -> hazelcastClient.getCluster().getMembers().forEach(member -> {
                    builder.addRow();
                    String str = member.toString();
                    builder.addValue(str);

                    Map<String, String> attributes = member.getAttributes();
                    builder.addValue(attributes);

                    MemberVersion version = member.getVersion();
                    builder.addValue(version);
                }));
    }

    @ShellMethod(key = "clusterstate", value = "Show Hazelcast cluster state")
    void clusterstate() {
        embedInTable(
                new String[]{"clusterstate"},
                builder -> {
                    builder.addRow();
                    ClusterState clusterState = hazelcastClient.getCluster().getClusterState();
                    builder.addValue(clusterState);
                });
    }
}