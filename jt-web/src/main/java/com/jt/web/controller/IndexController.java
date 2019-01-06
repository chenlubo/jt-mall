package com.jt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	//转向前台首页
	@RequestMapping("/index")
	public String index(){
		return "index";
	}
}
