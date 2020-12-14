package com.ksrate;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.ksrate.archive.ArchiveData;
import com.ksrate.data.Statistic;
import com.ksrate.metric.Metrics;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

@Log4j2
public class Main {
    public static Arguments arguments;
    static JavaSparkContext sc;
    static String appName = "ks-rate";
    private static ArchiveData archiveData;
    private static Metrics metrics;

    public static void main(String[] args) {
        initiate(args);
        spark();
    }

    private static void setup() {
        SparkConf conf = new SparkConf().setAppName(appName).setMaster("local[*]");
        sc = new JavaSparkContext(conf);

        sc.textFile(arguments.getLocalCsvBasePath())
                .map(Statistic::new)
                .foreach(statistic -> {
                    pushArchive(statistic);
                    pushMetrics(statistic);
                });

    }

    private static void spark() {
        setup();
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
        String gcsConfigPath = "./gcs_config.properties";

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
