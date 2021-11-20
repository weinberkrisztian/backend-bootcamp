package com.udemy.springsection2.controllers;

import com.udemy.springsection2.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardsController {


	@PostMapping("/myCards")
	public String getCardDetails(String randoMString) {
			return "Here are my card credentials!";
	}

}
