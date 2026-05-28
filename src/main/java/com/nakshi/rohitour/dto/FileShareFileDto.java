package com.nakshi.rohitour.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FileShareFileDto {
    private Long id;
    private Long folderId;
    private Long groupId;
    private Integer version;
    private String displayName;
    private String originalName;
    private String filePath;
    private Long fileSize;
    private String uploadedBy;
    private LocalDateTime uploadedAt;
    private List<FileShareFileDto> versions;
}
