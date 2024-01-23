package com.shopproject.shopbt.repository.FreeShippingMember;

import com.shopproject.shopbt.entity.FreeShippingMember;
import com.shopproject.shopbt.response.FreeShippingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreeShippingMemberRepository extends JpaRepository<FreeShippingMember, Long> {
    @Query("select new com.shopproject.shopbt.response.FreeShippingResponse(fs.freeShippingCode.freeShippingCodeId,fs.freeShippingCode.code, fs.freeShippingCode.discountPercent,fs.freeShippingCode.discountUnit,fs.freeShippingCode.expires,fs.isUsed) from FreeShippingMember fs " +
            "where fs.user.userid=:userId and fs.isUsed=:isUsed")
    List<FreeShippingResponse> findByUser(@Param("userId") Long userId, @Param("isUsed") boolean isUsed);
}
