package com.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>
{
	public abstract Cart findByProductIdAndUserId(Integer pid,Integer uid);

	public abstract Integer countByUserId(Integer uid);

	public abstract List<Cart> findByUserId(Integer uid);
}
