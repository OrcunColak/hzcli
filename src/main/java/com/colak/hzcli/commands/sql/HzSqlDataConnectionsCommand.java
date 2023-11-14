package com.colak.hzcli.commands.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@Slf4j
public class HzSqlDataConnectionsCommand extends HzSqlCommand {

    @ShellMethod(key = "sql.datacons", value = "List all data connections created by SQL")
    void dataConnections() {
//  SHOW DATA CONNECTIONS output is too short
//  +--------+---------------+--------------+
//  |name    |connection_type|resource_types|
//  +--------+---------------+--------------+
//  |postgres|jdbc           |["Table"]     |
//  +--------+---------------+--------------+

// This has better output
//  SELECT * FROM information_schema.dataconnections"
//  +---------+------+--------+----+------+-----------------------------------------------------------------------------------------+------+
//  |catalog  |schema|name    |type|shared|options                                                                                  |source|
//  +---------+------+--------+----+------+-----------------------------------------------------------------------------------------+------+
//  |hazelcast|public|postgres|jdbc|true  |{"jdbcUrl":"jdbc:postgresql://localhost:5432/db","user":"postgres","password":"postgres"}|SQL   |
//  +---------+------+--------+----+------+-----------------------------------------------------------------------------------------+------+
        sql("SELECT * FROM information_schema.dataconnections", Integer.MAX_VALUE);
    }
}
