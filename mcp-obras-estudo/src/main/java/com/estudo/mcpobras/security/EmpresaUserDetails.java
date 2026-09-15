package com.estudo.mcpobras.security;

import com.estudo.mcpobras.domain.Empresa;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class EmpresaUserDetails implements UserDetails {

    private final Empresa empresa;

    public EmpresaUserDetails(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_EMPRESA"));
    }

    @Override
    public String getPassword() {
        return empresa.getLoginPassword();
    }

    @Override
    public String getUsername() {
        return empresa.getLoginUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}