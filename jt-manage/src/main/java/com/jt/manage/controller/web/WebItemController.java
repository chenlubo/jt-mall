package com.jt.manage.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;

@Controller
public class WebItemController {
	@Autowired
	private ItemService itemService;
	
	//根据itemId获取Item的信息
	@RequestMapping("/web/item/{itemId}")
	@ResponseBody
	public Item getItemById(@PathVariable Long itemId){
		return itemService.queryById(itemId);
	}
	
	//根据itemId获取ItemDesc的信息
	@RequestMapping("/web/itemdesc/{itemId}")
	@ResponseBody
	public ItemDesc getItemDescById(@PathVariable Long itemId){
		return itemService.getItemDescByItemId(itemId);
	}
}
