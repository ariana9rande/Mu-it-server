package com.hjh.muit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String address;

    @Column
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column
    private Grade grade;

    @OneToMany(mappedBy = "user")
    @Column
    private List<Order> orders;

    @Getter
    public enum Grade {

        BRONZE(3),
        SILVER(5),
        GOLD(7),
        DIAMOND(10);

        private final int discountRate;

        Grade(int discountRate) {
            this.discountRate = discountRate;
        }
    }

    public enum Role {
        ADMIN,
        USER
    }
}
