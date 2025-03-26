package com.ecom.service_impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.IService.OrderService;
import com.ecom.model.Cart;
import com.ecom.model.OrderAddress;
import com.ecom.model.OrderRequest;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductOrderRepository;
import com.ecom.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService
{

	@Autowired
	private ProductOrderRepository prdOrderRepo;
	
	@Autowired
	private CartRepository cartRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public void saveOrder(Integer uid,OrderRequest orderRequest) 
	{
		List<Cart> carts = cartRepo.findByUserId(uid);
		
		for(Cart cart : carts)
		{
			ProductOrder order = new ProductOrder();

			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());

			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getPrice());

			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());
			order.setStatus("none");
			order.setPaymentType(orderRequest.getPaymentType());
			
			OrderAddress address = new OrderAddress();
			
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNo(orderRequest.getMobileNo());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());

			order.setOrderAddress(address);
			
			ProductOrder save = prdOrderRepo.save(order);
			System.out.println(save);
			
		}
	}

	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		List<ProductOrder> orders = prdOrderRepo.findByUserId(userId);
		return orders;
	}

	@Override
	public void specificUser(Integer uid, OrderRequest ordRequest,Product prd) 
	{
		UserDtls user = userRepo.findById(uid).get();
		
		ProductOrder order = new ProductOrder();

		order.setOrderId(UUID.randomUUID().toString());
		order.setOrderDate(LocalDate.now());
		
		order.setProduct(prd);
		order.setPrice(prd.getPrice());
		
		order.setQuantity(1);
		order.setUser(user);
		order.setStatus("none");
		order.setPaymentType(ordRequest.getPaymentType());
		
		OrderAddress address = new OrderAddress();
		
		address.setFirstName(ordRequest.getFirstName());
		address.setLastName(ordRequest.getLastName());
		address.setEmail(ordRequest.getEmail());
		address.setMobileNo(ordRequest.getMobileNo());
		address.setAddress(ordRequest.getAddress());
		address.setCity(ordRequest.getCity());
		address.setState(ordRequest.getState());
		address.setPincode(ordRequest.getPincode());

		order.setOrderAddress(address);
		
		
		ProductOrder save = prdOrderRepo.save(order);
		System.out.println(save);
		
	}

	@Override
	public boolean cancellOrder(Integer oid) 
	{
		ProductOrder productOrder = prdOrderRepo.findById(oid).get();
		
		if (!ObjectUtils.isEmpty(productOrder))
		{
			prdOrderRepo.delete(productOrder);
			return true;
		} 
		else 
		{
			return false;
		}
	}

}
