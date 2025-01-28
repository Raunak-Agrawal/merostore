package com.merostore.backend.utils.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.merostore.backend.config.BucketName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
@Data
public class FileStore {
    private final AmazonS3 amazonS3;

    private final String PLACEHOLDER_IMAGE = "placeholder-image.jpeg";

    public void upload(String path,
                       String fileName,
                       Map<String, String> metadata,
                       InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(metadata.getOrDefault("Content-Type", "image/jpeg"));
        objectMetadata.setContentLength(Long.parseLong(metadata.get("Content-Length")));

        try {
            amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }

    public byte[] download(String path, String key) {
        try {
            S3Object object = amazonS3.getObject(path, key);
            S3ObjectInputStream objectContent = object.getObjectContent();
            return IOUtils.toByteArray(objectContent);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download the file", e);
        }
    }

    public String getObject(Long folder, String key) {
        try {
            String path = String.format("%s/%s", BucketName.MEROSTORE.getBucketName(), folder);
            S3Object object = amazonS3.getObject(path, key);
            if (object.getKey().equalsIgnoreCase(key)) {
                return getObjectURI(folder, key);
            } else throw new IllegalStateException("Failed to get the file");
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to get the file", e);
        }
    }

    public String getObjectURI(Long folder, String key) {
        String path = String.format("%s/%s", BucketName.MEROSTORE.getBucketName(), folder);
        String prefixURL = "https://s3.ap-south-1.amazonaws.com/";
        return prefixURL + path + "/" + key;
    }

    public Boolean deleteObject(Long folder, String key) {
        String path = String.format("%s/%s", folder, key);
        log.info("Deleing object:{} , {}", path, key);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(BucketName.MEROSTORE.getBucketName(), path));
            return true;
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to delete the file", e);
        }
    }

    public String getPlaceholderImageURI() {
        String folder = "default";
        String path = String.format("%s/%s", BucketName.MEROSTORE.getBucketName(), folder);
        String prefixURL = "https://s3.ap-south-1.amazonaws.com/";
        return prefixURL + path + "/" + PLACEHOLDER_IMAGE;
    }

    public String getPlaceholderImage(){
        return PLACEHOLDER_IMAGE;
    }

}