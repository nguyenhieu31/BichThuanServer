package com.shopproject.shopbt.service.FreeShippingMember;

import com.shopproject.shopbt.entity.FreeShippingMember;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.response.FreeShippingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FreeShippingMemberService {
    List<FreeShippingResponse> getAllFreeShippingOfUser(User user);
}
