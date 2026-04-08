package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.dto.ProductFileDto;
import com.nakshi.rohitour.service.admin.AdminProductFileService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/products/{productId}/files")
public class AdminProductFileController {

    private final AdminProductFileService fileService;

    public AdminProductFileController(AdminProductFileService fileService) {
        this.fileService = fileService;
    }

    /* 첨부파일 목록 */
    @GetMapping
    public List<ProductFileDto> getFiles(@PathVariable Long productId) {
        return fileService.getFiles(productId);
    }

    /* 첨부파일 업로드 */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductFileDto upload(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return fileService.uploadFile(productId, file);
    }

    /* 첨부파일 삭제 */
    @DeleteMapping("/{fileId}")
    public void delete(
            @PathVariable Long productId,
            @PathVariable Long fileId
    ) throws IOException {
        fileService.deleteFile(fileId);
    }
}
