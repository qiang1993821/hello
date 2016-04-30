package com.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;

@SpringBootApplication
public class DemoApplication1 {

	public static void main(String[] args) {
		SpringApplication sa=new SpringApplication(DemoApplication1.class);
		sa.run(args);
	}

}
