package com.colak.hzcli.commands.sql;

import com.colak.hzcli.commands.AbstractCommand;
import com.colak.hzcli.commands.sql.util.SqlServiceUtil;
import com.hazelcast.sql.SqlService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@Slf4j
public class HzSqlCommand extends AbstractCommand {

    private Terminal terminal;

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @ShellMethod(key = "mappings", value = "Select mapping names of Hazelcast")
    void mappings() {
        sql("SELECT * from information_schema.mappings", Integer.MAX_VALUE);
    }

    @ShellMethod(key = "columns", value = "Select mapped columns Hazelcast")
    void columns() {
        sql("SELECT * from information_schema.columns", Integer.MAX_VALUE);
    }

    @ShellMethod(key = "tables", value = "Select mapped tables of Hazelcast")
    void tables() {
        sql("SELECT * from information_schema.mappings", Integer.MAX_VALUE);
    }

//    sql "CREATE DATA CONNECTION IF NOT EXISTS foo TYPE JDBC SHARED OPTIONS('jdbcUrl'='jdbc:postgresql://localhost:5432/db','user'='postgres','password'='postgres')"
//    sql "CREATE OR REPLACE MAPPING myworker  DATA CONNECTION foo"
//    sql "SELECT * from myworker"
//    sql "SELECT * from myworker1"
//    sql "select * from table(generate_stream(1)) LIMIT 2"

    @ShellMethod(key = "sql", value = "Select with HZ SQL")
    void sql(@ShellOption @Valid @NotNull String sql,
             @ShellOption(value = {"-p",}, help = "Optional page size", defaultValue = "10") int pageSize) {
        try {
            SqlService sqlService = hazelcastClient.getSql();

            SqlServiceUtil sqlServiceUtil = new SqlServiceUtil(this, sqlService, terminal, inputReader);
            sqlServiceUtil.sql(sql, pageSize);

            shellHelper.printSuccess("Success");
        } catch (Exception exception) {
            log.error("Sql Exception: {}", exception.getMessage(), exception);
            shellHelper.printError("Failed");
        }
    }
}
