package fr.essam.payment.paymentapi.application.rest.utils;

import fr.essam.payment.paymentapi.domain.exception.BasicAuthenticationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@UtilityClass
@Slf4j
public class BasicAuthValidator {

    private static final String BASIC_AUTH_PREFIX = "Basic";
    private static final String BASIC_AUTH_SEPARATOR = ":";

    public void validateBasicAuth(String basicAuthorization, AuthenticationManager authenticationManager) {

        checkIfBasicAuthProvided(basicAuthorization);

        Authentication authenticationRequest = buildAuthenticationData(basicAuthorization);

        Authentication authenticationResponse = authenticateUser(authenticationManager, authenticationRequest);

        checkIfValidAuthenticateResponse(authenticationResponse);

        log.info("User is authorised");

    }

    private static void checkIfValidAuthenticateResponse(Authentication authenticationResponse) {
        if(!authenticationResponse.isAuthenticated()) {
            throw new BasicAuthenticationException("Not authorised user");
        }
    }

    private static Authentication authenticateUser(AuthenticationManager authenticationManager, Authentication authenticationRequest) {
        try {
            return authenticationManager.authenticate(authenticationRequest);
        } catch (AuthenticationException e) {
            throw new BasicAuthenticationException("Not authorised user");
        }
    }

    private static Authentication buildAuthenticationData(String basicAuthorization) {
        byte[] credentialsToken = Base64.getDecoder().decode(basicAuthorization.substring(BASIC_AUTH_PREFIX.length()).trim());

        final String[] credentials = new String(credentialsToken, StandardCharsets.UTF_8).split(BASIC_AUTH_SEPARATOR, 2);

        return UsernamePasswordAuthenticationToken.unauthenticated(credentials[0], credentials[1]);
    }

    private static void checkIfBasicAuthProvided(String basicAuthorization) {
        if(basicAuthorization == null || !basicAuthorization.toLowerCase().startsWith("basic")) {
            throw new BasicAuthenticationException("No basic authorisation header");
        }
    }
}
