package com.example.demo;

import org.springframework.http.HttpHeaders;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/upload")
    public ResponseEntity<ClassPathResource> uploadForm() {
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.TEXT_HTML)
                .body(new ClassPathResource("templates/upload.html"));
    }

    @PostMapping("/images/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a file");
            }
            ImageData imageData = new ImageData();
            imageData.setName(file.getOriginalFilename());
            imageData.setType(file.getContentType());
            imageData.setData(file.getBytes());
            imageService.saveImage(imageData);
            return ResponseEntity.ok("Image uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading image: " + e.getMessage());
        }
    }

    @GetMapping("/getImageById/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable Long id) {
        Optional<ImageData> imageDataOptional = imageService.getImageById(id);
        if (imageDataOptional.isPresent()) {
            ImageData imageData = imageDataOptional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(imageData.getType()));
            headers.setContentDispositionFormData("inline", imageData.getName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageData.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
