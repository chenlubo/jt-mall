package com.jt.dubbo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jt.cart.service.CartService;
import com.jt.dubbo.pojo.Cart;

public class CartServiceImpl implements DubboCartService{
	@Autowired
	private CartService cartService;

	public List<Cart> myCart(Long userId) {
		return cartService.myCart(userId);
	}

	public void saveCart(Cart cart) {
		cartService.saveCart(cart);
	}

	public void updateNum(Cart cart) {
		cartService.updateNum(cart);
	}

	public void deleteCart(Cart cart) {
		cartService.deleteCart(cart);
	}


}
