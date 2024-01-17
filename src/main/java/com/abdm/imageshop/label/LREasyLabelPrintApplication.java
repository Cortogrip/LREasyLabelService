/*----------------------------------------------------------------------------------------------------------------------
 * LR Easy Label Print © 2024 by l Simonnet is licensed under Attribution-NonCommercial-NoDerivatives 4.0 International
 *
 * LR Easy Label Print use a deployed service to print labels.
 * See http://localhost:8087/getting_started.html or http://auborddesmauves.fr/products/lrplugins for documentation.
 * ------------------------------------------------------------------------------------------------------------------ */
package com.abdm.imageshop.label;

// Imports
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.apache.commons.io.FilenameUtils;

/**
 * Main entrypoint.
 */
@SpringBootApplication
@Slf4j
@ConfigurationPropertiesScan(basePackageClasses = LREasyLabelPrintApplication.class)
public class LREasyLabelPrintApplication {

	public static void main(String[] args) {

		SpringApplication.run(LREasyLabelPrintApplication.class, args);
		// normalize_();
	}

	private static void normalize_() {

		System.out.println("*** Normalization ***");

		String filename = "X:\\JCG\\.\\org.apache.commons.io.FilenameUtils Example\\notes.txt";
		System.out.println("Before: " + filename);
		String normalized = FilenameUtils.normalize(filename);
		System.out.println("After single dot normalization: " + normalized);

		filename = "X:\\JCG\\articles\\..\\notes.txt";
		System.out.println("Before: " + filename);
		normalized = FilenameUtils.normalize(filename);
		System.out.println("After double dot normalization: " + normalized);

		String directory = "C:\\Users\\lsimonnet\\Travail\\Dev\\label-worker";
		filename = "../generated/17012024125530.pdf";
		System.out.println("Before: " + directory + "/" + filename);
		normalized = FilenameUtils.normalize(directory + "/" + filename);
		System.out.println("After normalization: " + normalized);
		System.out.println("After normalization: " + FilenameUtils.separatorsToUnix(normalized));
	}
}
