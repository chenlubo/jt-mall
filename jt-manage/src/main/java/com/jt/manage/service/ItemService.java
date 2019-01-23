package com.jt.manage.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.service.BaseService;
import com.jt.common.service.RedisService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

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
	private RedisService redisService;
	@Autowired
	private RabbitTemplate rabbitTemplate;	//spring创建

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

		rabbitTemplate.convertAndSend("item_insert", item.getId());

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

		//删除缓存，还要修改rabbitmq
		//redisService.del("ITEM_"+item.getId());

		String routingKey = "item.update";
		rabbitTemplate.convertAndSend(routingKey, "ITEM_"+item.getId());
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
		return itemDescMapper.selectByPrimaryKey(itemId);
	}

}
