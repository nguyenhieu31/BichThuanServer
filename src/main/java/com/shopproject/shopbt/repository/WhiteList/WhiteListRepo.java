package com.shopproject.shopbt.repository.WhiteList;

import com.shopproject.shopbt.entity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface WhiteListRepo extends JpaRepository<WhiteList, Long> {
    Long deleteByToken(String token);
}
