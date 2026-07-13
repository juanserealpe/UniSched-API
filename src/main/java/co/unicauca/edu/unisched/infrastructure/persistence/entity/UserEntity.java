package co.unicauca.edu.unisched.infrastructure.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import co.unicauca.edu.unisched.domain.model.RolesEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "username")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<RolesEnum> roles = new HashSet<>();

    protected UserEntity() {
    }

    public UserEntity(String username,
                      String password,
                      String email,
                      Set<RolesEnum> roles) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles == null
                ? new HashSet<>()
                : new HashSet<>(roles);
    }

    public UserEntity(String username,
                  String password,
                  String email) {

        this.username = username;
        this.password = password;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RolesEnum> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolesEnum> roles) {
    this.roles = roles == null
            ? new HashSet<>()
            : new HashSet<>(roles);
}

}