package com.jt.manage.service;

import org.springframework.stereotype.Service;

import com.jt.common.spring.exetend.PropertyConfig;

@Service
public class PropertyService {
	//springmvc是子容器，引用父容器spring，部分资源是可以，部分资源是不行。，下面不行
	@PropertyConfig
	public String REPOSITORY_PATH;
	@PropertyConfig
	public String IMAGE_BASE_URL;
	
}
