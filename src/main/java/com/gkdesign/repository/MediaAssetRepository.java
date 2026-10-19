package com.gkdesign.repository;

import com.gkdesign.model.MediaAsset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediaAssetRepository extends MongoRepository<MediaAsset, String> {
    Page<MediaAsset> findByFilenameContainingIgnoreCaseOrFolderContainingIgnoreCase(
            String filename, String folder, Pageable pageable);
}
