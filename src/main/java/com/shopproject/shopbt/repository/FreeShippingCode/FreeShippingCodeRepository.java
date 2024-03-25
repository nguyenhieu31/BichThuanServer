package com.shopproject.shopbt.repository.FreeShippingCode;

import com.shopproject.shopbt.entity.FreeShippingCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeShippingCodeRepository extends JpaRepository<FreeShippingCode,Long> {
}
