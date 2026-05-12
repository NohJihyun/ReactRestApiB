package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.AirDetailDto;
import com.nakshi.rohitour.repository.admin.AdminAirDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminAirDetailService {

    private final AdminAirDetailMapper detailMapper;

    public AdminAirDetailService(AdminAirDetailMapper detailMapper) {
        this.detailMapper = detailMapper;
    }

    public AirDetailDto getDetail(Long productId) {
        return detailMapper.findByProductId(productId);
    }

    @Transactional
    public AirDetailDto save(Long productId, AirDetailDto dto) {
        dto.setProductId(productId);
        AirDetailDto existing = detailMapper.findByProductId(productId);
        if (existing == null) {
            detailMapper.insert(dto);
        } else {
            detailMapper.update(dto);
        }
        return detailMapper.findByProductId(productId);
    }
}
