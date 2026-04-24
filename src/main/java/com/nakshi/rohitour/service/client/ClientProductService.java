package com.nakshi.rohitour.service.client;

import com.nakshi.rohitour.dto.ProductDto;
import com.nakshi.rohitour.dto.ProductFileDto;
import com.nakshi.rohitour.dto.ProductImageDto;
import com.nakshi.rohitour.repository.client.ClientProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientProductService {

    private final ClientProductMapper mapper;

    public ClientProductService(ClientProductMapper mapper) {
        this.mapper = mapper;
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
}
