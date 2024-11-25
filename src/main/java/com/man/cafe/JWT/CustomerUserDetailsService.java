package com.man.cafe.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.man.cafe.dto.UserDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService{

	@Autowired
	private UserDao userDao;
	
	private com.man.cafe.POJO.User userDetail;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.info("Inside Load user By Username {}", username);
		userDetail=userDao.findByEmailId(username);
		
		if(!Objects.isNull(userDetail)) {
			return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
		}
		else {
			throw new UsernameNotFoundException("User Not Found");
		}
	}
     public com.man.cafe.POJO.User getUserDetails(){
    	
    	 return userDetail;
     }
}
