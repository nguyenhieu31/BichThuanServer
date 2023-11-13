package com.shopproject.shopbt.service.color;

import com.shopproject.shopbt.dto.ColorsDTO;
import com.shopproject.shopbt.entity.Color;
import com.shopproject.shopbt.repository.color.ColorRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ColorServiceImpl implements ColorService{
    private ColorRepository colorRepository;
    private ModelMapper modelMapper;

    @Override
    public void create_Color(ColorsDTO colorsDTO) {
        colorRepository.save(modelMapper.map(colorsDTO, Color.class));
    }

    @Override
    public ColorsDTO findColorById(Long id) {
        return modelMapper.map(colorRepository.findById(id).get(), ColorsDTO.class);
    }
}
