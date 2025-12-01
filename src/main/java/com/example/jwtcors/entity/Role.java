package com.example.jwtcors.entity;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Set.of(
            Permission.READ_USERS, Permission.CREATE_USERS, Permission.DELETE_USERS,
            Permission.READ_PRODUCTS, Permission.CREATE_PRODUCTS, Permission.UPDATE_PRODUCTS, Permission.DELETE_PRODUCTS
    )),
    USER(Set.of(
            Permission.READ_USERS,
            Permission.READ_PRODUCTS
    ));

    public final Set<Permission> permissions;

    Role(Set<Permission> permissions){
        this.permissions = permissions;
    }

    public Set<String> getAuthorities() {
        // permisos
        Set<String> authorities = permissions.stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        // rol (Spring requiere ROLE_)
        authorities.add("ROLE_" + this.name());

        return authorities;
    }
}

