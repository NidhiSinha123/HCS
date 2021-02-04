package com.cg.healthcaresystemrest.repository;
/*
 * Author : Nidhi
 * Description : This inteface will perform CRUD operations for DiagnosticCenter Entity.
 * Created Date : 9th October,2019 
 * 
 * */
import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cg.healthcaresystemrest.dto.DiagnosticCenter;


@Repository
public interface CenterRepository extends JpaRepository<DiagnosticCenter,BigInteger> {

	@Query("FROM DiagnosticCenter WHERE isDeleted = 'false'")
	public List<DiagnosticCenter> getCenterList();
}
