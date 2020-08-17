package com.nest.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nest.manage.service.testService;

@Controller
public class test {
	@Autowired
    private testService test;
	
	@ResponseBody
	@RequestMapping("/lvs")
	public String lvs() {
		
		return test.getTest();
	}

	@RequestMapping("/hello")
	public String hello() {
		
		return "hello";
	}
}
