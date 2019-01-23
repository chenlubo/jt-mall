package com.jt.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.order.mapper.OrderMapper;
import com.jt.order.pojo.Order;

@Service
public class OrderService {
	@Autowired
	private OrderMapper orderMapper;
	
	public Order queryById(String orderId){
		return orderMapper.queryById(orderId);
	}
	
	public String create(Order order){
		String orderId = order.getUserId() +""+ System.currentTimeMillis();
		order.setOrderId(orderId);
		
		orderMapper.create(order);
		return orderId;
	}
}
