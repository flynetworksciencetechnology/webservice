package com.flypay.utils.comparator;

import java.util.Comparator;
import java.util.Map;

public class ParamKeyComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2);
    }
}
