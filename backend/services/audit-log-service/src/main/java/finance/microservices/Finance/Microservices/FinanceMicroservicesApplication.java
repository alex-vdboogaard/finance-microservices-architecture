package finance.microservices.Finance.Microservices;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import finance.microservices.audit.AuditLog;
import finance.microservices.audit.AuditLogRepository;

@SpringBootApplication(scanBasePackages = "finance.microservices")
@EnableJpaRepositories(basePackages = "finance.microservices.audit")
@EntityScan(basePackages = "finance.microservices.audit")
public class FinanceMicroservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceMicroservicesApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(AuditLogRepository repository) {
		return (args) -> {
			AuditLog log1 = new AuditLog();
			log1.setServiceName("payment-service");
			log1.setEvent("Payment processed successfully");
			log1.setTimestamp(LocalDateTime.now());
			repository.save(log1);

			AuditLog log2 = new AuditLog();
			log2.setServiceName("user-service");
			log2.setEvent("User created");
			log2.setTimestamp(LocalDateTime.now());
			repository.save(log2);
		};
	}
}
