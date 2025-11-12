package com.openclassrooms.mddapi.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

/**
 * Configuration principale de la sécurité Spring Security pour l'application.
 * <p>
 * Cette classe définit :
 * <ul>
 *     <li>Les règles d’autorisation des requêtes HTTP.</li>
 *     <li>Le mécanisme d’authentification basé sur JWT (JSON Web Token).</li>
 *     <li>Le chiffrement des mots de passe via BCrypt.</li>
 *     <li>La configuration du {@link AuthenticationManager} pour l’authentification des utilisateurs.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Annotée avec {@link Configuration} et {@link EnableWebSecurity}, cette classe est automatiquement détectée
 * et activée lors du démarrage de l’application Spring Boot.
 * </p>
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * Clé secrète utilisée pour signer et vérifier les JWT.
     * <p>
     * Elle est injectée depuis le fichier de configuration de l’application
     * (généralement dans {@code application.properties} ou {@code application.yml}).
     * </p>
     */
    @Value("${security.jwt.secret-key}")
    private String jwtKey;

    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     * <p>
     * Cette méthode :
     * <ul>
     *     <li>Désactive la protection CSRF (non nécessaire pour une API REST stateless).</li>
     *     <li>Active la configuration CORS par défaut.</li>
     *     <li>Configure la gestion de session comme stateless (sans session serveur).</li>
     *     <li>Définit les routes publiques et les routes nécessitant une authentification.</li>
     *     <li>Active la gestion des tokens JWT pour l’authentification.</li>
     * </ul>
     * </p>
     *
     * @param http la configuration de sécurité HTTP fournie par Spring.
     * @return une instance de {@link SecurityFilterChain} configurée.
     * @throws Exception en cas d’erreur de configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    /**
     * Définit le bean du chiffreur de mots de passe.
     * <p>
     * Utilise l’algorithme {@link BCryptPasswordEncoder} pour garantir un chiffrement sécurisé
     * et non réversible des mots de passe en base de données.
     * </p>
     *
     * @return une instance de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Crée et configure l’encodeur JWT.
     * <p>
     * Cet encodeur est responsable de la génération des tokens JWT signés
     * à partir de la clé secrète définie dans la configuration.
     * </p>
     *
     * @return un {@link JwtEncoder} configuré avec la clé secrète.
     */
    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
    }

    /**
     * Crée et configure le décodeur JWT.
     * <p>
     * Cet objet est utilisé pour vérifier et décoder les tokens JWT reçus
     * lors des requêtes entrantes.
     * </p>
     *
     * @return un {@link JwtDecoder} configuré pour l’algorithme HMAC SHA-256.
     */
    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), 0, this.jwtKey.getBytes().length, "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    /**
     * Configure le gestionnaire d’authentification principal de Spring Security.
     * <p>
     * Il utilise un {@link DaoAuthenticationProvider} basé sur un {@link CustomUserDetailsService}
     * pour charger les utilisateurs depuis la base de données, ainsi qu’un encodeur BCrypt
     * pour vérifier les mots de passe.
     * </p>
     *
     * @param customUserDetailsService service personnalisé chargé de récupérer les utilisateurs.
     * @return une instance de {@link AuthenticationManager} configurée.
     */
    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService customUserDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return new ProviderManager(provider);
    }
}
