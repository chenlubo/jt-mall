package com.jt.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.common.spring.exetend.PropertyConfig;

@Controller
@RequestMapping("/page")
public class PageController {
	//通用转向页面，任何一个jsp
	@RequestMapping("/{pageName}")	//注解必须加，因为RESTFul+post同时调用
	public String goHome(@PathVariable String pageName){
		return pageName;
	}
}
