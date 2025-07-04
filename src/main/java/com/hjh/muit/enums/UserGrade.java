package com.hjh.muit.enums;

import lombok.Getter;

@Getter
public enum UserGrade {

    BRONZE(3),
    SILVER(5),
    GOLD(7),
    DIAMOND(10);

    private final int discountRate;

    UserGrade(int discountRate) {
        this.discountRate = discountRate;
    }
}