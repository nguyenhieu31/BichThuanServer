package com.shopproject.shopbt.service.size;

import com.shopproject.shopbt.dto.SizesDTO;
import com.shopproject.shopbt.entity.Size;
import com.shopproject.shopbt.repository.size.SizeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SizeServiceImpl implements SizeService{
    private SizeRepository sizeRepository;
    private ModelMapper modelMapper;

    @Override
    public void create_Size(SizesDTO sizesDTO) {
        sizeRepository.save(modelMapper.map(sizesDTO, Size.class));
    }

    @Override
    public SizesDTO findSizeById(Long id) {
        return modelMapper.map(sizeRepository.findById(id).get(), SizesDTO.class);
    }
}
