package com.colak.hzcli.commands.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ListUtils {

    public <T> List<T> takeNElements(List<T> list, int numberOfItems) {
        List<T> sublist = new ArrayList<>();
        for (int index = 0; index < numberOfItems; index++) {
            sublist.add(list.remove(0));
        }
        return sublist;
    }
}
