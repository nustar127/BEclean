package com.clean.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.clean.demo.entity.Image;
import com.clean.demo.entity.Service;
import com.clean.demo.repository.ImageRepository;
import com.clean.demo.repository.ServiceRepository;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ImageRepository imageRepository;

    public Service updateService(Long id, Service newService) {
        return serviceRepository.findById(id)
                .map(service -> {

                    if (newService.getName() != null) {
                        service.setName(newService.getName());
                    }
                    if (newService.getDescription() != null) {
                        service.setDescription(newService.getDescription());
                    }
                    if (newService.getPrice() != null) {
                        service.setPrice(newService.getPrice());
                    }
                    if (newService.getTime() != null) {
                        service.setTime(newService.getTime());
                    }
                    if (newService.getDepedensOnArea() != null) {
                        service.setDepedensOnArea(newService.getDepedensOnArea());
                    }

                    service.setImages(newService.getImages());

                    if (newService.getFeaturedImage() != null &&
                            newService.getFeaturedImage().getId() != null) {

                        Image managedImage = imageRepository
                                .findById(newService.getFeaturedImage().getId())
                                .orElseThrow(() -> new RuntimeException("Image not found"));

                        service.setFeaturedImage(managedImage);
                    }

                    return serviceRepository.save(service);
                })
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }
}