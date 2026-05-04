package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.CruisePriceDto;
import com.nakshi.rohitour.repository.admin.AdminCruisePriceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminCruisePriceService {

    private final AdminCruisePriceMapper priceMapper;

    public AdminCruisePriceService(AdminCruisePriceMapper priceMapper) {
        this.priceMapper = priceMapper;
    }

    public List<CruisePriceDto> getPrices(Long productId) {
        return priceMapper.findByProductId(productId);
    }

    public CruisePriceDto addPrice(Long productId, CruisePriceDto dto) {
        dto.setProductId(productId);
        dto.setSortOrder(priceMapper.findByProductId(productId).size());
        priceMapper.insert(dto);
        return dto;
    }

    public CruisePriceDto updatePrice(Long priceId, CruisePriceDto dto) {
        CruisePriceDto existing = priceMapper.findById(priceId);
        existing.setCabinType(dto.getCabinType());
        existing.setPriceAdult(dto.getPriceAdult());
        existing.setPriceChild(dto.getPriceChild());
        existing.setPriceInfant(dto.getPriceInfant());
        existing.setSortOrder(dto.getSortOrder());
        priceMapper.update(existing);
        return existing;
    }

    public void deletePrice(Long priceId) {
        priceMapper.delete(priceId);
    }

    /* 전체 교체 (기존 삭제 후 새 목록 INSERT) */
    @Transactional
    public List<CruisePriceDto> replaceAll(Long productId, List<CruisePriceDto> prices) {
        priceMapper.deleteByProductId(productId);
        for (int i = 0; i < prices.size(); i++) {
            CruisePriceDto dto = prices.get(i);
            dto.setProductId(productId);
            dto.setSortOrder(i);
            priceMapper.insert(dto);
        }
        return priceMapper.findByProductId(productId);
    }
}
