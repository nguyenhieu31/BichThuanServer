package com.shopproject.shopbt.service.FreeShippingMember;


import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.FreeShippingMember.FreeShippingMemberRepository;
import com.shopproject.shopbt.response.FreeShippingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class FreeShippingMemberServiceImpl implements FreeShippingMemberService{
    private final FreeShippingMemberRepository freeShippingMemberRepository;
    @Override
    public List<FreeShippingResponse> getAllFreeShippingOfUser(User user) {
        List<FreeShippingResponse> responses= freeShippingMemberRepository.findByUser(user.getUserid(),false);
        List<FreeShippingResponse> voucherNotExpires= new ArrayList<>();
        for(FreeShippingResponse voucher: responses){
            if(voucher.getExpires().isAfter(LocalDateTime.now())){
                voucherNotExpires.add(voucher);
            }
        }
        return voucherNotExpires;
    }
}
