package com.colak.hzcli.commands.sql;

import com.colak.hzcli.commands.AbstractCommand;
import com.colak.hzcli.commands.util.IteratorUtil;
import com.hazelcast.sql.SqlColumnMetadata;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;
import com.hazelcast.sql.SqlRowMetadata;
import com.hazelcast.sql.SqlService;
import com.hazelcast.sql.impl.ResultIterator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.jline.reader.EndOfFileException;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.TableModelBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ShellComponent
@Slf4j
public class HzSqlCommand extends AbstractCommand {

    @Autowired
    @Lazy
    private Terminal terminal;


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
            try (SqlResult sqlResult = sqlService.execute(sql)) {
                if (sqlResult.isRowSet()) {
                    printSelectResult(sqlResult, pageSize);
                }
                shellHelper.printSuccess("Success");
            }
        } catch (Exception exception) {
            log.error("Sql Exception: {}", exception.getMessage(), exception);
            shellHelper.printError("Failed");
        }
    }

    private void printSelectResult(SqlResult sqlResult, int pageSize) {
        String[] columnNames = getColumnNames(sqlResult);
        int numberOfColumns = columnNames.length;

        ResultIterator<SqlRow> iterator = (ResultIterator<SqlRow>) sqlResult.iterator();

        int page = 1;
        boolean continueLoop = true;
        NonBlockingReader nonBlockingReader = terminal.reader();

        while (continueLoop) {
            List<SqlRow> list = new ArrayList<>();
            boolean iteratorDone = IteratorUtil.takeElementsFromStream(iterator, list);
            if (iteratorDone) {
                break;
            }

            while (list.size() >= pageSize) {
                List<SqlRow> sublist = subList(list, pageSize);

                embedInTable(columnNames,
                        builder -> sublist.forEach(sqlRow -> printSqlRow(builder, sqlRow, numberOfColumns)));
                page++;
                try {
                    inputReader.prompt("Press any key to view the next page " + page);
                } catch (EndOfFileException exception) {
                    continueLoop = false;
                }
            }

            try {
                if (!list.isEmpty()) {
                    embedInTable(columnNames,
                            builder -> list.forEach(sqlRow -> printSqlRow(builder, sqlRow, numberOfColumns)));
                }
                int read = nonBlockingReader.read(1);
                // Ctrl + D
                if (read == -1) {
                    continueLoop = false;
                }
            } catch (IOException exception) {
                continueLoop = false;
            }
        }
    }

    List<SqlRow> subList(List<SqlRow> list, int numberOfItems) {
        List<SqlRow> sublist = new ArrayList<>();
        for (int index = 0; index < numberOfItems; index++) {
            sublist.add(list.remove(0));
        }
        return sublist;
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
