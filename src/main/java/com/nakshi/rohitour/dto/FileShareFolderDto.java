package com.nakshi.rohitour.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FileShareFolderDto {
    private Long id;
    private String name;
    private Long parentId;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<FileShareFolderDto> children;
}
