/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.server.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;

/**
 * S3 Storage Service for Large Datasets.
 * 
 * Uses MinIO or AWS S3 to store large scientific data files.
 */
@Service
public class S3StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(S3StorageService.class);

    private S3Client s3Client;
    private S3Presigner presigner;

    @Value("${storage.s3.endpoint:http://localhost:9000}")
    private String endpoint;

    @Value("${storage.s3.region:us-east-1}")
    private String region;

    @Value("${storage.s3.access-key:minioadmin}")
    private String accessKey;

    @Value("${storage.s3.secret-key:minioadmin}")
    private String secretKey;

    @Value("${storage.s3.bucket:jscience-data}")
    private String bucketName;

    @Value("${storage.s3.enabled:false}")
    private boolean enabled;

    public S3StorageService() {
    }

    // Initialize manually or via @PostConstruct if enabled
    @javax.annotation.PostConstruct
    public void init() {
        if (!enabled) {
            LOG.info("S3 Storage Service DISABLED");
            return;
        }

        try {
            StaticCredentialsProvider creds = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey));

            this.s3Client = S3Client.builder()
                    .endpointOverride(URI.create(endpoint))
                    .region(Region.of(region))
                    .credentialsProvider(creds)
                    .forcePathStyle(true) // Required for MinIO
                    .build();

            this.presigner = S3Presigner.builder()
                    .endpointOverride(URI.create(endpoint))
                    .region(Region.of(region))
                    .credentialsProvider(creds)
                    .build();

            ensureBucketExists();
            LOG.info("S3 Storage Service ENABLED (Endpoint: {}, Bucket: {})", endpoint, bucketName);
        } catch (Exception e) {
            LOG.error("Failed to initialize S3 Storage Service", e);
        }
    }

    private void ensureBucketExists() {
        try {
            s3Client.createBucket(b -> b.bucket(bucketName));
        } catch (Exception e) {
            // Bucket likely exists or permissions issue
            LOG.debug("Bucket verification: {}", e.getMessage());
        }
    }

    public void uploadFile(String key, Path filePath) {
        if (s3Client == null)
            return;
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(), filePath);
    }

    public String generateDownloadUrl(String key) {
        if (presigner == null)
            return null;

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(b -> b.bucket(bucketName).key(key))
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }
}
