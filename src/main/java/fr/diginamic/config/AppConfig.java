package fr.diginamic.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuration Spring classique.
 * Permet à Spring de scanner tous les @Service, @Repository et @Component
 */
@Configuration
@ComponentScan(basePackages = "fr.diginamic")
public class AppConfig {
}

