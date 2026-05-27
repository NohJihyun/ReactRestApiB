package com.nakshi.rohitour.repository.visit;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VisitMapper {
    void upsert(@Param("visitorUuid") String visitorUuid, @Param("deviceType") String deviceType);
    int countToday();
    int countTodayByDevice(@Param("deviceType") String deviceType);
}
