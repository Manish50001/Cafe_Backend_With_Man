package com.man.cafe.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.man.cafe.JWT.CustomerUserDetailsService;
import com.man.cafe.JWT.JwtFilter;
import com.man.cafe.JWT.JwtUtil;
import com.man.cafe.POJO.User;
import com.man.cafe.constents.CafeConstents;
import com.man.cafe.dto.UserDao;
import com.man.cafe.service.UserService;
import com.man.cafe.utils.CafeUtils;
import com.man.cafe.utils.EmailUtils;
import com.man.cafe.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
    @Autowired
	private AuthenticationManager authenticationManager;
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtFilter filter;
    
    @Autowired
    EmailUtils emailUtils;
    
    
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		
		log.info("Inside singup{}",requestMap);
		
		try {
			if(vilidateSignUpMap(requestMap)) {
				
				User user=userDao.findByEmailId(requestMap.get("email"));
				
				if(Objects.isNull(user)) {
					
					userDao.save(getUserFromMap(requestMap));
					return CafeUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
					
					
				}else {
					return CafeUtils.getResponseEntity("Email already Exist", HttpStatus.BAD_REQUEST);
				}
				
			}
			else{
				return CafeUtils.getResponseEntity(CafeConstents.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
		e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);	
	}

	private boolean vilidateSignUpMap(Map<String,String>requestMap) {
		if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")&&
		requestMap.containsKey("email")	&& requestMap.containsKey("password")) {
			return true;
		}
		return false;
	}
	
	private User getUserFromMap(Map<String,String>requestMap) {
		User user=new User();
		user.setName(requestMap.get("name"));
		user.setEmail(requestMap.get("email"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setPassword(requestMap.get("password"));
		user.setRole("user");
		user.setStatus("false");
		return user;
		
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		log.info("Inside login");
		
		try {
			Authentication auth=authenticationManager.authenticate(
         new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
			
			
		if(auth.isAuthenticated()) {
			 
			if(customerUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
				
				
				
				return new ResponseEntity<String>("{\"token\":\""+
			 jwtUtil.generateToken(customerUserDetailsService.getUserDetails().getEmail(),
					 customerUserDetailsService.getUserDetails().getRole())+"\"}",
						HttpStatus.OK);
				
			}
			
			else{
			return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval."+"\"}",
					HttpStatus.BAD_REQUEST);	
			}
		}
		}catch(Exception e) {
			log.error("{}",e);
		}
		return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}",
				HttpStatus.BAD_REQUEST);	
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {

   try {
	
	   if(filter.isAdmin()) {
		   return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
	   }
	   else {
		   return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
	   }
    }catch(Exception e) {
    	 e.printStackTrace();
    }
   
   return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	
	
	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			System.out.println(filter.isAdmin());
			if(filter.isAdmin()) {
			Optional<User>optional=	userDao.findById(Integer.parseInt(requestMap.get("id")));
				
			if(!optional.isEmpty()) {
				
				userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
				
				sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
				
				return CafeUtils.getResponseEntity("User Status Updated SuccessFully", HttpStatus.OK);
			}else {
				CafeUtils.getResponseEntity("User id Does not exist", HttpStatus.OK);
			}
				
			}else {
				return CafeUtils.getResponseEntity(CafeConstents.UNAUTORIZED_ACCESS, HttpStatus.BAD_REQUEST);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
		
		allAdmin.remove(filter.getCurrentUser());
//		if(status!=null && status.equalsIgnoreCase("true")) {
//			emailUtils.sendSimpleMessage(filter.getCurrentUser(), "Account Approved", "USER:- "+user+ " \n is approved by \nADMIN:-"+filter.getCurrentUser(), allAdmin);
//		}else {
//			emailUtils.sendSimpleMessage(filter.getCurrentUser(), "Account Disabled", "USER:- "+user+ " \n is approved by \nADMIN:-"+filter.getCurrentUser(), allAdmin);
//				
//		}
		
	}

	@Override
	public ResponseEntity<String> checkToken() {
		
		return CafeUtils.getResponseEntity("true", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		
		try {
			
			User user=userDao.findByEmail(filter.getCurrentUser());
			if(!user.equals(null)) {
				
				if(user.getPassword().equals(requestMap.get("oldPassword"))) {
				
				user.setPassword(requestMap.get("newPassword"));
				userDao.save(user);
				return CafeUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
					
				}
				return CafeUtils.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
				
			}
			return CafeUtils.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
	
		try {
			
			User user=userDao.findByEmail(requestMap.get("email"));
			if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
		//	emailUtils.forgotMail(user.getEmail(), "Credentials by Cafe Management System",user.getPassword());
			}
				return CafeUtils.getResponseEntity("Check Your Mail for Credentials", HttpStatus.OK);
				
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
