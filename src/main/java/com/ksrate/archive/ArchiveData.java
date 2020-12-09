package com.ksrate.archive;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import com.ksrate.Main;
import com.ksrate.data.Statistic;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArchiveData {

    private static final String TEMP_BLOB_NAME = "tempBlob";
    private String bucketName;
    private String blobName;
    private Storage storage;

    private ArchiveData(Properties properties) {
        bucketName = properties.getProperty("gcs.bucket.name");
        blobName = properties.getProperty("gcs.blob.name");
        storage = getStorage(properties.getProperty("gcs.auth.path"));
    }

    public static ArchiveData getInstance() {
        try {
            final Properties properties = new Properties();
            properties.load(Files.newInputStream(Paths.get(Main.arguments.getGcsConfigPath())));
            return new ArchiveData(properties);
        } catch (FileNotFoundException | NoSuchFileException e) {
            System.out.println("Start without GCS");
            return new NoGCS();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void push(Statistic statistic) {
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
        final Blob blob = storage.get(bucketName, blobName);
        if (blob == null) {
            storage.create(blobInfo);
        }
        final byte[] data = (statistic.getSource() + "\n").getBytes(StandardCharsets.UTF_8);
        compose(data, blobInfo);
    }

    private void compose(byte[] data, BlobInfo blobInfo) {
        BlobId newBlobId = BlobId.of(bucketName, TEMP_BLOB_NAME);
        BlobInfo newBlobInfo = BlobInfo.newBuilder(newBlobId).build();
        storage.create(newBlobInfo, data);
        Storage.ComposeRequest request = Storage.ComposeRequest.newBuilder()
                .setTarget(blobInfo)
                .addSource(blobName)
                .addSource(TEMP_BLOB_NAME)
                .build();
        storage.compose(request);
        storage.delete(newBlobId);
    }

    private Storage getStorage(String authJson) {
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
