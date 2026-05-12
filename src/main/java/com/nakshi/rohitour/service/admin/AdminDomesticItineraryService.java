package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.DomesticItineraryDto;
import com.nakshi.rohitour.dto.DomesticItineraryImageDto;
import com.nakshi.rohitour.dto.DomesticItineraryScheduleDto;
import com.nakshi.rohitour.repository.admin.AdminDomesticItineraryImageMapper;
import com.nakshi.rohitour.repository.admin.AdminDomesticItineraryMapper;
import com.nakshi.rohitour.repository.admin.AdminDomesticItineraryScheduleMapper;
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
public class AdminDomesticItineraryService {

    private final AdminDomesticItineraryMapper itineraryMapper;
    private final AdminDomesticItineraryImageMapper imageMapper;
    private final AdminDomesticItineraryScheduleMapper scheduleMapper;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public AdminDomesticItineraryService(
            AdminDomesticItineraryMapper itineraryMapper,
            AdminDomesticItineraryImageMapper imageMapper,
            AdminDomesticItineraryScheduleMapper scheduleMapper
    ) {
        this.itineraryMapper = itineraryMapper;
        this.imageMapper = imageMapper;
        this.scheduleMapper = scheduleMapper;
    }

    public List<DomesticItineraryDto> getItineraries(Long productId) {
        List<DomesticItineraryDto> list = itineraryMapper.findByProductId(productId);
        for (DomesticItineraryDto dto : list) {
            dto.setSchedules(scheduleMapper.findByItineraryId(dto.getId()));
            dto.setImages(imageMapper.findByItineraryId(dto.getId()));
        }
        return list;
    }

    public DomesticItineraryDto create(
            Long productId,
            int dayNumber, String title, String description,
            String hotelName,
            String shoppingCenterName, String shoppingExchangeInfo, String shoppingInfo,
            int sortOrder
    ) {
        DomesticItineraryDto dto = new DomesticItineraryDto();
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

    public DomesticItineraryDto update(
            Long id,
            int dayNumber, String title, String description,
            String hotelName,
            String shoppingCenterName, String shoppingExchangeInfo, String shoppingInfo,
            int sortOrder
    ) {
        DomesticItineraryDto existing = itineraryMapper.findById(id);
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
        List<DomesticItineraryImageDto> images = imageMapper.findByItineraryId(id);
        for (DomesticItineraryImageDto img : images) {
            deletePhysicalFile(img.getImagePath());
        }
        itineraryMapper.delete(id);
    }

    @Transactional
    public DomesticItineraryScheduleDto addSchedule(Long itineraryId, String time, String description, int sortOrder) {
        DomesticItineraryScheduleDto dto = new DomesticItineraryScheduleDto();
        dto.setItineraryId(itineraryId);
        dto.setTime(time);
        dto.setDescription(description);
        dto.setSortOrder(sortOrder);
        scheduleMapper.insert(dto);
        return dto;
    }

    @Transactional
    public DomesticItineraryScheduleDto updateSchedule(Long scheduleId, String time, String description, int sortOrder) {
        DomesticItineraryScheduleDto dto = scheduleMapper.findById(scheduleId);
        dto.setTime(time);
        dto.setDescription(description);
        dto.setSortOrder(sortOrder);
        scheduleMapper.update(dto);
        return dto;
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleMapper.delete(scheduleId);
    }

    public DomesticItineraryImageDto uploadImage(
            Long productId, Long itineraryId, MultipartFile file, String imageType
    ) throws IOException {
        String relativePath = saveFile(productId, file, imageType);
        int sortOrder = imageMapper.findByItineraryId(itineraryId).size();

        DomesticItineraryImageDto dto = new DomesticItineraryImageDto();
        dto.setItineraryId(itineraryId);
        dto.setProductId(productId);
        dto.setImagePath(relativePath);
        dto.setImageType(imageType);
        dto.setSortOrder(sortOrder);
        imageMapper.insert(dto);
        return dto;
    }

    public void deleteImage(Long imageId) throws IOException {
        DomesticItineraryImageDto img = imageMapper.findById(imageId);
        if (img != null) {
            deletePhysicalFile(img.getImagePath());
            imageMapper.delete(imageId);
        }
    }

    private String saveFile(Long productId, MultipartFile file, String imageType) throws IOException {
        String subType = imageType.toLowerCase();
        String ext = getExtension(file.getOriginalFilename());
        String fileName = subType + "_" + UUID.randomUUID() + "." + ext;
        String relativePath = "/uploads/products/" + productId + "/domestic/" + fileName;
        Path fullPath = Paths.get(uploadBaseDir, "products", String.valueOf(productId), "domestic", fileName);
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
