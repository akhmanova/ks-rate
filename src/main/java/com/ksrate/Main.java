package com.ksrate;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.ksrate.archive.ArchiveData;
import com.ksrate.data.Statistic;
import com.ksrate.metric.Metrics;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Log4j2
public class Main {
    public static Arguments arguments;
    private static ArchiveData archiveData;
    private static Metrics metrics;

    public static void main(String[] args) throws IOException {
        initiate(args);
        String localCsvBasePath = arguments.getLocalCsvBasePath();
        if (localCsvBasePath != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(localCsvBasePath))) {
                String row;
                while ((row = reader.readLine()) != null) {
                    final Statistic statistic = new Statistic(row);
                    pushMetrics(statistic);
                    pushArchive(statistic);

                }
            }
        } else {
            throw new IllegalArgumentException("Processing without LocalCsv base not supported. "
                    + "Use 'csvPath <path>' parameter.");
        }
    }

    private static void initiate(String[] args) {
        arguments = Arguments.getArgs(args);
        archiveData = ArchiveData.getInstance();
        metrics = new Metrics();
    }

    //For metric works
    private static void pushMetrics(Statistic statistic) {
        metrics.push(statistic);
    }

    //For archive works
    private static void pushArchive(Statistic statistic) {
        archiveData.push(statistic);
    }


    @Getter
    public static class Arguments {

        @Parameter(names = {"csvPath"}, description = "Path to local csv file")
        String localCsvBasePath;

        @Parameter(names = {"gcsConfig"}, description = "Path to Google Cloud Storage module config file")
        String gcsConfigPath = "gcs_config.properties";

        public static Arguments getArgs(String... args) {
            Arguments params = new Arguments();
            JCommander.newBuilder()
                    .addObject(params)
                    .build()
                    .parse(args);
            return params;
        }
    }
}
