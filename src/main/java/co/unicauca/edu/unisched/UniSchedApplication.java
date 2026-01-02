package co.unicauca.edu.unisched;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for UniSched.
 * The study plan and knowledge base are automatically initialized
 * through StudyPlanService.
 */
@SpringBootApplication
public class UniSchedApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniSchedApplication.class, args);
    }

}
