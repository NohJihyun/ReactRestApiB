package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.SchoolTripItineraryDto;
import com.nakshi.rohitour.dto.SchoolTripItineraryImageDto;
import com.nakshi.rohitour.dto.SchoolTripItineraryScheduleDto;
import com.nakshi.rohitour.repository.admin.AdminSchoolTripItineraryImageMapper;
import com.nakshi.rohitour.repository.admin.AdminSchoolTripItineraryMapper;
import com.nakshi.rohitour.repository.admin.AdminSchoolTripItineraryScheduleMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AdminSchoolTripItineraryService {

    private final AdminSchoolTripItineraryMapper itineraryMapper;
    private final AdminSchoolTripItineraryImageMapper imageMapper;
    private final AdminSchoolTripItineraryScheduleMapper scheduleMapper;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public AdminSchoolTripItineraryService(
            AdminSchoolTripItineraryMapper itineraryMapper,
            AdminSchoolTripItineraryImageMapper imageMapper,
            AdminSchoolTripItineraryScheduleMapper scheduleMapper
    ) {
        this.itineraryMapper = itineraryMapper;
        this.imageMapper = imageMapper;
        this.scheduleMapper = scheduleMapper;
    }

    public List<SchoolTripItineraryDto> getItineraries(Long productId) {
        List<SchoolTripItineraryDto> list = itineraryMapper.findByProductId(productId);
        for (SchoolTripItineraryDto dto : list) {
            dto.setSchedules(scheduleMapper.findByItineraryId(dto.getId()));
            dto.setImages(imageMapper.findByItineraryId(dto.getId()));
        }
        return list;
    }

    public SchoolTripItineraryDto create(
            Long productId,
            int dayNumber, String title, String description,
            String hotelName,
            String shoppingCenterName, String shoppingExchangeInfo, String shoppingInfo,
            int sortOrder
    ) {
        SchoolTripItineraryDto dto = new SchoolTripItineraryDto();
        dto.setProductId(productId);
        dto.setDayNumber(dayNumber);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setHotelName(hotelName);
        dto.setShoppingCenterName(shoppingCenterName);
        dto.setShoppingExchangeInfo(shoppingExchangeInfo);
        dto.setShoppingInfo(shoppingInfo);
        dto.setSortOrder(sortOrder);
        itineraryMapper.insert(dto);
        dto.setSchedules(List.of());
        dto.setImages(List.of());
        return dto;
    }

    public SchoolTripItineraryDto update(
            Long id,
            int dayNumber, String title, String description,
            String hotelName,
            String shoppingCenterName, String shoppingExchangeInfo, String shoppingInfo,
            int sortOrder
    ) {
        SchoolTripItineraryDto existing = itineraryMapper.findById(id);
        existing.setDayNumber(dayNumber);
        existing.setTitle(title);
        existing.setDescription(description);
        existing.setHotelName(hotelName);
        existing.setShoppingCenterName(shoppingCenterName);
        existing.setShoppingExchangeInfo(shoppingExchangeInfo);
        existing.setShoppingInfo(shoppingInfo);
        existing.setSortOrder(sortOrder);
        itineraryMapper.update(existing);
        existing.setSchedules(scheduleMapper.findByItineraryId(id));
        existing.setImages(imageMapper.findByItineraryId(id));
        return existing;
    }

    public void delete(Long id) throws IOException {
        List<SchoolTripItineraryImageDto> images = imageMapper.findByItineraryId(id);
        for (SchoolTripItineraryImageDto img : images) {
            deletePhysicalFile(img.getImagePath());
        }
        itineraryMapper.delete(id);
    }

    @Transactional
    public SchoolTripItineraryScheduleDto addSchedule(Long itineraryId, String time, String description, int sortOrder) {
        SchoolTripItineraryScheduleDto dto = new SchoolTripItineraryScheduleDto();
        dto.setItineraryId(itineraryId);
        dto.setTime(time);
        dto.setDescription(description);
        dto.setSortOrder(sortOrder);
        scheduleMapper.insert(dto);
        return dto;
    }

    @Transactional
    public SchoolTripItineraryScheduleDto updateSchedule(Long scheduleId, String time, String description, int sortOrder) {
        SchoolTripItineraryScheduleDto dto = scheduleMapper.findById(scheduleId);
        dto.setTime(time);
        dto.setDescription(description);
        dto.setSortOrder(sortOrder);
        scheduleMapper.update(dto);
        return dto;
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleMapper.delete(scheduleId);
    }

    public SchoolTripItineraryImageDto uploadImage(
            Long productId, Long itineraryId, MultipartFile file, String imageType
    ) throws IOException {
        String relativePath = saveFile(productId, file, imageType);
        int sortOrder = imageMapper.findByItineraryId(itineraryId).size();

        SchoolTripItineraryImageDto dto = new SchoolTripItineraryImageDto();
        dto.setItineraryId(itineraryId);
        dto.setProductId(productId);
        dto.setImagePath(relativePath);
        dto.setImageType(imageType);
        dto.setSortOrder(sortOrder);
        imageMapper.insert(dto);
        return dto;
    }

    public void deleteImage(Long imageId) throws IOException {
        SchoolTripItineraryImageDto img = imageMapper.findById(imageId);
        if (img != null) {
            deletePhysicalFile(img.getImagePath());
            imageMapper.delete(imageId);
        }
    }

    private String saveFile(Long productId, MultipartFile file, String imageType) throws IOException {
        String subType = imageType.toLowerCase();
        String ext = getExtension(file.getOriginalFilename());
        String fileName = subType + "_" + UUID.randomUUID() + "." + ext;
        String relativePath = "/uploads/products/" + productId + "/school/" + fileName;
        Path fullPath = Paths.get(uploadBaseDir, "products", String.valueOf(productId), "school", fileName);
        Files.createDirectories(fullPath.getParent());
        file.transferTo(fullPath);
        return relativePath;
    }

    private void deletePhysicalFile(String relativePath) throws IOException {
        if (relativePath == null || relativePath.isBlank()) return;
        String localPath = relativePath.replace("/uploads/", "");
        Path fullPath = Paths.get(uploadBaseDir, localPath);
        Files.deleteIfExists(fullPath);
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) return "jpg";
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    }
}
