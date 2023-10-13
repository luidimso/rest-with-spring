package com.luidimso;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

	private static final String template = "Hello %s!";
	private final AtomicLong counter = new AtomicLong();
	
	
	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
		
	}
	
	@GetMapping("/sum/{n1}/{n2}")
	public Double sum(@PathVariable(value = "n1") String n1, @PathVariable(value = "n1") String n2) {
		return 1D;
		
	}
		
}
