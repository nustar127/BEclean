package com.clean.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.clean.demo.entity.Image;
import com.clean.demo.entity.Service;
import com.clean.demo.entity.ServiceRequirement;
import com.clean.demo.entity.Category;
import com.clean.demo.repository.CategoryRepository;
import com.clean.demo.repository.ImageRepository;
import com.clean.demo.repository.ServiceRepository;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void deleteImageFromService(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow();

        serviceRepository.findAllByFeaturedImage(image)
                .forEach(service -> service.setFeaturedImage(null));

        serviceRepository.findAllByImagesGalleryContains(image)
                .forEach(service -> service.getImages().remove(image));

        imageRepository.delete(image);
    }

    public Service updateService(Long id, Service newService) {
        return serviceRepository.findById(id)
                .map(service -> {
                    service.setName(newService.getName());
                    service.setDescription(newService.getDescription());
                    service.setType(newService.getType());
                    service.setPrice(newService.getPrice());
                    service.setTime(newService.getTime());
                    service.setDepedensOnArea(newService.getDepedensOnArea());
                    service.setCategories(resolveCategories(newService.getCategories()));
                    service.setImages(resolveImages(newService.getImages()));
                    service.setFeaturedImage(resolveFeaturedImage(newService.getFeaturedImage()));
                    service.setRequirments(normalizeRequirements(service, newService.getRequirments()));
                    service.setPriceForAdditionalMeter(newService.getPriceForAdditionalMeter());

                    return serviceRepository.save(service);
                })
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    private Image resolveFeaturedImage(Image featuredImage) {
        if (featuredImage == null) {
            return null;
        }
        if (featuredImage.getId() == null) {
            throw new RuntimeException("Featured image id is required");
        }

        return imageRepository.findById(featuredImage.getId())
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    private List<Image> resolveImages(List<Image> images) {
        if (images == null) {
            return new ArrayList<>();
        }

        List<Image> managedImages = new ArrayList<>();
        for (Image image : images) {
            if (image == null || image.getId() == null) {
                throw new RuntimeException("Each gallery image must have an id");
            }

            Image managedImage = imageRepository.findById(image.getId())
                    .orElseThrow(() -> new RuntimeException("Image not found"));
            managedImages.add(managedImage);
        }
        return managedImages;
    }

    private List<Category> resolveCategories(List<Category> categories) {
        if (categories == null) {
            return new ArrayList<>();
        }

        List<Category> managedCategories = new ArrayList<>();
        for (Category category : categories) {
            if (category == null || category.getId() == null) {
                throw new RuntimeException("Each category must have an id");
            }

            Category managedCategory = categoryRepository.findById(category.getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            managedCategories.add(managedCategory);
        }

        return managedCategories;
    }

    private List<ServiceRequirement> normalizeRequirements(Service service, List<ServiceRequirement> requirements) {
        if (requirements == null) {
            return new ArrayList<>();
        }

        for (ServiceRequirement requirement : requirements) {
            requirement.setService(service);
        }

        return requirements;
    }
}
