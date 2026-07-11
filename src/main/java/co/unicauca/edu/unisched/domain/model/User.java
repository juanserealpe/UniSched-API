package co.unicauca.edu.unisched.domain.model;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String username;
    private String password;
    private Set<RolesEnum> roles;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
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

    public Set<RolesEnum> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolesEnum> roles) {
        this.roles = roles;
    }

    public void addRole(RolesEnum role) {
        roles.add(role);
    }

    public void removeRole(RolesEnum role) {
        roles.remove(role);
    }

    public boolean hasRole(RolesEnum role) {
        return roles.contains(role);
    }
}