package com.example.sundforluft.services;

import java.util.Date;

public interface SundForluftDataBroker {
    void Load();
    SundforluftDataModel[] GetData(Date start, Date end);
}
