package com.nakshi.rohitour.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** 요청 → 디스크 파일 직접 서빙
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadBaseDir + "/");
    }
}
