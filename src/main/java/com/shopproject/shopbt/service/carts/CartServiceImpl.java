package com.shopproject.shopbt.service.carts;

import com.shopproject.shopbt.dto.CartsDTO;
import com.shopproject.shopbt.entity.Cart;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.carts.CartRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Override
    public void create_Cart(CartsDTO cartsDTO) {
        Cart cart = modelMapper.map(cartsDTO, Cart.class);

        cartRepository.save(cart);
    }

    @Override
    public CartsDTO findCartById(Long id) {
        return modelMapper.map(cartRepository.findById(id).get(), CartsDTO.class);
    }

    @Override
    public void update_Cart(CartsDTO cartsDTO) {
        Cart cart = modelMapper.map(cartsDTO, Cart.class);
        User user = userRepository.findById(cartsDTO.getUserId()).get();
        cart.setUser(user);

        cartRepository.save(cart);
    }

    @Override
    public void delete_CartById(Long id) {
        cartRepository.deleteById(id);
    }
}
