package com.jt.order.mapper;

import java.util.Date;

import com.jt.order.pojo.Order;

public interface OrderMapper {
	public Order queryById(String orderId);
	public void create(Order order);
	
	public void paymentOrderScan(Date date);
}
