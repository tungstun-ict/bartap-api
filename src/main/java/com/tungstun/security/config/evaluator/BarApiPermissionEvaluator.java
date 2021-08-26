package com.tungstun.security.config.evaluator;

import com.tungstun.security.application.UserService;
import com.tungstun.security.data.model.User;
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
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return hasPrivilege(username, barId, (List<String>) permissions);
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permissions) {
        if ((auth == null) || (targetType == null) || !(permissions instanceof List)) {
            return false;
        }
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return hasPrivilege(username, Long.valueOf((String) targetId), (List<String>) permissions);
    }

    private boolean hasPrivilege(String username, Long barId, List<String> permissions) {
        User user = (User) this.userService.loadUserByUsername(username);
        String role = user.getAuthoritiesMap().get(barId);
        for (String permission : permissions) {
            if (role != null && role.equals(permission)) return true;
        }
        return false;
    }
}
