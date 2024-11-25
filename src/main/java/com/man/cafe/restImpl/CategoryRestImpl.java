package com.man.cafe.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.man.cafe.POJO.Category;
import com.man.cafe.constents.CafeConstents;
import com.man.cafe.rest.CategoryRest;
import com.man.cafe.service.CategoryService;
import com.man.cafe.utils.CafeUtils;
@RestController
public class CategoryRestImpl implements CategoryRest {
 
	@Autowired
	CategoryService categoryService;
	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requiredMap) {

         try {
        	 return categoryService.addNewCategory(requiredMap);
         }catch (Exception e) {
			e.printStackTrace();
		}
         return CafeUtils.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		
		try {
		 return categoryService.getAllCategory(filterValue);	
		}catch (Exception e) {
		e.printStackTrace();
		}
		return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {

        try {
       return 	categoryService.updateCategory(requestMap);
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        return CafeUtils.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
