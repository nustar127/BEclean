package com.clean.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.clean.demo.entity.Image;
import com.clean.demo.entity.Service;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    List<Service> findAllByFeaturedImage(Image image);

    List<Service> findAllByImagesGalleryContains(Image image);
}