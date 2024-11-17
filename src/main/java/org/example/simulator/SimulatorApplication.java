package org.example.simulator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@SpringBootApplication
public class SimulatorApplication implements CommandLineRunner {

	@Autowired
	private KafkaProducerService kafkaProducerService;

	public static void main(String[] args) {
		SpringApplication.run(SimulatorApplication.class, args);
	}

	@Override
	public void run(String... args) {
		kafkaProducerService.sendMessage("lab5", "user-1", "Message with key 1");
		kafkaProducerService.sendMessage("lab5","user-2", "Message with key 2");
	}

}

