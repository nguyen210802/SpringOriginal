package com.example.identityService.service;

import com.example.identityService.dto.request.AuthenticationRequest;
import com.example.identityService.dto.request.IntrospectRequest;
import com.example.identityService.dto.response.AuthenticationResponse;
import com.example.identityService.dto.response.IntrospectResponse;
import com.example.identityService.dto.response.RefreshTokenRequest;
import com.example.identityService.dto.response.RefreshTokenResponse;
import com.nimbusds.jose.JOSEException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse authenticateWithGoogle(OAuth2User principal);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    AuthenticationResponse refreshToken(RefreshTokenRequest request);
}
