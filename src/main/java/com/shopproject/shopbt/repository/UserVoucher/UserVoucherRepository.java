package com.shopproject.shopbt.repository.UserVoucher;

import com.shopproject.shopbt.entity.User_Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoucherRepository extends JpaRepository<User_Voucher,Long> {
}
