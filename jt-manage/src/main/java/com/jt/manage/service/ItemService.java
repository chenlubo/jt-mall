package com.jt.manage.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.service.BaseService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import redis.clients.jedis.JedisCluster;

@Service
public class ItemService extends BaseService<Item>{
	@PropertyConfig
	private String REPOSITORY_PATH;
	@PropertyConfig
	private String IMAGE_BASE_URL;
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
    @Autowired
    public JedisCluster jedisCluster;

    public String ITEM_KEY = "ITEM_";
    public String ITEM_DESC_KEY = "ITEM_DESC_";
    private static final ObjectMapper MAPPER = new ObjectMapper();


    //查询商品列表，按修改时间倒叙
	public EasyUIResult queryItemList(Integer pageNum, Integer pageSize){
		//标识分页开始， mybatis规则：拦截器只拦截下面第一条查询的SQL语句
		PageHelper.startPage(pageNum, pageSize);	
		List<Item> itemList = itemMapper.queryItemList();
		//List<Item> itemList1 = itemMapper.queryItemList();
		//封装当前页和记录总数对象
		PageInfo<Item> pageInfo = new PageInfo<Item>(itemList);
		
		return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
	}
	
	//新增商品，商品描述
	public void saveItem(Item item, String desc){
		item.setStatus(1);		//1正常，2删除
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		
		itemMapper.insertSelective(item);
		
		//新增商品详情
		ItemDesc itemDesc = new ItemDesc();
		//有值，mybatis+mysql（函数
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		
		itemDescMapper.insertSelective(itemDesc);
	}
	
	//修改商品
	public void updateItem(Item item, String desc){
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(item.getUpdated());
		
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
	}
	
	//批量删除
	public void deleteItem(Long[] ids){
		itemDescMapper.deleteByIDS(ids);	//主外键一致
		itemMapper.deleteByIDS(ids);
	}
	
	//修改状态
	public void updateStatus(Integer val, Long[] ids){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", val);
		params.put("ids", ids);
		
		itemMapper.updateStatus(params);
	}
	
	//根据ItemId获取ItemDesc对象，把外键当做主键
	public ItemDesc getItemDescByItemId(Long itemId){
        //增加缓存
        try{
            String jsonData = jedisCluster.get(ITEM_DESC_KEY+itemId);
            if(StringUtils.isNotEmpty(jsonData)){
                ItemDesc desc = MAPPER.readValue(jsonData, ItemDesc.class);
                return desc;
            }
        }catch(Exception e){
            e.printStackTrace();
	   	}

        ItemDesc desc = itemDescMapper.selectByPrimaryKey(itemId);

        //写缓存
        try{
            String jsonData = MAPPER.writeValueAsString(desc);
            jedisCluster.set(ITEM_DESC_KEY+itemId, jsonData);
        }catch(Exception e){
            e.printStackTrace();
        }

        return desc;


	}

    public Item getItemById(Long itemId) throws Exception{
        //增加缓存
        try{
            String jsonData = jedisCluster.get(ITEM_KEY+itemId);
            if(StringUtils.isNotEmpty(jsonData)){
                Item item = MAPPER.readValue(jsonData, Item.class);
                return item;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

//        //通过httpClient发起http请求，请求后台
//        String url = "http://manage.jt.com/web/item/"+itemId;
//        //注意有超时的问题，
//        String jsonData = httpClientService.doGet(url, "utf-8");
//        //把json串转成单个pojo对象
//        Item item = MAPPER.readValue(jsonData, Item.class);
        Item item = itemMapper.selectByPrimaryKey(itemId);

        //写缓存
//        try{
//            jedisCluster.set(ITEM_KEY+itemId, jsonData);
//        }catch(Exception e){
//            e.printStackTrace();
//        }

        return item;
    }


}
