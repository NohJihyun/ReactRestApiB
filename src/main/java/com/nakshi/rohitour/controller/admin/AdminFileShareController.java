package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.dto.FileShareFileDto;
import com.nakshi.rohitour.dto.FileShareFolderDto;
import com.nakshi.rohitour.service.admin.AdminFileShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/file-share")
@RequiredArgsConstructor
public class AdminFileShareController {

    private final AdminFileShareService adminFileShareService;

    @Value("${app.file-share-admins:}")
    private String fileShareAdminsRaw;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    private void checkAccess(Principal principal) {
        if (fileShareAdminsRaw == null || fileShareAdminsRaw.isBlank()) return;
        Set<String> allowed = Set.copyOf(Arrays.asList(fileShareAdminsRaw.split(",")));
        if (principal == null || !allowed.contains(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "파일 공유 접근 권한이 없습니다.");
        }
    }

    // ===== FOLDER =====

    @GetMapping("/folders")
    public List<FileShareFolderDto> getFolders(Principal principal) {
        checkAccess(principal);
        return adminFileShareService.getFolderTree();
    }

    @PostMapping("/folders")
    public FileShareFolderDto createFolder(
            @RequestParam String name,
            @RequestParam(required = false) Long parentId,
            Principal principal) {
        checkAccess(principal);
        return adminFileShareService.createFolder(name, parentId, principal.getName());
    }

    @PatchMapping("/folders/{id}/name")
    public void renameFolder(@PathVariable Long id, @RequestParam String name, Principal principal) {
        checkAccess(principal);
        adminFileShareService.renameFolder(id, name);
    }

    @DeleteMapping("/folders/{id}")
    public void deleteFolder(@PathVariable Long id, Principal principal) {
        checkAccess(principal);
        adminFileShareService.deleteFolder(id);
    }

    // ===== FILE =====

    @GetMapping("/folders/{folderId}/files")
    public List<FileShareFileDto> getFiles(@PathVariable Long folderId, Principal principal) {
        checkAccess(principal);
        return adminFileShareService.getFiles(folderId);
    }

    @GetMapping("/files/{groupId}/versions")
    public List<FileShareFileDto> getVersions(@PathVariable Long groupId, Principal principal) {
        checkAccess(principal);
        return adminFileShareService.getVersions(groupId);
    }

    @PostMapping("/folders/{folderId}/files")
    public FileShareFileDto uploadFile(
            @PathVariable Long folderId,
            @RequestParam MultipartFile file,
            Principal principal) throws Exception {
        checkAccess(principal);
        return adminFileShareService.uploadFile(folderId, file, principal.getName());
    }

    @PostMapping("/files/{groupId}/versions")
    public FileShareFileDto uploadVersion(
            @PathVariable Long groupId,
            @RequestParam Long folderId,
            @RequestParam MultipartFile file,
            Principal principal) throws Exception {
        checkAccess(principal);
        return adminFileShareService.uploadVersion(groupId, folderId, file, principal.getName());
    }

    @GetMapping("/files/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id, Principal principal) {
        checkAccess(principal);
        FileShareFileDto fileDto = adminFileShareService.getFileById(id);
        if (fileDto == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        String diskPath = uploadBaseDir + fileDto.getFilePath().substring("/uploads".length());
        Path path = Paths.get(diskPath);
        Resource resource = new FileSystemResource(path);
        if (!resource.exists()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");

        String encodedName = URLEncoder.encode(fileDto.getOriginalName(), StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/files/{id}")
    public void deleteFile(@PathVariable Long id, Principal principal) {
        checkAccess(principal);
        adminFileShareService.deleteFileVersion(id);
    }

    @GetMapping("/check")
    public Map<String, Boolean> checkAccess(Principal principal, @RequestParam(required = false) String dummy) {
        try {
            checkAccess(principal);
            return Map.of("authorized", true);
        } catch (Exception e) {
            return Map.of("authorized", false);
        }
    }
}
