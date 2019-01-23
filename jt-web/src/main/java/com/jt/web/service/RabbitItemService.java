package com.jt.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.service.RedisService;

@Service
public class RabbitItemService {
	@Autowired 
	private RedisService redisService;
	
	//消息就传递给方法的参数，自动转类型
	public void updateItem(String itemKey){
//		redisService.del(itemKey);
	}
}
