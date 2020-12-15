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
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Seconds;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.pubsub.PubsubUtils;
import org.apache.spark.streaming.pubsub.SparkGCPCredentials;
import org.apache.spark.streaming.pubsub.SparkPubsubMessage;

import java.util.concurrent.TimeUnit;

@Log4j2
public class Main {
    private static final String PROJECT_ID = "ks-rate-297815";
    private static final String SUBSCRIPTION = "ks-rate";
    private static final String SERVICE_ACCOUNT_JSON_KEY_FILE_PATH = "ks-rate-app.json";
    public static Arguments arguments;
    private static JavaSparkContext sc;
    private static JavaStreamingContext jsc;
    private static String appName = "ks-rate";
    private static ArchiveData archiveData;
    private static Metrics metrics;

    public static void main(String[] args) {
        initiate(args);
        spark();
    }

    private static void initiate(String[] args) {
        arguments = Arguments.getArgs(args);
        SparkConf conf = new SparkConf().setAppName(appName).setMaster("local[*]");
//        sc = new JavaSparkContext(conf);
        jsc = new JavaStreamingContext(conf, Seconds.apply(15));

        archiveData = ArchiveData.getInstance();
        metrics = new Metrics();
    }

    private static void spark() {
        JavaReceiverInputDStream<SparkPubsubMessage> dStream = PubsubUtils.createStream(
                jsc,
                PROJECT_ID,
                SUBSCRIPTION,
                new SparkGCPCredentials.Builder()
                        .jsonServiceAccount(SERVICE_ACCOUNT_JSON_KEY_FILE_PATH)
                        .build(),
                StorageLevel.MEMORY_AND_DISK_SER()
        );
        dStream.foreachRDD(rdd -> rdd.map(SparkPubsubMessage::getData)
                .map(String::new)
                .map(Statistic::new)
                .foreach(statistic -> {

                    pushArchive(statistic);
                    pushMetrics(statistic);
                })
        );

        try {
            jsc.start();
            // Let the job run for the given duration and then terminate it.
            jsc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            jsc.stop(true, true);
        }

//        sc.textFile(arguments.getLocalCsvBasePath())
//                .map(Statistic::new)
//                .foreach(statistic -> {
//                    pushArchive(statistic);
//                    pushMetrics(statistic);
//                });
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

        @Parameter(names = {"dbp"}, description = "Path to db connection properties file")
        String dbPropertiesPath = "./connection.properties";

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
