package com.tungstun.security.application.authorization;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthorizationCommandHandler {
//    private final UserRepository userRepository;
//
//    public AuthorizationCommandHandler(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public User loadUserById(Long id) throws UsernameNotFoundException {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException(String.format("User with id '%s' was not found", id)));
//    }
//
//    public boolean handle(AuthorizeUser command) {
//        User owner = loadUserById(command.ownerId());
//        User user = loadUserById(command.userId());
//        user.authorize(command.barId(), Role.getRole(command.role()));
//
//        //getbar get person
//        return owner.authorize(user, command.barId(), ));
//    }
//
//    public boolean handle(RevokeUserAuthorization command) {
//        User owner = loadUserById(command.ownerId());
//        User user = loadUserById(command.userId());
//        return owner.revokeUserAuthorization(user, command.barId());
//    }
//
//    public boolean handle(AuthorizeNewBarOwnership command) {
//        User user = loadUserById(command.userId());
//        return user.newBarAuthorization(command.barId());
//    }
//
//    /**
//     * DON'T USE
//     * Currently only removes a user's ownership of bar. After removing user cannot undo or get access back to the bar.
//     * Implement soft delete or give access to other user.
//     */
//    public boolean handle(RevokeBarOwnerShip command) {
//        User owner = loadUserById(command.ownerId());
//        return owner.revokeOwnership(command.barId());
//    }
}
