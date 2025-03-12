package com.tus.microservices.model;

public record DoctorRecord(
		Long id,
        String name,
        String specialization,
        String phoneNumber,
        String email
        ) {

}
