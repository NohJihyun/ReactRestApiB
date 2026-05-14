package com.nakshi.rohitour.service.client;

import com.nakshi.rohitour.dto.SearchKeywordDto;
import com.nakshi.rohitour.repository.client.SearchKeywordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchKeywordService {

    private final SearchKeywordMapper mapper;

    public SearchKeywordService(SearchKeywordMapper mapper) {
        this.mapper = mapper;
    }

    public void log(String keyword) {
        String kw = keyword.trim();
        if (kw.isEmpty()) return;
        mapper.upsert(kw);
    }

    public List<SearchKeywordDto> getPopular(int limit) {
        return mapper.findTop(Math.min(limit, 20));
    }
}
