package com.shopproject.shopbt.repository.BlackList;

import com.shopproject.shopbt.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListRepo extends JpaRepository<BlackList,Long> {

    Optional<BlackList> findByToken(String token);
}
