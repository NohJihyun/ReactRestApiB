package com.nakshi.rohitour.service.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AdminProductVideoService {

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public String uploadVideo(Long productId, MultipartFile file) throws IOException {
        String ext = getExtension(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "." + ext;
        String relativePath = "/uploads/products/" + productId + "/video/" + fileName;

        Path fullPath = Paths.get(uploadBaseDir, "products", String.valueOf(productId), "video", fileName);
        Files.createDirectories(fullPath.getParent());
        file.transferTo(fullPath);

        return relativePath;
    }

    public void deleteVideo(String relativePath) throws IOException {
        String localPath = relativePath.replace("/uploads/", "");
        Path fullPath = Paths.get(uploadBaseDir, localPath);
        Files.deleteIfExists(fullPath);
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) return "mp4";
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    }
}
