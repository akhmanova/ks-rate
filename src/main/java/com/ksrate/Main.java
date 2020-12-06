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
        final Metrics metrics = new Metrics();
        metrics.push(statistic);
    }

    //For archive works
    private static void pushArchive(Statistic statistic) {
        final ArchiveData archiveData = new ArchiveData();
        archiveData.push(statistic);
    }


    public static class Arguments {

        @Getter
        @Parameter(names = {"csvPath"}, description = "Path to local csv file")
        String localCsvBasePath;

        @Getter
        @Parameter(names = {"gcsAuth"}, description = "Path to Google Cloud Storage Auth json file")
        String gcsJsonAuthFilePath;

        @Getter
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
