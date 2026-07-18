package co.unicauca.edu.unisched.domain.model;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String username;
    private String password;
    private String email;
    private final Set<RolesEnum> roles;

    public User(String username,
                String password,
                String email) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = new HashSet<>();
    }

    public User(String username,
                String password,
                String email, Set<RolesEnum> roles) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles == null
                ? new HashSet<>()
                : new HashSet<>(roles);
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

    public Set<RolesEnum> getRoles() {
        return Set.copyOf(roles);
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