package com.shopproject.shopbt.repository.Manager;

import com.shopproject.shopbt.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepo extends JpaRepository<Manager, Long> {
    Optional<Manager> findByManagerName(String email);
}
