package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.CruiseItineraryDto;
import com.nakshi.rohitour.dto.CruiseItineraryImageDto;
import com.nakshi.rohitour.dto.CruiseItineraryScheduleDto;
import com.nakshi.rohitour.repository.admin.AdminCruiseItineraryImageMapper;
import com.nakshi.rohitour.repository.admin.AdminCruiseItineraryMapper;
import com.nakshi.rohitour.repository.admin.AdminCruiseItineraryScheduleMapper;
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
public class AdminCruiseItineraryService {

    private final AdminCruiseItineraryMapper itineraryMapper;
    private final AdminCruiseItineraryImageMapper imageMapper;
    private final AdminCruiseItineraryScheduleMapper scheduleMapper;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public AdminCruiseItineraryService(
            AdminCruiseItineraryMapper itineraryMapper,
            AdminCruiseItineraryImageMapper imageMapper,
            AdminCruiseItineraryScheduleMapper scheduleMapper
    ) {
        this.itineraryMapper = itineraryMapper;
        this.imageMapper = imageMapper;
        this.scheduleMapper = scheduleMapper;
    }

    /* 일정 목록 (스케줄 + 이미지 포함) */
    public List<CruiseItineraryDto> getItineraries(Long productId) {
        List<CruiseItineraryDto> list = itineraryMapper.findByProductId(productId);
        for (CruiseItineraryDto dto : list) {
            dto.setSchedules(scheduleMapper.findByItineraryId(dto.getId()));
            dto.setImages(imageMapper.findByItineraryId(dto.getId()));
        }
        return list;
    }

    /* 일정 등록 */
    public CruiseItineraryDto create(
            Long productId,
            int dayNumber, String title, String description,
            String shoppingCenterName, String shoppingExchangeInfo, String shoppingInfo,
            String hotelName, int sortOrder
    ) {
        CruiseItineraryDto dto = new CruiseItineraryDto();
        dto.setProductId(productId);
        dto.setDayNumber(dayNumber);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setShoppingCenterName(shoppingCenterName);
        dto.setShoppingExchangeInfo(shoppingExchangeInfo);
        dto.setShoppingInfo(shoppingInfo);
        dto.setHotelName(hotelName);
        dto.setSortOrder(sortOrder);
        itineraryMapper.insert(dto);
        dto.setSchedules(List.of());
        dto.setImages(List.of());
        return dto;
    }

    /* 일정 수정 */
    public CruiseItineraryDto update(
            Long id,
            int dayNumber, String title, String description,
            String shoppingCenterName, String shoppingExchangeInfo, String shoppingInfo,
            String hotelName, int sortOrder
    ) {
        CruiseItineraryDto existing = itineraryMapper.findById(id);
        existing.setDayNumber(dayNumber);
        existing.setTitle(title);
        existing.setDescription(description);
        existing.setShoppingCenterName(shoppingCenterName);
        existing.setShoppingExchangeInfo(shoppingExchangeInfo);
        existing.setShoppingInfo(shoppingInfo);
        existing.setHotelName(hotelName);
        existing.setSortOrder(sortOrder);
        itineraryMapper.update(existing);
        existing.setSchedules(scheduleMapper.findByItineraryId(id));
        existing.setImages(imageMapper.findByItineraryId(id));
        return existing;
    }

    /* 일정 삭제 (이미지 파일도 함께 삭제, 스케줄은 DB CASCADE) */
    public void delete(Long id) throws IOException {
        List<CruiseItineraryImageDto> images = imageMapper.findByItineraryId(id);
        for (CruiseItineraryImageDto img : images) {
            deletePhysicalFile(img.getImagePath());
        }
        itineraryMapper.delete(id);
    }

    /* 스케줄 추가 */
    @Transactional
    public CruiseItineraryScheduleDto addSchedule(Long itineraryId, String time, String description, int sortOrder) {
        CruiseItineraryScheduleDto dto = new CruiseItineraryScheduleDto();
        dto.setItineraryId(itineraryId);
        dto.setTime(time);
        dto.setDescription(description);
        dto.setSortOrder(sortOrder);
        scheduleMapper.insert(dto);
        return dto;
    }

    /* 스케줄 수정 */
    @Transactional
    public CruiseItineraryScheduleDto updateSchedule(Long scheduleId, String time, String description, int sortOrder) {
        CruiseItineraryScheduleDto dto = scheduleMapper.findById(scheduleId);
        dto.setTime(time);
        dto.setDescription(description);
        dto.setSortOrder(sortOrder);
        scheduleMapper.update(dto);
        return dto;
    }

    /* 스케줄 삭제 */
    public void deleteSchedule(Long scheduleId) {
        scheduleMapper.delete(scheduleId);
    }

    /* 이미지 업로드 */
    public CruiseItineraryImageDto uploadImage(
            Long productId, Long itineraryId, MultipartFile file, String imageType
    ) throws IOException {
        String relativePath = saveFile(productId, file, imageType);
        int sortOrder = imageMapper.findByItineraryId(itineraryId).size();

        CruiseItineraryImageDto dto = new CruiseItineraryImageDto();
        dto.setItineraryId(itineraryId);
        dto.setProductId(productId);
        dto.setImagePath(relativePath);
        dto.setImageType(imageType);
        dto.setSortOrder(sortOrder);
        imageMapper.insert(dto);
        return dto;
    }

    /* 이미지 삭제 */
    public void deleteImage(Long imageId) throws IOException {
        CruiseItineraryImageDto img = imageMapper.findById(imageId);
        if (img != null) {
            deletePhysicalFile(img.getImagePath());
            imageMapper.delete(imageId);
        }
    }

    private String saveFile(Long productId, MultipartFile file, String imageType) throws IOException {
        String subType = imageType.toLowerCase();
        String ext = getExtension(file.getOriginalFilename());
        String fileName = subType + "_" + UUID.randomUUID() + "." + ext;
        String relativePath = "/uploads/products/" + productId + "/cruise/" + fileName;
        Path fullPath = Paths.get(uploadBaseDir, "products", String.valueOf(productId), "cruise", fileName);
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
