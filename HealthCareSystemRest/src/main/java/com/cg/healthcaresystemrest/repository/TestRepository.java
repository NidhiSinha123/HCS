package com.cg.healthcaresystemrest.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.healthcaresystemrest.dto.Test;


/*
 * Author : Nidhi
 * Description : This inteface will perform CRUD operations for Test Entity.
 * Created Date : 9th October,2019 
 * 
 * */
@Repository
public interface TestRepository extends JpaRepository<Test,BigInteger> {

}
