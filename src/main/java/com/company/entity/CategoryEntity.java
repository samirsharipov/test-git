package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String key;

    @Column(nullable = false, name = "name_uz")
    private  String nameUz;

    @Column(nullable = false, name = "name_ru")
    private  String nameRu;

    @Column(nullable = false, name = "name_en")
    private  String nameEn;


    @Column(nullable = false)
    private Boolean visible=Boolean.TRUE;


    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();

    public CategoryEntity(String key) {
        this.key = key;
    }
}
