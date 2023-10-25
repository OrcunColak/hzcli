package com.colak.hzcli.commands;

import com.hazelcast.sql.SqlColumnMetadata;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;
import com.hazelcast.sql.SqlRowMetadata;
import com.hazelcast.sql.SqlService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.jline.reader.EndOfFileException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.TableModelBuilder;

import java.util.Iterator;
import java.util.List;

@ShellComponent
@Slf4j
public class HzSqlCommands extends AbstractCommand {

//    sql "CREATE DATA CONNECTION IF NOT EXISTS foo TYPE JDBC SHARED OPTIONS('jdbcUrl'='jdbc:postgresql://localhost:5432/db','user'='postgres','password'='postgres')"
//    sql "CREATE OR REPLACE MAPPING myworker  DATA CONNECTION foo"
//    sql "SELECT * from myworker"
//    sql "SELECT * from myworker1"
//    sql "select * from table(generate_stream(1)) LIMIT 2"

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

        Iterator<SqlRow> iterator = sqlResult.iterator();

        int page = 1;
        final int fetchSize = 10;
        boolean continueLoop = true;
        while (continueLoop) {
            List<SqlRow> list = IteratorUtil.takeNElements(iterator, fetchSize);
            if (list.isEmpty()) {
                break;
            }
            page++;
            embedInTable(columnNames,
                    builder -> list.forEach(sqlRow -> printSqlRow(builder, sqlRow, numberOfColumns)));
            if (iterator.hasNext()) {
                try {
                    inputReader.prompt("Press any key to view the next page " + page);
                } catch (EndOfFileException exception) {
                    continueLoop = false;
                }
            }
        }

    }
    private void printSqlRow(TableModelBuilder<Object> builder, SqlRow sqlRow, int numberOfColumns) {
        builder.addRow();
        for (int index = 0; index < numberOfColumns; index++) {
            builder.addValue(sqlRow.getObject(index));
        }
    }

    private String[] getColumnNames(SqlResult sqlResult) {
        SqlRowMetadata rowMetadata = sqlResult.getRowMetadata();
        return rowMetadata.getColumns().stream()
                .map(SqlColumnMetadata::getName)
                .toArray(String[]::new);
    }
}
