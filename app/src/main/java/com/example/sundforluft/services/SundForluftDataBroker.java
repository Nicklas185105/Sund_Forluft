package com.example.sundforluft.services;

import java.time.LocalDateTime;
import java.util.List;

public interface SundForluftDataBroker {
    void Load();
    List<SundforluftDataModel> GetData(LocalDateTime start, LocalDateTime end);
}
