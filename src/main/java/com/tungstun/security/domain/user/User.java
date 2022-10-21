package com.tungstun.security.domain.user;

import com.tungstun.barapi.domain.person.Person;
import com.tungstun.common.phonenumber.PhoneNumber;
import com.tungstun.exception.CannotAuthenticateException;
import com.tungstun.exception.NotAuthorizedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Id
    private UUID id;

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

    @Embedded
    private PhoneNumber phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Authorization> authorizations;

    public User() {
    }

    public User(UUID id, String username, String password, String mail, String firstName, String lastName, String phoneNumber, List<Authorization> authorizations) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = Optional.ofNullable(phoneNumber)
                .map(PhoneNumber::new)
                .orElse(null);
        this.authorizations = authorizations;
    }

    public UUID getId() {
        return id;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public void canAuthenticate() {
        if (!isAccountNonExpired())
            throw new CannotAuthenticateException("Account expired. An expired account cannot be authenticated.");
        if (!isAccountNonLocked())
            throw new CannotAuthenticateException("Account locked. A locked account cannot be authenticated.");
        if (!isCredentialsNonExpired())
            throw new CannotAuthenticateException("Account credentials expired. Expired credentials prevent authentication.");
        if (!isEnabled())
            throw new CannotAuthenticateException("Account disabled. A disabled account cannot be authenticated.");
    }

    public boolean newBarAuthorization(UUID barId, Person person) {
        addAuthorization(barId, Role.OWNER, person);
        return true;
    }

    public boolean authorize(UUID barId, Role role, Person person) {
//        if (!isOwner(barIdentification)) throw new NotAuthorizedException("User has to be Owner of bar to authorize other users");
//        if (this.equals(user)) throw new IllegalArgumentException("Cannot change your own bar role");
        if (role == Role.OWNER) throw new IllegalArgumentException("Cannot make an other person than yourself owner");
        return addAuthorization(barId, role, person);
    }

    private boolean isOwner(UUID barId) {
        return authorizations.stream()
                .filter(authorization -> authorization.getBarId().equals(barId))
                .anyMatch(authorization -> authorization.getRole() == Role.OWNER);
    }

    private boolean addAuthorization(UUID barId, Role role, Person person) {
        authorizations.stream()
                .filter(authorization -> authorization.getBarId().equals(barId))
                .findAny()
                .ifPresentOrElse(
                        authorization -> authorization.updateRole(role),
                        () -> authorizations.add(new Authorization(UUID.randomUUID(), barId, role, person)));
        return true;
    }

    public boolean revokeUserAuthorization(User user, UUID barId) {
        if (!isOwner(barId))
            throw new NotAuthorizedException("User has to be Owner of bar to revoke authorize other users");
        if (this.equals(user)) throw new IllegalArgumentException("Cannot revoke your own bar ownership authorization");
        return user.revokeAuthorization(barId);
    }

//    public boolean revokeOwnership(UUID barIdentification) {
//        if (!isOwner(barIdentification)) throw new NotAuthorizedException("User has to be Owner to revoke ownership");
//        return revokeAuthorization(barIdentification);
//    }

    private boolean revokeAuthorization(UUID barId) {
        return authorizations.removeIf(authorization -> authorization.getBarId().equals(barId));
    }

    public Map<UUID, String> getAuthorizations() {
        return authorizations.stream()
                .collect(Collectors.toMap(
                        Authorization::getBarId,
                        authorization -> authorization.getRole().name()));
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(username, user.username)
                && Objects.equals(password, user.password)
                && Objects.equals(mail, user.mail)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && Objects.equals(phoneNumber, user.phoneNumber)
                && Objects.equals(authorizations, user.authorizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, mail, firstName, lastName, phoneNumber, authorizations);
    }
}
