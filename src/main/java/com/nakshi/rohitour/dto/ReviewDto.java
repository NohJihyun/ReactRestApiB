package com.nakshi.rohitour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ReviewDto {
    private Long id;
    private Long productId;
    private String productName;
    private Long userId;
    private String userName;
    private String writerType;   // GENERAL / STUDENT / TEACHER
    private int rating;
    private String content;
    private String status;       // PUBLISHED / HIDDEN
    private String rootCategoryName;
    private List<ReviewImageDto> images;
    private List<ReviewCommentDto> comments;
    private int commentCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
