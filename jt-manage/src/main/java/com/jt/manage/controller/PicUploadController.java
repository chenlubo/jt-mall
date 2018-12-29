package com.jt.manage.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.manage.pojo.PicUploadResult;
import com.jt.manage.service.PropertyService;

@Controller
public class PicUploadController {
	@Autowired
	private PropertyService propertyService;
	
	//文件上传，uploadFile名称初始化时定义，必须相同
	//通用文件上传，方便集成到其它项目
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PicUploadResult upload(MultipartFile uploadFile){
		PicUploadResult result = new PicUploadResult();
		
		//1.获取文件名称和扩展名
		String fileName = uploadFile.getOriginalFilename();
		String extName = fileName.substring(fileName.lastIndexOf("."));
		
		//2.判断文件后缀类型，正则表达式 abc.jpg,abc.png
		/*
		 * 正则：^正则表达式开始，. 匹配任何单个字符；?0到1个字符串匹配多个字符，*0到多个子串匹配，()分组，|或者，$正则表达式结束
		 * .*具有贪婪的性质，首先匹配到不能匹配为止，根据后面的正则表达式，会进行回溯。
		 * (?i)不区分大小写
		 * .*？则相反，一个匹配以后，就往下进行，所以不会进行回溯，具有最小匹配的性质。
		 */
		if(!fileName.matches("^(?i).*?\\.(jpg|png|gif|bmp)$")){
			result.setError(1);
		}else{
			//3.判断是否为木马文件，从包装的上传的文件获取流，强制转换成图片对象
			try {
				BufferedImage image = ImageIO.read(uploadFile.getInputStream());
				result.setHeight(""+image.getHeight());
				result.setWidth(""+image.getWidth());
				
				//4.生成两个路径：
				//绝对路径，图片保存的路径		c:/jt-upload/images/2017/06/12/239847293874.jpg
				//新文件名称：当前毫秒数+3位随机数与
				String newFileName = ""+System.currentTimeMillis() + RandomUtils.nextInt(100, 999) + extName;
				String _dir = "/images/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date())+"/";
				String path = propertyService.REPOSITORY_PATH + _dir;
				//相当路径，图片网上访问路径		http://image.jt.com/images/2017/06/12/239847293874.jpg
				String url = propertyService.IMAGE_BASE_URL + _dir + newFileName;
				result.setUrl(url);
				
				//5.如果目录不存在就生成
				File dir = new File(path); 
				if(!dir.exists()){	//目录不存在
					dir.mkdirs();	//创建多级目录
				}
				
				//6.保存文件
				uploadFile.transferTo(new File(path+newFileName));
			} catch (IOException e) {
				result.setError(1);		//不能抛异常，必须设置出错
				e.printStackTrace();
			}
		}
		return result;
	}
}
