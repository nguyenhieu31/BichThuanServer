package com.shopproject.shopbt.repository.color;

import com.shopproject.shopbt.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.stream.Stream;

public interface ColorRepository extends JpaRepository<Color, Long> {
    Color findByColorId(Integer id);
}
