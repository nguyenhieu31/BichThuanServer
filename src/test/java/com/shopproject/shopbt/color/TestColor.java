package com.shopproject.shopbt.color;

import com.shopproject.shopbt.dto.ColorsDTO;
import com.shopproject.shopbt.service.color.ColorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestColor {
    @Autowired
    private ColorService colorService;

    @Test
    void create(){
//        ColorsDTO colorsDTO = new ColorsDTO();
//        colorsDTO.setName("Green");
//
//        colorService.create_Color(colorsDTO);
    }

    @Test
    void findById(){
//        Long id = 1L;
//        ColorsDTO colorsDTO = colorService.findColorById(id);
//
//        System.out.println(colorsDTO.getName());
    }
}
