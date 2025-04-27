package com.hjh.muit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column
    private String artist;

    @Column
    private String description;

    @Column
    private String genre;

    @Column
    private String label;

    @Column
    private int stock;

    @Column
    private Date releasedDate;

    @Column
    private AlbumType albumType;

    @Column
    private String coverImageUrl;

    @OneToMany(mappedBy = "album")
    private List<OrderItem> orderItems;

    public enum AlbumType {
        SINGLE,
        EP,
        LP,
        REPACKAGE,
        LIVE,
        COMPILATION,
        SOUNDTRACK,
        REMIX,
        SINGLE_DIGITAL
    }
}
