package com.jt.manage.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jt.common.po.BasePojo;

@JsonIgnoreProperties(ignoreUnknown=true)	//遇到不认识的属性忽略
@Table(name="tb_item_cat")	//表和类的映射
public class ItemCat extends BasePojo{
	@Id	//主键
	@GeneratedValue(strategy=GenerationType.IDENTITY)	//自增
	private Long id;
	
	@Column(name="parent_id")	//忘掉，全局驼峰规则配置
	private Long parentId;
	
	private String name;
	private Integer status;
	private Integer sortOrder;
	private Boolean isParent;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	
	//为EasyUI.tree准备格式
	public String getText() {
		return this.name;
	}
	public String getState(){
		return this.isParent?"closed":"open";	//如果是树枝，默认关闭，支持异步加载
	}
}
