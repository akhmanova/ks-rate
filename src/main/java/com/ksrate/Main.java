package com.ksrate;

import com.ksrate.archive.ArchiveData;
import com.ksrate.data.Statistic;
import com.ksrate.metric.Metrics;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Log4j2
public class Main {
    final static ArchiveData archiveData = new ArchiveData();
    final static Metrics metrics = new Metrics();

    public static void main(String[] args) throws IOException {
        String path = args[0];
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String row;
        while ((row = reader.readLine()) != null) {
            final Statistic statistic = new Statistic(row);
            pushMetrics(statistic);
            pushArchive(statistic);

        }
        reader.close();
    }

    //For metric works
    private static void pushMetrics(Statistic statistic) {
        metrics.push(statistic);
    }

    //For archive works
    private static void pushArchive(Statistic statistic) {
        archiveData.push(statistic);
    }
}
