package com.nakshi.rohitour.controller;

import com.nakshi.rohitour.repository.visit.VisitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VisitController {

    private final VisitMapper visitMapper;

    @PostMapping("/visit")
    public void recordVisit(@RequestParam String uuid, @RequestParam String device) {
        String deviceType = "MOBILE".equals(device) ? "MOBILE" : "PC";
        visitMapper.upsert(uuid, deviceType);
    }
}
