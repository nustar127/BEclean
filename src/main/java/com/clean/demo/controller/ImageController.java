package com.clean.demo.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.clean.demo.entity.Image;
import com.clean.demo.repository.ImageRepository;

import jakarta.annotation.Nullable;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("uploads/images")
public class ImageController {

    @Value("${upload.path}")
    private String uploadDir;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("")
    private Iterable<Image> findAll() {
        return imageRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Image> findById(@PathVariable("id") Long imageId) {
        return imageRepository.findById(imageId);
    }

    @PostMapping("")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @Nullable String alt) {
        try {
            String filePath = saveImage(file);

            Image image = new Image();
            image.setAlt(alt);
            image.setFilename(filePath);

            Image savedImage = imageRepository.save(image);

            return ResponseEntity.ok("Image uploaded successfully: " + savedImage);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String identifier = UUID.randomUUID().toString();
        String fileName = identifier + "_" + file.getOriginalFilename();

        Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @PutMapping("/{id}")
    public Image changeImage(@RequestBody String alt, @PathVariable("id") Long imageId) {
        return imageRepository.findById(imageId)
                .map(image -> {
                    image.setAlt(alt);
                    return imageRepository.save(image);
                }).orElseGet(() -> {
                    return null;
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable("id") Long imageId) {
        try {
            Optional<Image> imageExist = imageRepository.findById(imageId);

            if(imageExist.isPresent()) {
                Image image = imageExist.get();

                String filename = image.getFilename();

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.delete(uploadPath.resolve(filename));
                imageRepository.deleteById(imageId);

                return ResponseEntity.status(HttpStatus.OK).body("Message deleted");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No image found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting image");
        }
    }
}
