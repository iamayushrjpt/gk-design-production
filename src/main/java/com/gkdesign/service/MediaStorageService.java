package com.gkdesign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaStorageService {

    private final S3Client r2Client;

    @Value("${app.r2.bucket}")
    private String bucket;

    @Value("${app.r2.public-url}")
    private String publicUrl;

    /**
     * Uploads a file to Cloudflare R2 under a date-partitioned,
     * collision-resistant object key and returns its public URL.
     */
    public UploadResult upload(MultipartFile file, String folder)
            throws Exception {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        String datePath = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String safeFolder = (folder == null || folder.isBlank())
                ? "general"
                : folder.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9-]", "-");

        String originalName = sanitizeFilename(
                file.getOriginalFilename()
        );

        String objectKey = "media/%s/%s/%s-%s".formatted(
                safeFolder,
                datePath,
                UUID.randomUUID().toString().substring(0, 8),
                originalName
        );

        String contentType = file.getContentType() != null
                ? file.getContentType()
                : "application/octet-stream";

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(contentType)
                .contentLength(file.getSize())
                .build();

        // Read the file once into memory. The byte array can be
        // safely read again by the AWS SDK if the request is retried.
        byte[] fileBytes = file.getBytes();

        r2Client.putObject(
                request,
                RequestBody.fromBytes(fileBytes)
        );

        String url = "%s/%s".formatted(
                publicUrl.replaceAll("/+$", ""),
                objectKey
        );

        return new UploadResult(url, originalName, objectKey);
    }

    public void delete(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Object key cannot be empty."
            );
        }

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        r2Client.deleteObject(request);
    }

    private String sanitizeFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            return "upload";
        }

        return filename.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9.-]", "-");
    }

    public record UploadResult(
            String url,
            String filename,
            String objectKey
    ) {}
}
