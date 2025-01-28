package com.merostore.backend.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    MEROSTORE("merostore.tech");
    private final String bucketName;
}