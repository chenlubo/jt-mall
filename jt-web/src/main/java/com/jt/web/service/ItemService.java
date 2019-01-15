package com.jt.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.service.RedisService;
import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemDesc;

@Service
public class ItemService {
	@Autowired
	private HttpClientService httpClientService;
	@Autowired
	private RedisService redisService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//发起请求访问后台系统
	public Item getItemById(Long itemId){
		try{
			//增加商品的缓存
			String ITEM_KEY = "ITEM_"+itemId;
			String jsonData = redisService.get(ITEM_KEY);
			if(StringUtils.isEmpty(jsonData)){
				//发起httpClient请求
				String url = "http://manage.jt.com/web/item/" + itemId;
				jsonData = httpClientService.doGet(url);
				
				//写缓存
				redisService.set(ITEM_KEY, jsonData);
			}
						
			Item item = MAPPER.readValue(jsonData, Item.class);
			return item;
		}catch(Exception e){
			//todo
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	//请求后台系统返回商品详情
	public ItemDesc getItemDescById(Long itemId) throws Exception{
		String url = "http://manage.jt.com/web/itemdesc/"+itemId;
		String jsonData = httpClientService.doGet(url);
		
		return MAPPER.readValue(jsonData, ItemDesc.class);
	}
}
