package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ImageRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String name);
}