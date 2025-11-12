package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.config.CustomUserDetailsService;
import com.openclassrooms.mddapi.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Service responsable de la génération des tokens JWT pour l’authentification.
 * <p>
 * Ce service s’appuie sur {@link JwtEncoder} pour encoder les jetons et sur
 * {@link CustomUserDetailsService} pour accéder aux informations utilisateur si nécessaire.
 * </p>
 *
 * <p>
 * Le token généré contient plusieurs informations standards (claims) :
 * <ul>
 *     <li><strong>issuer</strong> — l’émetteur du token,</li>
 *     <li><strong>issuedAt</strong> — la date de génération,</li>
 *     <li><strong>expiresAt</strong> — la date d’expiration (valide 24h),</li>
 *     <li><strong>subject</strong> — l’identifiant principal de l’utilisateur (ici, son e-mail).</li>
 * </ul>
 * </p>
 *
 * <p>
 * Le chiffrement est effectué avec l’algorithme {@link MacAlgorithm#HS256}.
 * </p>
 *
 * @see org.springframework.security.oauth2.jwt.JwtEncoder
 * @see org.springframework.security.oauth2.jwt.JwtClaimsSet
 * @see com.openclassrooms.mddapi.config.SpringSecurityConfig
 *
 * @author PA-SCHAMING
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class JWTService {

    /** Composant Spring Security responsable de l’encodage des tokens JWT. */
    private final JwtEncoder jwtEncoder;


    /**
     * Génère un token JWT pour un utilisateur donné.
     * <p>
     * Le token est valide pendant une durée de 24 heures.
     * </p>
     *
     * @param userDto l’utilisateur pour lequel le token doit être généré.
     * @return le token JWT encodé sous forme de {@link String}.
     */
    public String generateToken(UserDTO userDto) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(userDto.getEmail())
                .build();

        JwtEncoderParameters jwtEncoderParameters =
                JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
