package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.dto.*;
import com.nakshi.rohitour.service.admin.AdminDomesticDetailService;
import com.nakshi.rohitour.service.admin.AdminDomesticItineraryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/products/{productId}")
public class AdminDomesticController {

    private final AdminDomesticItineraryService itineraryService;
    private final AdminDomesticDetailService    detailService;

    public AdminDomesticController(
            AdminDomesticItineraryService itineraryService,
            AdminDomesticDetailService detailService
    ) {
        this.itineraryService = itineraryService;
        this.detailService    = detailService;
    }

    /* ── 일정 CRUD ── */

    @GetMapping("/domestic-itineraries")
    public List<DomesticItineraryDto> getItineraries(@PathVariable Long productId) {
        return itineraryService.getItineraries(productId);
    }

    @PostMapping("/domestic-itineraries")
    public DomesticItineraryDto createItinerary(
            @PathVariable Long productId,
            @RequestParam int dayNumber,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String hotelName,
            @RequestParam(required = false) String shoppingCenterName,
            @RequestParam(required = false) String shoppingExchangeInfo,
            @RequestParam(required = false) String shoppingInfo,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.create(
                productId, dayNumber, title, description,
                hotelName, shoppingCenterName, shoppingExchangeInfo, shoppingInfo,
                sortOrder
        );
    }

    @PutMapping("/domestic-itineraries/{id}")
    public DomesticItineraryDto updateItinerary(
            @PathVariable Long productId,
            @PathVariable Long id,
            @RequestParam int dayNumber,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String hotelName,
            @RequestParam(required = false) String shoppingCenterName,
            @RequestParam(required = false) String shoppingExchangeInfo,
            @RequestParam(required = false) String shoppingInfo,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.update(
                id, dayNumber, title, description,
                hotelName, shoppingCenterName, shoppingExchangeInfo, shoppingInfo,
                sortOrder
        );
    }

    @DeleteMapping("/domestic-itineraries/{id}")
    public void deleteItinerary(
            @PathVariable Long productId,
            @PathVariable Long id
    ) throws IOException {
        itineraryService.delete(id);
    }

    /* ── 스케줄 CRUD ── */

    @PostMapping("/domestic-itineraries/{itineraryId}/schedules")
    public DomesticItineraryScheduleDto addSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @RequestParam(required = false) String time,
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.addSchedule(itineraryId, time, description, sortOrder);
    }

    @PutMapping("/domestic-itineraries/{itineraryId}/schedules/{scheduleId}")
    public DomesticItineraryScheduleDto updateSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId,
            @RequestParam(required = false) String time,
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.updateSchedule(scheduleId, time, description, sortOrder);
    }

    @DeleteMapping("/domestic-itineraries/{itineraryId}/schedules/{scheduleId}")
    public void deleteSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId
    ) {
        itineraryService.deleteSchedule(scheduleId);
    }

    /* ── 이미지 CRUD ── */

    @PostMapping(value = "/domestic-itineraries/{itineraryId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DomesticItineraryImageDto uploadImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("imageType") String imageType
    ) throws IOException {
        return itineraryService.uploadImage(productId, itineraryId, file, imageType);
    }

    @DeleteMapping("/domestic-itineraries/{itineraryId}/images/{imageId}")
    public void deleteImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long imageId
    ) throws IOException {
        itineraryService.deleteImage(imageId);
    }

    /* ── 국내 상세 (포함/불포함, 가이드, 가격) ── */

    @GetMapping("/domestic-details")
    public DomesticDetailDto getDetail(@PathVariable Long productId) {
        return detailService.getDetail(productId);
    }

    @PutMapping("/domestic-details")
    public DomesticDetailDto saveDetail(
            @PathVariable Long productId,
            @RequestBody DomesticDetailDto dto
    ) {
        return detailService.save(productId, dto);
    }
}
