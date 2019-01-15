package com.jt.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.Order;

@Service
public class OrderService{
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//获取用户选择的商品的信息
	public List<Cart> getCartList(Long userId) throws Exception{
		String url = "http://cart.jt.com/cart/query/" + userId;
		String jsonData = httpClientService.doGet(url);
		JsonNode jsonNode = MAPPER.readTree(jsonData);
		JsonNode data = jsonNode.get("data");
		
		Object obj = null;
        if (data.isArray() && data.size() > 0) {
            obj = MAPPER.readValue(data.traverse(),
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        }
        
        return (List<Cart>)obj;
	}
	
	//创建订单
	public String createOrder(Order order) throws JsonProcessingException, Exception{
		//访问订单的业务接口，参数时json
		String url = "http://order.jt.com/order/create";
		String orderId = httpClientService.doPostJson(url, MAPPER.writeValueAsString(order));;
		return orderId;
	}
	
	//根据订单的id获取订单
	public Order getOrderById(String orderId) throws Exception{
		String url = "http://order.jt.com/order/query/"+orderId;
		String orderJson = httpClientService.doGet(url);
		return MAPPER.readValue(orderJson, Order.class);
	}
}
