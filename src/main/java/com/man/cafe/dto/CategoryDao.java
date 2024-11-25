package com.man.cafe.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.man.cafe.POJO.Category;

public interface CategoryDao extends JpaRepository<Category, Integer> {

	List<Category>getAllCategory();
}
