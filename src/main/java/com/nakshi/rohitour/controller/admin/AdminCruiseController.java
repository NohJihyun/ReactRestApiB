package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.dto.*;
import com.nakshi.rohitour.service.admin.AdminCruiseDetailService;
import com.nakshi.rohitour.service.admin.AdminCruiseItineraryService;
import com.nakshi.rohitour.service.admin.AdminCruisePriceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/products/{productId}")
public class AdminCruiseController {

    private final AdminCruiseItineraryService itineraryService;
    private final AdminCruiseDetailService    detailService;
    private final AdminCruisePriceService     priceService;

    public AdminCruiseController(
            AdminCruiseItineraryService itineraryService,
            AdminCruiseDetailService detailService,
            AdminCruisePriceService priceService
    ) {
        this.itineraryService = itineraryService;
        this.detailService    = detailService;
        this.priceService     = priceService;
    }

    /* ── 일정 CRUD ── */

    @GetMapping("/cruise-itineraries")
    public List<CruiseItineraryDto> getItineraries(@PathVariable Long productId) {
        return itineraryService.getItineraries(productId);
    }

    @PostMapping("/cruise-itineraries")
    public CruiseItineraryDto createItinerary(
            @PathVariable Long productId,
            @RequestParam int dayNumber,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String shoppingCenterName,
            @RequestParam(required = false) String shoppingExchangeInfo,
            @RequestParam(required = false) String shoppingInfo,
            @RequestParam(required = false) String hotelName,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.create(
                productId, dayNumber, title, description,
                shoppingCenterName, shoppingExchangeInfo, shoppingInfo,
                hotelName, sortOrder
        );
    }

    @PutMapping("/cruise-itineraries/{id}")
    public CruiseItineraryDto updateItinerary(
            @PathVariable Long productId,
            @PathVariable Long id,
            @RequestParam int dayNumber,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String shoppingCenterName,
            @RequestParam(required = false) String shoppingExchangeInfo,
            @RequestParam(required = false) String shoppingInfo,
            @RequestParam(required = false) String hotelName,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.update(
                id, dayNumber, title, description,
                shoppingCenterName, shoppingExchangeInfo, shoppingInfo,
                hotelName, sortOrder
        );
    }

    @DeleteMapping("/cruise-itineraries/{id}")
    public void deleteItinerary(
            @PathVariable Long productId,
            @PathVariable Long id
    ) throws IOException {
        itineraryService.delete(id);
    }

    /* ── 스케줄 CRUD ── */

    @PostMapping("/cruise-itineraries/{itineraryId}/schedules")
    public CruiseItineraryScheduleDto addSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @RequestParam(required = false) String time,
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.addSchedule(itineraryId, time, description, sortOrder);
    }

    @PutMapping("/cruise-itineraries/{itineraryId}/schedules/{scheduleId}")
    public CruiseItineraryScheduleDto updateSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId,
            @RequestParam(required = false) String time,
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.updateSchedule(scheduleId, time, description, sortOrder);
    }

    @DeleteMapping("/cruise-itineraries/{itineraryId}/schedules/{scheduleId}")
    public void deleteSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId
    ) {
        itineraryService.deleteSchedule(scheduleId);
    }

    /* ── 이미지 CRUD ── */

    @PostMapping(value = "/cruise-itineraries/{itineraryId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CruiseItineraryImageDto uploadImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("imageType") String imageType
    ) throws IOException {
        return itineraryService.uploadImage(productId, itineraryId, file, imageType);
    }

    @DeleteMapping("/cruise-itineraries/{itineraryId}/images/{imageId}")
    public void deleteImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long imageId
    ) throws IOException {
        itineraryService.deleteImage(imageId);
    }

    /* ── 스케줄별 이미지 ── */

    @PostMapping(value = "/cruise-itineraries/{itineraryId}/schedules/{scheduleId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CruiseItineraryImageDto uploadScheduleImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return itineraryService.uploadScheduleImage(productId, itineraryId, scheduleId, file);
    }

    @DeleteMapping("/cruise-itineraries/{itineraryId}/schedules/{scheduleId}/images/{imageId}")
    public void deleteScheduleImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId,
            @PathVariable Long imageId
    ) throws IOException {
        itineraryService.deleteImage(imageId);
    }

    /* ── 크루즈 상세 (포함/불포함, 가이드 미팅정보) ── */

    @GetMapping("/cruise-details")
    public CruiseDetailDto getDetail(@PathVariable Long productId) {
        return detailService.getDetail(productId);
    }

    @PutMapping("/cruise-details")
    public CruiseDetailDto saveDetail(
            @PathVariable Long productId,
            @RequestBody CruiseDetailDto dto
    ) {
        return detailService.save(productId, dto);
    }

    /* ── 크루즈 가격 ── */

    @GetMapping("/cruise-prices")
    public List<CruisePriceDto> getPrices(@PathVariable Long productId) {
        return priceService.getPrices(productId);
    }

    @PostMapping("/cruise-prices")
    public CruisePriceDto addPrice(
            @PathVariable Long productId,
            @RequestBody CruisePriceDto dto
    ) {
        return priceService.addPrice(productId, dto);
    }

    @PutMapping("/cruise-prices/{priceId}")
    public CruisePriceDto updatePrice(
            @PathVariable Long productId,
            @PathVariable Long priceId,
            @RequestBody CruisePriceDto dto
    ) {
        return priceService.updatePrice(priceId, dto);
    }

    @DeleteMapping("/cruise-prices/{priceId}")
    public void deletePrice(
            @PathVariable Long productId,
            @PathVariable Long priceId
    ) {
        priceService.deletePrice(priceId);
    }
}
