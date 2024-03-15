package org.cs304.backend.constant;

import lombok.Getter;

@Getter
public enum constant_User {
    ADMIN(0),
    USER(1);

    private final int value;

    constant_User(int value) {
        this.value = value;
    }

}
