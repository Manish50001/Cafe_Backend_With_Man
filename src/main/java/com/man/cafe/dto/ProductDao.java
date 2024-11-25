package com.man.cafe.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.man.cafe.POJO.Product;
import com.man.cafe.wrapper.ProductWrapper;

import jakarta.transaction.Transactional;

public interface ProductDao extends JpaRepository<Product, Integer>{

	
	List<ProductWrapper>getAllProduct();
	@Modifying
	@Transactional

	Integer updateProductStatus(@Param("status")String status,@Param("id")Integer id); 


	List<ProductWrapper>getByProdcutCategory(@Param("id")Integer id);

	ProductWrapper getProductById(@Param("id")Integer id);
	
}
