package com.jt.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.cart.pojo.Cart;
import com.jt.cart.service.CartService;
import com.jt.common.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	
	//我的购物车 http://cart.jt.com/cart/query/{userId}
	@RequestMapping("/query/{userId}")
	@ResponseBody
	public SysResult myCart(@PathVariable Long userId){
		List<Cart> cartList = cartService.myCart(userId);
		return SysResult.oK(cartList);
	}
	
	//保存商品到购物车中
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveCart(Cart cart){
		cartService.saveCart(cart);
		return SysResult.oK();
	}
	
	//修改商品的数量
	@RequestMapping("/update/num/{userId}/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(Cart cart){
		cartService.updateNum(cart);
		return SysResult.oK();
	}
	
	//删除购物车中的商品
	@RequestMapping("/delete/{userId}/{itemId}")
	@ResponseBody
	public SysResult deleteCart(Cart cart){
		cartService.deleteByWhere(cart);
		return SysResult.oK();
	}
}
