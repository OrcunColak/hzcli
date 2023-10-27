package com.colak.hzcli.commands.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@UtilityClass
public class IteratorUtil {

    public static <T> List<T> takeNElements(Iterator<T> iterator, int n) {
        List<T> elements = new ArrayList<>();

        for (int index = 0; index < n && iterator.hasNext(); index++) {
            elements.add(iterator.next());
        }
        return elements;
    }
}
