package com.example.sundforluft.services;

import java.util.ArrayList;
import java.util.List;

public class TypeArrayCaster {
    @SuppressWarnings (value="unchecked")
    public static <T> List<T> cast(Object[] inputArray) {
        ArrayList<T> arr = new ArrayList<>();
        for (Object input : inputArray) {
            arr.add((T) input);
        }
        return arr;
    }

}
