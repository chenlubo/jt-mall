package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.pojo.ItemCat;
import com.jt.manage.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	//查询所有 http://localhost:8081/itemcat/queryAll
	@RequestMapping("queryAll")
	@ResponseBody	//返回json
	public List<ItemCat> queryAll(){
		return itemCatService.queryAll();
	}

	@RequestMapping("/list")
	@ResponseBody		//设置默认值，根节点父节点id=0
	public List<ItemCat> list(@RequestParam(defaultValue="0")Long id){
		return itemCatService.list(id);
	}
}
