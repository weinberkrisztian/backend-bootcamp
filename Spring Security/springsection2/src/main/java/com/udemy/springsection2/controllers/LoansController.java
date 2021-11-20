package com.udemy.springsection2.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {
	
	@GetMapping("/myLoans")
	public String getLoanDetails(String input) {
		return "Here are the loan details from the DB";
	}

}
