package com.ecom.service_impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.ecom.IService.CartService;
import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService
{
	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	
	@Override
	public Cart saveCart(Integer pid, Integer uid) 
	{
		UserDtls user = userRepo.findById(uid).get();
		Product product = productRepo.findById(pid).get();
		
		Cart cartStatus = cartRepo.findByProductIdAndUserId(pid, uid);
		
		Cart cart=null;
		
		if(ObjectUtils.isEmpty(cartStatus))
		{
			cart=new Cart();
			System.out.println("CartServiceImpl.saveCart() true");
			
			cart.setProduct(product);
			cart.setUser(user);
			cart.setQuantity(1);
			cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getPrice());
			
		}
		else
		{
			System.out.println("CartServiceImpl.saveCart() false");
			cart=cartStatus;
			cart.setQuantity(cart.getQuantity()+1);
			cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getPrice());
		}
		
		return cartRepo.save(cart);
	}

	@Override
	public List<Cart> getCartsByUser(Integer uid) 
	{
		List<Cart> carts = cartRepo.findByUserId(uid);

		Double totalOrderPrice = 0.0;
		
		List<Cart> updateCarts = new ArrayList<>();
		
		for (Cart c : carts) {
			Double totalPrice = (c.getProduct().getPrice() * c.getQuantity());
			c.setTotalPrice(totalPrice);
			totalOrderPrice = totalOrderPrice + totalPrice;
			c.setTotalAmount(totalOrderPrice);
			updateCarts.add(c);
		}
		
		return updateCarts;
	}

	@Override
	public int getCountCart(Integer uid) 
	{
		Integer countByUserId = cartRepo.countByUserId(uid);
		return countByUserId;
	}

	@Override
	@Transactional
	public void updateCartQuantity(String sy, Integer cid)
	{
		Cart cart = cartRepo.findById(cid).get();
		
		Integer updateQuantity=0;
		
		if (sy.equalsIgnoreCase("de"))
		{
			updateQuantity=cart.getQuantity()-1;
			
			if(updateQuantity <= 0)
			{
				System.out.println("CartServiceImpl.updateCartQuantity()");
				cartRepo.delete(cart);
				return;  
			}
		} 
		else 
		{
			updateQuantity = cart.getQuantity()+1;
		}
		
		cart.setQuantity(updateQuantity);
		cartRepo.save(cart);
	}

	@Override
	public void deleteCartById(Integer cid) 
	{
		Cart cart = cartRepo.findById(cid).get();
		
		if(!ObjectUtils.isEmpty(cart))
		{
			cartRepo.delete(cart);
		}
		
	}
}
