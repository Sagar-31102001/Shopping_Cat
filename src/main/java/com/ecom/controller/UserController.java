package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.model.Cart;
import com.ecom.model.OrderRequest;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service_impl.CartServiceImpl;
import com.ecom.service_impl.OrderServiceImpl;
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
	
	@Autowired
	private OrderServiceImpl ordServiceImpl;
	
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
	
	@GetMapping("/orders")
	public String orderPage(Principal p , Model m)
	{
		UserDtls user= getLoginUserDetails(p);
		List<Cart> carts = cartServiceImpl.getCartsByUser(user.getId());
		
		
		if (!carts.isEmpty()) 
		{

			Double totalAmount = carts.get(carts.size()-1).getTotalAmount();
			Double withTaxPrice=totalAmount+250+100;
			m.addAttribute("carts",carts);
			m.addAttribute("totalAmount", totalAmount);
			m.addAttribute("withTaxPrice", withTaxPrice);
			return "/user/order";
		} 
		else 
		{
			return "/user/empty";
		}
		
	}
	
	@PostMapping("/save-order")
	public String saveOrder(@ModelAttribute OrderRequest orderRequest , Principal p)
	{
		System.out.println(orderRequest);
		UserDtls user = getLoginUserDetails(p);
		
		ordServiceImpl.saveOrder(user.getId(), orderRequest);
		return "redirect:/user/success";
	}
	
	@GetMapping("/success")
	public String loadSuccess() {
		return "/user/success";
	}
	
	@GetMapping("/user-orders")
	public String myOrder(Model m, Principal p) {
		UserDtls loginUser = getLoginUserDetails(p);
		List<ProductOrder> orders = ordServiceImpl.getOrdersByUser(loginUser.getId());
		m.addAttribute("orders", orders);
		
		return "/user/my_orders";
	}
	
	// For Specific Product Order
	@GetMapping("/specific_order")
	public String specificOrder(@RequestParam Integer pid,Model m,Principal p)
	{
		Product product = productServiceImpl.getProductById(pid);
		System.out.println(product);
		
		m.addAttribute("price", product.getPrice());
		Double withTexPrice=product.getPrice()+250+100;
		m.addAttribute("withTexPrice", withTexPrice);
		m.addAttribute("product", product);
		
		// User Details
		UserDtls user = getLoginUserDetails(p);
		m.addAttribute("user",user);
		
		return "/user/specific_order";
	}
	
	@PostMapping("/specific-save-order")
	public String specificSaveOrder(@ModelAttribute OrderRequest orderRequest,@RequestParam Integer id,Principal p)
	{
		Product product = productServiceImpl.getProductById(id);
		UserDtls user = getLoginUserDetails(p);
		System.out.println("-------------------------");
		System.out.println(product);
		System.out.println(user);
		
		
		ordServiceImpl.specificUser(user.getId(),orderRequest, product);
		
		return "redirect:/user/success";
	}
	
	// Order Cancellation
	@GetMapping("/cancel")
	public String cancellProductOrder(@RequestParam Integer oid,HttpSession session)
	{
		boolean cancellOrder = ordServiceImpl.cancellOrder(oid);
		
		if (cancellOrder) 
		{
			session.setAttribute("succMsg", "Order Successfully Cancelled "+oid);
			
			return "redirect:/user/user-orders";
		}
		else 
		{
			session.setAttribute("errorMsg", "Order Cancellation Faild...!!!");
			
			return "redirect:/user/user-orders";
		}
		
	}
	
}
