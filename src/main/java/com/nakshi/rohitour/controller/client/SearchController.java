package com.nakshi.rohitour.controller.client;

import com.nakshi.rohitour.dto.SearchKeywordDto;
import com.nakshi.rohitour.service.client.SearchKeywordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchKeywordService service;

    public SearchController(SearchKeywordService service) {
        this.service = service;
    }

    /** POST /api/search/log?keyword=xxx — 검색어 카운트 누적 */
    @PostMapping("/log")
    public void log(@RequestParam String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            service.log(keyword.trim());
        }
    }

    /** GET /api/search/popular?limit=10 — 인기 검색어 목록 */
    @GetMapping("/popular")
    public List<SearchKeywordDto> popular(@RequestParam(defaultValue = "10") int limit) {
        return service.getPopular(limit);
    }
}
