package fiveguys.webide.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("사용자"), ADMIN("관리자"),
    ;

    private String role;

    UserRole(String role) {
        this.role = role;
    }
}
