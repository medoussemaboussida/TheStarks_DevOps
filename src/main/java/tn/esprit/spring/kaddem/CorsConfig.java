package tn.esprit.spring.kaddem;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.logging.Logger;

@Configuration
public class CorsConfig {
    private static final Logger LOGGER = Logger.getLogger(CorsConfig.class.getName());

    @Bean
    public CorsFilter corsFilter() {
        LOGGER.info("Initialisation du filtre CORS");

        // Configuration CORS
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:4200"); // Origine autorisée (sans "/" final)
        corsConfig.addAllowedMethod("GET");    // Méthodes HTTP autorisées
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.addAllowedMethod("OPTIONS"); // Important pour les requêtes préliminaires
        corsConfig.addAllowedHeader("*");       // Tous les en-têtes autorisés
        corsConfig.setMaxAge(3600L);            // Durée de validité du préflight (1 heure)

        // Source des configurations CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Appliquer à toutes les routes

        return new CorsFilter(source);
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration(CorsFilter corsFilter) {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>(corsFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE); // Priorité maximale pour s'exécuter en premier
        LOGGER.info("Enregistrement du filtre CORS avec priorité maximale");
        return registration;
    }
}