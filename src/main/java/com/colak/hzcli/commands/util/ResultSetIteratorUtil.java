package com.colak.hzcli.commands.util;

import com.hazelcast.sql.SqlRow;
import com.hazelcast.sql.impl.ResultIterator;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class ResultSetIteratorUtil {
    public boolean takeAllElements(ResultIterator<SqlRow> iterator, List<SqlRow> elements) {
        while (true) {
            ResultIterator.HasNextResult hasNextResult = iterator.hasNext(1, TimeUnit.SECONDS);
            if (hasNextResult.equals(ResultIterator.HasNextResult.DONE)) {
                return true;
            } else if (hasNextResult.equals(ResultIterator.HasNextResult.YES)) {
                elements.add(iterator.next());
            } else if (hasNextResult.equals(ResultIterator.HasNextResult.TIMEOUT)) {
                break;
            }
        }
        return false;
    }
}
