package de.davelee.trams.operations;

import de.davelee.trams.operations.service.FileSystemStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * This class starts the Trams Operations application using Spring Boot.
 * @author Dave Lee
 */
@SpringBootApplication
public class TramsOperationsApplication {

	/**
	 * Main method to start the application.
	 * @param args a <code>String</code> array of arguments which are not read by the application at present.
	 */
	public static void main(final String[] args) {
		SpringApplication.run(TramsOperationsApplication.class, args);
	}

	/**
	 * This method deletes all files currently located in the specified upload directory for the application and/or creates this file.
	 * @param fileSystemStorageService a <code>FileSystemStorageStorage</code> object which performs the actual processing.
	 * @return a <code>CommandLineRunner</code> object which does the processing.
	 */
	@Bean
	CommandLineRunner init(final FileSystemStorageService fileSystemStorageService) {
		return (args) -> {
			fileSystemStorageService.deleteAll();
			fileSystemStorageService.init();
		};
	}

}
