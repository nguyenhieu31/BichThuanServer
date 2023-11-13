package com.shopproject.shopbt.service.size;

import com.shopproject.shopbt.dto.SizesDTO;

public interface SizeService {
    void create_Size(SizesDTO sizesDTO);

    SizesDTO findSizeById(Long id);
}
