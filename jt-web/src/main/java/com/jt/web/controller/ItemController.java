package com.jt.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemDesc;
import com.jt.web.service.ItemService;

@Controller
@RequestMapping("/items")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	//根据商品id获取商品信息	http://www.jt.com/items/562379.html
	@RequestMapping("/{itemId}")
	public String getItem(@PathVariable Long itemId, Model model) throws Exception{
		Item item = itemService.getItemById(itemId);
		model.addAttribute("item", item);
		
		ItemDesc itemDesc = itemService.getItemDescById(itemId);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
