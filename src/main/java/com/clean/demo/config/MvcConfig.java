package com.clean.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = new File(uploadFolder).getAbsolutePath();
        String resourceLocation = "file:" + path + "/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
                
        System.out.println("Картинки раздаются из: " + resourceLocation);
    }
}
