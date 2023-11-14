package com.colak.hzcli.commands.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@Slf4j
public class HzSqlMappingsCommand extends HzSqlCommand {

    @ShellMethod(key = "sql.mappings", value = "List all mappings created by SQL")
    void mappings() {
        sql("SELECT * from information_schema.mappings", Integer.MAX_VALUE);
    }
}
