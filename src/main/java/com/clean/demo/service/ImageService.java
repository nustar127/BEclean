package com.clean.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clean.demo.entity.Image;
import com.clean.demo.repository.ImageRepository;
import com.clean.demo.repository.ServiceRepository;
import com.clean.demo.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ImageRepository imageRepository;

    public void deleteImageFromService(Image image) {
        inventoryRepository.findAllByFeaturedImage(image)
                .forEach(service -> service.setFeaturedImage(null));

        serviceRepository.findAllByFeaturedImage(image)
                .forEach(service -> service.setFeaturedImage(null));

        serviceRepository.findAllByImagesGalleryContains(image)
                .forEach(service -> service.getImages().remove(image));

        imageRepository.delete(image);
    }
}