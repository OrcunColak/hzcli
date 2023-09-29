package com.colak.hzcli.commands;

import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@Slf4j
public class HzSqlCommands extends AbstractCommand {

    @ShellMethod("Execute HZ SQL")
    void sql(@ShellOption @Valid @NotNull String sql) {
        try {
            SqlService sqlService = hazelcastClient.getSql();
            try (SqlResult sqlResult = sqlService.execute(sql)) {
            }
        } catch (Exception exception) {
            log.error("Sql Exception:", exception);
        }

    }


}
