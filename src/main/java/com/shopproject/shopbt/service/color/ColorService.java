package com.shopproject.shopbt.service.color;

import com.shopproject.shopbt.dto.ColorsDTO;
import org.springframework.stereotype.Service;

public interface ColorService {
    void create_Color(ColorsDTO colorsDTO);

    ColorsDTO findColorById(Long id);
}
