package com.shopproject.shopbt.repository.Address;

import com.shopproject.shopbt.entity.Address;
import com.shopproject.shopbt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address,Long> {
    @Query("select a from Address a where a.id=:addressId")
    Optional<Address> findAddressByAddressId(@Param("addressId") Long id);
    @Query("select a from Address a where lower(a.address) like lower(concat('%', :address ,'%')) and a.user.userid=:userId")
    Optional<Address> findAddressByAddressName(@Param("address") String address,@Param("userId") Long userId);
    List<Address> findByUser(User user);
    @Query("select a from Address a where a.phonePayment=:phonePayment")
    Optional<Address> findAddressByPhonePayment(@Param("phonePayment") String phonePayment);
}
