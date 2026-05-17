package com.nakshi.rohitour.service.inquiry;

import com.nakshi.rohitour.common.paging.PageRequest;
import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.dto.InquiryDto;
import com.nakshi.rohitour.dto.InquiryRequestDto;
import com.nakshi.rohitour.dto.InquirySearchDto;
import com.nakshi.rohitour.repository.inquiry.InquiryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryMapper inquiryMapper;

    @Transactional
    public void createInquiry(InquiryRequestDto req, Long userId) {
        inquiryMapper.insert(req, userId);
    }

    public List<InquiryDto> getMyInquiries(Long userId) {
        return inquiryMapper.findByUserId(userId);
    }

    public InquiryDto getInquiry(Long inquiryId, Long userId, boolean isAdmin) {
        InquiryDto inquiry = inquiryMapper.findById(inquiryId);
        if (inquiry == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (!isAdmin && !inquiry.getUserId().equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return inquiry;
    }

    public PageResponse<InquiryDto> getInquiries(InquirySearchDto search) {
        List<InquiryDto> list = inquiryMapper.findAll(search);
        int total = inquiryMapper.countAll(search);
        return new PageResponse<>(list, total, new PageRequest(search.getPage(), search.getSize()));
    }

    @Transactional
    public void updateAnswer(Long inquiryId, String answer) {
        InquiryDto inquiry = inquiryMapper.findById(inquiryId);
        if (inquiry == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        inquiryMapper.updateAnswer(inquiryId, answer);
    }

    @Transactional
    public void updateStatus(Long inquiryId, String status) {
        inquiryMapper.updateStatus(inquiryId, status);
    }

    @Transactional
    public void deleteInquiry(Long inquiryId) {
        inquiryMapper.delete(inquiryId);
    }

    public Map<String, Integer> getNewCount() {
        return Map.of("count", inquiryMapper.countNew());
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("total", inquiryMapper.countTotal());
        stats.put("newCount", inquiryMapper.countNew());
        stats.put("completedCount", inquiryMapper.countCompleted());
        stats.put("categoryStats", inquiryMapper.countByCategory());
        return stats;
    }
}
