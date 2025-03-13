package com.example.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Document(collection = "users")

public class User {

	@Id
	@EqualsAndHashCode.Include
	private String id;
	
	@Field
	private String username;
	
	@Field
	private String password;
	
	@Field
	private boolean status;
	
	@Field
	private List<com.example.demo.model.Role> roles;
	
	

}
