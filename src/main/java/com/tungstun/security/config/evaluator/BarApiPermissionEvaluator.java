package com.tungstun.security.config.evaluator;

import com.tungstun.security.application.UserService;
import com.tungstun.security.data.model.User;
import com.tungstun.security.data.model.UserProfile;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class BarApiPermissionEvaluator implements PermissionEvaluator {
    private final UserService USER_SERVICE;

    public BarApiPermissionEvaluator(UserService userService) {
        this.USER_SERVICE = userService;
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)){
            return false;
        }
        Long barId = (Long) targetDomainObject;
        String username = ((UserProfile) auth.getPrincipal()).getUsername();
        return hasPrivilege(username, barId, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        String username = ((UserProfile) auth.getPrincipal()).getUsername();
        return hasPrivilege(username, Long.valueOf((String) targetId), permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(String username, Long barId, String permission) {
        User user = (User) this.USER_SERVICE.loadUserByUsername(username);
        String role = user.getAuthoritiesMap().get(barId);
        return role != null && role.equals(permission);
    }
}
