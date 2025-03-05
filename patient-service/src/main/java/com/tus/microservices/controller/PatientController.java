package com.tus.microservices.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/v1")
@RestController
public class PatientController {
	
	@GetMapping("/patient")
	public String sayHello() {
		log.info("Patient logs");
		return "hello patient";
	}
}
