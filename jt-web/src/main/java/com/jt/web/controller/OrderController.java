package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.Order;
import com.jt.web.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	//转向订单页面 /order/create.html
	@RequestMapping("/create")
	public String createOrder(Model model) throws Exception{
		//准备数据
		Long userId = 7L;
		List<Cart> carts = orderService.getCartList(userId);
		model.addAttribute("carts", carts);
		
		return "order-cart";
	}
	
	//提交表单 /order/submit
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult submit(Order order) throws JsonProcessingException, Exception{
		Long userId = 7L;
		order.setUserId(userId);
		
		String orderId = orderService.createOrder(order);
		return SysResult.oK(orderId);
	}
	
	//转向下单成功页面 http://www.jt.com/order/success.html?id=71498545769178
	@RequestMapping("/success")
	public String success(String id, Model model) throws Exception{
		//准备数据
		Order order = orderService.getOrderById(id);
		model.addAttribute("order", order);
		
		return "success";
	}
		
}
