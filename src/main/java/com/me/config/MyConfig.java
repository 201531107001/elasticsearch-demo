package com.me.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
	@Bean
	public TransportClient client() throws UnknownHostException{
		InetAddress address = InetAddress.getByName("localhost");
		TransportAddress node = new TransportAddress(address, 9300);
		
		Settings settings = Settings.builder()
				.put("cluster.name", "gqm")
				.build();
		
		@SuppressWarnings("unchecked")
		TransportClient client = new PreBuiltTransportClient(settings);
		client.addTransportAddress(node); //可以添加多个节点
		
		return client;
	}
}
