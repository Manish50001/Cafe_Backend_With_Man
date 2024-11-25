package com.man.cafe.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {
	
	private CafeUtils() {
		
	}
   public static ResponseEntity<String>getResponseEntity(String responseMessage,HttpStatus httpStatus){
	   
		return new ResponseEntity<>("{\"message\":\" "+responseMessage+ "\"}",HttpStatus.INTERNAL_SERVER_ERROR);

   }
}
