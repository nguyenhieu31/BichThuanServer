package com.shopproject.shopbt.service.gallery_image;

import com.shopproject.shopbt.dto.GalleryImagesDTO;
import com.shopproject.shopbt.entity.Gallery_Image;
import com.shopproject.shopbt.entity.Product;
import com.shopproject.shopbt.repository.gallery_image.Gallery_ImageRepository;
import com.shopproject.shopbt.repository.product.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Gallery_ImageServiceImpl implements Gallery_ImageService{
    private Gallery_ImageRepository galleryImageRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Override
    public void create_Gallery_Image(GalleryImagesDTO galleryImagesDTO) {
        Gallery_Image galleryImage = modelMapper.map(galleryImagesDTO, Gallery_Image.class);
        Product product = productRepository.findById(galleryImagesDTO.getProductId()).get();
        galleryImage.setProduct(product);

        galleryImageRepository.save(galleryImage);
    }

    @Override
    public GalleryImagesDTO findGallery_ImageById(Long id) {
        Gallery_Image galleryImage = galleryImageRepository.findById(id).get();
        GalleryImagesDTO galleryImagesDTO = modelMapper.map(galleryImage, GalleryImagesDTO.class);
        return galleryImagesDTO;
    }

    @Override
    public void update_Gallery_Image(GalleryImagesDTO galleryImagesDTO) {
        Gallery_Image update_galleryImage = galleryImageRepository.findById(galleryImagesDTO.getId()).get();
        Product update_product = productRepository.findById(galleryImagesDTO.getProductId()).get();
        update_galleryImage.setProduct(update_product);
//        update_galleryImage.setImage(galleryImagesDTO.getImage());

        galleryImageRepository.save(update_galleryImage);
    }

    @Override
    public void delete_Gallery_ImageById(Long id) {
        galleryImageRepository.deleteById(id);
    }
}
