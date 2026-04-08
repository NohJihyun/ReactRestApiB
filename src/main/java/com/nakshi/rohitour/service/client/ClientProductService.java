package com.nakshi.rohitour.service.client;

import com.nakshi.rohitour.dto.ProductDto;
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
}
