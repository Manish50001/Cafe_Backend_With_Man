package com.man.cafe.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name="User.findByEmailId",query="select u from User u where u.email=:email")

@NamedQuery(name="User.getAllUser",query = "select new com.man.cafe.wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user'")

@NamedQuery(name="User.updateStatus",query="update User u set u.status=:status where u.id=:id")

@NamedQuery(name="User.getAllAdmin",query = "select u.email  from User u where u.role='admin'")



@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
@Data
public class User implements Serializable {
	

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	private Integer id;
	
	@Column(name ="name")
	private String name;
	
	@Column(name = "contctNumber")
	private String contactNumber;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name="status")
	private String status;
	
	@Column(name = "role")
	private String role;
	
	
	
	
	
	
	

}
