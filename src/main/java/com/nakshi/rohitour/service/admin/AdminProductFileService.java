package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.ProductFileDto;
import com.nakshi.rohitour.repository.admin.AdminProductFileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AdminProductFileService {

    private final AdminProductFileMapper fileMapper;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public AdminProductFileService(AdminProductFileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    /* 첨부파일 목록 조회 */
    public List<ProductFileDto> getFiles(Long productId) {
        return fileMapper.findByProductId(productId);
    }

    /* 첨부파일 업로드 */
    public ProductFileDto uploadFile(Long productId, MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String ext = getExtension(originalName);
        String fileType = detectFileType(ext);
        String uuid = UUID.randomUUID().toString();
        String storedName = uuid + "." + ext;
        String relativePath = "/uploads/products/" + productId + "/files/" + storedName;

        Path fullPath = Paths.get(uploadBaseDir, "products", String.valueOf(productId), "files", storedName);
        Files.createDirectories(fullPath.getParent());
        file.transferTo(fullPath);

        int sortOrder = fileMapper.countByProductId(productId);

        ProductFileDto dto = new ProductFileDto();
        dto.setProductId(productId);
        dto.setFilePath(relativePath);
        dto.setFileName(originalName);
        dto.setFileType(fileType);
        dto.setFileSize(file.getSize());
        dto.setSortOrder(sortOrder);
        fileMapper.insert(dto);

        return dto;
    }

    /* 첨부파일 삭제 */
    public void deleteFile(Long fileId) throws IOException {
        ProductFileDto dto = fileMapper.findById(fileId);
        if (dto != null) {
            deletePhysicalFile(dto.getFilePath());
            fileMapper.delete(fileId);
        }
    }

    /* 디스크 파일 삭제 */
    private void deletePhysicalFile(String relativePath) throws IOException {
        String localPath = relativePath.replace("/uploads/", "");
        Path fullPath = Paths.get(uploadBaseDir, localPath);
        Files.deleteIfExists(fullPath);
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) return "bin";
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    }

    private String detectFileType(String ext) {
        return switch (ext) {
            case "pdf"              -> "PDF";
            case "xlsx", "xls"     -> "EXCEL";
            case "docx", "doc"     -> "WORD";
            case "jpg", "jpeg",
                 "png", "gif","webp"-> "IMAGE";
            default                 -> "ETC";
        };
    }
}
