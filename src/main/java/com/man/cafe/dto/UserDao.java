package com.man.cafe.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.man.cafe.POJO.User;
import com.man.cafe.wrapper.UserWrapper;

import jakarta.transaction.Transactional;

public interface UserDao extends JpaRepository<User, Integer> {
	
	User findByEmailId( @Param("email") String email);
     List<UserWrapper>getAllUser();
    
     List<String>getAllAdmin();
     
     @Transactional
     @Modifying
     Integer updateStatus(@Param("status")String status,@Param("id")Integer id);
    
     User findByEmail(String email);

}
