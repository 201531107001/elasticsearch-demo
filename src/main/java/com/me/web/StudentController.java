package com.me.web;

import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.me.entity.Student;

@RestController
@RequestMapping("/")
public class StudentController {
	
	@Autowired
	TransportClient client;
	
	@GetMapping(value="/get")
	public ResponseEntity<Map<?,?>> getByRequestParam(@RequestParam(value = "id", defaultValue = "") String id){
		GetResponse result = client.prepareGet("people", "student", id).get();
		
		System.out.println(result.getSourceAsString());
		System.out.println(id);
		if(result.isExists()){
			return new ResponseEntity<Map<?,?>>(result.getSource(), HttpStatus.OK);
		}else{
			return new ResponseEntity<Map<?,?>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/get/{id}")
	public ResponseEntity<Map<?,?>> getByUrlValue(@PathVariable String id){
		GetResponse result = client.prepareGet("people", "student", id).get();
		
		System.out.println(result.getSourceAsString());
		System.out.println(id);
		if(result.isExists()){
			return new ResponseEntity<Map<?,?>>(result.getSource(), HttpStatus.OK);
		}else{
			return new ResponseEntity<Map<?,?>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/hello", produces="application/json")
	public Student hello(){
		Student student = new Student("gqm", 19);
		return student;
	}
	
}
