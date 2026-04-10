package com.gym.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.gym.model.AppUser;
import com.gym.model.UserRole;

@Component
public class CurrentUser {

    public AppUser requireUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AppUserDetails details)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return details.getUser();
    }

    public Long requireMemberId() {
        AppUser user = requireUser();
        if (user.getRole() != UserRole.MEMBER || user.getMemberId() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Member access required");
        }
        return user.getMemberId();
    }
}
