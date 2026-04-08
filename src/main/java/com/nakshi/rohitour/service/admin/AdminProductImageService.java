package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.ProductImageDto;
import com.nakshi.rohitour.repository.admin.AdminProductImageMapper;
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
public class AdminProductImageService {

    private final AdminProductImageMapper imageMapper;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public AdminProductImageService(AdminProductImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    /* 이미지 목록 조회 */
    public List<ProductImageDto> getImages(Long productId) {
        return imageMapper.findByProductId(productId);
    }

    /* 이미지 업로드 */
    public ProductImageDto uploadImage(Long productId, MultipartFile file, String imageType) throws IOException {
        // 썸네일은 1장만 허용 — 기존 썸네일 교체
        if ("THUMBNAIL".equals(imageType)) {
            ProductImageDto existing = imageMapper.findThumbnail(productId);
            if (existing != null) {
                deletePhysicalFile(existing.getImagePath());
                imageMapper.delete(existing.getId());
            }
        }

        // 파일 저장
        String relativePath = saveFile(productId, file, imageType);

        // sortOrder 결정
        int sortOrder = "THUMBNAIL".equals(imageType) ? 0 : imageMapper.countDetail(productId);

        // DB 저장
        ProductImageDto dto = new ProductImageDto();
        dto.setProductId(productId);
        dto.setImagePath(relativePath);
        dto.setImageType(imageType);
        dto.setSortOrder(sortOrder);
        imageMapper.insert(dto);

        return dto;
    }

    /* 이미지 삭제 */
    public void deleteImage(Long imageId) throws IOException {
        // imagePath를 직접 조회하기 위해 findByProductId 대신 별도 처리
        // 여기선 컨트롤러에서 productId로 목록을 가져와 찾거나, 간단히 경로만 넘겨받는다
        // → 컨트롤러에서 productId의 이미지 목록에서 찾아 경로 넘겨줌
        imageMapper.delete(imageId);
    }

    /* 이미지 삭제 (경로 포함) */
    public void deleteImage(Long imageId, String imagePath) throws IOException {
        deletePhysicalFile(imagePath);
        imageMapper.delete(imageId);
    }

    /* 이미지 순서 변경 */
    public void updateOrder(List<java.util.Map<String, Object>> items) {
        for (java.util.Map<String, Object> item : items) {
            Long id = Long.valueOf(item.get("id").toString());
            int sortOrder = Integer.parseInt(item.get("sortOrder").toString());
            imageMapper.updateOrder(id, sortOrder);
        }
    }

    /* 파일 저장 (디스크) */
    private String saveFile(Long productId, MultipartFile file, String imageType) throws IOException {
        String subDir = "THUMBNAIL".equals(imageType) ? "thumbnail" : "detail";
        String ext = getExtension(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "." + ext;
        String relativePath = "/uploads/products/" + productId + "/" + subDir + "/" + fileName;

        Path fullPath = Paths.get(uploadBaseDir, "products", String.valueOf(productId), subDir, fileName);
        Files.createDirectories(fullPath.getParent());
        file.transferTo(fullPath);

        return relativePath;
    }

    /* 디스크 파일 삭제 */
    private void deletePhysicalFile(String relativePath) throws IOException {
        // /uploads/products/... → uploadBaseDir/products/...
        String localPath = relativePath.replace("/uploads/", "");
        Path fullPath = Paths.get(uploadBaseDir, localPath);
        Files.deleteIfExists(fullPath);
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) return "jpg";
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    }
}
