package com.nakshi.rohitour.repository.inquiry;

import com.nakshi.rohitour.dto.InquiryDto;
import com.nakshi.rohitour.dto.InquiryRequestDto;
import com.nakshi.rohitour.dto.InquirySearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquiryMapper {
    void insert(@Param("req") InquiryRequestDto req, @Param("userId") Long userId);
    List<InquiryDto> findAll(InquirySearchDto search);
    int countAll(InquirySearchDto search);
    InquiryDto findById(Long inquiryId);
    List<InquiryDto> findByUserId(Long userId);
    void updateAnswer(@Param("inquiryId") Long inquiryId, @Param("answer") String answer);
    void updateStatus(@Param("inquiryId") Long inquiryId, @Param("status") String status);
    void delete(Long inquiryId);
    int countNew();
    int countTotal();
    int countCompleted();
    List<Map<String, Object>> countByCategory();
}
