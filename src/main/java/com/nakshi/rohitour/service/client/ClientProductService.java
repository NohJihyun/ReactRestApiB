package com.nakshi.rohitour.service.client;

import com.nakshi.rohitour.dto.*;
import com.nakshi.rohitour.repository.admin.AdminAirDetailMapper;
import com.nakshi.rohitour.repository.admin.AdminCruiseDetailMapper;
import com.nakshi.rohitour.repository.admin.AdminCruisePriceMapper;
import com.nakshi.rohitour.repository.admin.AdminDomesticDetailMapper;
import com.nakshi.rohitour.repository.admin.AdminSchoolTripDetailMapper;
import com.nakshi.rohitour.repository.client.ClientProductMapper;
import com.nakshi.rohitour.service.admin.AdminAirItineraryService;
import com.nakshi.rohitour.service.admin.AdminCruiseItineraryService;
import com.nakshi.rohitour.service.admin.AdminDomesticItineraryService;
import com.nakshi.rohitour.service.admin.AdminSchoolTripItineraryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientProductService {

    private final ClientProductMapper mapper;
    private final AdminCruiseItineraryService cruiseItineraryService;
    private final AdminCruiseDetailMapper cruiseDetailMapper;
    private final AdminCruisePriceMapper cruisePriceMapper;
    private final AdminAirItineraryService airItineraryService;
    private final AdminAirDetailMapper airDetailMapper;
    private final AdminDomesticItineraryService domesticItineraryService;
    private final AdminDomesticDetailMapper domesticDetailMapper;
    private final AdminSchoolTripItineraryService schoolTripItineraryService;
    private final AdminSchoolTripDetailMapper schoolTripDetailMapper;

    public ClientProductService(
            ClientProductMapper mapper,
            AdminCruiseItineraryService cruiseItineraryService,
            AdminCruiseDetailMapper cruiseDetailMapper,
            AdminCruisePriceMapper cruisePriceMapper,
            AdminAirItineraryService airItineraryService,
            AdminAirDetailMapper airDetailMapper,
            AdminDomesticItineraryService domesticItineraryService,
            AdminDomesticDetailMapper domesticDetailMapper,
            AdminSchoolTripItineraryService schoolTripItineraryService,
            AdminSchoolTripDetailMapper schoolTripDetailMapper
    ) {
        this.mapper = mapper;
        this.cruiseItineraryService = cruiseItineraryService;
        this.cruiseDetailMapper = cruiseDetailMapper;
        this.cruisePriceMapper = cruisePriceMapper;
        this.airItineraryService = airItineraryService;
        this.airDetailMapper = airDetailMapper;
        this.domesticItineraryService = domesticItineraryService;
        this.domesticDetailMapper = domesticDetailMapper;
        this.schoolTripItineraryService = schoolTripItineraryService;
        this.schoolTripDetailMapper = schoolTripDetailMapper;
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

    public List<AirItineraryDto> getAirItineraries(Long productId) {
        return airItineraryService.getItineraries(productId);
    }

    public AirDetailDto getAirDetail(Long productId) {
        return airDetailMapper.findByProductId(productId);
    }

    public List<DomesticItineraryDto> getDomesticItineraries(Long productId) {
        return domesticItineraryService.getItineraries(productId);
    }

    public DomesticDetailDto getDomesticDetail(Long productId) {
        return domesticDetailMapper.findByProductId(productId);
    }

    public List<SchoolTripItineraryDto> getSchoolTripItineraries(Long productId) {
        return schoolTripItineraryService.getItineraries(productId);
    }

    public SchoolTripDetailDto getSchoolTripDetail(Long productId) {
        return schoolTripDetailMapper.findByProductId(productId);
    }

    public List<ProductDto> search(String keyword, int limit) {
        return mapper.search(keyword, limit);
    }
}
