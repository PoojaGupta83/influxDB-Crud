package com.example.influxDemo.service;

import com.example.influxDemo.entity.NTemperature;
import com.influxdb.client.*;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class InfluxService {


    private static char[] token = "z01W7X9kl2JtlEF2J7f-M3as0oo6rMelLVRgneZW89bkJXIbQEOBfbjVlGXo-FTETA_zRMJzT7o4HAJ-3Z-94g==".toCharArray();
    private static String org = "primary";
    private static String bucket = "secondary";

    InfluxDBClient client = InfluxDBClientFactory
            .create("http://localhost:8086", token, org, bucket);

    public InfluxDBClient connect() {
        InfluxDBClient client = InfluxDBClientFactory
                .create("http://localhost:8086", token, org, bucket);
        return client;
    }


    public void writeData() {
        InfluxDBClient client = InfluxDBClientFactory
                .create("http://localhost:8086", token, org, bucket);
        WriteApiBlocking writeApi = client.getWriteApiBlocking();

        // Write by Data Point
        Point point = Point.measurement("Ntemperature")
                .addTag("location", "west")
                .addField("value", 55D)
                .time(Instant.now(), WritePrecision.MS);
        writeApi.writePoint(point);

        // Write by LineProtocol
        writeApi.writeRecord(WritePrecision.MS, "Ntemperature,location=north value=60.0");
        // Write by POJO
        NTemperature temperature = new NTemperature();
        temperature.setLocation("south");
        temperature.setValue(65D);
        temperature.setTime(Instant.now());
        writeApi.writeMeasurement(WritePrecision.MS, temperature);
        client.close();
    }


    public void readData() {
        InfluxDBClient client = InfluxDBClientFactory
                .create("http://localhost:8086", token, org, bucket);

        // Query data
        String query = "from(bucket:\"secondary\") |> range(start: 0) |> filter(fn: (r) => r._measurement == \"Ntemperature\")";
        QueryApi queryApi = client.getQueryApi();

        // Map to POJO

        List<NTemperature> temperatures = queryApi.query(query, NTemperature.class);
        for (NTemperature temperature : temperatures) {
            System.out.println("location:" + temperature.getLocation() + ", " + "value:" + temperature.getValue() + " at " + "time:" + temperature.getTime());
        }

        client.close();


    }


    public void deleteData() {
        InfluxDBClient client = InfluxDBClientFactory
                .create("http://localhost:8086", token, org, bucket);

        DeleteApi deleteApi = client.getDeleteApi();

        OffsetDateTime start = OffsetDateTime.now().minus(24, ChronoUnit.MINUTES);
        OffsetDateTime stop = OffsetDateTime.now();
        deleteApi.delete(start, stop, "", bucket, org);

        client.close();
    }


    public void updateData() {
        InfluxDBClient client = InfluxDBClientFactory
                .create("http://localhost:8086", token, org, bucket);
        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        writeApi.writeRecord(WritePrecision.MS, "Ntemperature,location=north-east value=60.0");


    }
}
