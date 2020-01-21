package com.example.sundforluft.services;

import java.util.ArrayList;

@SuppressWarnings (value="unchecked")
public class TypeArrayCaster<T> {
    private ArrayList<T> output;

    public TypeArrayCaster(Object[] array) {
        output = new ArrayList<T>();
        for (Object object : array) {
            output.add((T) object);
        }
    }

    public ArrayList<T> getOutput(){return output;}
}
