package com.shopproject.shopbt.gallery_image;

import com.shopproject.shopbt.dto.GalleryImagesDTO;
import com.shopproject.shopbt.service.gallery_image.Gallery_ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest
public class TestGallery_Image {
    @Autowired
    private Gallery_ImageService galleryImageService;

    @Test
    void create() throws IOException {
        GalleryImagesDTO galleryImagesDTO = new GalleryImagesDTO();
        MultipartFile file = new MockMultipartFile("1.jpg", new FileInputStream(new File("D:\\dev-spring-boot\\image\\1.jpg")));
        galleryImagesDTO.setImage(file.getBytes());
        galleryImagesDTO.setProductId(4L);

        galleryImageService.create_Gallery_Image(galleryImagesDTO);
    }

    @Test
    void findById(){
        Long id = 1L;
        GalleryImagesDTO galleryImagesDTO = galleryImageService.findGallery_ImageById(id);
        System.out.println(galleryImagesDTO.getId());
        System.out.println(galleryImagesDTO.getProductId());
    }

    @Test
    void update() throws IOException {
        Long id = 1L;
        GalleryImagesDTO galleryImagesDTO = galleryImageService.findGallery_ImageById(id);
        MultipartFile file = new MockMultipartFile("3.jpg", new FileInputStream(new File("D:\\dev-spring-boot\\image\\3.jpg")));
        galleryImagesDTO.setImage(file.getBytes());
        galleryImagesDTO.setProductId(3L);

        galleryImageService.update_Gallery_Image(galleryImagesDTO);
    }
}
