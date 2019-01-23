package com.jt.cart.mapper;

import com.jt.common.mapper.SysMapper;
import com.jt.dubbo.pojo.Cart;

public interface CartMapper extends SysMapper<Cart>{
	public void updateNum(Cart cart);
}
