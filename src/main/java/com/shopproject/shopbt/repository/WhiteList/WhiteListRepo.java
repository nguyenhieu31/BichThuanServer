package com.shopproject.shopbt.repository.WhiteList;

import com.shopproject.shopbt.entity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WhiteListRepo extends JpaRepository<WhiteList, Long> {
    Optional<WhiteList> findByToken(String token);
    Optional<WhiteList> findByUserId(String userId);
    Long deleteByToken(String token);
}
