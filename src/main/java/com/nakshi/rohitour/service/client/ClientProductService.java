package com.nakshi.rohitour.service.client;

import com.nakshi.rohitour.dto.*;
import com.nakshi.rohitour.repository.admin.AdminCruiseDetailMapper;
import com.nakshi.rohitour.repository.admin.AdminCruisePriceMapper;
import com.nakshi.rohitour.repository.client.ClientProductMapper;
import com.nakshi.rohitour.service.admin.AdminCruiseItineraryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientProductService {

    private final ClientProductMapper mapper;
    private final AdminCruiseItineraryService cruiseItineraryService;
    private final AdminCruiseDetailMapper cruiseDetailMapper;
    private final AdminCruisePriceMapper cruisePriceMapper;

    public ClientProductService(
            ClientProductMapper mapper,
            AdminCruiseItineraryService cruiseItineraryService,
            AdminCruiseDetailMapper cruiseDetailMapper,
            AdminCruisePriceMapper cruisePriceMapper
    ) {
        this.mapper = mapper;
        this.cruiseItineraryService = cruiseItineraryService;
        this.cruiseDetailMapper = cruiseDetailMapper;
        this.cruisePriceMapper = cruisePriceMapper;
    }

    public List<ProductDto> getByCategory(String categoryName) {
        return mapper.findPublishedByRootCategory(categoryName);
    }

    public ProductDto getById(Long productId) {
        return mapper.findById(productId);
    }

    public List<ProductImageDto> getImages(Long productId) {
        return mapper.findImagesByProductId(productId);
    }

    public List<ProductFileDto> getFiles(Long productId) {
        return mapper.findFilesByProductId(productId);
    }

    public List<CruiseItineraryDto> getCruiseItineraries(Long productId) {
        return cruiseItineraryService.getItineraries(productId);
    }

    public CruiseDetailDto getCruiseDetail(Long productId) {
        return cruiseDetailMapper.findByProductId(productId);
    }

    public List<CruisePriceDto> getCruisePrices(Long productId) {
        return cruisePriceMapper.findByProductId(productId);
    }
}
