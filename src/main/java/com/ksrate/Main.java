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

    public static void main(String[] args) throws IOException {
        arguments = Arguments.getArgs(args);
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

    //For metric works
    private static void pushMetrics(Statistic statistic) {
        final Metrics metrics = new Metrics();
        metrics.push(statistic);
    }

    //For archive works
    private static void pushArchive(Statistic statistic) {
        final ArchiveData archiveData = new ArchiveData();
        archiveData.push(statistic);
    }


    @Getter
    public static class Arguments {

        @Parameter(names = {"csvPath"}, description = "Path to local csv file")
        String localCsvBasePath;

        @Parameter(names = {"gcsAuth"}, description = "Path to Google Cloud Storage Auth json file")
        String gcsJsonAuthFilePath;

        @Parameter(names = {"--noGcs"}, description = "Disable GSC")
        boolean noGoogleCloudStorage = false;

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
