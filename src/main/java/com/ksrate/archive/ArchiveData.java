package com.ksrate.archive;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import com.ksrate.Main;
import com.ksrate.data.Statistic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class ArchiveData {
    public void push(Statistic statistic) {
        if (Main.arguments.isNoGoogleCloudStorage()) {
            return;
        }
        Storage storage = getStorage();
        final String bucketName = "ks-archive";
        final String objectName = "statistic";
        Bucket bucket = storage.create(BucketInfo.of(bucketName));
        final byte[] data = statistic.getSource().getBytes(StandardCharsets.UTF_8);
        bucket.create(objectName, data);
    }

    private Storage getStorage() {
        String authJson = Main.arguments.getGcsJsonAuthFilePath();
        if (authJson != null) {
            try (final FileInputStream jsonInputStream = new FileInputStream(authJson)) {
                return StorageOptions.newBuilder()
                        .setCredentials(ServiceAccountCredentials.fromStream(jsonInputStream))
                        .build().getService();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            return StorageOptions.getDefaultInstance().getService();
        }
    }
}
