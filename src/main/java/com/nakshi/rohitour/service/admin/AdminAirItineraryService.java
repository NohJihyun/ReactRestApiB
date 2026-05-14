package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.AirItineraryDto;
import com.nakshi.rohitour.dto.AirItineraryImageDto;
import com.nakshi.rohitour.dto.AirItineraryScheduleDto;
import com.nakshi.rohitour.repository.admin.AdminAirItineraryImageMapper;
import com.nakshi.rohitour.repository.admin.AdminAirItineraryMapper;
import com.nakshi.rohitour.repository.admin.AdminAirItineraryScheduleMapper;
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
public class AdminAirItineraryService {

    private final AdminAirItineraryMapper itineraryMapper;
    private final AdminAirItineraryImageMapper imageMapper;
    private final AdminAirItineraryScheduleMapper scheduleMapper;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public AdminAirItineraryService(
            AdminAirItineraryMapper itineraryMapper,
            AdminAirItineraryImageMapper imageMapper,
            AdminAirItineraryScheduleMapper scheduleMapper
    ) {
        this.itineraryMapper = itineraryMapper;
        this.imageMapper = imageMapper;
        this.scheduleMapper = scheduleMapper;
    }

    public List<AirItineraryDto> getItineraries(Long productId) {
        List<AirItineraryDto> list = itineraryMapper.findByProductId(productId);
        for (AirItineraryDto dto : list) {
            List<AirItineraryScheduleDto> schedules = scheduleMapper.findByItineraryId(dto.getId());
            for (AirItineraryScheduleDto schedule : schedules) {
                schedule.setImages(imageMapper.findByScheduleId(schedule.getId()));
            }
            dto.setSchedules(schedules);
            dto.setImages(imageMapper.findByItineraryId(dto.getId()));
        }
        return list;
    }

    public AirItineraryDto create(
            Long productId,
            int dayNumber, String title, String description,
            String hotelName,
            String shoppingCenterName, String shoppingExchangeInfo, String shoppingInfo,
            int sortOrder
    ) {
        AirItineraryDto dto = new AirItineraryDto();
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

    public AirItineraryDto update(
            Long id,
            int dayNumber, String title, String description,
            String hotelName,
            String shoppingCenterName, String shoppingExchangeInfo, String shoppingInfo,
            int sortOrder
    ) {
        AirItineraryDto existing = itineraryMapper.findById(id);
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
        List<AirItineraryImageDto> images = imageMapper.findByItineraryId(id);
        for (AirItineraryImageDto img : images) {
            deletePhysicalFile(img.getImagePath());
        }
        itineraryMapper.delete(id);
    }

    @Transactional
    public AirItineraryScheduleDto addSchedule(Long itineraryId, String time, String description, int sortOrder) {
        AirItineraryScheduleDto dto = new AirItineraryScheduleDto();
        dto.setItineraryId(itineraryId);
        dto.setTime(time);
        dto.setDescription(description);
        dto.setSortOrder(sortOrder);
        scheduleMapper.insert(dto);
        return dto;
    }

    @Transactional
    public AirItineraryScheduleDto updateSchedule(Long scheduleId, String time, String description, int sortOrder) {
        AirItineraryScheduleDto dto = scheduleMapper.findById(scheduleId);
        dto.setTime(time);
        dto.setDescription(description);
        dto.setSortOrder(sortOrder);
        scheduleMapper.update(dto);
        return dto;
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleMapper.delete(scheduleId);
    }

    public AirItineraryImageDto uploadImage(
            Long productId, Long itineraryId, MultipartFile file, String imageType
    ) throws IOException {
        String relativePath = saveFile(productId, file, imageType);
        int sortOrder = imageMapper.findByItineraryId(itineraryId).size();

        AirItineraryImageDto dto = new AirItineraryImageDto();
        dto.setItineraryId(itineraryId);
        dto.setProductId(productId);
        dto.setImagePath(relativePath);
        dto.setImageType(imageType);
        dto.setSortOrder(sortOrder);
        imageMapper.insert(dto);
        return dto;
    }

    public AirItineraryImageDto uploadScheduleImage(
            Long productId, Long itineraryId, Long scheduleId, MultipartFile file
    ) throws IOException {
        String relativePath = saveFile(productId, file, "schedule");
        int sortOrder = imageMapper.findByScheduleId(scheduleId).size();

        AirItineraryImageDto dto = new AirItineraryImageDto();
        dto.setItineraryId(itineraryId);
        dto.setScheduleId(scheduleId);
        dto.setProductId(productId);
        dto.setImagePath(relativePath);
        dto.setImageType("SCHEDULE");
        dto.setSortOrder(sortOrder);
        imageMapper.insert(dto);
        return dto;
    }

    public void deleteImage(Long imageId) throws IOException {
        AirItineraryImageDto img = imageMapper.findById(imageId);
        if (img != null) {
            deletePhysicalFile(img.getImagePath());
            imageMapper.delete(imageId);
        }
    }

    private String saveFile(Long productId, MultipartFile file, String imageType) throws IOException {
        String subType = imageType.toLowerCase();
        String ext = getExtension(file.getOriginalFilename());
        String fileName = subType + "_" + UUID.randomUUID() + "." + ext;
        String relativePath = "/uploads/products/" + productId + "/air/" + fileName;
        Path fullPath = Paths.get(uploadBaseDir, "products", String.valueOf(productId), "air", fileName);
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
