package com.colak.hzcli.commands;

import com.hazelcast.sql.SqlColumnMetadata;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRowMetadata;
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

//    sql "CREATE DATA CONNECTION IF NOT EXISTS foo TYPE JDBC SHARED OPTIONS('jdbcUrl'='jdbc:postgresql://localhost:5432/db','user'='postgres','password'='postgres')"
//    sql "CREATE OR REPLACE MAPPING myworker  DATA CONNECTION foo"
//    sql "SELECT * from myworker"
//    sql "SELECT * from myworker1"

    @ShellMethod("Select with HZ SQL")
    void sql(@ShellOption @Valid @NotNull String sql) {
        try {
            SqlService sqlService = hazelcastClient.getSql();
            try (SqlResult sqlResult = sqlService.execute(sql)) {
                if (sqlResult.isRowSet()) {
                    printSelectResult(sqlResult);
                }
                shellHelper.printSuccess("Success");
            }
        } catch (Exception exception) {
            log.error("Sql Exception: {}", exception.getMessage(), exception);
            shellHelper.printError("Failed");
        }
    }

    private void printSelectResult(SqlResult sqlResult) {
        String[] columnNames = getColumnNames(sqlResult);
        int numberOfColumns = columnNames.length;
        embedInTable(columnNames,
                builder -> sqlResult.forEach(sqlRow -> {
                    builder.addRow();
                    for (int index = 0; index < numberOfColumns; index++) {
                        builder.addValue(sqlRow.getObject(index));
                    }
                }));
    }

    private String[] getColumnNames(SqlResult sqlResult) {
        SqlRowMetadata rowMetadata = sqlResult.getRowMetadata();
        return rowMetadata.getColumns().stream()
                .map(SqlColumnMetadata::getName)
                .toArray(String[]::new);
    }
}
