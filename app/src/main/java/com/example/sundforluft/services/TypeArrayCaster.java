package com.example.sundforluft.services;

import java.util.ArrayList;

@SuppressWarnings (value="unchecked")
class TypeArrayCaster<T> {
    private ArrayList<T> output;

    TypeArrayCaster(Object[] array) {
        output = new ArrayList<>();
        for (Object object : array) {
            output.add((T) object);
        }
    }

    ArrayList<T> getOutput(){return output;}
}
