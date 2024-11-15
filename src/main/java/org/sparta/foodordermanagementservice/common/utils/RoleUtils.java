package org.sparta.foodordermanagementservice.common.utils;

import org.sparta.foodordermanagementservice.entity.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

public class RoleUtils {

    private RoleUtils() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }

    public static boolean hasMasterRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRole.Authority.MASTER));
    }

    public static boolean hasManagerRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(UserRole.Authority.MANAGER));
    }
}