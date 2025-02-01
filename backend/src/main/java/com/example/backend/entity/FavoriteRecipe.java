package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorite_recipes")
public class FavoriteRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)  // 기존 recipeName → title로 변경
    private String title;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String sourceUrl;
}
