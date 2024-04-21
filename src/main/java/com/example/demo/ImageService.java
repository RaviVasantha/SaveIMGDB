package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void saveImage(ImageData imageData) {
        imageRepository.save(imageData);
    }
    
    public Optional<ImageData> getImageById(Long id) {
        return imageRepository.findById(id);
    }
}
