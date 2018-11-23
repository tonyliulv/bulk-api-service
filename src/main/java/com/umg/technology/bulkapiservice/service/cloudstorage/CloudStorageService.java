package com.umg.technology.bulkapiservice.service.cloudstorage;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

// Handle all Cloud Storage requests
@Slf4j
@Service("cloudStorageService")
public class CloudStorageService {

    private Storage storage;
    private Bucket bucket;

    @Value("${cloudstorage.bucketname}")
    private String bucketName;

    public CloudStorageService() {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public String writeGcs(final String data, final String blobName){

        log.info("CloudStorageService.writeGcs() start...");


        Bucket bucket = getBucket(bucketName);

        BlobId blobId = saveString(blobName,data, bucket);
        return blobId.getName();
    }

    public String readGcs(final String blobName) {
        log.info("CloudStorageService.writeGcs() start...");
        BlobId blobId = BlobId.of(bucketName, blobName);

        if (blobId == null)
            return "blobId is null!";

        Blob blob = storage.get(blobId);
        if ( blob != null)
            return new String(blob.getContent());
        else
            return "No Blob found!";
    }

    private Bucket getBucket(final String bucketName) {
        bucket = storage.get(bucketName);
        if (bucket == null) {
            log.info("Creatubg a new bucket : " + bucketName);
            bucket = storage.create(BucketInfo.of(bucketName));
        }

        return bucket;
    }

    // Save a string to a blob
    private BlobId saveString(final String blobName, final String value, final Bucket bucket) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        Blob blob = bucket.create(blobName, bytes);
        return blob.getBlobId();
    }

}
