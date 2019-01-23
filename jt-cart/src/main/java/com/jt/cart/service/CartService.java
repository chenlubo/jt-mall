package com.jt.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.common.service.BaseService;
import com.jt.dubbo.pojo.Cart;

@Service
public class CartService extends BaseService<Cart>{
	@Autowired
	private CartMapper cartMapper;
	
	//我的购物车
	public List<Cart> myCart(Long userId){
		Cart params = new Cart();
		params.setUserId(userId);
		
		return cartMapper.select(params);
	}
	
	//保存商品到购物车
	public void saveCart(Cart cart){
		//判断此用户的此商品是否存在？
		Cart params = new Cart();
		params.setUserId(cart.getUserId());
		params.setItemId(cart.getItemId());
		
		Cart oldCart = super.queryByWhere(params);
		
		if(null == oldCart){		//此用户的此商品不存在
			//新增
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insertSelective(cart);
		}else{
			//修改数量=页面提交的数量+旧的数量
			cart.setId(oldCart.getId());
			cart.setNum(cart.getNum() + oldCart.getNum());
			cartMapper.updateByPrimaryKeySelective(cart);
		}
	}
	
	//修改商品数量=页面的数量
	public void updateNum(Cart cart){
		cartMapper.updateNum(cart);
	}
	
	public void deleteCart(Cart cart){
		super.deleteByWhere(cart);
	}
}
