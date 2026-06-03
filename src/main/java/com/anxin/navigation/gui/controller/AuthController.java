package com.anxin.navigation.gui.controller;

import com.anxin.navigation.application.auth.AuthService;
import com.anxin.navigation.domain.model.Admin;

import java.util.Objects;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = Objects.requireNonNull(authService, "authService must not be null");
    }

    public Admin loginAdmin(String username, String password) {
        return authService.loginAdmin(username, password);
    }
}
