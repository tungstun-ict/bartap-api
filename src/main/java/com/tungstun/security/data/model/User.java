package com.tungstun.security.data.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "mail", unique = true)
    private String mail;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserBarAuthorization> userBarAuthorizations;

    public User() { }
    public User(String username, String password, String mail, String firstName, String lastName, List<UserBarAuthorization> userBarAuthorizations) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userBarAuthorizations = userBarAuthorizations;
    }

    public Long getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getMail() { return mail; }

    public void setMail(String mail) { this.mail = mail; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public List<UserBarAuthorization> getUserBarAuthorizations() { return userBarAuthorizations; }

    public boolean addUserBarAuthorizations(UserBarAuthorization userBarAuthorization) {
        if (!this.userBarAuthorizations.contains(userBarAuthorization)) return this.userBarAuthorizations.add(userBarAuthorization);
        return false;
    }

    public boolean removeUserBarAuthorizations(UserBarAuthorization userBarAuthorization) {
        return this.userBarAuthorizations.remove(userBarAuthorization);
    }

    public Map<Long, String> getAuthoritiesMap() {
        Map<Long, String> barAuth = new HashMap<>();
        for (UserBarAuthorization userBarAuthorization : userBarAuthorizations) {
            barAuth.put(userBarAuthorization.getBarId(), userBarAuthorization.getRole().toString());
        }
        return barAuth;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(mail, user.mail) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, mail, firstName, lastName);
    }
}
