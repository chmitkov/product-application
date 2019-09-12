package org.ch.productshop.domain.models.view;

import org.ch.productshop.domain.models.service.RoleServiceModel;

import java.util.Set;

public class UserAllViewModel {

    private String id;
    private String username;
    private String password;
    private String email;
    private Set<String> authorities;

    public UserAllViewModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
