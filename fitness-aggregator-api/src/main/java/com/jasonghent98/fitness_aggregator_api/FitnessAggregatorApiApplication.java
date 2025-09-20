package com.jasonghent98.fitness_aggregator_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FitnessAggregatorApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessAggregatorApiApplication.class, args);
	}

}
