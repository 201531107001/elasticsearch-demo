package com.me.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import httputil.RpcHttpUtil;

public class StudentControllerTest {

	@Test
	public void add() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", "gqp");
		paramMap.put("age", "24");
		paramMap.put("date", "1994-04-15");
		
		String url = "http://localhost:8080/elasticsearch-demo/add";
		System.out.println(call(url, RpcHttpUtil.POST, paramMap));
	}
	
	@Test
	public void update() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "PFRZ6WYBj648KJrMkwV-");
		paramMap.put("age", "26");
		paramMap.put("date", "1992-10-15");
		
		String url = "http://localhost:8080/elasticsearch-demo/update";
		System.out.println(call(url, RpcHttpUtil.POST, paramMap));
	}
	
	@Test
	public void delete() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "_update");
		
		String url = "http://localhost:8080/elasticsearch-demo/delete";
		System.out.println(call(url, RpcHttpUtil.GET, paramMap));
	}
	
	@Test
	public void get() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", "PFRZ6WYBj648KJrMkwV-");
		
		String url = "http://localhost:8080/elasticsearch-demo/get";
		System.out.println(call(url, RpcHttpUtil.GET, paramMap));
	}
	
	public String call(String url,String method,Map<String, String> paramMap){
		String str = null;
		try {
			str = RpcHttpUtil.invokeHttp(url, method, paramMap);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return str;
	}

}
