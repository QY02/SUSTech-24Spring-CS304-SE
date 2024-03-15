package org.cs304.backend.constant;

import lombok.Getter;

@Getter
public enum constant_EventStatus {
    AUDITING(0),//审核中
    PASSED(1),//审核通过
    REJECTED(2);//审核未通过

    private final int value;

    constant_EventStatus(int value) {
        this.value = value;
    }

}
