package com.example.influxDemo.entity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "weather")
public class Weather1 {
    @Column(tag = true)
    public String location;
    @Column
    public Double value;
    @Column(timestamp = true)
    public Instant time;
}