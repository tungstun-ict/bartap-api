package com.tungstun.security.config.evaluator;

import com.tungstun.security.application.user.UserQueryHandler;
import com.tungstun.security.domain.user.User;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Component
public class BarApiPermissionEvaluator implements PermissionEvaluator {
    private final UserQueryHandler userQueryHandler;

    public BarApiPermissionEvaluator(UserQueryHandler userQueryHandler) {
        this.userQueryHandler = userQueryHandler;
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permissions) {
        if ((auth == null) || (targetDomainObject == null) || !(permissions instanceof List)){
            return false;
        }
        UUID barId = UUID.fromString((String) targetDomainObject);
        UserDetails user = (UserDetails) auth.getPrincipal();
        return hasPrivilege(user.getUsername(), barId, (List<String>) permissions);
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permissions) {
        if ((auth == null) || (targetType == null) || !(permissions instanceof List)) {
            return false;
        }
        UserDetails user = (UserDetails) auth.getPrincipal();
        return hasPrivilege(user.getUsername(), UUID.fromString((String) targetId), (List<String>) permissions);
    }

    private boolean hasPrivilege(String username, UUID barId, List<String> permissions) {
        User user = (User) userQueryHandler.loadUserByUsername(username);
        String role = user.getAuthorizations().get(barId);
        if (role == null) return false;
        return permissions.stream().anyMatch(role::equals);
    }
}
