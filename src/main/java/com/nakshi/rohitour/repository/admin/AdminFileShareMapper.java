package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.FileShareFileDto;
import com.nakshi.rohitour.dto.FileShareFolderDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminFileShareMapper {

    // Folder
    List<FileShareFolderDto> findAllFolders();
    void insertFolder(FileShareFolderDto dto);
    void updateFolderName(@Param("id") Long id, @Param("name") String name);
    void deleteFolder(@Param("id") Long id);

    // File
    List<FileShareFileDto> findLatestFilesByFolderId(@Param("folderId") Long folderId);
    List<FileShareFileDto> findVersionsByGroupId(@Param("groupId") Long groupId);
    FileShareFileDto findFileById(@Param("id") Long id);
    void insertFile(FileShareFileDto dto);
    void updateGroupId(@Param("id") Long id, @Param("groupId") Long groupId);
    void updateFilePath(@Param("id") Long id, @Param("filePath") String filePath);
    int findNextVersion(@Param("groupId") Long groupId);
    void deleteFile(@Param("id") Long id);
}
