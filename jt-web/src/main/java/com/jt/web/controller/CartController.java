package com.jt.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.dubbo.DubboCartService;
import com.jt.dubbo.pojo.Cart;
import com.jt.web.threadlocal.UserThreadlocal;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private DubboCartService dubboCartService;
	
	//我的购物车 /cart/show.html
	@RequestMapping("/show")
	public String show(HttpServletRequest request, Model model) throws Exception{
		//准备数据
		Long userId = UserThreadlocal.getUserId();
		List<Cart> cartList = dubboCartService.myCart(userId);
		model.addAttribute("cartList", cartList);
		
		return "cart";
	}
	
	//加入购物车
	@RequestMapping("/add/{itemId}")
	public String add(Cart cart) throws Exception{
		Long userId = UserThreadlocal.getUserId();
		cart.setUserId(userId);
		
		dubboCartService.saveCart(cart);
		
		return "redirect:/cart/show.html";	//和浏览器输入地址相同
	}
	
	//修改商品数量 /service/cart/update/num/1474391959/2
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public String updateNum(Cart cart) throws Exception{
		Long userId = UserThreadlocal.getUserId();
		cart.setUserId(userId);
		
		dubboCartService.updateNum(cart);
		return "";	//模拟返回json
	}
	
	//删除商品 /cart/delete/1474391954.html
	@RequestMapping("/delete/{itemId}")
	public String delete(Cart cart) throws Exception{
		Long userId = UserThreadlocal.getUserId();
		cart.setUserId(userId);
		
		dubboCartService.deleteCart(cart);
		
		return "redirect:/cart/show.html";
	}
}
