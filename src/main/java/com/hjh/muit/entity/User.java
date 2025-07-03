package com.hjh.muit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column
    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private String provider;

    @Column
    private String providerId;

    @Column
    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column
    private UserGrade grade;

    @OneToMany(mappedBy = "user")
    @Column
    private List<Order> orders;

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

    public enum UserRole {
        ADMIN,
        USER
    }
}
