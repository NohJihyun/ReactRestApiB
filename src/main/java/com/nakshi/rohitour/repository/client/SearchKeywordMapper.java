package com.nakshi.rohitour.repository.client;

import com.nakshi.rohitour.dto.SearchKeywordDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchKeywordMapper {

    void upsert(@Param("keyword") String keyword);

    List<SearchKeywordDto> findTop(@Param("limit") int limit);
}
