package com.tus.microservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.microservices.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{

	List<Patient> findByNameContainingIgnoreCase(String name);
}
