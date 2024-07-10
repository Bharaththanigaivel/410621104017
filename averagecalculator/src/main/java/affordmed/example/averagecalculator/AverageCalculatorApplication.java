package affordmed.example.averagecalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class AverageCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AverageCalculatorApplication.class, args);
	}
}
