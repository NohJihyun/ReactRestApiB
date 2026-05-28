package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.FileShareFileDto;
import com.nakshi.rohitour.dto.FileShareFolderDto;
import com.nakshi.rohitour.repository.admin.AdminFileShareMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminFileShareService {

    private final AdminFileShareMapper adminFileShareMapper;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    // ===== FOLDER =====

    public List<FileShareFolderDto> getFolderTree() {
        List<FileShareFolderDto> all = adminFileShareMapper.findAllFolders();
        return buildTree(all);
    }

    private List<FileShareFolderDto> buildTree(List<FileShareFolderDto> all) {
        Map<Long, FileShareFolderDto> map = new HashMap<>();
        List<FileShareFolderDto> roots = new ArrayList<>();
        for (FileShareFolderDto f : all) {
            f.setChildren(new ArrayList<>());
            map.put(f.getId(), f);
        }
        for (FileShareFolderDto f : all) {
            if (f.getParentId() == null) {
                roots.add(f);
            } else {
                FileShareFolderDto parent = map.get(f.getParentId());
                if (parent != null) parent.getChildren().add(f);
            }
        }
        return roots;
    }

    public FileShareFolderDto createFolder(String name, Long parentId, String createdBy) {
        FileShareFolderDto dto = new FileShareFolderDto();
        dto.setName(name);
        dto.setParentId(parentId);
        dto.setCreatedBy(createdBy);
        adminFileShareMapper.insertFolder(dto);
        return dto;
    }

    public void renameFolder(Long id, String name) {
        adminFileShareMapper.updateFolderName(id, name);
    }

    public void deleteFolder(Long id) {
        List<FileShareFileDto> files = adminFileShareMapper.findLatestFilesByFolderId(id);
        for (FileShareFileDto f : files) {
            List<FileShareFileDto> versions = adminFileShareMapper.findVersionsByGroupId(f.getGroupId());
            for (FileShareFileDto v : versions) {
                deleteFromDisk(v.getFilePath());
            }
        }
        adminFileShareMapper.deleteFolder(id);
    }

    // ===== FILE =====

    public List<FileShareFileDto> getFiles(Long folderId) {
        return adminFileShareMapper.findLatestFilesByFolderId(folderId);
    }

    public List<FileShareFileDto> getVersions(Long groupId) {
        return adminFileShareMapper.findVersionsByGroupId(groupId);
    }

    public FileShareFileDto uploadFile(Long folderId, MultipartFile file, String uploadedBy) throws IOException {
        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";

        FileShareFileDto dto = new FileShareFileDto();
        dto.setFolderId(folderId);
        dto.setGroupId(0L);
        dto.setVersion(1);
        dto.setDisplayName(originalName);
        dto.setOriginalName(originalName);
        dto.setFileSize(file.getSize());
        dto.setUploadedBy(uploadedBy);
        dto.setFilePath("");
        adminFileShareMapper.insertFile(dto);

        Long id = dto.getId();
        String filePath = saveToDisk(file, id, 1, originalName);

        adminFileShareMapper.updateGroupId(id, id);
        adminFileShareMapper.updateFilePath(id, filePath);
        dto.setGroupId(id);
        dto.setFilePath(filePath);
        return dto;
    }

    public FileShareFileDto uploadVersion(Long groupId, Long folderId, MultipartFile file, String uploadedBy) throws IOException {
        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        int nextVersion = adminFileShareMapper.findNextVersion(groupId);

        List<FileShareFileDto> existing = adminFileShareMapper.findVersionsByGroupId(groupId);
        String displayName = existing.isEmpty() ? originalName : existing.get(0).getDisplayName();

        FileShareFileDto dto = new FileShareFileDto();
        dto.setFolderId(folderId);
        dto.setGroupId(groupId);
        dto.setVersion(nextVersion);
        dto.setDisplayName(displayName);
        dto.setOriginalName(originalName);
        dto.setFileSize(file.getSize());
        dto.setUploadedBy(uploadedBy);
        dto.setFilePath("");
        adminFileShareMapper.insertFile(dto);

        String filePath = saveToDisk(file, groupId, nextVersion, originalName);
        adminFileShareMapper.updateFilePath(dto.getId(), filePath);
        dto.setFilePath(filePath);
        return dto;
    }

    public FileShareFileDto getFileById(Long id) {
        return adminFileShareMapper.findFileById(id);
    }

    public void deleteFileVersion(Long id) {
        FileShareFileDto file = adminFileShareMapper.findFileById(id);
        if (file != null) {
            deleteFromDisk(file.getFilePath());
            adminFileShareMapper.deleteFile(id);
        }
    }

    private String saveToDisk(MultipartFile file, Long groupId, int version, String originalName) throws IOException {
        Path dir = Paths.get(uploadBaseDir + "/file-share/" + groupId + "/");
        Files.createDirectories(dir);
        String fileName = "v" + version + "_" + originalName;
        file.transferTo(dir.resolve(fileName).toFile());
        return "/uploads/file-share/" + groupId + "/" + fileName;
    }

    private void deleteFromDisk(String filePath) {
        if (filePath == null || filePath.isBlank()) return;
        try {
            String diskPath = uploadBaseDir + filePath.substring("/uploads".length());
            Files.deleteIfExists(Paths.get(diskPath));
        } catch (IOException ignored) {}
    }
}
