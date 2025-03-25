package com.website.loveconnect.service;

import com.website.loveconnect.dto.request.AuthenticationRequest;

public interface AuthenticationService {
    boolean authenticate(AuthenticationRequest authenticationRequest);
}
