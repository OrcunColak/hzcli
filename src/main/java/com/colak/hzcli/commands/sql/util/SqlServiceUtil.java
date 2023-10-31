package com.colak.hzcli.commands.sql.util;

import com.colak.hzcli.commands.AbstractCommand;
import com.colak.hzcli.shell.InputReader;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlService;
import lombok.RequiredArgsConstructor;
import org.jline.terminal.Terminal;

@RequiredArgsConstructor
public class SqlServiceUtil {

    private final AbstractCommand abstractCommand;

    private final SqlService sqlService;

    private final Terminal terminal;

    private final InputReader inputReader;

    public void sql(String sql) {
        sql(sql, Integer.MAX_VALUE);
    }

    public void sql(String sql, int pageSize) {
        try (SqlResult sqlResult = sqlService.execute(sql)) {
            if (sqlResult.isRowSet()) {
                SqlResultPrinter sqlResultPrinter = new SqlResultPrinter(abstractCommand,
                        terminal,
                        inputReader,
                        sqlResult,
                        pageSize);
                sqlResultPrinter.print();
            }
        }
    }
}
