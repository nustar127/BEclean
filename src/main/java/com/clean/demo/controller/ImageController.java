package com.clean.demo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.entity.Image;
import com.clean.demo.repository.ImageRepository;
import com.clean.demo.service.ImageService;

import jakarta.annotation.Nullable;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/uploads/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Value("${upload.path}")
    private String uploadDir;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping
    public ApiResponse<Iterable<Image>> findAll() {
        return ApiResponse.success(imageRepository.findAll(), "Images founded");
    }

    @GetMapping("/{id}")
    public ApiResponse<Image> findById(@PathVariable("id") Long imageId) {
        return imageRepository.findById(imageId)
                .map(image -> ApiResponse.success(image, "Image founded"))
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));
    }

    @PostMapping
    public ApiResponse<Image> uploadImage(@RequestParam("file") MultipartFile file, @Nullable String alt) {
        try {
            String filePath = saveImage(file);

            Image image = new Image();
            image.setAlt(alt);
            image.setFilename(filePath);

            return ApiResponse.success(imageRepository.save(image), "Image uploaded successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
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
    public ApiResponse<Image> changeImage(@RequestBody String alt, @PathVariable("id") Long imageId) {
        Image updated = imageRepository.findById(imageId)
                .map(image -> {
                    image.setAlt(alt);
                    return imageRepository.save(image);
                })
                .orElseThrow(() -> new RuntimeException("Image not found"));

        return ApiResponse.success(updated, "Alt text updated");
    }

    @DeleteMapping
    public ApiResponse<Void> deleteByIds(@RequestBody Iterable<Long> ids) {
        try {
            for (Long imageId : ids) {
                Image image = imageRepository.findById(imageId)
                        .orElseThrow(() -> new RuntimeException("Image not found"));

                deletePhysicalFile(image);
                imageService.deleteImageFromService(image);
                imageRepository.delete(image);

            }
            return ApiResponse.success(null, "Images deleted");
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteImage(@PathVariable("id") Long imageId) {
        try {
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Image not found"));

            deletePhysicalFile(image);
            imageService.deleteImageFromService(image);
            imageRepository.delete(image);

            return ApiResponse.success(null, "Image deleted");
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage());
        }
    }

    private void deletePhysicalFile(Image image) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(image.getFilename());
        Files.deleteIfExists(filePath);
    }
}
