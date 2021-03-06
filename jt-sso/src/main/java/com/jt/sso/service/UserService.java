package com.jt.sso.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.BaseService;
import com.jt.common.service.RedisService;
import com.jt.sso.mapper.UserMapper;
import com.jt.sso.pojo.User;
import redis.clients.jedis.JedisCluster;

@Service
public class UserService extends BaseService<User>{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RedisService redisService;
	@Autowired
	public JedisCluster jedisCluster;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//用户监测
	public Boolean check(String val, Integer typeNum){
		Map<String,Object> params = new HashMap<String,Object>();
		if(1==typeNum){
			params.put("colname", "username");
		}else if(2==typeNum){
			params.put("colname", "phone");
		}else{
			params.put("colname", "email");
		}
		params.put("val", val);
		
		Integer i = userMapper.check(params);
		if(0==i){	//不存在
			return false;
		}else{
			return true;
		}
	}
	
	//注册
	public String saveRegister(User user){
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		//因为页面上不填写，随便写的值，防止数据库唯一校验出错
		user.setEmail("temp_"+user.getPhone());
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		
		userMapper.insertSelective(user);
		return user.getUsername();
	}
	
	//登录
	public String saveLogin(String username, String passwd) throws JsonProcessingException{
		//1.根据用户名查询，和密码进行比较（规范）
		String ticket = "";
		User params = new User();
		params.setUsername(username);
		
		User curUser = super.queryByWhere(params);
		if(null!=curUser){
			//2.密码进行比较
			String newPasswd = DigestUtils.md5Hex(passwd);
			if(newPasswd.equals(curUser.getPassword())){
				//3.生成ticket：唯一性、动态性、混淆
				ticket = DigestUtils.md2Hex(System.currentTimeMillis()+curUser.getUsername() + curUser.getId());
				//4.把当前用户信息放入redis，设置一个定期自动删除，设置过期：7天，10天
				jedisCluster.set(ticket, MAPPER.writeValueAsString(curUser));	//60*60*24*7可以，jvm编译时值被算出来了
			}
		}
		return ticket;
	}
}
