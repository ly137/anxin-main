package com.anxin.navigation.domain.model;

public final class NormalUser extends User {
    public NormalUser(String username, String passwordHash) {
        super(username, passwordHash, Role.USER);
    }
}

