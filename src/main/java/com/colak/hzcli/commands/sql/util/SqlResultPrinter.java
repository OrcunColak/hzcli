package com.colak.hzcli.commands.sql.util;

import com.colak.hzcli.commands.AbstractCommand;
import com.colak.hzcli.commands.util.ListUtils;
import com.colak.hzcli.commands.util.ResultSetIteratorUtil;
import com.colak.hzcli.shell.InputReader;
import com.hazelcast.sql.SqlColumnMetadata;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;
import com.hazelcast.sql.SqlRowMetadata;
import com.hazelcast.sql.impl.ResultIterator;
import lombok.RequiredArgsConstructor;
import org.jline.reader.EndOfFileException;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import org.springframework.shell.table.TableModelBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
class SqlResultPrinter {

    private final AbstractCommand abstractCommand;

    private final Terminal terminal;

    private final InputReader inputReader;

    private final SqlResult sqlResult;

    private final int pageSize;

    int page = 1;
    boolean continueLoop = true;

    public void print() {
        String[] columnNames = getColumnNames(sqlResult);
        int numberOfColumns = columnNames.length;

        ResultIterator<SqlRow> iterator = (ResultIterator<SqlRow>) sqlResult.iterator();

        NonBlockingReader nonBlockingReader = terminal.reader();

        while (continueLoop) {
            List<SqlRow> list = new ArrayList<>();
            boolean iteratorDone = ResultSetIteratorUtil.takeAllElements(iterator, list);
            if (iteratorDone) {
                printDoneIterator(list,columnNames);
                break;
            }
            try {
                if (!list.isEmpty()) {
                    abstractCommand.embedInTable(columnNames,
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

    private void printDoneIterator (List<SqlRow> list,String[] columnNames) {
        if ((list.isEmpty() && page == 1)) {
            printEmptyResultSet(columnNames);
            return;
        }
        int numberOfColumns = columnNames.length;
        while (!list.isEmpty()) {
            List<SqlRow> sublist = ListUtils.removeNElements(list, pageSize);

            abstractCommand.embedInTable(columnNames,
                    builder -> sublist.forEach(sqlRow -> printSqlRow(builder, sqlRow, numberOfColumns)));
            page++;
            if (!list.isEmpty()) {
                try {
                    inputReader.prompt("Press any key to view the next page " + page);
                } catch (EndOfFileException exception) {
                    continueLoop = false;
                    break;
                }
            }
        }
    }
    private void printEmptyResultSet(String[] columnNames) {
        abstractCommand.embedInTable(columnNames,
                builder -> {

                });
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
