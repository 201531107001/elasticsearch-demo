package com.me.web;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.apache.log4j.Logger;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.me.entity.Student;

@RestController
@RequestMapping("/")
public class StudentController {
	
	private static Logger logger = Logger.getLogger(StudentController.class);
	
	@Autowired
	TransportClient client;
	
	/**
	 * 通过id查询student
	 * @param id
	 * @return
	 */
	@GetMapping(value="/get")
	public ResponseEntity<Map<?,?>> getByRequestParam(@RequestParam(value = "id", defaultValue = "") String id){
		
		logger.info("get"+id);
		
		GetResponse result = client.prepareGet("people", "student", id).get();
		if(result.isExists()){
			logger.info(result);
			return new ResponseEntity<Map<?,?>>(result.getSource(), HttpStatus.OK);
		}else{
			logger.error("not found"+id);
			return new ResponseEntity<Map<?,?>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * 通过id查询student
	 * @param id
	 * @return
	 */
	@GetMapping(value="/get/{id}")
	public ResponseEntity<Map<?,?>> getByUrlValue(@PathVariable String id){
		GetResponse result = client.prepareGet("people", "student", id).get();
		
		if(result.isExists()){
			logger.info("get id:"+id);
			return new ResponseEntity<Map<?,?>>(result.getSource(), HttpStatus.OK);
		}else{
			return new ResponseEntity<Map<?,?>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * 添加学生
	 * @param name 姓名
	 * @param age 年龄
	 * @param date 出生日期
	 * @return
	 */
	@PostMapping(value="/add")
	@ResponseBody
	public ResponseEntity<?> add(
			@RequestParam(value="name") String name,
			@RequestParam(name="age") int age,
			@RequestParam(name="date") @DateTimeFormat(pattern="yyyy-MM-dd")Date date){
		logger.info("add"+" name:"+name+" age:"+age+" date:"+date);
		try {
			XContentBuilder content = XContentFactory
				.jsonBuilder()
				.startObject()
				.field("name",name)
				.field("age",age)
				.field("date",date.getTime())
				.endObject();
			
			IndexResponse result = client.prepareIndex("people", "student")
				.setSource(content)
				.get();
			logger.info("add name:"+name);
			return new ResponseEntity<String>(result.getId(),HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("filed add"+" name:"+name+" age:"+age+" date:"+date);
			return new ResponseEntity<Map<?,?>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 删除学生
	 * @param id
	 * @return
	 */
	@GetMapping(value="/delete")
	public ResponseEntity<?> delete(@RequestParam(name="id") String id){
		
		logger.info("delete id:"+id);
		DeleteResponse result = client.prepareDelete("people", "student", id).get();
		return new ResponseEntity<String>(result.getResult().toString(), HttpStatus.OK);
	}
	
	/**
	 * 更新数据
	 * @param id
	 * @param name
	 * @param age
	 * @param date
	 * @return
	 */
	@PostMapping(value="/update")
	public ResponseEntity<Result> update(
			@RequestParam(name="id")String id,
			@RequestParam(name="name",required=false) String name,
			@RequestParam(name="age",defaultValue="-1") int age,
			@RequestParam(name="date",required=false) 
			@DateTimeFormat(pattern="yyyy-MM-dd") Date date){
		
		UpdateRequest update = new UpdateRequest("people", "student", id);
		XContentBuilder builder;
		try {
			builder = XContentFactory.jsonBuilder().startObject();
			if(name != null){
				builder.field("name",name);
			}
			
			if(age != -1){
				builder.field("age",age);
			}
			
			if(date != null){
				builder.field("date",date.getTime());
			}
			
			builder.endObject();
			update.doc(builder);
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("update id:"+id);
		try {
			UpdateResponse result = client.update(update).get();
			return new ResponseEntity<Result>(result.getResult(),HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Result>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 测试
	 * @return
	 */
	@GetMapping(value="/hello", produces="application/json")
	public Student hello(){
		Student student = new Student("gqm", 19);
		return student;
	}
	
}
