package com.jt.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.service.BaseService;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat>{
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	//到后台查询商品分类，返回java对象，列表
	public List<ItemCat> list(Long id){
		ItemCat params = new ItemCat();
		params.setStatus(1);	//1正常2删除
		params.setParentId(id);
		
		List<ItemCat> itemCatList = itemCatMapper.select(params);
		return itemCatList;
	}
}
