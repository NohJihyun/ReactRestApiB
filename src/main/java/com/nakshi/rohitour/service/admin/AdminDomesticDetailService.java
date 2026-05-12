package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.DomesticDetailDto;
import com.nakshi.rohitour.repository.admin.AdminDomesticDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminDomesticDetailService {

    private final AdminDomesticDetailMapper detailMapper;

    public AdminDomesticDetailService(AdminDomesticDetailMapper detailMapper) {
        this.detailMapper = detailMapper;
    }

    public DomesticDetailDto getDetail(Long productId) {
        return detailMapper.findByProductId(productId);
    }

    @Transactional
    public DomesticDetailDto save(Long productId, DomesticDetailDto dto) {
        dto.setProductId(productId);
        DomesticDetailDto existing = detailMapper.findByProductId(productId);
        if (existing == null) {
            detailMapper.insert(dto);
        } else {
            detailMapper.update(dto);
        }
        return detailMapper.findByProductId(productId);
    }
}
