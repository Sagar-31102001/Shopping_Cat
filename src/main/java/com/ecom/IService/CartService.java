package com.ecom.IService;

import java.util.List;

import com.ecom.model.Cart;

public interface CartService 
{
	public Cart saveCart(Integer pid,Integer uid);
	
	public List<Cart> getCartsByUser(Integer uid);
	
	public abstract int getCountCart(Integer uid);
	
	// Cart Quantity Update
	public abstract void updateCartQuantity(String sy,Integer cid);
	
	// Remove Cart Items
	public abstract void deleteCartById(Integer cid);
	
	
}
