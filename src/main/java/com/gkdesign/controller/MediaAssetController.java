package com.gkdesign.controller;

import com.gkdesign.dto.response.ApiResponse;
import com.gkdesign.dto.response.PagedResponse;
import com.gkdesign.model.MediaAsset;
import com.gkdesign.service.MediaAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/media")
@RequiredArgsConstructor
@Slf4j
public class MediaAssetController {

    private final MediaAssetService mediaAssetService;

    @GetMapping
    public ApiResponse<PagedResponse<MediaAsset>> search(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "40") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ApiResponse.ok(PagedResponse.from(mediaAssetService.search(query, pageable)));
    }

    @PostMapping
    public ApiResponse<MediaAsset> saveMetadata(@RequestBody MediaAsset asset, Authentication auth) {
        return ApiResponse.ok(mediaAssetService.saveMetadata(asset, auth.getName()), "Media saved.");
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ApiResponse<MediaAsset> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder,
            Authentication auth
    ) throws Exception {
        log.info("Upload API reached");

        log.info(
                "Upload request: filename={}, size={}, contentType={}",
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType()
        );
        if (file.isEmpty()) {
            throw new IllegalArgumentException("No file was received.");
        }
        return ApiResponse.ok(mediaAssetService.upload(file, folder, auth.getName()), "Image uploaded.");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        mediaAssetService.delete(id);
        return ApiResponse.ok(null, "Media deleted.");
    }
}
