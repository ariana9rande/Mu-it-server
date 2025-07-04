package com.hjh.muit.entity;

import com.hjh.muit.enums.UserGrade;
import com.hjh.muit.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return loginId;
    }
}
