package com.tungstun.security.config.evaluator;

import com.tungstun.security.application.UserService;
import com.tungstun.security.domain.user.User;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class BarApiPermissionEvaluator implements PermissionEvaluator {
    private final UserService userService;

    public BarApiPermissionEvaluator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permissions) {
        if ((auth == null) || (targetDomainObject == null) || !(permissions instanceof List)){
            return false;
        }
        Long barId = (Long) targetDomainObject;
        UserDetails user = (UserDetails) auth.getPrincipal();
        return hasPrivilege(user.getUsername(), barId, (List<String>) permissions);
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permissions) {
        if ((auth == null) || (targetType == null) || !(permissions instanceof List)) {
            return false;
        }
        UserDetails user = (UserDetails) auth.getPrincipal();
        return hasPrivilege(user.getUsername(), Long.valueOf((String) targetId), (List<String>) permissions);
    }

    private boolean hasPrivilege(String username, Long barId, List<String> permissions) {
        User user = (User) this.userService.loadUserByUsername(username);
        String role = user.getAuthorizations().get(barId);
        for (String permission : permissions) {
            if (role != null && role.equals(permission)) return true;
        }
        return false;
    }
}
