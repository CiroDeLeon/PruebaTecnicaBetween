package com.between.app;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;






@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableDiscoveryClient
public class AppApplication {
	   
	public static void main(String[] args) throws UnknownHostException {
		int port = new Random().nextInt(10000) + 10000;
		SpringApplication app = new SpringApplication(AppApplication.class);
		Map<String,Object> prop=new HashMap<>();
		prop.put("server.port",String.valueOf(port));
	    app.setDefaultProperties(prop);
	    app.run(args);
	}

}
