package com.jt.manage.pojo;

//封装图片上传结果，这样KindEditor才能进行处理
public class PicUploadResult {
	private Integer error=0;	//0正常1错误 
	private String height;		//类型必须字符串
	private String width;
	private String url;			//相对路径，访问图片链接
	public Integer getError() {
		return error;
	}
	public void setError(Integer error) {
		this.error = error;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
