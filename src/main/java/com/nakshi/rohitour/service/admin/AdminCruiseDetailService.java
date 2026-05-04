package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.CruiseDetailDto;
import com.nakshi.rohitour.repository.admin.AdminCruiseDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminCruiseDetailService {

    private final AdminCruiseDetailMapper detailMapper;

    public AdminCruiseDetailService(AdminCruiseDetailMapper detailMapper) {
        this.detailMapper = detailMapper;
    }

    public CruiseDetailDto getDetail(Long productId) {
        return detailMapper.findByProductId(productId);
    }

    /* 없으면 INSERT, 있으면 UPDATE (upsert) */
    @Transactional
    public CruiseDetailDto save(Long productId, CruiseDetailDto dto) {
        dto.setProductId(productId);
        CruiseDetailDto existing = detailMapper.findByProductId(productId);
        if (existing == null) {
            detailMapper.insert(dto);
        } else {
            detailMapper.update(dto);
        }
        return detailMapper.findByProductId(productId);
    }
}
