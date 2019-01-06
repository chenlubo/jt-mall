package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.util.CookieUtils;
import com.jt.web.pojo.User;

@Service
public class UserService{
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public String saveRegister(User user) throws Exception{
		
		String url = "http://sso.jt.com/user/register";
		//参数封装在一个map中，没有类型，都是字符串
		Map<String,String> params = new HashMap<String,String>();
		params.put("username", user.getUsername());
		params.put("password", user.getPassword());
		params.put("phone", user.getPhone());
		params.put("email", user.getEmail());
		
		//jsonData代表SysResult，不行，SysResult写不标准，ObjectMapper不能转回SysResult
		String jsonData = httpClientService.doPost(url, params);
		JsonNode jsonNode = MAPPER.readTree(jsonData);
		String username = jsonNode.get("data").asText();	//从SysResult对象中只获取data属性值
		return username;
	}
	
	public String saveLogin(String username, String password) throws Exception{
		String ticket = null;
		String url = "http://sso.jt.com/user/login";
		Map<String,String> params = new HashMap<String,String>();
		params.put("u", username);
		params.put("p", password);
		
		String jsonData = httpClientService.doPost(url, params);
		try{
			ticket = MAPPER.readTree(jsonData).get("data").asText();
		}catch(Exception e){
			//todo写日志
		}
		
		return ticket;
	}
}
