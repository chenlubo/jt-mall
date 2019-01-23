package com.jt.dubbo;

import java.util.List;

import com.jt.dubbo.pojo.Cart;

public interface DubboCartService {
	public List<Cart> myCart(Long userId);
	public void saveCart(Cart cart);
	public void updateNum(Cart cart);
	public void deleteCart(Cart cart);
}
