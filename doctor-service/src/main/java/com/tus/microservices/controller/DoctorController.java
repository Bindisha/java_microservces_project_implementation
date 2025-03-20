package com.tus.microservices.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tus.microservices.model.DoctorRecord;
import com.tus.microservices.model.Specialization;
import com.tus.microservices.service.DoctorService;
import com.tus.microservices.service.PatientClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/v1/doctor")
@RestController
public class DoctorController {
	
	@Autowired
	private DoctorService doctorService;
	
	@Autowired
	private PatientClient patientClient;
	
	@GetMapping("/test")
	public String sayHello() {
		log.info("Doctor logs");
		return "hello doctor";
	}
	
	@PostMapping
	public ResponseEntity<DoctorRecord> createDoctor(@RequestBody DoctorRecord doctorRecord) {
		log.info("Save Doctor");
		if(doctorRecord!=null) {
			DoctorRecord doctorDetail = doctorService.saveDoctor(doctorRecord);
			return new ResponseEntity<>(doctorDetail,HttpStatus.OK);
		}
		return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping
	public ResponseEntity<List<DoctorRecord>> getAllDoctors(@RequestParam(required = false) String name,@RequestParam(required = false) String specialization) {
		log.info("get doctors list");
		try {
			List<DoctorRecord> records = new ArrayList();
			
			if (StringUtils.hasText(name))
				doctorService.getAllDoctorDataByName(name).forEach(records::add);
			else if(StringUtils.hasText(specialization))
				try {
	                Specialization specEnum = Specialization.valueOf(specialization); // Convert String to Enum
	                records.addAll(doctorService.getDoctorsBySpecialization(specEnum));
	            } catch (IllegalArgumentException e) {
	                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid specialization value
	            }
			else
				doctorService.getAllDoctorData().forEach(records::add);

			if (records.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(records, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DoctorRecord> getTutorialById(@PathVariable("id") Long id) {
		log.info("get doctor by id:"+id);
		if(id!=null)
		{
			DoctorRecord doctorDetail = doctorService.getDoctorDetails(id);
			if(doctorDetail!=null) {
				return new ResponseEntity<>(doctorDetail, HttpStatus.OK);
			}
		}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<DoctorRecord> updateTutorial(@PathVariable("id") Long id, @RequestBody DoctorRecord updateData) {
		
		DoctorRecord result;
		if(id!=null) {
			DoctorRecord existData=doctorService.updateDoctor(id,updateData);
			if(existData!=null) {
					return new ResponseEntity<>(existData, HttpStatus.OK);	
			}
			else
				{
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteRecord(@PathVariable("id") Long id) {
		if(id!=null)
		{
			doctorService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		else
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping
	public ResponseEntity<HttpStatus> deleteAllRecords() {
		try {
			doctorService.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
