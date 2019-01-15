package com.jt.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Cart;

@Service
public class CartService{
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public List<Cart> show(Long userId) throws Exception{
		//访问购物车的系统 
		String url = "http://cart.jt.com/cart/query/"+userId;
		String jsonData = httpClientService.doGet(url);
		JsonNode jsonNode = MAPPER.readTree(jsonData);
		JsonNode cartListNode = jsonNode.get("data");	//单独获取data的值
		
		Object obj = null;
        if (cartListNode.isArray() && cartListNode.size() > 0) {
            obj = MAPPER.readValue(cartListNode.traverse(),
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        }
        return (List<Cart>)obj;
	}
	
	public void saveCart(Cart cart) throws Exception{
		String url = "http://cart.jt.com/cart/save";
		Map<String,String> map = new HashMap<String,String>();
		map.put("userId", ""+cart.getUserId());
		map.put("itemId", ""+cart.getItemId());
		map.put("itemTitle", ""+cart.getItemTitle());
		map.put("itemImage", ""+cart.getItemImage());
		map.put("itemPrice", ""+cart.getItemPrice());
		map.put("num", ""+cart.getNum());
		
		httpClientService.doPost(url, map, "utf-8");
	}
	
	public void updateNum(Long userId, Long itemId, Integer num) throws Exception{
		String url = "http://cart.jt.com/cart/update/num/"+userId+"/"+itemId+"/"+num;
		httpClientService.doGet(url);
	}

	public void delete(Long userId, Long itemId) throws Exception {
		String url = "http://cart.jt.com/cart/delete/"+userId+"/"+itemId;
		httpClientService.doGet(url);
	}
}
