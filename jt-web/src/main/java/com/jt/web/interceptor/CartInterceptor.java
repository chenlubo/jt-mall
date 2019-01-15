package com.jt.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.util.CookieUtils;
import com.jt.web.pojo.User;
import com.jt.web.threadlocal.UserThreadlocal;

//购物车拦截器，拦截请求获取userId
public class CartInterceptor implements HandlerInterceptor{
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override	//在拦截之前执行
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1.从cookie中获取ticket
		String ticket = CookieUtils.getCookieValue(request, "JT_TICKET");
		if(StringUtils.isNotEmpty(ticket)){
			//2.访问sso查询
			String url = "http://sso.jt.com/user/query/"+ticket;
			String jsonData = httpClientService.doGet(url);
			if(StringUtils.isNotEmpty(jsonData)){
				//3.user.json，解析出userId
				JsonNode jsonNode = MAPPER.readTree(jsonData);
				String userJson = jsonNode.get("data").asText();
				User curUser = MAPPER.readValue(userJson, User.class);
				UserThreadlocal.set(curUser);
				
				return true;	//放行true，不放行false
			}
		}
		
		//转向登录的页面 http://www.jt.com/user/login.html
		response.sendRedirect("/user/login.html");
		return false;
	}

	@Override	//在拦截之后执行
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override	//在jsp页面渲染之前
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
