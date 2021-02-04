package com.cg.healthcaresystemrest.repository;

/*
 * author: Jayesh Gaur
 */

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cg.healthcaresystemrest.dto.User;

/*
 * Author: Jayesh Gaur
 * Description: User Repository class which handles all database related interactions for the DTO class User.
 * Created on: October 9, 2019
 */
@Repository
public interface UserRepository extends JpaRepository<User, BigInteger> {
	
	public User findByUserEmailAndUserPassword(@Param("email")String email, @Param("password")String password);

	public Optional<User> findByUserEmail(String userEmail);
	
	public Optional<User> findByContactNo(BigInteger contactNo);
	
}
