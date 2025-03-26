package com.ecom.IService;

import java.util.List;

import com.ecom.model.OrderRequest;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;

public interface OrderService 
{
	public void saveOrder(Integer uid,OrderRequest ordRequest);
	
	public abstract void specificUser(Integer uid,OrderRequest ordRequest,Product prd);
	
	public List<ProductOrder> getOrdersByUser(Integer userId);
	
	public abstract boolean cancellOrder(Integer oid);

}
