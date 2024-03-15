package org.cs304.backend.constant;

import lombok.Getter;

@Getter
public enum constant_EventType {
    LECTURE(0),//讲座
    WORKSHOP(1),//工作坊
    COMPETITION(2),//比赛
    PERFORMANCE(3),//表演
    EXHIBITION(4),//展览
    FORUM(5),//论坛
    SPORTS(6),//体育
    VOLUNTEER(7),//志愿
    COLLEGE_EDUCATION(8),//学院
    SALON(9),//沙龙
    TRAINING(10),//培训
    CLUB_ACTIVITY(11),//社团
    OTHER(12);//其他

    private final int value;

    constant_EventType(int value) {
        this.value = value;
    }

}
