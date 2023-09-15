package com.shopproject.shopbt.service.gallery_image;

import com.shopproject.shopbt.dto.GalleryImagesDTO;

public interface Gallery_ImageService {
    void create_Gallery_Image(GalleryImagesDTO galleryImagesDTO);

    GalleryImagesDTO findGallery_ImageById(Long id);

    void update_Gallery_Image(GalleryImagesDTO galleryImagesDTO);

    void delete_Gallery_ImageById(Long id);
}
