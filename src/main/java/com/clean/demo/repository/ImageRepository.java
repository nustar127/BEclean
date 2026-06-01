package com.clean.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clean.demo.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long>  {

}