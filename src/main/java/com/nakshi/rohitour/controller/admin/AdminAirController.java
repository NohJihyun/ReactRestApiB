package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.dto.*;
import com.nakshi.rohitour.service.admin.AdminAirDetailService;
import com.nakshi.rohitour.service.admin.AdminAirItineraryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/products/{productId}")
public class AdminAirController {

    private final AdminAirItineraryService itineraryService;
    private final AdminAirDetailService    detailService;

    public AdminAirController(
            AdminAirItineraryService itineraryService,
            AdminAirDetailService detailService
    ) {
        this.itineraryService = itineraryService;
        this.detailService    = detailService;
    }

    /* ── 일정 CRUD ── */

    @GetMapping("/air-itineraries")
    public List<AirItineraryDto> getItineraries(@PathVariable Long productId) {
        return itineraryService.getItineraries(productId);
    }

    @PostMapping("/air-itineraries")
    public AirItineraryDto createItinerary(
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

    @PutMapping("/air-itineraries/{id}")
    public AirItineraryDto updateItinerary(
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

    @DeleteMapping("/air-itineraries/{id}")
    public void deleteItinerary(
            @PathVariable Long productId,
            @PathVariable Long id
    ) throws IOException {
        itineraryService.delete(id);
    }

    /* ── 스케줄 CRUD ── */

    @PostMapping("/air-itineraries/{itineraryId}/schedules")
    public AirItineraryScheduleDto addSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @RequestParam(required = false) String time,
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.addSchedule(itineraryId, time, description, sortOrder);
    }

    @PutMapping("/air-itineraries/{itineraryId}/schedules/{scheduleId}")
    public AirItineraryScheduleDto updateSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId,
            @RequestParam(required = false) String time,
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.updateSchedule(scheduleId, time, description, sortOrder);
    }

    @DeleteMapping("/air-itineraries/{itineraryId}/schedules/{scheduleId}")
    public void deleteSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId
    ) {
        itineraryService.deleteSchedule(scheduleId);
    }

    /* ── 이미지 CRUD ── */

    @PostMapping(value = "/air-itineraries/{itineraryId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AirItineraryImageDto uploadImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("imageType") String imageType
    ) throws IOException {
        return itineraryService.uploadImage(productId, itineraryId, file, imageType);
    }

    @DeleteMapping("/air-itineraries/{itineraryId}/images/{imageId}")
    public void deleteImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long imageId
    ) throws IOException {
        itineraryService.deleteImage(imageId);
    }

    @PostMapping(value = "/air-itineraries/{itineraryId}/schedules/{scheduleId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AirItineraryImageDto uploadScheduleImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return itineraryService.uploadScheduleImage(productId, itineraryId, scheduleId, file);
    }

    @DeleteMapping("/air-itineraries/{itineraryId}/schedules/{scheduleId}/images/{imageId}")
    public void deleteScheduleImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId,
            @PathVariable Long imageId
    ) throws IOException {
        itineraryService.deleteImage(imageId);
    }

    /* ── 항공 상세 (포함/불포함, 가이드, 가격, 항공편 정보) ── */

    @GetMapping("/air-details")
    public AirDetailDto getDetail(@PathVariable Long productId) {
        return detailService.getDetail(productId);
    }

    @PutMapping("/air-details")
    public AirDetailDto saveDetail(
            @PathVariable Long productId,
            @RequestBody AirDetailDto dto
    ) {
        return detailService.save(productId, dto);
    }
}
