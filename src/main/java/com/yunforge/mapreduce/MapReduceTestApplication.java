package com.yunforge.mapreduce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("hadoop-context.xml")
public class MapReduceTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapReduceTestApplication.class, args);
	}
}
