package com.jt.order.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.SysResult;
import com.jt.order.pojo.Order;
import com.jt.order.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//创建订单 http://order.jt.com/order/create
	@RequestMapping("/create")
	@ResponseBody
	public String create(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException{
		Order order = MAPPER.readValue(json, Order.class);
		
		String orderId = orderService.create(order);
		return orderId;
	}
	
	//按订单id来查询订单 http://order.jt.com/order/query/71487577654381
	@RequestMapping("/query/{orderId}")
	@ResponseBody
	public Order queryById(@PathVariable String orderId){
		return orderService.queryById(orderId);
	}

}
