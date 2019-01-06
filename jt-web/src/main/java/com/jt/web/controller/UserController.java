package com.jt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	//转向注册页面
	@RequestMapping("/register")
	public String register(){
		return "register";
	}
	
	//注册
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult doRegister(User user) throws Exception{
		String username = userService.saveRegister(user);
		return SysResult.oK(username);
	}
	
	//转向登录页面
	@RequestMapping("/login")
	public String login(){
		return "login";
	}	
	
	//登录
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ticket = userService.saveLogin(username, password);
		String cookieName = "JT_TICKET";
		//ticket就放在本地的浏览器的cookie中，唯一标识这个用户，通过ticket就可以去redis获取自己的user的信息
		CookieUtils.setCookie(request, response, cookieName, ticket);
		return SysResult.oK();
	}
	
	//登出 http://www.jt.com/user/logout.html
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		//删除cookie
		CookieUtils.deleteCookie(request, response, "JT_TICKET");
		
		return "index";
	}
}
