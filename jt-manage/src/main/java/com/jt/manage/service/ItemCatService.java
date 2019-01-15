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
        ItemCat params = new ItemCat();	//where条件传递
        params.setStatus(1);		//1正常2删除
        List<ItemCat> itemCatList = super.queryListByWhere(params);

        //获取每个节点下的所有的子节点	<Long,List<ItemCat>> id，当前节点下的所有数据
        Map<Long,List<ItemCat>> map = new HashMap<Long,List<ItemCat>>();
        for(ItemCat cat : itemCatList){
            //map中还为构建这个节点
            if(!map.containsKey(cat.getParentId())){
                //当这个节点还不存在时，创建空的ArrayList
                map.put(cat.getParentId(), new ArrayList<ItemCat>());
            }
            map.get(cat.getParentId()).add(cat);
        }

        //组织ItemCatResult的结构
        ItemCatResult result = new ItemCatResult();
        //遍历一级菜单
        for(ItemCat cat1 : map.get(0L)){
            ItemCatData d1 = new ItemCatData();	//一级菜单
            String url = "/products/" + cat1.getId() + ".html";
            d1.setUrl(url);
            d1.setName("<a href=\""+url+"\">" + cat1.getName() + "</a>");

            List<ItemCatData> list1 = new ArrayList<ItemCatData>();
            //遍历二级菜单
            for(ItemCat cat2 : map.get(cat1.getId())){
                ItemCatData d2 = new ItemCatData();	//二级菜单
                d2.setUrl("/products/"+ cat2.getId() + ".html");
                d2.setName(cat2.getName());

                List<String> list2 = new ArrayList<String>();
                //遍历三级菜单
                for(ItemCat cat3 : map.get(cat2.getId())){
                    list2.add("/products/"+cat3.getId()+".html|"+cat3.getName());
                }
                d2.setItems(list2);

                list1.add(d2);
            }

            d1.setItems(list1);

            if(result.getItemCats().size()>14){
                break;	//如果一级菜单超过14个就退出
            }
            //遍历完一条一级菜单，就把它放入list集合
            result.getItemCats().add(d1);
        }
        return result;
    }

}

