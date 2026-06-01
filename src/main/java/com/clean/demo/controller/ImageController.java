package com.clean.demo.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import com.clean.demo.dto.ApiResponse;
import com.clean.demo.entity.Image;
import com.clean.demo.repository.ImageRepository;
import com.clean.demo.service.ImageService;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
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
        return ApiResponse.success(imageRepository.findAll(Sort.by(Sort.Direction.ASC, "id")), "Images founded");
    }

    @GetMapping("/{id}")
    public ApiResponse<Image> findById(@PathVariable("id") Long imageId) {
        return imageRepository.findById(imageId)
                .map(image -> ApiResponse.success(image, "Image founded"))
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<Image>> uploadImage(
            @RequestParam(value = "file", required = false) List<MultipartFile> fileParts,
            @RequestParam(value = "files", required = false) List<MultipartFile> filesParts,
            @RequestParam(required = false) String alt) {
        try {
            List<MultipartFile> files = collectFiles(fileParts, filesParts);
            List<Image> savedImages = new ArrayList<>();
            for (MultipartFile file : files) {
                String filePath = saveImage(file);

                Image image = new Image();
                image.setAlt(alt);
                image.setFilename(filePath);

                savedImages.add(imageRepository.save(image));
            }

            return ApiResponse.success(savedImages, "Images uploaded successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    private List<MultipartFile> collectFiles(List<MultipartFile> fileParts, List<MultipartFile> filesParts) {
        List<MultipartFile> files = new ArrayList<>();
        if (fileParts != null) {
            files.addAll(fileParts);
        }
        if (filesParts != null) {
            files.addAll(filesParts);
        }

        files = files.stream()
                .filter(Objects::nonNull)
                .filter(file -> !file.isEmpty())
                .toList();

        if (files.isEmpty()) {
            throw new IllegalArgumentException("At least one image file is required");
        }

        return files;
    }

    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String identifier = UUID.randomUUID().toString();
        String originalFilename = StringUtils.cleanPath(Objects.toString(file.getOriginalFilename(), ""));
        String safeFilename = StringUtils.hasText(originalFilename)
                ? Paths.get(originalFilename.replace("\\", "/")).getFileName().toString()
                : "image";
        String fileName = identifier + "_" + safeFilename;

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
