package com.example.sundforluft.services.DataBroker;

import java.time.LocalDateTime;
import java.util.List;

public interface DataBroker {
    boolean load(LocalDateTime start, LocalDateTime end);
    List<AirQualityDataModel> getData();
}
