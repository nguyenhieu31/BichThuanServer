package com.shopproject.shopbt.repository.size;

import com.shopproject.shopbt.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Stream;


public interface SizeRepository extends JpaRepository<Size, Long> {
    Size findBySizeId(Long id);
}
