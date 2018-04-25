package diploma.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"diploma.controllers","diploma.server","diploma.services","diploma.dao"})
@EnableAutoConfiguration
public class ApplicationRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationRunner.class, args);
	}
}
