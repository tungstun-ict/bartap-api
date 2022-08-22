package com.tungstun.security.util.account;

import com.tungstun.security.domain.user.UserRepository;
import com.tungstun.security.presentation.dto.request.UserRegistrationRequest;
import com.tungstun.security.util.validation.NonSpaceValidator;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountException;

@Component
public class RegistrationValidator {
    private final UserRepository userRepository;

    public RegistrationValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateRegistrationDetails(UserRegistrationRequest userRegistrationRequest) throws AccountException {
        validateInput(userRegistrationRequest);
        validateUniqueAccount(userRegistrationRequest);
    }

    private void validateInput(UserRegistrationRequest userRegistrationRequest) {
        validateUsername(userRegistrationRequest.username);
        validateMail(userRegistrationRequest.mail);
        validatePassword(userRegistrationRequest.password);
    }

    private void validateUsername(String username) {
        boolean isValid = NonSpaceValidator.validate(username);
        if (!isValid) throw new IllegalArgumentException("Username cannot contain spaces");
    }

    private void validateMail(String mail) {
//        boolean isValid = new EmailValidator().isValid(mail, null);
        //todo fix
//        if (!isValid) throw new IllegalArgumentException("Invalid Email address");
    }

    private void validatePassword(String password) {
        boolean isValid = NonSpaceValidator.validate(password);
        if (!isValid) throw new IllegalArgumentException("Password cannot contain spaces");
    }

    private void validateUniqueAccount(UserRegistrationRequest userRegistrationRequest) throws AccountException {
        validateUniqueUsername(userRegistrationRequest.username);
        validateUniqueMail(userRegistrationRequest.mail);
    }

    private void validateUniqueUsername(String username) throws AccountException {
        boolean exists = this.userRepository.findByUsername(username).isPresent();
        if (exists) throw new AccountException("Account with username already exists");
    }

    private void validateUniqueMail(String mail) throws AccountException {
        boolean exists = this.userRepository.findByMail(mail).isPresent();
        if (exists) throw new AccountException("Account with mail already exists");
    }
}
