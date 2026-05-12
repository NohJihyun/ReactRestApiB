package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.SchoolTripDetailDto;
import com.nakshi.rohitour.repository.admin.AdminSchoolTripDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminSchoolTripDetailService {

    private final AdminSchoolTripDetailMapper detailMapper;

    public AdminSchoolTripDetailService(AdminSchoolTripDetailMapper detailMapper) {
        this.detailMapper = detailMapper;
    }

    public SchoolTripDetailDto getDetail(Long productId) {
        return detailMapper.findByProductId(productId);
    }

    @Transactional
    public SchoolTripDetailDto save(Long productId, SchoolTripDetailDto dto) {
        dto.setProductId(productId);
        SchoolTripDetailDto existing = detailMapper.findByProductId(productId);
        if (existing == null) {
            detailMapper.insert(dto);
        } else {
            detailMapper.update(dto);
        }
        return detailMapper.findByProductId(productId);
    }
}
