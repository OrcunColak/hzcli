package com.colak.hzcli.commands.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ListUtils {

    public <T> List<T> removeNElements(List<T> list, int numberOfItems) {
        List<T> sublist = new ArrayList<>();
        int upperLimit = Math.min(numberOfItems, list.size());
        for (int index = 0; index < upperLimit; index++) {
            sublist.add(list.remove(0));
        }
        return sublist;
    }
}
