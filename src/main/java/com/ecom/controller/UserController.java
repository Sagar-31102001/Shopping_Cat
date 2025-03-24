package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.service_impl.CartServiceImpl;
import com.ecom.service_impl.ProductServiceImpl;
import com.ecom.service_impl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController
{
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private CartServiceImpl cartServiceImpl;
	
	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@GetMapping("/")
	public String home()
	{
		return "redirect:/products";
	}
	
	@ModelAttribute
	public void getUserDetails(Principal principal,Model m)
	{
		if(principal!=null)
		{
			String email = principal.getName();
			
			UserDtls user = userService.getUserByEmail(email);
			
			m.addAttribute("user", user);
			
			int countCart = cartServiceImpl.getCountCart(user.getId());
			m.addAttribute("count", countCart);
		}
	}
	
	
	@GetMapping("/addCart")
	public String addToCart(@RequestParam Integer pid,@RequestParam Integer uid,HttpSession session)
	{
		System.out.println("UserController.addToCart()");
		
		Cart cart = cartServiceImpl.saveCart(pid, uid);
		
		if(ObjectUtils.isEmpty(cart))
		{
			session.setAttribute("errorMsg", "Product Add to Cart Faild");
		}
		else
		{
			session.setAttribute("succMsg", "Product Add to Cart Success");
		}
		
		return "redirect:/viewproduct/"+pid;
	}

	
	@GetMapping("/cart")
	public String loadCartPage(Principal p, Model m)
	{
		UserDtls user= getLoginUserDetails(p);
		List<Cart> carts = cartServiceImpl.getCartsByUser(user.getId());
		
		
		if (!carts.isEmpty()) 
		{

			Double totalAmount = carts.get(carts.size()-1).getTotalAmount();
			
			m.addAttribute("carts",carts);
			m.addAttribute("totalAmount", totalAmount);
			return "/user/cart";
		} 
		else 
		{
			return "/user/empty";
		}
		
		
	}

	private UserDtls getLoginUserDetails(Principal p) {
		String name = p.getName();
		UserDtls userDtls = userService.getUserByEmail(name);
		return userDtls;
	}
	
	// Update Cart Quantity
	@GetMapping("/cartQuantityUpdate")
	public String updateCartQuantity(@RequestParam String sy,@RequestParam Integer cid)
	{
		cartServiceImpl.updateCartQuantity(sy,cid);
		return "redirect:/user/cart";
	}
	
	// Remove The Cart Item
	@GetMapping("/deleteCart")
	public String removeCart(@RequestParam Integer cid)
	{
		cartServiceImpl.deleteCartById(cid);
		return "redirect:/user/cart";
	}
	
	// order place
	@GetMapping("/orderPlace")
	public String orderPlace(@RequestParam Integer pid,Model m)
	{
		Product product = productServiceImpl.getProductById(pid);
		
		System.out.println(product);
		
		m.addAttribute("product", product);
		return "/user/order";
	}
}
