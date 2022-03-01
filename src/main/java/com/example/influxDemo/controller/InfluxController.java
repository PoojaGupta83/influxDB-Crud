package com.example.influxDemo.controller;

import com.example.influxDemo.service.InfluxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/influx")
public class InfluxController {
    @Autowired
    private InfluxService influxService;

    @GetMapping(value = "/connect")
    public void connect() throws Exception {
        influxService.connect();
    }

    @PostMapping(value = "/write")
    public void writeData() throws Exception {
        influxService.writeData();
    }

    @GetMapping(value = "/read")
    public void readData() {
        influxService.readData();
    }

    @DeleteMapping(value = "/delete")
    public void deleteData() {
        influxService.deleteData();
    }

    @PutMapping(value = "/update")
    public void updateData() {
        influxService.updateData();
    }
}