package com.jt.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
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

    //查询商品分类为前台实现，3级分类菜单结构
    public ItemCatResult getItemCatList(){
        ItemCatResult result = new ItemCatResult();	//声明存储的对象
        List<ItemCat> cats = super.queryAll();		//查询所有3级菜单

        //获取当前菜单下的所有的子菜单，形成一个数组
        Map<Long,List<ItemCat>> map = new HashMap<Long,List<ItemCat>>();
        for(ItemCat itemCat: cats){
            if(!map.containsKey(itemCat.getParentId())){
                //创建一个元素，元素内容
                map.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            map.get(itemCat.getParentId()).add(itemCat);
        }

        //构建3级菜单结构
        List<ItemCatData> list1 = new ArrayList<ItemCatData>();
        //为一级菜单构建它的所有子菜单
        for(ItemCat itemCat1 : map.get(0L)){		//遍历一级菜单
            ItemCatData data1 = new ItemCatData();
            data1.setUrl("/products/"+itemCat1.getId()+".html");
            data1.setName("<a href='/products/"+itemCat1.getId()+".html'>"+itemCat1.getName()+"</a>");

            //遍历二级菜单
            List<ItemCatData> list2 = new ArrayList<ItemCatData>();
            for(ItemCat itemCat2: map.get(itemCat1.getId())){
                ItemCatData data2 = new ItemCatData();
                data2.setUrl("/products/"+itemCat2.getId()+".html");
                data2.setName(itemCat2.getName());

                //遍历三级菜单
                //三级菜单只是一个字符串，和一级、二级结构不同
                List<String> list3 = new ArrayList<String>();
                for(ItemCat itemCat3 : map.get(itemCat2.getId())){
                    list3.add("/products/"+itemCat3.getId()+".html|"+itemCat3.getName());
                }
                data2.setItems(list3);
                list2.add(data2);
            }
            data1.setItems(list2);
            list1.add(data1);

            //首页菜单要求只返回14条
            if(list1.size()>14){
                break;
            }
        }
        result.setItemCats(list1);
        return result;
    }

}

