package com.shopproject.shopbt.repository.gallery_image;

import com.shopproject.shopbt.entity.Gallery_Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface Gallery_ImageRepository extends JpaRepository<Gallery_Image, Long> {
    Set<Gallery_Image> findGallery_ImageById(Long id);
}
