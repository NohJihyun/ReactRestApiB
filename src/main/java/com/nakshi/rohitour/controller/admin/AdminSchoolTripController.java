package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.dto.*;
import com.nakshi.rohitour.service.admin.AdminSchoolTripDetailService;
import com.nakshi.rohitour.service.admin.AdminSchoolTripItineraryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/products/{productId}")
public class AdminSchoolTripController {

    private final AdminSchoolTripItineraryService itineraryService;
    private final AdminSchoolTripDetailService    detailService;

    public AdminSchoolTripController(
            AdminSchoolTripItineraryService itineraryService,
            AdminSchoolTripDetailService detailService
    ) {
        this.itineraryService = itineraryService;
        this.detailService    = detailService;
    }

    /* ── 일정 CRUD ── */

    @GetMapping("/school-trip-itineraries")
    public List<SchoolTripItineraryDto> getItineraries(@PathVariable Long productId) {
        return itineraryService.getItineraries(productId);
    }

    @PostMapping("/school-trip-itineraries")
    public SchoolTripItineraryDto createItinerary(
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

    @PutMapping("/school-trip-itineraries/{id}")
    public SchoolTripItineraryDto updateItinerary(
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

    @DeleteMapping("/school-trip-itineraries/{id}")
    public void deleteItinerary(
            @PathVariable Long productId,
            @PathVariable Long id
    ) throws IOException {
        itineraryService.delete(id);
    }

    /* ── 스케줄 CRUD ── */

    @PostMapping("/school-trip-itineraries/{itineraryId}/schedules")
    public SchoolTripItineraryScheduleDto addSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @RequestParam(required = false) String time,
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.addSchedule(itineraryId, time, description, sortOrder);
    }

    @PutMapping("/school-trip-itineraries/{itineraryId}/schedules/{scheduleId}")
    public SchoolTripItineraryScheduleDto updateSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId,
            @RequestParam(required = false) String time,
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int sortOrder
    ) {
        return itineraryService.updateSchedule(scheduleId, time, description, sortOrder);
    }

    @DeleteMapping("/school-trip-itineraries/{itineraryId}/schedules/{scheduleId}")
    public void deleteSchedule(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId
    ) {
        itineraryService.deleteSchedule(scheduleId);
    }

    /* ── 이미지 CRUD ── */

    @PostMapping(value = "/school-trip-itineraries/{itineraryId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SchoolTripItineraryImageDto uploadImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("imageType") String imageType
    ) throws IOException {
        return itineraryService.uploadImage(productId, itineraryId, file, imageType);
    }

    @DeleteMapping("/school-trip-itineraries/{itineraryId}/images/{imageId}")
    public void deleteImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long imageId
    ) throws IOException {
        itineraryService.deleteImage(imageId);
    }

    @PostMapping(value = "/school-trip-itineraries/{itineraryId}/schedules/{scheduleId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SchoolTripItineraryImageDto uploadScheduleImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return itineraryService.uploadScheduleImage(productId, itineraryId, scheduleId, file);
    }

    @DeleteMapping("/school-trip-itineraries/{itineraryId}/schedules/{scheduleId}/images/{imageId}")
    public void deleteScheduleImage(
            @PathVariable Long productId,
            @PathVariable Long itineraryId,
            @PathVariable Long scheduleId,
            @PathVariable Long imageId
    ) throws IOException {
        itineraryService.deleteImage(imageId);
    }

    /* ── 수학여행 상세 ── */

    @GetMapping("/school-trip-details")
    public SchoolTripDetailDto getDetail(@PathVariable Long productId) {
        return detailService.getDetail(productId);
    }

    @PutMapping("/school-trip-details")
    public SchoolTripDetailDto saveDetail(
            @PathVariable Long productId,
            @RequestBody SchoolTripDetailDto dto
    ) {
        return detailService.save(productId, dto);
    }
}
