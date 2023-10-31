package com.colak.hzcli.commands.util;

import com.hazelcast.sql.SqlRow;
import com.hazelcast.sql.impl.ResultIterator;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class IteratorUtil {

    public <T> List<T> takeNElements(Iterator<T> iterator, int n) {
        List<T> elements = new ArrayList<>();

        for (int index = 0; index < n && iterator.hasNext(); index++) {
            elements.add(iterator.next());
        }
        return elements;
    }

    public boolean takeElementsFromResultIterator(ResultIterator<SqlRow> iterator, List<SqlRow> elements) {
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
