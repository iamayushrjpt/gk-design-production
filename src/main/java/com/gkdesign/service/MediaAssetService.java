package com.gkdesign.service;

import com.gkdesign.exception.ResourceNotFoundException;
import com.gkdesign.model.MediaAsset;
import com.gkdesign.repository.MediaAssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaAssetService {

    private final MediaAssetRepository repository;
    private final MediaStorageService storageService;

    @Value("${app.r2.public-url}")
    private String publicUrl;

    @Value("${app.r2.bucket}")
    private String bucket;

    public Page<MediaAsset> search(String query, Pageable pageable) {
        String q = query == null ? "" : query;
        Page<MediaAsset> page = repository.findByFilenameContainingIgnoreCaseOrFolderContainingIgnoreCase(q, q, pageable);
        page.forEach(this::withCurrentUrl);
        return page;
    }

    /** Real file upload — streams the file to Cloudflare R2, then saves the resulting URL as metadata. */
    public MediaAsset upload(MultipartFile file, String folder, String uploadedBy) throws Exception {
        MediaStorageService.UploadResult result = storageService.upload(file, folder);

        MediaAsset asset = MediaAsset.builder()
                .url(result.url())
                .filename(result.filename())
                .folder(folder == null || folder.isBlank() ? "general" : folder)
                .sizeBytes(file.getSize())
                .objectKey(result.objectKey())
                .uploadedBy(uploadedBy)
                .build();

        return repository.save(asset);
    }

    /** Adds an asset by external URL (no Cloudflare R2 object — nothing to clean up on delete beyond the metadata record). */
    public MediaAsset saveMetadata(MediaAsset asset, String uploadedBy) {
        asset.setUploadedBy(uploadedBy);
        return repository.save(asset);
    }

    public void delete(String id) {
        MediaAsset asset = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media asset not found: " + id));

        if (asset.getObjectKey() != null) {
            try {
                storageService.delete(asset.getObjectKey());
            } catch (Exception e) {
                // Metadata delete should still succeed even if the storage
                // delete fails (e.g. Cloudflare R2 temporarily unreachable) — log it
                // rather than blocking the admin action.
                log.warn("Could not delete Cloudflare R2 object '{}': {}", asset.getObjectKey(), e.getMessage());
            }
        }

        repository.deleteById(id);
    }

    /**
     * Rebuilds `url` from the CURRENT `R2_PUBLIC_URL` config for any asset
     * that was actually uploaded to Cloudflare R2 (has an `objectKey`). This means
     * changing `R2_PUBLIC_URL` (e.g. moving from localhost to a real
     * domain, or fixing a wrong value) immediately fixes every existing
     * thumbnail on next read — no data migration, no re-uploading needed.
     * Assets added by external URL (no objectKey) are left untouched, since
     * there's no Cloudflare R2 object to rebuild a URL from.
     */
    private void withCurrentUrl(MediaAsset asset) {
        if (asset.getObjectKey() != null && !asset.getObjectKey().isBlank()) {
            asset.setUrl("%s/%s".formatted(publicUrl.replaceAll("/$", ""), asset.getObjectKey()));
        }
    }
}
