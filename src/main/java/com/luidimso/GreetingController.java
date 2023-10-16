package com.luidimso;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luidimso.exceptions.ResourceNotFoundException;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

	private static final String template = "Hello %s!";
	private final AtomicLong counter = new AtomicLong();
	
	
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
		
	}
	
	@GetMapping("/sum/{n1}/{n2}")
	public Double sum(@PathVariable(value = "n1") String n1, @PathVariable(value = "n2") String n2) throws Exception {
		if(!isNumeric(n1) || !isNumeric(n2)) {
			throw new ResourceNotFoundException("Please, insert a numeric number");
		}
		
		return convertToDouble(n1) + convertToDouble(n2);
		
	}
	
	
	public boolean isNumeric(String number) {
		if(number == null) {
			return false;
		}
		
		String n = number.replaceAll(",", ".");
		
		return n.matches("[-+]?[0-9]*\\.?[0-9]+");
	}
	
	public Double convertToDouble(String number) {
		if(number == null) {
			return 0D;
		}
		
		String n = number.replaceAll(",", ".");
		
		if(isNumeric(n)) {
			return Double.parseDouble(n);
		}
		
		return 0D;
	}
	
		
}
